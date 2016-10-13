package com.lancheng.jneng.entity;

import java.util.List;

/**
 * Created by AFLF on 2016/9/6.获取新闻分类id
 */
public class CommunityIdType <T>{
    private String result;
    private String detail;
    private List <T>  list;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
        return "CommunityIdType{" +
                "result='" + result + '\'' +
                ", detail='" + detail + '\'' +
                ", list=" + list +
                '}';
    }
}
