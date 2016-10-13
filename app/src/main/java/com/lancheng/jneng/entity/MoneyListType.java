package com.lancheng.jneng.entity;

import java.util.List;

/**
 * Created by AFLF on 2016/7/28.资金列表
 */
public class MoneyListType <T>{
    private String result;
    private String count;
    private String pgcount;
    private String page;
    private String detail;
    private List<T> list;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPgcount() {
        return pgcount;
    }

    public void setPgcount(String pgcount) {
        this.pgcount = pgcount;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "MoneyListType{" +
                "result='" + result + '\'' +
                ", count='" + count + '\'' +
                ", pgcount='" + pgcount + '\'' +
                ", page='" + page + '\'' +
                ", detail='" + detail + '\'' +
                ", list=" + list +
                '}';
    }
}
