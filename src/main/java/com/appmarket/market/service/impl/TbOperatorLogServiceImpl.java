package com.appmarket.market.service.impl;

import com.appmarket.market.bean.TbOperateLog;
import com.appmarket.market.bean.TbOperateLogExample;
import com.appmarket.market.entity.request.TbOperateLogQry;
import com.appmarket.market.mapper.TbOperateLogMapper;
import com.appmarket.market.service.TbOperatorLogService;
import com.appmarket.market.utils.DateUtil;
import com.appmarket.market.utils.ParamsUtils;
import com.kingson.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Kingson.chan on 2017/2/21.
 * Email:chenjingxiong@yunnex.com.
 */


@Service
public class TbOperatorLogServiceImpl implements TbOperatorLogService {
    @Autowired
    TbOperateLogMapper tbOperateLogMapper;
    @Override
    public int insertLog(String account, String title, String log, String ip) {
        TbOperateLog tbOperateLog = new TbOperateLog();
        tbOperateLog.setUserAccount(account);
        tbOperateLog.setTitle(title);
        tbOperateLog.setLog(log);
        tbOperateLog.setIp(ip);
        return tbOperateLogMapper.insertSelective(tbOperateLog);
    }

    @Override
    public Page<TbOperateLog> queryPageList(TbOperateLogQry query, Pageable pageable) {
        TbOperateLogExample tbOperateLogExample = new TbOperateLogExample();
        //设置查询排序
        String orderStr = ParamsUtils.orderString(pageable);
        if(StringUtil.isNotBlank(orderStr)){
            tbOperateLogExample.setOrderByClause(ParamsUtils.orderString(pageable));
        }

        //设置查询分页
        com.kingson.common.Plugin.Page page = new com.kingson.common.Plugin.Page();
        page.setBegin(pageable.getOffset());
        page.setLength(pageable.getPageSize());
        tbOperateLogExample.setPage(page);

        TbOperateLogExample.Criteria tbOperateLogCriteria = tbOperateLogExample.createCriteria();
        if(StringUtil.isNotBlank(query.getUserAccount())){
            tbOperateLogCriteria.andUserAccountEqualTo(query.getUserAccount());
        }
        if(StringUtil.isNotBlank(query.getTitle())){
            tbOperateLogCriteria.andTitleLike(query.getTitle());
        }

        if(StringUtil.isNotBlank(query.getLog())){
            tbOperateLogCriteria.andLogLike(query.getLog());
        }

        if(StringUtil.isNotBlank(query.getBeginDate())  &&StringUtil.isNotBlank(query.getEndDate())){
            try {
                tbOperateLogCriteria.andTimeBetween(DateUtil.parse(query.getBeginDate()+" 00:00:00","yyyy-MM-dd HH:mm:ss"),(DateUtil.parse(query.getEndDate()+" 23:59:59","yyyy-MM-dd HH:mm:ss")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<TbOperateLog> tbOperateLogList = tbOperateLogMapper.selectByExample(tbOperateLogExample);
        //设置总数
        int count = tbOperateLogMapper.countByExample(tbOperateLogExample);

        return new PageImpl(tbOperateLogList, pageable, count);
    }
}
