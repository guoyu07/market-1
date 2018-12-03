package com.appmarket.market.bean;

import org.hibernate.validator.constraints.Length;

/**
 * Created by kingson.chan on 2017/5/11.
 * Email:chenjingxiong@yunnex.com.
 */
public class SnKeyQry {
    @Length(min = 32,max = 32)
    String snid;
    @Length(min = 84,max = 84)
    String hwid;

    public String getSnid() {
        return snid;
    }

    public void setSnid(String snid) {
        this.snid = snid;
    }

    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }
}
