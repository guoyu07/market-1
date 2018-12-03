package com.appmarket.market.service;

import com.appmarket.market.bean.SnKey;
import com.appmarket.market.bean.TbTerminal;
import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.request.TbTerminalQry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by kingson.chan on 2017/4/17.
 * Email:chenjingxiong@yunnex.com.
 */
public interface TerminalService {
    Page<TbTerminal> queryPageList(TbTerminalQry query, Pageable pageable);

    String saveOrUpdateList(List<TbTerminal> tbTerminalList, TbUser tbUser);

    int deleteTerminalInfo(String imei,Integer uid);

    TbTerminal queryById(String imei);

    int insert(TbTerminal tbTerminal);

    int update(TbTerminal tbTerminal);

    List<SnKey> snKeyList(List<String> snList);

}
