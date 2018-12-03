package com.appmarket.market.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kingson.chan on 2017/2/4.
 * Email:chenjingxiong@yunnex.com.
 * 过滤器JavaWeb三大组件之一，它与Servlet很相似！不它过滤器是用来拦截请求的，而不是处理请求的
 *
 * 过滤器的应用场景：
    执行目标资源之前做预处理工作，例如设置编码，这种试通常都会放行，只是在目标资源执行之前做一些准备工作
    通过条件判断是否放行，例如校验当前用户是否已经登录，或者用户IP是否已经被禁用；
    在目标资源执行后，做一些后续的特殊处理工作，例如把目标资源输出的数据进行处理
 */
public class CommonFilter implements Filter{
    private static final Logger logger = LoggerFactory.getLogger(CommonFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;


        String contextPath = httpServletRequest.getContextPath();
        String requestUri = httpServletRequest.getRequestURI();
        Object obj = httpServletRequest.getSession().getAttribute("userInfo");

        if (null == obj
                && !requestUri.startsWith(contextPath + "/resources")
                && !requestUri.startsWith(contextPath + "/api")
                && !requestUri.startsWith(contextPath + "/sys/codeValid")
                && !requestUri.startsWith(contextPath + "/sys/toLogin")
                && !requestUri.startsWith(contextPath + "/sys/logout")
                && !requestUri.startsWith(contextPath + "/sys/login")) { //未登录
            if (httpServletRequest.getHeader("x-requested-with") != null && httpServletRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) { //如果是ajax请求响应头会有，x-requested-with
                httpServletResponse.setHeader("sessionstatus", "timeout");//在响应头设置session状态
                return;
            }

        }
        chain.doFilter(request, response);
    }


}
