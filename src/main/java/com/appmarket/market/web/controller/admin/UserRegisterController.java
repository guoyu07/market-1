package com.appmarket.market.web.controller.admin;

import com.appmarket.market.bean.TbUserRegister;
import com.appmarket.market.entity.response.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/register")
public class UserRegisterController {

    /**
     * 新增用户
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Result add(TbUserRegister register) {


        Result result = new Result(Result.Status.ERROR,"sucess");

//        Subject subject = SecurityUtils.getSubject();
//        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
//        logger.info("queryPageList,{}\n{}",query,pageable);
//        try {
//            Page<TbUser> page = sysUserService.queryPageList(query, pageable);
//            result.setStatus(Result.Status.OK);
//            result.setMessage(page);
//        } catch (Exception e) {
//            logger.error("UserController-list||sysError:",e);
//            result.addError(e.getMessage());
//        }
        return result;
    }
}
