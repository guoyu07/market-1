package com.appmarket.market.entity.request;

import com.appmarket.market.bean.TbAppStore;

/**
 * Created by kingson.chan on 2017/4/18.
 * Email:chenjingxiong@yunnex.com.
 */
public class TbAppStoreQry extends TbAppStore {
    private String nameLike;
    private String beginDate;
    private String endDate;
    private String appTypeName;
    private String uidName;

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAppTypeName() {
        return appTypeName;
    }

    public void setAppTypeName(String appTypeName) {
        this.appTypeName = appTypeName;
    }

    public String getUidName() {
        return uidName;
    }

    public void setUidName(String uidName) {
        this.uidName = uidName;
    }
}
