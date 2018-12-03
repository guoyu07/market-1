package com.appmarket.market.component;


import com.appmarket.market.entity.TbMenuVo;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class MenuTags extends TagSupport
{
    private String enName;
    private TbMenuVo activeNode = null;
    private StringBuffer sb = null;

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public int doStartTag() throws JspException
    {
        HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
        String ctx = request.getContextPath();
        List<TbMenuVo> menus = (List)request.getSession().getAttribute("menus");
        JspWriter out = this.pageContext.getOut();
        try {
            if (StringUtils.isNotEmpty(this.enName))
                getActiveNode(menus, this.enName);
            this.sb = new StringBuffer("<ul class='sidebar-menu'>");
            String strCls = "";
            for (TbMenuVo vo : menus) {
                strCls = (this.activeNode != null) && (vo.getId().equals(this.activeNode.getParentId())) ? "treeview active" : "treeview";
                if (StringUtils.isEmpty(vo.getUrl()))
                    this.sb.append("<li class='" + strCls + "'><a href='javascript:void(0);'><span>" + vo.getName() + "</span><i class='fa fa-angle-left pull-right'></i></a>");
                else
                    this.sb.append("<li class='" + strCls + "'><a href='" + ctx + vo.getUrl() + "'><span>" + vo.getName() + "</span><i class='fa fa-angle-left pull-right'></i></a>");
                if (vo.getChildMenus().size() > 0) {
                    this.sb.append("<ul class='treeview-menu'>");
                    for (TbMenuVo child : vo.getChildMenus()) {
                        strCls = child.getShortEnName().equals(this.enName) ? "active" : "";
                        this.sb.append("<li class='" + strCls + "'><a href='" + ctx + child.getUrl() + "'><i class='fa fa-angle-double-right'></i>" + child.getName() + "</a></li>");
                    }
                    this.sb.append("</ul>");
                }
                this.sb.append("</li>");
            }

            this.sb.append("</ul>");
            out.print(this.sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Tag.SKIP_PAGE;//忽略标签后面的JSP内容
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
}
