package com.appmarket.market.service.impl;

import com.appmarket.market.bean.*;
import com.appmarket.market.entity.request.TbTerminalQry;
import com.appmarket.market.mapper.TbImeiAppMapper;
import com.appmarket.market.mapper.TbTerminalMapper;
import com.appmarket.market.service.TerminalService;
import com.appmarket.market.utils.SnKeyUtil;
import com.kingson.common.utils.ObjectUtil;
import com.kingson.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingson.chan on 2017/4/17.
 * Email:chenjingxiong@yunnex.com.
 */
@Service
public class TerminalServiceImpl implements TerminalService{
    private static final Logger logger = LoggerFactory.getLogger(TerminalServiceImpl.class);
    @Autowired
    TbTerminalMapper tbTerminalMapper;
    @Autowired
    TbImeiAppMapper tbImeiAppMapper;
    @Override
    public Page<TbTerminal> queryPageList(TbTerminalQry query, Pageable pageable) {
        TbTerminalExample tbTerminalExample = new TbTerminalExample();

        //设置查询分页
        com.kingson.common.Plugin.Page page = new com.kingson.common.Plugin.Page();
        if(ObjectUtil.isNotObjectEmpty(pageable)){
            
            page.setBegin(pageable.getOffset());
            page.setLength(pageable.getPageSize());
            //设置查询排序
            /*String orderStr = ParamsUtils.orderString(pageable);
            if(StringUtil.isNotBlank(orderStr)){
                tbTerminalExample.setOrderByClause(ParamsUtils.orderString(pageable));
            }*/
        }
        else {
            page.setBegin(0);
            page.setLength(20);
        }
        tbTerminalExample.setPage(page);
        TbTerminalExample.Criteria tbTerminalCriteria =  tbTerminalExample.createCriteria();

        if(null != query.getUid()){
            tbTerminalCriteria.andUidEqualTo(query.getUid());
        }

        if(StringUtil.isNotBlank(query.getNameLike())){
            tbTerminalCriteria.andImeiLike("%" + query.getNameLike() + "%");
        }

        if(StringUtil.isNotBlank(query.getSn())){
            tbTerminalCriteria.andSnLike("%" + query.getSn() + "%");
        }

        if(StringUtil.isNotBlank(query.getUidName())){
            tbTerminalCriteria.andUidEqualTo(Integer.parseInt(query.getUidName()));
        }

        if(StringUtil.isNotBlank(query.getAreaName())){
            tbTerminalCriteria.andAreaEqualTo(query.getAreaName());
        }

        List<TbTerminal> tbTerminalList = tbTerminalMapper.selectByExample(tbTerminalExample);
        //设置总数
        int count = tbTerminalMapper.countByExample(tbTerminalExample);
        return new PageImpl(tbTerminalList, pageable, count);

    }

    @Override
    @Transactional
    public String saveOrUpdateList(List<TbTerminal> tbTerminalList, TbUser tbUser) {
        logger.info("批量导入数据");
        for (TbTerminal tbTerminal : tbTerminalList) {
            if(tbUser.getType() != 1){//一般用户只能更新
                int result = tbTerminalMapper.updateByPrimaryKeySelective(tbTerminal);
            }
            else {
                TbTerminal tbTerminalInfo = tbTerminalMapper.selectByPrimaryKey(tbTerminal.getImei());
                if(null != tbTerminalInfo){
                    int result = tbTerminalMapper.updateByPrimaryKeySelective(tbTerminal);
                }
                else {
                    tbTerminalMapper.insertSelective(tbTerminal);
                }
            }

        }
        return "0";
    }

    @Override
    @Transactional
    public int deleteTerminalInfo(String imei, Integer uid) {
        //删除终端以及它所有的应用
        TbImeiAppExample tbImeiAppExample = new TbImeiAppExample();
        TbImeiAppExample.Criteria tbImeiAppCriteria = tbImeiAppExample.createCriteria();
        tbImeiAppCriteria.andImeiNoEqualTo(imei);
        tbImeiAppMapper.deleteByExample(tbImeiAppExample);
        return tbTerminalMapper.deleteByPrimaryKey(imei);
    }

    @Override
    public TbTerminal queryById(String imei) {
        return tbTerminalMapper.selectByPrimaryKey(imei);
    }

    @Override
    public int insert(TbTerminal tbTerminal) {
        return tbTerminalMapper.insertSelective(tbTerminal);
    }

    @Override
    @Transactional
    public int update(TbTerminal tbTerminal) {
        return tbTerminalMapper.updateByPrimaryKeySelective(tbTerminal);
    }

    @Override
    public List<SnKey> snKeyList(List<String> snList) {
        List<SnKey> snKeyList = new ArrayList<>();
        for (String s : snList) {
            if(StringUtil.isNotBlank(s)){
                s = SnKeyUtil.byteArray2HexString(s.getBytes());
                SnKey snKey = SnKeyUtil.getSnKey(s);
                if(null == snKey){
                    SnKey snKey1 = new SnKey();
                    snKey1.setSn(s);
                    snKey1.setSnum("没有设置");
                    snKey1.setKey("没有设置");
                    snKeyList.add(snKey1);
                }
                else {
                    String sn = snKey.getSn();
                    snKey.setSn(new String(SnKeyUtil.hexString2ByteArray(sn)));
                    String snum = snKey.getSnum();
                    snKey.setSnum(snum);
                    snKeyList.add(snKey);
                }


            }
        }
        return snKeyList;
    }
}
