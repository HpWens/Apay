package com.lancheng.jneng.entity;

/**
 * Created by AFLF on 2016/8/2.我的页面实体类
 */
public class MyzoeTypeInfo {
    private String moneys;//":"999990.00",可用余额
    private String freeze;//":"0",冻结金额
    private String turnover;//"0",体验金额
    private String transfer;//"0"转出金额
    private String username;//":"							用户名
    private String rellname;//":"",							真实姓名
    private String head;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRellname() {
        return rellname;
    }

    public void setRellname(String rellname) {
        this.rellname = rellname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getMoneys() {
        return moneys;
    }

    public void setMoneys(String moneys) {
        this.moneys = moneys;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    @Override
    public String toString() {
        return "MyzoeTypeInfo{" +
                "moneys='" + moneys + '\'' +
                ", freeze='" + freeze + '\'' +
                ", turnover='" + turnover + '\'' +
                ", transfer='" + transfer + '\'' +
                ", username='" + username + '\'' +
                ", rellname='" + rellname + '\'' +
                ", head='" + head + '\'' +
                '}';
    }
}
