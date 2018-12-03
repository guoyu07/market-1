package com.appmarket.market.component;


import com.appmarket.market.entity.TbMenuVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class ContentHeaderTag extends TagSupport
{
    private String enName;
    private StringBuffer sb = null;

    private TbMenuVo activeNode = null;
    private TbMenuVo tmpNode = null;

    public void setEnName(String enName)
    {
        this.enName = enName;
    }

    public int doStartTag()
            throws JspException
    {
        HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
        List menus = (List)request.getSession().getAttribute("menus");
        JspWriter out = this.pageContext.getOut();
        boolean flag = true;
        try {
            getActiveNode(menus, this.enName);
            this.sb = new StringBuffer("</ol>");
            this.sb.insert(0, "<li class='active'>" + this.activeNode.getName() + "</li>");
            while (flag) {
                if ((this.activeNode != null) && (this.activeNode.getParentId().longValue() > 0L)) {
                    this.tmpNode = null;
                    getMenuVo(menus, this.activeNode.getParentId());
                    if (this.tmpNode != null) {
                        this.activeNode = this.tmpNode;
                        this.sb.insert(0, "<li>" + this.tmpNode.getName() + "</li>");
                    }
                } else {
                    flag = false;
                }
            }
            this.sb.insert(0, "<ol class='breadcrumb'><li><a href='" + request.getContextPath() + "/index'><i class='fa fa-dashboard'></i> 首页</a></li>");
            out.print(this.sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 5;
    }

    private void getActiveNode(List<TbMenuVo> menus, String enName) {
        if ((this.activeNode != null) && (enName.equals(this.activeNode.getShortEnName()))) return;
        for (TbMenuVo node : menus) {
            if (node.getShortEnName().equals(enName)) {
                this.activeNode = node;
            }
            if (node.getChildMenus().size() > 0)
                getActiveNode(node.getChildMenus(), enName);
        }
    }

    private void getMenuVo(List<TbMenuVo> menus, Integer menuId) {
        if ((this.tmpNode != null) && (menuId.equals(this.tmpNode.getId()))) return;
        for (TbMenuVo node : menus) {
            if (node.getId().equals(menuId)) {
                this.tmpNode = node;
            }
            if (node.getChildMenus().size() > 0)
                getMenuVo(node.getChildMenus(), menuId);
        }
    }
}
