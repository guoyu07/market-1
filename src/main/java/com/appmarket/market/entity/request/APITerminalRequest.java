package com.appmarket.market.entity.request;

import com.appmarket.market.bean.TbTerminal;

import javax.validation.constraints.NotNull;

/**
 * Created by kingson.chan on 2017/4/21.
 * Email:chenjingxiong@yunnex.com.
 */
public class APITerminalRequest extends TbTerminal {
    @NotNull
    private  int currentPage = 0;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
