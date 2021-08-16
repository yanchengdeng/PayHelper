package com.xq.payhelper.entity;

public class Response<T> {

    private int    code;
    private String msg;
    private T      content;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return content;
    }

    public void setErrorCode(int errorCode) {
        this.code = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.msg = errorMsg;
    }

    public void setData(T data) {
        this.content = data;
    }
}