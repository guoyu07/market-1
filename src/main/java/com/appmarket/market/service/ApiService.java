package com.appmarket.market.service;

import com.appmarket.market.bean.TbAppStore;
import com.appmarket.market.bean.TbSysAppStore;
import com.appmarket.market.bean.TbTerminal;
import com.appmarket.market.entity.request.APIAppRequest;
import com.appmarket.market.entity.request.APITerminalRequest;

import java.util.List;

/**
 * Created by kingson.chan on 2017/4/20.
 * Email:chenjingxiong@yunnex.com.
 */
public interface ApiService {
    List<TbAppStore> getAppList(APITerminalRequest apiTerminalRequest);

    List<TbSysAppStore> getWhiteList(APITerminalRequest apiTerminalRequest);

    int updateCount(APIAppRequest apiAppRequest);

    int updateTerminalPosition(APITerminalRequest apiTerminalRequest);
    
    TbTerminal getTbTerminalInfo(String imei);

}
