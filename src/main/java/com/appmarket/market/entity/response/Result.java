package com.appmarket.market.entity.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by Kingson.chan on 2017/2/20.
 * Email:chenjingxiong@yunnex.com.
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 5905715228490291386L;
    private Result.Status status;
    private Object message;

    public Result() {
    }

    public Result(Result.Status status, Object message) {
        this.status = status;
        this.message = message;
    }

    public void addOK(Object message) {
        this.message = message;
        this.status = Result.Status.OK;
    }

    public void addError(Object message) {
        this.message = message;
        this.status = Result.Status.ERROR;
    }

    public Result.Status getStatus() {
        return this.status;
    }

    public void setStatus(Result.Status status) {
        this.status = status;
    }

    public Object getMessage() {
        return this.message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static enum Status {
        OK,
        ERROR;

        private Status() {
        }
    }
}
