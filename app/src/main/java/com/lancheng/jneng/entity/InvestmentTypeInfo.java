package com.lancheng.jneng.entity;

/**
 * 投资列表详情
 * Created by AFLF on 2016/7/31.
 */
public class InvestmentTypeInfo {

    private String id;//服务编号
    
    private String name;//标题
    private String src;//":"/url",									图
    private String address;//":"冰激凌招加盟",							地址
    private String price;//总投资
    private String deadline;//投资期限
    private String status;//投资状态
    private String interest;//年利
    private String investment;//已投资比
    private String point;//奖励
    private String remark;//备注说明
    private String content;//投资详情
    private String addtime;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInvestment() {
        return investment;
    }

    public void setInvestment(String investment) {
        this.investment = investment;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "InvestmentTypeInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", src='" + src + '\'' +
                ", address='" + address + '\'' +
                ", price='" + price + '\'' +
                ", deadline='" + deadline + '\'' +
                ", status='" + status + '\'' +
                ", interest='" + interest + '\'' +
                ", investment='" + investment + '\'' +
                ", point='" + point + '\'' +
                ", remark='" + remark + '\'' +
                ", content='" + content + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
