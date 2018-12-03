package com.appmarket.market.entity.request;


import com.appmarket.market.bean.TbArea;

/**
 * Created by Kingson.chan on 2017/2/20.
 * Email:chenjingxiong@yunnex.com.
 */
public class TbAppTypeQry extends TbArea {
    private String nameLike;
    private String beginDate;
    private String endDate;
    private String isQueryAll;

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

    public String getIsQueryAll() {
        return isQueryAll;
    }

    public void setIsQueryAll(String isQueryAll) {
        this.isQueryAll = isQueryAll;
    }
}
