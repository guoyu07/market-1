package com.appmarket.market.service;

import com.appmarket.market.bean.TbAppStore;
import com.appmarket.market.bean.TbSysAppStore;
import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.request.TbAppStoreQry;
import com.appmarket.market.entity.request.TbSysAppStoreQry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by kingson.chan on 2017/4/18.
 * Email:chenjingxiong@yunnex.com.
 */
public interface AppService {
    Page<TbAppStore> queryPageList(TbAppStoreQry query, Pageable pageable);

    int saveOrUpdateList(List<TbAppStore> tbAppStoreList, TbUser tbUser);

    int deleteAppInfo(Integer appId,Integer uid);

    TbAppStore queryById(Integer appId);

    int insertApp(TbAppStore tbAppStore);

    int updateApp(TbAppStore tbAppStore);

    Page<TbSysAppStore> queryPageWhiteList(TbSysAppStoreQry query, Pageable pageable);

    int deleteSysAppInfo(Integer appId,Integer uid);

    int insertSysApp(TbSysAppStore tbSysAppStore);

    int updateSysApp(TbSysAppStore tbSysAppStore);

    TbSysAppStore querySysAppById(Integer appId);

    int saveAppByImei(List<String> imeiList,List<Integer> appIdList,TbUser tbUser);

    List<TbAppStore> queryAppListByImei(String imei);

    List<Integer> queryAppIdListByImei(String imei);

    TbAppStore querySysAppByPackageName(TbAppStore tbAppStore,TbUser loginUser);

}
