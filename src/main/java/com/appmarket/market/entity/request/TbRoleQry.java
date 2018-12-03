package com.appmarket.market.entity.request;


import com.appmarket.market.bean.TbRole;

/**
 * Created by Kingson.chan on 2017/2/20.
 * Email:chenjingxiong@yunnex.com.
 */
public class TbRoleQry extends TbRole {
    private String nameLike;

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }
}
