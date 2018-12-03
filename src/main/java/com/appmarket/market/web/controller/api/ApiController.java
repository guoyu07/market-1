package com.appmarket.market.web.controller.api;


import com.appmarket.market.bean.*;
import com.appmarket.market.entity.request.APIAppRequest;
import com.appmarket.market.entity.request.APITerminalRequest;
import com.appmarket.market.entity.response.APIResponse;
import com.appmarket.market.entity.response.Result;
import com.appmarket.market.service.ApiService;
import com.appmarket.market.service.TbUserService;
import com.appmarket.market.service.TerminalService;
import com.appmarket.market.utils.SnKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by kingson.chan on 2017/4/20.
 * Email:chenjingxiong@yunnex.com.
 */
@Controller
@RequestMapping("/api/")
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);


    @Autowired
    ApiService apiService;

    @Autowired
    TbUserService sysUserService;

    @Autowired
    TerminalService terminalService;

    /**
     * 根据imei获取应用列表
     * @param apiTerminalRequest
     * @param pageable
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAppList",method = RequestMethod.POST)
    public APIResponse getAppList(@RequestBody  APITerminalRequest apiTerminalRequest,  @PageableDefault Pageable pageable) {
        APIResponse apiResponse = new APIResponse("-1","异常");
        try {
            List<TbAppStore> tbAppStoreList = apiService.getAppList(apiTerminalRequest);
            apiResponse.setCode("0");
            apiResponse.setData(tbAppStoreList);
        } catch (Exception e) {
            logger.error("ApiController-getAppList||sysError:",e);
            apiResponse.setData(e);
        }
        return apiResponse;
    }

    /**
     *
     * 获取白名单
     * @param apiTerminalRequest
     * @param pageable
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getWhiteList",method = RequestMethod.POST)
    public APIResponse getWhiteList(@RequestBody  APITerminalRequest apiTerminalRequest, @PageableDefault Pageable pageable) {
        APIResponse apiResponse = new APIResponse("-1","异常");
        try {
            List<TbSysAppStore> tbSysAppStoreList = apiService.getWhiteList(apiTerminalRequest);
            apiResponse.setCode("0");
            apiResponse.setData(tbSysAppStoreList);
        } catch (Exception e) {
            logger.error("ApiController-getWhiteList||sysError:",e);
            apiResponse.setData(e);
        }
        return apiResponse;
    }


    /**
     * 更新下载次数
     * @param apiAppRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateCount")
    public APIResponse updateCount(@RequestBody APIAppRequest apiAppRequest){
        APIResponse apiResponse = new APIResponse("-1","异常");
        if(apiService.updateCount(apiAppRequest) > 0){
            apiResponse.setCode("0");
            apiResponse.setData("成功");
        }
        return apiResponse;
    }


    /**
     * 更新终端位置
     * @param apiTerminalRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateTerminalPosition")
    public APIResponse updateTerminalPosition(@RequestBody  APITerminalRequest apiTerminalRequest, BindingResult bindingResult){
        APIResponse apiResponse = new APIResponse("-1","异常");
        if(apiService.updateTerminalPosition(apiTerminalRequest) > 0){
            apiResponse.setCode("0");
            apiResponse.setData("成功");
        }
        return apiResponse;
    }


    /**
     * 查询终端密钥
     * @param snKeyQry
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSnKey")
    public APIResponse getSnKey(@RequestBody @Valid SnKeyQry snKeyQry, BindingResult bindingResult){
        APIResponse apiResponse = new APIResponse("-1","异常");
        SnKey snKey = SnKeyUtil.getSnKey(snKeyQry.getSnid());
        if(null != snKey){
            apiResponse.setCode("0");
            apiResponse.setData(snKey);
        }
        return apiResponse;
    }

    /**
     * 生成终端密钥
     * @param snKeyQry
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/setSnKey")
    public APIResponse setSnKey(@RequestBody @Valid  SnKeyQry snKeyQry, BindingResult bindingResult){
        APIResponse apiResponse = new APIResponse("-1","异常");
        SnKey snKey = SnKeyUtil.setSnKey(snKeyQry.getSnid(),snKeyQry.getHwid());
        if(null != snKey){
            apiResponse.setCode("0");
            apiResponse.setData(snKey);
        }
        return apiResponse;
    }

    /**
     * 返回原始报文
     * @param snKeyQry
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/setSnKeyByte")
    public byte[] setSnKeyByByte(@RequestBody @Valid  SnKeyQry snKeyQry, BindingResult bindingResult){
        byte[] snKey = SnKeyUtil.setSnKeyByByte(snKeyQry.getSnid(),snKeyQry.getHwid());
        if(null != snKey){
            logger.info("返回值：" + SnKeyUtil.byteArray2HexString(snKey));
            return snKey;
        }
        byte[] result = new byte[1];
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updatePosition",method = {RequestMethod.POST,RequestMethod.GET})
    public APIResponse updatePosition(@RequestBody TbTerminal tbTerminal) {
        APIResponse apiResponse = new APIResponse("1","success");
        if(null == tbTerminal || org.apache.commons.lang3.StringUtils.isBlank(tbTerminal.getImei()) || null == tbTerminal.getShopLatitude() || null == tbTerminal.getShopLongitude()) {
            apiResponse.setData("updateFaild");
            apiResponse.setCode("-1");
            return apiResponse;
        }
        TbTerminal updateBean = new TbTerminal();
        updateBean.setImei(tbTerminal.getImei());
        updateBean.setShopLatitude(tbTerminal.getShopLatitude());
        updateBean.setShopLongitude(tbTerminal.getShopLongitude());
        updateBean.setUpdateTime(new Date());
        try {
            terminalService.update(updateBean);
        } catch (Exception e) {
            logger.error("更新app经纬度失败" + e.getMessage());
            apiResponse.setData("updateCommitFaild");
            apiResponse.setCode("-1");
        }
        return  apiResponse;
    }
}
