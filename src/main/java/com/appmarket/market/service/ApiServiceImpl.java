package com.appmarket.market.service;

import com.appmarket.market.bean.*;
import com.appmarket.market.entity.request.APIAppRequest;
import com.appmarket.market.entity.request.APITerminalRequest;
import com.appmarket.market.mapper.*;
import com.kingson.common.Plugin.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingson.chan on 2017/4/20.
 * Email:chenjingxiong@yunnex.com.
 */
@Service
public class ApiServiceImpl implements ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);
    @Autowired
    TbAppStoreMapper tbAppStoreMapper;
    @Autowired
    TbImeiAppMapper tbImeiAppMapper;
    @Autowired
    TbSysAppStoreMapper tbSysAppStoreMapper;
    @Autowired
    TbTerminalMapper tbTerminalMapper;
    @Autowired
    TbUserMapper tbUserMapper;


    @Override
    public List<TbAppStore> getAppList(APITerminalRequest apiTerminalRequest) {
        TbTerminal tbTerminal = getTbTerminalInfo(apiTerminalRequest.getImei());
        if(tbTerminal == null){
            return null;
        }
        TbImeiAppExample tbImeiAppExample = new TbImeiAppExample();
        TbImeiAppExample.Criteria tbImeiAppCriteria = tbImeiAppExample.createCriteria();
        tbImeiAppCriteria.andImeiNoLike(apiTerminalRequest.getImei()+"%");
        Page page = new Page();
        if(apiTerminalRequest.getCurrentPage() <= 1){
            page.setBegin(0);
        }
        else {
            page.setBegin((apiTerminalRequest.getCurrentPage() -1) * 5);
        }
        page.setLength(5);
        tbImeiAppExample.setPage(page);
        List<TbImeiApp> tbImeiAppList = tbImeiAppMapper.selectByExample(tbImeiAppExample);
        List<Integer> appIdList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(tbImeiAppList)){
            for (TbImeiApp tbImeiApp : tbImeiAppList) {
                appIdList.add(tbImeiApp.getAppId());
            }

            TbAppStoreExample tbAppStoreExample = new TbAppStoreExample();
            TbAppStoreExample.Criteria tbAppStoreCriteria = tbAppStoreExample.createCriteria();
            tbAppStoreCriteria.andIdIn(appIdList);
            List<TbAppStore> tbAppStoreList = tbAppStoreMapper.selectByExample(tbAppStoreExample);
            return tbAppStoreList;
        }
        return null;
    }

    @Override
    public List<TbSysAppStore> getWhiteList(APITerminalRequest  apiTerminalRequest) {
        TbTerminal tbTerminal = tbTerminalMapper.selectByPrimaryKey(apiTerminalRequest.getImei());
        if(null != tbTerminal){
            TbUserExample tbUserExample = new TbUserExample();
            TbUserExample.Criteria  tbUserCriteria = tbUserExample.createCriteria();
            tbUserCriteria.andTypeEqualTo(1);//获取超管用户，超管配置的白名单所有用户有效
            List<TbUser> tbUserList = tbUserMapper.selectByExample(tbUserExample);
            List<Integer> uidList = new ArrayList<>();
            uidList.add(tbTerminal.getUid());
            for (TbUser tbUser : tbUserList) {
                uidList.add(tbUser.getId());
            }
            TbSysAppStoreExample tbSysAppStoreExample = new TbSysAppStoreExample();
            Page page = new Page();
            if(apiTerminalRequest.getCurrentPage() <= 1){
                page.setBegin(0);
            }
            else {
                page.setBegin((apiTerminalRequest.getCurrentPage() -1) * 5);
            }

            page.setLength(5);
            tbSysAppStoreExample.setPage(page);
            TbSysAppStoreExample.Criteria tbSysAppStoreCriteria =  tbSysAppStoreExample.createCriteria();
            tbSysAppStoreCriteria.andUidIn(uidList);
            List<TbSysAppStore> tbSysAppStoreList = tbSysAppStoreMapper.selectByExample(tbSysAppStoreExample);
            return tbSysAppStoreList;
        }
        return null;
    }

    @Override
    public int updateCount(APIAppRequest apiAppRequest) {
        TbAppStore tbAppStore = tbAppStoreMapper.selectByPrimaryKey(apiAppRequest.getId());
        if(null != tbAppStore ){
            apiAppRequest.setDownCount(tbAppStore.getDownCount() + 1);
            return tbAppStoreMapper.updateByPrimaryKeySelective(apiAppRequest);
        }
        return 0;

    }

    @Override
    public int updateTerminalPosition(APITerminalRequest apiTerminalRequest) {
        return tbTerminalMapper.updateByPrimaryKeySelective(apiTerminalRequest);
    }

    /**
     * 模糊查询
     * @param imei
     * @return
     */
    @Override
    public TbTerminal getTbTerminalInfo(String imei) {
        TbTerminalExample tbTerminalExample = new TbTerminalExample();
        TbTerminalExample.Criteria  tbTerminalCriteria = tbTerminalExample.createCriteria();
        tbTerminalCriteria.andImeiLike(imei +"%");
        List<TbTerminal> tbTerminalList = tbTerminalMapper.selectByExample(tbTerminalExample);
        if(!CollectionUtils.isEmpty(tbTerminalList)){
            if(tbTerminalList.size() == 1){
                return tbTerminalList.get(0);
            }
        }
        return null;
    }
}
