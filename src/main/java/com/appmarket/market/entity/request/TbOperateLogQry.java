package com.appmarket.market.entity.request;


import com.appmarket.market.bean.TbOperateLog;

import java.util.Date;

/**
 * Created by Kingson.chan on 2017/2/21.
 * Email:chenjingxiong@yunnex.com.
 */
public class TbOperateLogQry extends TbOperateLog {
    private String beginDate;
    private String endDate;

    private Date begin;
    private Date end;

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
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
