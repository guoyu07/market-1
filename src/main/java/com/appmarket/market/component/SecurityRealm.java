package com.appmarket.market.component;


import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.TbRoleVo;
import com.appmarket.market.service.TbMenuService;
import com.appmarket.market.service.TbRoleService;
import com.appmarket.market.service.TbUserService;
import com.appmarket.market.utils.SecurityHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户身份验证,授权 Realm 组件
 * 域，Shiro从从Realm获取安全数据（如用户、角色、权限），就是说SecurityManager要验证用户身份，
 * 那么它需要从Realm获取相应的用户进行比较以确定用户身份是否合法；
 * 也需要从Realm得到用户相应的角色/权限进行验证用户是否能进行操作；
 * 可以把Realm看成DataSource，即安全数据源。
 **/
public class SecurityRealm extends AuthorizingRealm {

    @Resource
    private TbUserService sysUserService;

    @Resource
    private TbRoleService roleService;

    @Resource
    private TbMenuService menuService;

    /**
     * 获取授权信息
     * 为当前登录的Subject授予角色和权限
     * 该方法的调用时机为需授权资源被访问时
     * 并且每次访问需授权资源时都会执行该方法中的逻辑，如果开启了缓存，就不需要每次进行该方法调用
     * 本项目已经开启了缓存，shiroCacheManager
     *
     * Authrizer：授权器，或者访问控制器，用来决定主体是否有权限进行相应的操作；
     * 即控制着用户能访问应用中的哪些功能；
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = (SimpleAuthorizationInfo) this.getSession("authUserPermission");

        if(authorizationInfo == null)
            authorizationInfo = new SimpleAuthorizationInfo();
        else
            return authorizationInfo;
        //principals：身份，即主体的标识属性，可以是任何东西，如用户名、邮箱等，唯一即可。
        // 一个主体可以有多个principals，但只有一个Primary principals，一般是用户名/密码/手机号。
        String account = String.valueOf(principals.getPrimaryPrincipal());
        //从数据库中获取当前登录用户的详细信息
        final TbUser user = sysUserService.queryById(account);
        final List<TbRoleVo> roleInfos = roleService.getRolesByUserId(user.getName());
        List<String> strPermission = new ArrayList<String>();
        for (TbRoleVo role : roleInfos) {
            //为当前用户设置角色
            authorizationInfo.addRole("admin");
            final List<Map<String,Object>> permissions = menuService.selectPermissionsByRoleId(role.getId());
            String sign = null;
            String[] per = null;
            //实体类Role中包含有角色权限的实体类信息
            for (Map<String,Object> map: permissions) {
                // 添加权限
                per = String.valueOf(map.get("permission")).split(";");
                sign = String.valueOf(map.get("permission_sign"));
                for (String s : per) {
                    if(!(strPermission.contains(sign+s)))
                        strPermission.add(sign + s);
                }
            }
        }
        //为当前用户设置权限
        authorizationInfo.addStringPermissions(strPermission);
        this.setSession("authUserPermission",authorizationInfo);
        return authorizationInfo;
    }

    /**
     * 获取身份认证信息，验证当前登录的Subject
     * 该方法的调用时机为SysController.login()方法中执行Subject.login()时
     *
     * Authenticator：认证器，负责主体认证的，这是一个扩展点，如果用户觉得Shiro默认的不好，
     * 可以自定义实现；其需要认证策略（Authentication Strategy），即什么情况下算用户认证通过了；
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取基于用户名和密码的令牌
        //实际上这个token是从SysController里面subject.login(token)传过来的
        //两个token的引用都是一样的
        String account = String.valueOf(token.getPrincipal());//获取身份
        String password = SecurityHelper.getMd5Hex(new String((char[]) token.getCredentials()).getBytes());//获取凭据
        // 通过数据库进行验证
        TbUser qry = new TbUser();
        qry.setName(account);
        qry.setPassword(password);
        TbUser authentication = sysUserService.authentication(qry);
        if (authentication == null) {
            throw new AuthenticationException("用户名或密码错误.");
        }
        if("OFF".equals(authentication.getStatus())){
            throw new AuthenticationException("用户状态为失效.");
        }
        //AuthenticationInfo提供给SecurityManager来创建Subject（提供身份信息）
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(account, password, getName());
        return authenticationInfo;
    }
    /**
     * * 将一些数据放到ShiroSession中,以便于其它地方使用
     *   比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
     */
    private void setSession(Object key, Object value){
        Subject currentUser = SecurityUtils.getSubject();
        if(null != currentUser){
            Session session = currentUser.getSession();
            if(null != session){
                session.setAttribute(key, value);
            }
        }
    }

    private Object getSession(Object key){
        Subject currentUser = SecurityUtils.getSubject();
        if(null != currentUser){
            Session session = currentUser.getSession();
            if(null != session){
                return session.getAttribute(key);
            }
        }
        return null;
    }
}
