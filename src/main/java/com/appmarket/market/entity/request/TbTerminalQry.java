package com.appmarket.market.entity.request;

import com.appmarket.market.bean.TbTerminal;

/**
 * Created by kingson.chan on 2017/4/17.
 * Email:chenjingxiong@yunnex.com.
 */
public class TbTerminalQry extends TbTerminal {
    private String nameLike;
    private String beginDate;
    private String endDate;
    private String uidName;
    private String areaName;

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

    public String getUidName() {
        return uidName;
    }

    public void setUidName(String uidName) {
        this.uidName = uidName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
