package com.appmarket.market.service.impl;

import com.appmarket.market.bean.*;
import com.appmarket.market.entity.request.TbAppStoreQry;
import com.appmarket.market.entity.request.TbSysAppStoreQry;
import com.appmarket.market.mapper.TbAppStoreMapper;
import com.appmarket.market.mapper.TbImeiAppMapper;
import com.appmarket.market.mapper.TbSysAppStoreMapper;
import com.appmarket.market.service.AppService;
import com.appmarket.market.utils.ParamsUtils;
import com.kingson.common.utils.ObjectUtil;
import com.kingson.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingson.chan on 2017/4/18.
 * Email:chenjingxiong@yunnex.com.
 */
@Service
public class AppServiceImpl implements AppService {
    @Autowired
    TbAppStoreMapper tbAppStoreMapper;

    @Autowired
    TbSysAppStoreMapper tbSysAppStoreMapper;

    @Autowired
    TbImeiAppMapper tbImeiAppMapper;

    @Override
    public Page<TbAppStore> queryPageList(TbAppStoreQry query, Pageable pageable) {
        TbAppStoreExample tbAppStoreExample = new TbAppStoreExample();

        //设置查询分页
        com.kingson.common.Plugin.Page page = new com.kingson.common.Plugin.Page();
        if(ObjectUtil.isNotObjectEmpty(pageable)){
            page.setBegin(pageable.getOffset());
            page.setLength(pageable.getPageSize());
            //设置查询排序
            String orderStr = ParamsUtils.orderString(pageable);
            if(StringUtil.isNotBlank(orderStr)){
                tbAppStoreExample.setOrderByClause(ParamsUtils.orderString(pageable));
            }
        }
        else {
            page.setBegin(0);
            page.setLength(20);
        }
        tbAppStoreExample.setPage(page);
        TbAppStoreExample.Criteria tbAppStoreCriteria =  tbAppStoreExample.createCriteria();

        if(null != query.getUid()){
            tbAppStoreCriteria.andUidEqualTo(query.getUid());
        }

        if(StringUtil.isNotBlank(query.getNameLike())){
            tbAppStoreCriteria.andAppNameLike("%" + query.getNameLike() + "%");
        }
        if(StringUtil.isNotBlank(query.getPackageName())){
            tbAppStoreCriteria.andPackageNameLike("%"+ query.getPackageName() + "%");
        }
        if(StringUtil.isNotBlank(query.getAppTypeName())){
            tbAppStoreCriteria.andAppTypeEqualTo(query.getAppTypeName());
        }
        if(StringUtil.isNotBlank(query.getUidName())){
            tbAppStoreCriteria.andUidEqualTo(Integer.parseInt(query.getUidName()));
        }

        /*
        if(StringUtil.isNotBlank(query.getBeginDate())  &&StringUtil.isNotBlank(query.getEndDate())){
            try {
                tbTerminalCriteria.andCreateTimeBetween(DateUtil.parse(query.getBeginDate()+" 00:00:00","yyyy-MM-dd HH:mm:ss"),(DateUtil.parse(query.getEndDate()+" 23:59:59","yyyy-MM-dd HH:mm:ss")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }*/
        List<TbAppStore> tbAppStoreList = tbAppStoreMapper.selectByExample(tbAppStoreExample);
        //设置总数
        int count = tbAppStoreMapper.countByExample(tbAppStoreExample);
        return new PageImpl(tbAppStoreList, pageable, count);
    }

    @Override
    @Transactional
    public int saveOrUpdateList(List<TbAppStore> tbAppStoreList, TbUser tbUser) {
        for (TbAppStore tbAppStore : tbAppStoreList) {
            tbAppStoreMapper.insertSelective(tbAppStore);
        }
        return 1;
    }

    @Override
    public int deleteAppInfo(Integer appId, Integer uid) {
        return tbAppStoreMapper.deleteByPrimaryKey(appId);
    }

    @Override
    public TbAppStore queryById(Integer appId) {
        return tbAppStoreMapper.selectByPrimaryKey(appId);
    }

    @Override
    public int insertApp(TbAppStore tbAppStore) {
        return tbAppStoreMapper.insertSelective(tbAppStore);
    }

    @Override
    public int updateApp(TbAppStore tbAppStore) {
        return tbAppStoreMapper.updateByPrimaryKeySelective(tbAppStore);
    }

    @Override
    public Page<TbSysAppStore> queryPageWhiteList(TbSysAppStoreQry query, Pageable pageable) {
        TbSysAppStoreExample tbSysAppStoreExample = new TbSysAppStoreExample();

        //设置查询分页
        com.kingson.common.Plugin.Page page = new com.kingson.common.Plugin.Page();
        if(ObjectUtil.isNotObjectEmpty(pageable)){
            page.setBegin(pageable.getOffset());
            page.setLength(pageable.getPageSize());
            //设置查询排序
            String orderStr = ParamsUtils.orderString(pageable);
            if(StringUtil.isNotBlank(orderStr)){
                tbSysAppStoreExample.setOrderByClause(ParamsUtils.orderString(pageable));
            }
        }
        else {
            page.setBegin(0);
            page.setLength(20);
        }
        tbSysAppStoreExample.setPage(page);
        TbSysAppStoreExample.Criteria tbSysAppStoreCriteria =  tbSysAppStoreExample.createCriteria();

        if(null != query.getUid()){
            tbSysAppStoreCriteria.andUidEqualTo(query.getUid());
        }

        if(StringUtil.isNotBlank(query.getNameLike())){
            tbSysAppStoreCriteria.andPacketNameLike("%" + query.getNameLike() + "%");
        }

        /*
        if(StringUtil.isNotBlank(query.getBeginDate())  &&StringUtil.isNotBlank(query.getEndDate())){
            try {
                tbTerminalCriteria.andCreateTimeBetween(DateUtil.parse(query.getBeginDate()+" 00:00:00","yyyy-MM-dd HH:mm:ss"),(DateUtil.parse(query.getEndDate()+" 23:59:59","yyyy-MM-dd HH:mm:ss")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }*/
        List<TbSysAppStore> tbSysAppStoreList = tbSysAppStoreMapper.selectByExample(tbSysAppStoreExample);
        //设置总数
        int count = tbSysAppStoreMapper.countByExample(tbSysAppStoreExample);
        return new PageImpl(tbSysAppStoreList, pageable, count);
    }

    @Override
    public int deleteSysAppInfo(Integer appId, Integer uid) {
        return tbSysAppStoreMapper.deleteByPrimaryKey(appId);
    }

    @Override
    public int insertSysApp(TbSysAppStore tbSysAppStore) {
        return tbSysAppStoreMapper.insertSelective(tbSysAppStore);
    }

    @Override
    public int updateSysApp(TbSysAppStore tbSysAppStore) {
        return tbSysAppStoreMapper.updateByPrimaryKeySelective(tbSysAppStore);
    }

    @Override
    public TbSysAppStore querySysAppById(Integer appId) {
        return tbSysAppStoreMapper.selectByPrimaryKey(appId);
    }

    @Override
    @Transactional
    public int saveAppByImei(List<String> imeiList, List<Integer> appIdList,TbUser tbUser) {
        for (String s : imeiList) {
            if(StringUtil.isNotBlank(s)){
                TbImeiAppExample tbImeiAppExample = new TbImeiAppExample();
                TbImeiAppExample.Criteria tbImeiAppCriteria = tbImeiAppExample.createCriteria();
                tbImeiAppCriteria.andImeiNoEqualTo(s);
                tbImeiAppMapper.deleteByExample(tbImeiAppExample);
                for (Integer integer : appIdList) {
                    TbImeiApp tbImeiApp = new TbImeiApp();
                    tbImeiApp.setImeiNo(s);
                    tbImeiApp.setUid(tbUser.getId());
                    tbImeiApp.setAppId(integer);
                    tbImeiAppMapper.insertSelective(tbImeiApp);
                }
            }
        }
        return 0;
    }

    @Override
    public List<TbAppStore> queryAppListByImei(String imei) {
        TbImeiAppExample tbImeiAppExample = new TbImeiAppExample();
        TbImeiAppExample.Criteria tbImeiAppCriteria = tbImeiAppExample.createCriteria();
        tbImeiAppCriteria.andImeiNoEqualTo(imei);
        List<TbImeiApp> tbImeiAppList = tbImeiAppMapper.selectByExample(tbImeiAppExample);
        List<Integer> appIdlist = new ArrayList<>();
        if(!CollectionUtils.isEmpty(tbImeiAppList)){
            for (TbImeiApp tbImeiApp : tbImeiAppList) {
                appIdlist.add(tbImeiApp.getAppId());
            }
            TbAppStoreExample tbAppStoreExample = new TbAppStoreExample();
            TbAppStoreExample.Criteria tbAppStoreCriteria = tbAppStoreExample.createCriteria();
            tbAppStoreCriteria.andIdIn(appIdlist);
            return tbAppStoreMapper.selectByExample(tbAppStoreExample);
        }

        return null;

    }

    @Override
    public List<Integer> queryAppIdListByImei(String imei) {
        TbImeiAppExample tbImeiAppExample = new TbImeiAppExample();
        TbImeiAppExample.Criteria tbImeiAppCriteria = tbImeiAppExample.createCriteria();
        tbImeiAppCriteria.andImeiNoEqualTo(imei);
        List<TbImeiApp> tbImeiAppList = tbImeiAppMapper.selectByExample(tbImeiAppExample);
        List<Integer> appIdlist = new ArrayList<>();
        if(!CollectionUtils.isEmpty(tbImeiAppList)){
            for (TbImeiApp tbImeiApp : tbImeiAppList) {
                appIdlist.add(tbImeiApp.getAppId());
            }
        }
        return appIdlist;
    }

    @Override
    public TbAppStore querySysAppByPackageName(TbAppStore tbAppStore,TbUser loginUser) {
        TbAppStoreExample tbAppStoreExample =new TbAppStoreExample();
        TbAppStoreExample.Criteria  tbAppStoreCriteria = tbAppStoreExample.createCriteria();
        tbAppStoreCriteria.andPackageNameEqualTo(tbAppStore.getPackageName());
        tbAppStoreCriteria.andUidEqualTo(loginUser.getId());
        if(null != tbAppStore.getId()){
            tbAppStoreCriteria.andIdNotEqualTo(tbAppStore.getId());
        }
       
        List<TbAppStore> tbAppStoreList = tbAppStoreMapper.selectByExample(tbAppStoreExample);
        if(!CollectionUtils.isEmpty(tbAppStoreList)){
            return tbAppStoreList.get(0);
        }
        return null;
    }
}

