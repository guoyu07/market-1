package com.appmarket.market.service.impl;


import com.appmarket.market.bean.TbMenu;
import com.appmarket.market.bean.TbMenuExample;
import com.appmarket.market.entity.TbMenuVo;
import com.appmarket.market.mapper.AuthorityMapper;
import com.appmarket.market.mapper.TbMenuMapper;
import com.appmarket.market.service.TbMenuService;
import com.appmarket.market.utils.MenuFuncEnum;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
@Service
public class TbMenuServiceImpl implements TbMenuService {
    @Autowired
    AuthorityMapper authorityMapper;

    @Autowired
    TbMenuMapper tbMenuMapper;

    @Override
    public List<Map<String, Object>> selectPermissionsByRoleId(Integer roleId) {
        return authorityMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    public List<TbMenuVo> selectMenuByUserId(String userAccount) {
        return authorityMapper.selectMenuByUserId(userAccount);
    }

    @Override
    public String getRoleMenuJson(List<TbMenuVo> baseMenu, Integer roleId) {
        List<Map<String,Object>> roleMenu=null;
        Map<String,String> roleMenuMap = new HashMap<String, String>();
        if(roleId!=null){
            roleMenu= authorityMapper.selectPermissionsByRoleId(roleId);
            roleMenuMap = getRoleMenuMap(roleMenu);
        }

        StringBuffer sb = new StringBuffer("[");
        sb = getJson(baseMenu,roleMenuMap,sb);
        sb.append("]");
        return sb.toString();
    }

    private Map<String,String> getRoleMenuMap(List<Map<String,Object>> roleMenus){
        Map<String,String> map = new HashMap<String, String>();
        for (Map<String, Object> roleMenu : roleMenus) {
            map.put(roleMenu.get("menu_id").toString(),String.valueOf(roleMenu.get("permission")));
        }
        return map;
    }
    private StringBuffer getJson(List<TbMenuVo> menus,final Map<String,String> roleMenuMap,StringBuffer sb){
        String tmp[] = null;
        String roleMenuPermission=null;
        boolean checkFlag = false;
        for (TbMenuVo menu : menus) {
            sb.append("{id:'menu_").append(menu.getId()).append("',state : { opened : true},text:'").append(menu.getName()).append("'");
            if(menu.getChildMenus()!=null&&menu.getChildMenus().size()>0){
                sb.append(",children :[");
                this.getJson(menu.getChildMenus(),roleMenuMap,sb);
                sb.append("]");
            }else{
                if(StringUtils.isNotEmpty(menu.getPermissionList())){
                    tmp = menu.getPermission().split(";");
                    if(tmp.length>0){
                        sb.append(",children :[");
                        for (int i = 0; i < tmp.length; i++) {
                            String s = tmp[i];
                            if(i>0)sb.append(",");
                            roleMenuPermission=roleMenuMap.get(menu.getId().toString());
                            checkFlag=StringUtils.isNotEmpty(roleMenuPermission)&&roleMenuPermission.contains(s);

                            sb.append("{id:'permission_").append(menu.getId()).append("_"+s).append("',text:'")
                                    .append(MenuFuncEnum.valueOf(s.trim()).getMsg()).append("',")
                                    .append("state:{'selected':").append(checkFlag).append(" }");
                            sb.append("}");
                        }
                        sb.append("]");
                    }
                }
            }
            sb.append("},");
        }
        return sb.delete(sb.length()-1,sb.length());

    }

    @Override
    public Map<Integer, TbMenuVo> queryMap() {
        TbMenuExample tbMenuExample = new TbMenuExample();
        TbMenuExample.Criteria tbMenuCriteria = tbMenuExample.createCriteria();
        List<TbMenu> tbMenuList = tbMenuMapper.selectByExample(tbMenuExample);
        Map<Integer,TbMenuVo> map = new HashMap<>();
        for (TbMenu tbMenu : tbMenuList) {
            TbMenuVo tbMenuVo = new TbMenuVo();
            try {
                BeanUtils.copyProperties(tbMenuVo,tbMenu);
                map.put(tbMenu.getId(),tbMenuVo);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        return map;
    }
}
