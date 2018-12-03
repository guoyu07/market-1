package com.appmarket.market.web.controller.admin;

import com.appmarket.market.bean.TbOperateLog;
import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.request.TbOperateLogQry;
import com.appmarket.market.entity.response.Result;
import com.appmarket.market.service.TbOperatorLogService;
import com.appmarket.market.service.TbUserService;
import com.appmarket.market.utils.ServletUtils;
import com.google.code.kaptcha.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 2015/3/13.
 */
@Controller
@RequestMapping("/sys")
public class SysController {
    private static final Logger logger = LoggerFactory.getLogger(SysController.class);
    @Autowired
    private TbUserService sysUserService;

    @Autowired
    private TbOperatorLogService tbOperatorLogService;

    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    public String toLogin() {
        Subject subject = SecurityUtils.getSubject();
        // 已登陆则 跳到首页
        if (subject.isAuthenticated()) {
            return "redirect:/index";
        }
        return "/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(TbUser user, String verifycode, HttpServletRequest req, ModelAndView mav) {
        logger.info("用户登录");
        try {
            mav.setViewName("redirect:/index");
            /* Subject：主体，代表了当前“用户”，这个用户不一定是一个具体的人，与当前应用交互的任何东西都是Subject，如网络爬虫，机器人等；
            即一个抽象概念；所有Subject都绑定到SecurityManager，与Subject的所有交互都会委托给SecurityManager；
            可以把Subject认为是一个门面；SecurityManager才是实际的执行者*/
            Subject subject = SecurityUtils.getSubject();
            // 已登陆则 跳到首页
            if (subject.isAuthenticated()) {
                return mav;
            }
            String sessionKey = (String) req.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            logger.error("错误提示"+ sessionKey);
            if(!sessionKey.equals(verifycode)){
                mav.addObject("error", "请输入正确的验证码 ！");
                mav.setViewName("/login");
                return mav;
            }
            // 身份验证：SecurityManager负责真正的身份验证逻辑；它会委托给Authenticator进行身份验证；
            // 通过login登录，如果登录失败将抛出相应的AuthenticationException
            // Shiro提供了一个直接拿来用的UsernamePasswordToken，用于实现用户名/密码Token组
            subject.login(new UsernamePasswordToken(user.getName(), user.getPassword()));
            // 验证成功在Session中保存用户信息
            TbUser authUserInfo = sysUserService.queryById(user.getName());
            //authUserInfo.setLastTime(new Date());
            //sysUserService.updateById(authUserInfo);
            req.getSession().setAttribute("userInfo", authUserInfo);
            /*if (authUserInfo.getType() == 3) {
                MerchantInfo merchantInfo = this.merchantInfoService.queryById(authUserInfo.getPlatformAccount());
                req.getSession().setAttribute("agentMerchantInfo", merchantInfo);
            }
            userOperatorLogService.insertLog(user.getAccount(), "用户登录", "",ServletUtils.getClientIpAddress(req));*/
            tbOperatorLogService.insertLog(user.getName(), "用户登录", "", ServletUtils.getClientIpAddress(req));
        } catch (AuthenticationException e) {
            logger.error("login error:",e);
            // 身份验证失败
            mav.addObject("error", e.getMessage().toString());
            mav.setViewName("/login");
        }

        return mav;
    }

    /**
     * 用户登出
     * @param
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest req) {
        // 登出操作
        Subject subject = SecurityUtils.getSubject();

        try {
            TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
            tbOperatorLogService.insertLog(loginUser.getName(), "用户退出系统", "",ServletUtils.getClientIpAddress(req));
            req.getSession().removeAttribute("userInfo");
            //注销
            subject.logout();
        } catch (Exception e) {
            logger.error("logout error",e);
        }
        return "login";
    }
    @RequestMapping(value = "log",method = RequestMethod.GET)
    @RequiresPermissions(value = "sysManage:sysLogQry:READ")
    public ModelAndView listView() {
        return new ModelAndView("sysManage/sysLogList");
    }

    @ResponseBody
    @RequestMapping(value = "log",method = RequestMethod.POST)
    @RequiresPermissions(value = "sysManage:sysLogQry:READ")
    public Result list(TbOperateLogQry query, @PageableDefault Pageable pageable) {
        Result result = new Result(Result.Status.ERROR,"查询失败");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        Calendar cal = Calendar.getInstance();
        if(StringUtils.isEmpty(query.getEndDate()))
            query.setEndDate(sdf.format(cal.getTime()));
        if(StringUtils.isEmpty(query.getBeginDate())) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            query.setBeginDate(sdf.format(cal.getTime()));
        }
        try {
            Date bDate = sdf.parse(query.getBeginDate());
            Date eDate = sdf.parse(query.getEndDate());
            cal.setTime(eDate);
            cal.add(Calendar.MONTH,-3);
            if(cal.getTime().after(bDate)){
                result.setMessage("查询时间段不能超过3个月");
                return result;
            }
            query.setBegin(bDate);
            query.setEnd(eDate);
        } catch (ParseException e) {
            result.setMessage("请输入正确的查询时间段");
            return result;
        }
        Page<TbOperateLog> page = tbOperatorLogService.queryPageList(query,pageable);
        result.setStatus(Result.Status.OK);
        result.setMessage(page);

        return result;
    }



    @ResponseBody
    @RequestMapping(value = "/codeValid", method = RequestMethod.GET)
    public boolean checkValidateCode(String verifycode,HttpSession session){
        String sessionCode = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if(!sessionCode.equals(verifycode.toLowerCase())){
            return false;
        }
        return true;

    }

    /*
     * 如果Realm进行授权的话，应该继承AuthorizingRealm，其流程是：
     1.1、如果调用hasRole*，则直接获取AuthorizationInfo.getRoles()与传入的角色比较即可；
     1.2、首先如果调用如isPermitted(“user:view”)，首先通过PermissionResolver将权限字符串转换成相应的Permission实例，
     默认使用WildcardPermissionResolver，即转换为通配符的WildcardPermission；
     2、通过AuthorizationInfo.getObjectPermissions()得到Permission实例集合；通过AuthorizationInfo.
     getStringPermissions()得到字符串集合并通过PermissionResolver解析为Permission实例；然后获取用户的角色，并通过RolePermissionResolver解析角色对应的权限集合（默认没有实现，可以自己提供）；
     3、接着调用Permission. implies(Permission p)逐个与传入的权限比较，如果有匹配的则返回true，否则false。
     */

    /**
     * 基于角色 标识的权限控制案例
     */
    @RequestMapping(value = "/admin")
    @ResponseBody
    @RequiresRoles(value = "admin")
    public String admin() {
        //编程式角色授权，或者采用注解式授权 @RequiresRoles(value = "admin")
        // hasRole*进行角色验证，验证后返回true/false；而checkRole*验证失败时抛出AuthorizationException异常
       /* Subject subject = SecurityUtils.getSubject();
        if(!subject.hasRole("admin")){
            return "没有admin角色,不能能访问";
        }*/

        return "拥有admin角色,能访问";
    }

    /**
     * 基于权限标识的权限控制案例
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    @RequiresPermissions(value = "busiManage:acqMerchantInfo:CREATE")
    public String create() {
        //编程式权限标识授权，或者采用注解式授权 @RequiresPermissions(value = "busiManage:acqMerchantInfo:CREATE")
        //isPermitted*进行权限验证，验证后返回true/false；而checkPermission*验证失败时抛出AuthorizationException。
        /*  Subject subject = SecurityUtils.getSubject();
        if(!subject.isPermitted("busiManage:acqMerchantInfo:CREATE")){
            return "没有admin:create权限,不能访问";
        }*/
        return "拥有admin:create权限,能访问";
    }
}
