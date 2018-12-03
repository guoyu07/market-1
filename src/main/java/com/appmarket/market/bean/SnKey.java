package com.appmarket.market.bean;

/**
 * Created by kingson.chan on 2017/5/8.
 * Email:chenjingxiong@yunnex.com.
 */
public class SnKey {
    private String sn;
    private String snum;
    private String key;


    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSnum() {
        return snum;
    }

    public void setSnum(String snum) {
        this.snum = snum;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "SnKey{" +
                "sn='" + sn + '\'' +
                ", snum='" + snum + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
