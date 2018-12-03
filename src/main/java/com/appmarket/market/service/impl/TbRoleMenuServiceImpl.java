package com.appmarket.market.service.impl;


import com.appmarket.market.bean.TbRoleMenu;
import com.appmarket.market.bean.TbRoleMenuExample;
import com.appmarket.market.mapper.TbRoleMenuMapper;
import com.appmarket.market.service.TbRoleMenuService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Kingson.chan on 2017/2/21.
 * Email:chenjingxiong@yunnex.com.
 */
public class TbRoleMenuServiceImpl implements TbRoleMenuService {
    @Autowired
    TbRoleMenuMapper tbRoleMenuMapper;
    @Override
    public void insertOrUpdateRoleMenus(List<Map<String, Object>> roleMenus) {
        Integer roleId = (Integer) roleMenus.iterator().next().get("roleId");
        TbRoleMenuExample tbRoleMenuExample = new TbRoleMenuExample();
        TbRoleMenuExample.Criteria tbRoleCriteria = tbRoleMenuExample.createCriteria();
        tbRoleCriteria.andRoleIdEqualTo(roleId);
        tbRoleMenuMapper.deleteByExample(tbRoleMenuExample);
        for (Map<String, Object> roleMenu : roleMenus) {
            TbRoleMenu tbRoleMenu = new TbRoleMenu();
            tbRoleMenu.setRoleId(MapUtils.getInteger(roleMenu, "roleId"));
            tbRoleMenu.setMenuId(MapUtils.getInteger(roleMenu,"menuId"));
            tbRoleMenu.setPermission(MapUtils.getString(roleMenu,"permission"));
            tbRoleMenu.setPermissionSign(MapUtils.getString(roleMenu,"permissionSign"));
            tbRoleMenuMapper.insertSelective(tbRoleMenu);
        }

    }
}
