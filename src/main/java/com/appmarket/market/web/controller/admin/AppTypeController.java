package com.appmarket.market.web.controller.admin;


import com.appmarket.market.bean.TbAppType;
import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.request.TbAppTypeQry;
import com.appmarket.market.entity.response.Result;
import com.appmarket.market.service.TbAppTypeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping("/app/type")
public class AppTypeController {
    private static final Logger logger = LoggerFactory.getLogger(AppTypeController.class);
    @Autowired
    private TbAppTypeService tbAppTypeService;

    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions(value = "sysManage:appTypeManage:READ")
    public ModelAndView listView() {
        ModelAndView mav = new ModelAndView("sysManage/appTypeList");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.POST)
    @RequiresPermissions(value = "sysManage:appTypeManage:READ")
    public Result list(TbAppTypeQry query, @PageableDefault Pageable pageable) {
        Result result = new Result(Result.Status.ERROR,"查询失败");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        logger.info("queryPageList,{}\n{}",query,pageable);
        try {
            Page<TbAppType> page = tbAppTypeService.queryPageList(query, pageable);
            result.setStatus(Result.Status.OK);
            result.setMessage(page);
        } catch (Exception e) {
            logger.error("UserController-list||sysError:",e);
            result.addError(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "del",method = RequestMethod.GET)
    @RequiresPermissions(value = "sysManage:appTypeManage:DELETE")
    public String del(Integer id) {
        try {
            TbAppType area = new TbAppType();
            area.setId(id);
            tbAppTypeService.deleteOne(area);
        } catch (Exception e) {
            logger.error("AppTypeController-del||sysError:",e);
        }
        return "redirect:/app/type";
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    @RequiresPermissions(value = "sysManage:appTypeManage:CREATE")
    public String add(TbAppType area) {
        try {
            if(null == area.getId()) {
                area.setCreateTime(new Date());
                tbAppTypeService.insertOne(area);
            } else
                tbAppTypeService.updateOne(area);
        } catch (Exception e) {
            logger.error("AppTypeController-add||sysError:",e);
        }
        return "redirect:/app/type";
    }

    @RequestMapping(value = "toAdd",method = RequestMethod.GET)
    @RequiresPermissions(value = "sysManage:appTypeManage:CREATE")
    public ModelAndView toAdd(TbAppType area) {
        ModelAndView mav = new ModelAndView("sysManage/editAppType");
        if(null != area && null != area.getId()) {
            mav.addObject("type",area);
        }
        return mav;
    }



}
