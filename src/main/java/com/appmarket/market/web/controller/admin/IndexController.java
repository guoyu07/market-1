package com.appmarket.market.web.controller.admin;


import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.TbMenuVo;
import com.appmarket.market.service.TbMenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/index")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private TbMenuService menuService;
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toIndex(HttpSession session,
                                HttpServletRequest request,ModelAndView mv) {
        List<TbMenuVo> menus = (List<TbMenuVo>) session.getAttribute("menus");
        TbUser userVo = (TbUser) session.getAttribute("userInfo");
        Subject subject = SecurityUtils.getSubject();
        if(userVo == null && subject.isAuthenticated())
            subject.logout();
        //如果登录成功调用isAuthenticated就会返回true，即已经通过身份验证
        if (!subject.isAuthenticated()) {
            mv.setViewName("login");
            return mv;
        }
        if(menus == null || menus.size() < 1){
            menus=menuService.selectMenuByUserId(userVo.getName());
        }
        session.setAttribute("menus", menus);
        mv.setViewName("index");
        return mv;
    }
}
