package com.lancheng.jneng.entity;

/**
 * Created by AFLF on 2016/8/2.
 */
public class BaseType<T> {
    private String  result;//	String	返回结果	否	0失败1成功
    private String detail;//	String	返回结果说明
    private T infor;

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

    public T getInfor() {
        return infor;
    }

    public void setInfor(T infor) {
        this.infor = infor;
    }
}
