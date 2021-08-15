package com.xq.payhelper.entity;

public class Result <T> {

    private int code; // 0 成功 其他失败
    private String content;
    private String msg;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return code;
    }

    public void setStatus(int status) {
        this.code = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
