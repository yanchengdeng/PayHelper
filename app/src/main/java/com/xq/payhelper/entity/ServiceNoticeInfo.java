package com.xq.payhelper.entity;

/**
 * use  : yanc
 * data : 2021/8/16
 * time : 3:09
 * desc :
 */
public class ServiceNoticeInfo {
    private int type;
    private String msg;

    public ServiceNoticeInfo(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
