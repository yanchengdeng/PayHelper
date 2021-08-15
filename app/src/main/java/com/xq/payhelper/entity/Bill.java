package com.xq.payhelper.entity;


/**
 * 监听到通知的时候生成一个账单,先保存在本地数据库,然后上传至服务器,如果成功会打上标记
 */
public class Bill {


    public static final String TABLE_NAME = "bill";
    public static final String _ID = "id";
    public static final String DATE = "create_date";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String MONEY = "money";
    public static final String SYNC = "sync";


    private long id;


    private String date;
    private String title;
    private String content;
    private String money;
    private int sync;


    public Bill() {
    }

    public Bill(String date, String title, String content, String money) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.money = money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }


    public void setSync(int sync) {
        this.sync = sync;
    }

    public int getSync() {
        return this.sync;
    }
}
