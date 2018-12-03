package com.appmarket.market.component;


import com.appmarket.market.utils.StringEx;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Created by Admin on 2015/3/16.
 */
public class NumToCNMoneyTags extends TagSupport {
    private String money;
    private StringBuffer sb = null;
    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            if(StringUtils.isNotEmpty(money)){
                out.print(StringEx.numToCNMoney(money.trim()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Tag.SKIP_PAGE;
    }

}
