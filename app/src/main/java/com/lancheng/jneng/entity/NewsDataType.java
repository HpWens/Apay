package com.lancheng.jneng.entity;

import java.util.List;

/**
 * Created by AFLF on 2016/7/26.新闻列表
 */
public class NewsDataType <T>{
   // result	String	返回结果	否
   // count	String	记录总数	否
   // pgcount	String	总页数	否
   // page	String	当前页	否
   // detail	String	返回结果说明	否
    private String result;
    private String count;
    private String page;
    private String detail;
    private String pgcount;
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

    public String getPgcount() {
        return pgcount;
    }

    public void setPgcount(String pgcount) {
        this.pgcount = pgcount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "NewsDataType{" +
                "result='" + result + '\'' +
                ", count='" + count + '\'' +
                ", page='" + page + '\'' +
                ", detail='" + detail + '\'' +
                ", pgcount='" + pgcount + '\'' +
                ", list=" + list +
                '}';
    }
}
