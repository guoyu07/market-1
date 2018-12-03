package com.appmarket.market.entity;


import com.appmarket.market.bean.TbMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
public class TbMenuVo extends TbMenu {
    private List<TbMenuVo> childMenus=new ArrayList<TbMenuVo>(0);
    private String permissionSign;
    private String permission;

    public List<TbMenuVo> getChildMenus() {
        return childMenus;
    }

    public void setChildMenus(List<TbMenuVo> childMenus) {
        this.childMenus = childMenus;
    }

    public String getPermissionSign() {
        return permissionSign;
    }

    public void setPermissionSign(String permissionSign) {
        this.permissionSign = permissionSign;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
