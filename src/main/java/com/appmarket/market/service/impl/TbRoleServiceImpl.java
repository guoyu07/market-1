package com.appmarket.market.service.impl;

import com.appmarket.market.bean.*;
import com.appmarket.market.entity.TbMenuVo;
import com.appmarket.market.entity.TbRoleVo;
import com.appmarket.market.entity.request.TbRoleQry;
import com.appmarket.market.mapper.AuthorityMapper;
import com.appmarket.market.mapper.TbRoleMapper;
import com.appmarket.market.mapper.TbRoleMenuMapper;
import com.appmarket.market.service.TbRoleService;
import com.appmarket.market.utils.ParamsUtils;
import com.kingson.common.utils.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
@Service
public class TbRoleServiceImpl implements TbRoleService {
    private Logger logger = LoggerFactory.getLogger(TbRoleServiceImpl.class);
    @Autowired
    AuthorityMapper authorityMapper;

    @Autowired
    TbRoleMapper tbRoleMapper;

    @Autowired
    TbRoleMenuMapper tbRoleMenuMapper;

    @Override
    public List<TbRoleVo> getRolesByUserId(String name) {
        return authorityMapper.selectRolesByUserId(name);
    }

    @Override
    public List<TbRole> queryList(TbRole tbRoleQry) {
        TbRoleExample tbRoleExample = new TbRoleExample();
        TbRoleExample.Criteria tbRoleCriteria = tbRoleExample.createCriteria();
        tbRoleCriteria.andSysLevelEqualTo(tbRoleQry.getSysLevel());
        tbRoleCriteria.andCreateUserEqualTo(tbRoleQry.getCreateUser());
        return tbRoleMapper.selectByExample(tbRoleExample);
    }

    @Override
    public Page<TbRole> queryPageList(TbRoleQry query, Pageable pageable) {
        TbRoleExample tbRoleExample = new TbRoleExample();
        String orderStr = ParamsUtils.orderString(pageable);
        if(StringUtil.isNotBlank(orderStr)){
            tbRoleExample.setOrderByClause(orderStr);
        }
        com.kingson.common.Plugin.Page page = new com.kingson.common.Plugin.Page();
        page.setBegin(pageable.getOffset());
        page.setLength(pageable.getPageSize());
        tbRoleExample.setPage(page);

        TbRoleExample.Criteria tbRoleCriteria = tbRoleExample.createCriteria();
        if(StringUtil.isNotBlank(query.getNameLike())){
            tbRoleCriteria.andNameEqualTo(query.getNameLike());
        }

        List<TbRole> tbRoleList = tbRoleMapper.selectByExample(tbRoleExample);
        int count = tbRoleMapper.countByExample(tbRoleExample);
        return new PageImpl(tbRoleList,pageable,count);
    }

    @Override
    public TbRole queryById(int roleId) {
        return tbRoleMapper.selectByPrimaryKey(roleId);
    }

    @Transactional
    @Override
    public boolean insertOrUpdateRoleMenus(TbRole role, String[] arrPermission, Map<Integer, TbMenuVo> allMenuMap) {
        logger.info("更新角色相应的菜单和权限");
        Integer menuId = null;
        String permission = null;
        Map<Integer,String> roleMenuMap = new HashMap<>();
        List<Map<String,Object>> roleMenus = new ArrayList<Map<String, Object>>();

        if(role.getId()==null){
            tbRoleMapper.insertSelective(role);
        }else{
            tbRoleMapper.updateByPrimaryKeySelective(role);
        }

        for (int i = 0; i < arrPermission.length; i++) {
            String s = arrPermission[i];
            if(s.startsWith("permission_")){
                menuId= new Integer(s.substring(11,s.lastIndexOf("_")));
                permission=s.substring(s.lastIndexOf("_")+1);
                permission+=";"+ StringUtils.trimToEmpty(roleMenuMap.get(menuId));
                roleMenuMap.put(menuId,permission);
            }
        }
        for (Integer mId : roleMenuMap.keySet()) {
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("roleId",role.getId());
            map.put("menuId",mId);
            map.put("permission",roleMenuMap.get(mId));
            StringBuffer sign = new StringBuffer();
            map.put("permissionSign",getPermissionSign(allMenuMap,mId,sign));
            roleMenus.add(map);
        }

        Integer roleId = (Integer) roleMenus.iterator().next().get("roleId");
        TbRoleMenuExample tbRoleMenuExample = new TbRoleMenuExample();
        TbRoleMenuExample.Criteria tbRoleCriteria = tbRoleMenuExample.createCriteria();
        tbRoleCriteria.andRoleIdEqualTo(roleId);
        tbRoleMenuMapper.deleteByExample(tbRoleMenuExample);
        for (Map<String, Object> roleMenu : roleMenus) {
            TbRoleMenu tbRoleMenu = new TbRoleMenu();
            tbRoleMenu.setRoleId(MapUtils.getInteger(roleMenu,"roleId"));
            tbRoleMenu.setMenuId(MapUtils.getInteger(roleMenu,"menuId"));
            tbRoleMenu.setPermission(MapUtils.getString(roleMenu,"permission"));
            tbRoleMenu.setPermissionSign(MapUtils.getString(roleMenu,"permissionSign"));
            tbRoleMenuMapper.insertSelective(tbRoleMenu);
        }
        return true;
    }

    private String getPermissionSign(Map<Integer,TbMenuVo> menuMap,Integer mId,StringBuffer sign){
        TbMenu vo = menuMap.get(mId);
        if(vo.getParentId()>0){
            getPermissionSign(menuMap,vo.getParentId(),sign);
        }
        sign.append(vo.getShortEnName()).append(":");
        return sign.toString();
    }

    @Transactional
    @Override
    public int deleteById(Integer roleId) {
        tbRoleMapper.deleteByPrimaryKey(roleId);
        TbRoleMenuExample tbRoleMenuExample = new TbRoleMenuExample();
        TbRoleMenuExample.Criteria tbRoleCriteria = tbRoleMenuExample.createCriteria();
        tbRoleCriteria.andRoleIdEqualTo(roleId);
        tbRoleMenuMapper.deleteByExample(tbRoleMenuExample);
        return 1;
    }
}
