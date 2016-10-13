package com.lancheng.jneng.entity;

/**
 * Created by AFLF on 2016/7/26.登陆实体类
 */
public class LoginType{
   // result	String	返回结果
   // detail	String	返回结果说明
    private String result;
    private String detail;
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
