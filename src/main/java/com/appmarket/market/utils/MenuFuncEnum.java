package com.appmarket.market.utils;

/**
 * Created by Admin on 2015/4/1.
 */
public enum MenuFuncEnum {
    READ("查询"),CREATE("添加"),APPLY("申请"),UPDATE("修改"),DELETE("删除"),EXPORT("导出"),IMPORT("导入"),REPLACE("换卡"),
    RESETPWD("重置密码"),MODIFYPWD("修改密码"),LOSS("挂失"),BLACK("黑名单"),AUDIT("审批"),DOPAY("付款");

    private String msg;
    private MenuFuncEnum(String msg)
    {
        this.msg = msg;
    }

    public String getMsg()
    {
        return this.msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public static void main(String[] args) {
        System.out.println( MenuFuncEnum.valueOf(" EXPORT").getMsg());
       
    }
}
