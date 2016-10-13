package com.lancheng.jneng.entity;

import java.util.List;

/**
 * Created by AFLF on 2016/7/26.注册的实体类
 */
public class RegisterType <T>{
    //result	String	返回结果	否	0失败1成功
    //detail	String	返回结果说明
   // infor	json	用户信息
    private String result;
    private String detail;
    private List<T> infor;

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

    public List<T> getInfor() {
        return infor;
    }

    public void setInfor(List<T> infor) {
        this.infor = infor;
    }

    @Override
    public String toString() {
        return "RegisterType{" +
                "result='" + result + '\'' +
                ", detail='" + detail + '\'' +
                ", infor=" + infor +
                '}';
    }
}
