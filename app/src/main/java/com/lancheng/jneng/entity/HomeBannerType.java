package com.lancheng.jneng.entity;

import java.util.List;

/**
 * Created by AFLF on 2016/7/25.用于首页的banner数据
 */
public class HomeBannerType<T> {
    private String result;//    result	String	返回结果	否	0失败1成功
    private String count	;//String	记录总数	否
    private String detail;//	String	返回结果说明
    private List<T> list;//广告列表

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

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
        return "HomeBannerType{" +
                "result='" + result + '\'' +
                ", count='" + count + '\'' +
                ", detail='" + detail + '\'' +
                ", list=" + list +
                '}';
    }
}
