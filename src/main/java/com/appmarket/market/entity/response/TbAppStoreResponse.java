package com.appmarket.market.entity.response;

import com.appmarket.market.bean.TbAppStore;

/**
 * Created by kingson.chan on 2017/4/19.
 * Email:chenjingxiong@yunnex.com.
 */
public class TbAppStoreResponse extends TbAppStore {
    String isCheck;

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }
}
