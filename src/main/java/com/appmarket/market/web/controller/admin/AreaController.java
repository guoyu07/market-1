package com.appmarket.market.web.controller.admin;


import com.appmarket.market.bean.TbArea;
import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.request.TbAreaQry;
import com.appmarket.market.entity.response.Result;
import com.appmarket.market.service.TbAreaService;
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

import java.nio.charset.Charset;
import java.util.Date;

@Controller
@RequestMapping("/area")
public class AreaController {
    private static final Logger logger = LoggerFactory.getLogger(AreaController.class);
    @Autowired
    private TbAreaService tbAreaService;

    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions(value = "sysManage:areaManage:READ")
    public ModelAndView listView() {
        ModelAndView mav = new ModelAndView("sysManage/areaList");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.POST)
    @RequiresPermissions(value = "sysManage:areaManage:READ")
    public Result list(TbAreaQry query, @PageableDefault Pageable pageable) {
        Result result = new Result(Result.Status.ERROR,"查询失败");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        logger.info("queryPageList,{}\n{}",query,pageable);
        try {
            Page<TbArea> page = tbAreaService.queryPageList(query, pageable);
            result.setStatus(Result.Status.OK);
            result.setMessage(page);
        } catch (Exception e) {
            logger.error("UserController-list||sysError:",e);
            result.addError(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "del",method = RequestMethod.GET)
    @RequiresPermissions(value = "sysManage:areaManage:DELETE")
    public String del(Integer id) {
        try {
            TbArea area = new TbArea();
            area.setId(id);
            tbAreaService.deleteOne(area);
        } catch (Exception e) {
            logger.error("AreaController-del||sysError:",e);
        }
        return "redirect:/area";
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    @RequiresPermissions(value = "sysManage:areaManage:CREATE")
    public String add(TbArea area) {
        try {
            if(null == area.getId()) {
                area.setCreateTime(new Date());
                tbAreaService.insertOne(area);
            }else {
                tbAreaService.updateOne(area);
            }
        } catch (Exception e) {
            logger.error("AreaController-add||sysError:",e);
        }
        return "redirect:/area";
    }

    @RequestMapping(value = "toAdd",method = RequestMethod.GET)
    @RequiresPermissions(value = "sysManage:areaManage:CREATE")
    public ModelAndView toAdd(TbArea area) {
        ModelAndView mav = new ModelAndView("sysManage/editArea");
        if(null != area && null != area.getId()) {
            mav.addObject("area",area);
        }
        return mav;
    }



}
