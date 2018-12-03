package com.appmarket.market.bean;

import java.io.Serializable;

public class TbUserRole implements Serializable {
    private Integer roleId;

    private String userAccount;

    private static final long serialVersionUID = 1L;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
}