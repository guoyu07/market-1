package com.appmarket.market.entity.response;

import java.io.Serializable;

/**
 * Created by kingson.chan on 2017/4/21.
 * Email:chenjingxiong@yunnex.com.
 */
public class APIResponse implements Serializable {
    private static final long serialVersionUID = 5905715228490291386L;
    private String code;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public APIResponse(String code, Object data) {
        this.code = code;
        this.data = data;
    }

    public APIResponse() {

    }
}
