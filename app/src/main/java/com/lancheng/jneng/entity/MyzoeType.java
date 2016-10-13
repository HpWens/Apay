package com.lancheng.jneng.entity;

/**
 * Created by AFLF on 2016/8/2. 我的页面
 */
public class MyzoeType<T> {
    private String result;//	String	返回结果	否	0失败1成功
    private String detail;//	String	返回结果说明	否
    private MyzoeTypeInfo infor;

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

    public MyzoeTypeInfo getInfor() {
        return infor;
    }

    public void setInfor(MyzoeTypeInfo infor) {
        this.infor = infor;
    }

    @Override
    public String toString() {
        return "MyzoeType{" +
                "result='" + result + '\'' +
                ", detail='" + detail + '\'' +
                ", infor=" + infor +
                '}';
    }
}
