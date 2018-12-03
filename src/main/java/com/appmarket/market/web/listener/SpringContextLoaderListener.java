package com.appmarket.market.web.listener;


import com.appmarket.market.utils.PropertiesUtil;
import com.appmarket.market.utils.SnKeyUtil;
import com.appmarket.market.utils.SpringContextUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.File;
import java.io.IOException;

/**
 * 监听器，需要去web.xml配置
 * <listener>
     <listener-class>com.kingson.ssmweb.web.listener.SpringContextLoaderListener</listener-class>
  </listener>
 * Created by Admin on 2015/9/10.
 *
 * ContextLoaderListener监听器的作用就是启动Web容器时，自动装配ApplicationContext的配置信息。
 * 因为它实现了ServletContextListener这个接口，在web.xml配置这个监听器，启动容器时，就会默认执行它实现的方法
 */
public class SpringContextLoaderListener extends ContextLoaderListener {
    Logger logger = LoggerFactory.getLogger(SpringContextLoaderListener.class);

    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        SpringContextUtil.setApplicationContext(ctx);
        String sysEnv = PropertiesUtil.getEntryValue("sysEnv");
        context.setAttribute("sysEnv", sysEnv);
        String deployVersion = PropertiesUtil.getEntryValue("deployVersion");
        context.setAttribute("deployVersion", deployVersion);

        SnKeyUtil.SERVER = PropertiesUtil.getEntryValue("encryption.server");
        SnKeyUtil.PORT = Integer.valueOf(PropertiesUtil.getEntryValue("encryption.port"));

        try {
            String defThemeColor = "skin-blue";
            String defLoginLogoPic = "";
            String defLoginBtColor = "bg-olive";
            String defNavBarTitleName = "应用市场管理平台";

            ((WebApplicationContext) SpringContextUtil.getApplicationContext()).getServletContext().setAttribute("themeColor", defThemeColor);
            ((WebApplicationContext) SpringContextUtil.getApplicationContext()).getServletContext().setAttribute("loginLogoPic", defLoginLogoPic);
            ((WebApplicationContext) SpringContextUtil.getApplicationContext()).getServletContext().setAttribute("loginBtColor", defLoginBtColor);
            ((WebApplicationContext) SpringContextUtil.getApplicationContext()).getServletContext().setAttribute("navBarTitleName", defNavBarTitleName);

        } catch (Exception e) {
            logger.error("系统初始化异常，取系统初始化参数失败。", e);
        }

        String tmpFilePath = context.getRealPath("") +  PropertiesUtil.getEntryValue("tmp.pic.path");

        boolean runFlag = true;
        try {
            logger.info("删除临时文件路径：" + tmpFilePath);
             FileUtils.deleteDirectory(new File(tmpFilePath));
        } catch (IOException e) {
            runFlag = false;
            logger.error("删除临时文件异常。", e);
        }
        this.logger.info("系统启动{}。运行环境：{}", runFlag ? "成功" : "失败", sysEnv);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);
    }
}