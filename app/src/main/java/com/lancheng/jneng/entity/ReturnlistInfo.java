package com.lancheng.jneng.entity;

/**
 * Created by AFLF on 2016/8/11.店铺回报列表
 */
public class ReturnlistInfo {
    private String id;//记录编号
    private String name;//":"投资名",							标题
    private String remark;//":"投资说明",						投资说明
    private String src;//":"/url",									图
    private String address;//":"冰激凌招加盟",							地址
    private String addtime;//":""
private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "ReturnlistInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", src='" + src + '\'' +
                ", address='" + address + '\'' +
                ", addtime='" + addtime + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
