package com.appmarket.market.entity.request;

import com.appmarket.market.bean.TbSysAppStore;

/**
 * Created by kingson.chan on 2017/4/19.
 * Email:chenjingxiong@yunnex.com.
 */
public class TbSysAppStoreQry extends TbSysAppStore {
    private String nameLike;
    private String beginDate;
    private String endDate;

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

}
