package com.appmarket.market.web.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.appmarket.market.bean.TbAppStore;
import com.appmarket.market.bean.TbArea;
import com.appmarket.market.bean.TbTerminal;
import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.request.TbAppStoreQry;
import com.appmarket.market.entity.request.TbAreaQry;
import com.appmarket.market.entity.request.TbTerminalQry;
import com.appmarket.market.entity.request.TbUserQry;
import com.appmarket.market.entity.response.Result;
import com.appmarket.market.entity.response.TbAppStoreResponse;
import com.appmarket.market.service.*;
import com.appmarket.market.utils.ServletUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by kingson.chan on 2017/4/19.
 * Email:chenjingxiong@yunnex.com.
 */
@Controller
@RequestMapping("/app/config")
public class AppConfigController {
    private static final Logger logger = LoggerFactory.getLogger(AppConfigController.class);

    @Autowired
    private TbOperatorLogService tbOperatorLogService;

    @Autowired
    TerminalService terminalService;

    @Autowired
    private AppService appService;

    @Autowired
    private TbUserService tbUserService;

    @Autowired
    private TbAreaService tbAreaService;

    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions(value = "appManage:appConfig:READ")
    public ModelAndView listView() {
        ModelAndView mav = new ModelAndView("appConfig/list");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");

        TbUserQry usrQry = new TbUserQry();
        usrQry.setIsQueryAll("Y");
        Page<TbUser>  users = tbUserService.queryPageList(usrQry,null);
        TbAreaQry areaQry = new TbAreaQry();
        areaQry.setIsQueryAll("Y");
        Page<TbArea>  areas = tbAreaService.queryPageList(areaQry, null);

        mav.addObject("areas",areas.getContent());
        mav.addObject("users",users.getContent());

        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @RequiresPermissions(value = "appManage:appConfig:READ")
    public Result list(TbTerminalQry query, @PageableDefault Pageable pageable) {
        Result result = new Result(Result.Status.ERROR,"查询失败");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        Integer role = loginUser.getType();
        if(role != 1){//一般角色
            query.setUid(loginUser.getId());
        }
        logger.info("queryPageList,{}\n{}",query,pageable);
        try {
            Page<TbTerminal> page = terminalService.queryPageList(query, pageable);
            TbUserQry usrQry = new TbUserQry();
            usrQry.setIsQueryAll("Y");
            Page<TbUser>  users = tbUserService.queryPageList(usrQry,null);
            Map<Integer, TbUser> userMap = new HashMap<>();
            for(TbUser user : users.getContent()) {
                userMap.put(user.getId(), user);
            }
            if(null != page && null != page.getContent()) for(TbTerminal terminal : page.getContent()) {
                if(null != terminal.getUid()) {
                    TbUser user = userMap.get(terminal.getUid());
                    if(null != user)
                        terminal.setUserIdName(user.getId() + "-" +user.getAccount());
                    else
                        terminal.setUserIdName(terminal.getUid().toString());
                }
            }
            result.setStatus(Result.Status.OK);
            result.setMessage(page);
        } catch (Exception e) {
            logger.error("UserController-list||sysError:",e);
            result.addError(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/showApp")
    @RequiresPermissions(value = "appManage:appConfig:READ")
    public ModelAndView showApp(@RequestParam("imeiJsonList") String imeiJsonList, @PageableDefault Pageable pageable) {
        JSONArray jsonArray = JSON.parseArray(imeiJsonList);
        List<String> imeiList = new ArrayList<>();
        String imeiJsonListTmp = "";
        for (Object o : jsonArray) {
            imeiList.add((String) o);
            imeiJsonListTmp = imeiJsonListTmp + (String) o + ",";
        }

        ModelAndView mav = new ModelAndView("appConfig/imeiApp");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        Integer role = loginUser.getType();
        TbAppStoreQry query = new TbAppStoreQry();
        if(role != 1){//一般角色
            query.setUid(loginUser.getId());

        }


        logger.info("queryPageList,{}\n{}",query,pageable);
        Page<TbAppStore> page = appService.queryPageList(query, pageable);

       
        List<Integer> appIdList = appService.queryAppIdListByImei(imeiList.get(0));
        List<TbAppStore> tbAppStoreList =page.getContent();
        List<TbAppStoreResponse> tbAppStoreResponses = new ArrayList<>();
        for (TbAppStore tbAppStore : tbAppStoreList) {
            TbAppStoreResponse tbAppStoreResponse = new TbAppStoreResponse();
            BeanUtils.copyProperties(tbAppStore,tbAppStoreResponse);
            tbAppStoreResponse.setIsCheck("false");
            if(imeiList.size()== 1) {
                for (Integer integer : appIdList) {
                    if (integer.intValue() == tbAppStoreResponse.getId().intValue()) {
                        tbAppStoreResponse.setIsCheck("true");
                    }
                }
            }
            tbAppStoreResponses.add(tbAppStoreResponse);
        }

        mav.addObject("tbAppStoreList",tbAppStoreResponses);
        mav.addObject("imeiJsonList",imeiJsonListTmp);
        return mav;

    }

    @RequestMapping(value = "/saveApp")
    @RequiresPermissions(value = "appManage:appConfig:CREATE")
    public String saveApp(@RequestParam("imeiJsonList") String imeiJsonList, @RequestParam("appJsonList") String appJsonList,
                          @PageableDefault Pageable pageable, HttpServletRequest req) {
        JSONArray jsonArray2 = JSON.parseArray(appJsonList);
        List<Integer> appList = new ArrayList<>();
        for (Object o : jsonArray2) {
            String id = (String) o;
            appList.add((Integer.valueOf(id)));
        }
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        List<String> imeiList = Arrays.asList(imeiJsonList.split(","));
        appService.saveAppByImei(imeiList,appList,loginUser);
        tbOperatorLogService.insertLog(loginUser.getName(), "存储应用",JSON.toJSONString(appList), ServletUtils.getClientIpAddress(req));
        return "redirect:/app/config";

    }


    @RequestMapping(value = "/show/{imei}", method = RequestMethod.GET)
    @RequiresPermissions(value = "appManage:appConfig:READ")
    public ModelAndView queryById(@PathVariable("imei") String imei) {
        ModelAndView mav = new ModelAndView("appConfig/showApp");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        List<TbAppStore> vo = appService.queryAppListByImei(imei);
        mav.addObject("tbAppStoreList",vo);
        return mav;
    }

}
