package com.appmarket.market.web.controller.admin;

import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.response.Result;
import com.appmarket.market.service.TbUserService;
import com.appmarket.market.utils.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/myInfo")
public class MyInfoController {
    private static final Logger logger = LoggerFactory.getLogger(MyInfoController.class);
    @Autowired
    private TbUserService sysUserService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView myInfo(HttpSession session, ModelAndView mv) {

        TbUser userVo = (TbUser) session.getAttribute("userInfo");
        String parentUser = null;


        mv.addObject("user",userVo);
        mv.setViewName("user/myInfo");
        return mv;
    }
    @RequestMapping(value = "/pwd",method = RequestMethod.GET)
    public ModelAndView pwd() {
        return new ModelAndView("user/modifyPwd");
    }
    @ResponseBody
    @RequestMapping(value = "/pwd",method = RequestMethod.POST)
    public Result editPwd(HttpSession session,String oldPwd,
                                String newPwd1,String newPwd2,String verifyCode) {
        String code = (String)session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        Result result = new Result(Result.Status.OK,"操作成功");
        if(!verifyCode.equals(code)){
            result.setStatus(Result.Status.ERROR);
            result.setMessage("验证码不正确");
            return result;
        }
        TbUser userVo = (TbUser) session.getAttribute("userInfo");

        if(!userVo.getPassword().equals(SecurityHelper.getMd5Hex(oldPwd.getBytes()))){

            result.setStatus(Result.Status.ERROR);
            result.setMessage("用户密码不正确");
            return result;
        }
        if(!newPwd1.equals(newPwd2)) {
            result.setStatus(Result.Status.ERROR);
            result.setMessage("两次输入密码不一致");
            return result;
        }
        TbUser vo = new TbUser();
        vo.setId(userVo.getId());
        vo.setName(userVo.getName());
        vo.setPassword(SecurityHelper.getMd5Hex(newPwd1.getBytes()));
        sysUserService.updateByIdSelective(vo);
        return result;
    }

}
