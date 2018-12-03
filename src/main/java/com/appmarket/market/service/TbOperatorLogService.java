package com.appmarket.market.service;


import com.appmarket.market.bean.TbOperateLog;
import com.appmarket.market.entity.request.TbOperateLogQry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Kingson.chan on 2017/2/21.
 * Email:chenjingxiong@yunnex.com.
 */
public interface TbOperatorLogService {
    public int insertLog(String account, String title, String log, String ip);

    Page<TbOperateLog> queryPageList(TbOperateLogQry query, Pageable pageable);

}
