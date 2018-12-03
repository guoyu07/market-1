package com.appmarket.market.entity.request;

import com.appmarket.market.bean.TbAppStore;

/**
 * Created by kingson.chan on 2017/4/21.
 * Email:chenjingxiong@yunnex.com.
 */
public class APIAppRequest extends TbAppStore {
    private  int currentPage = 0;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
