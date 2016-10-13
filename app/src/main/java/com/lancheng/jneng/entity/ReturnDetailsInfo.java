package com.lancheng.jneng.entity;

/**
 * Created by AFLF on 2016/8/2./回报详情
 */
public class ReturnDetailsInfo {
    private String id;//":"1",							记录编号
    private String name;//":"",							投资项目名
    private String sales;//":"0",							销售额
    private String cost;//":"0",							成本
    private String runcost;//":"0",						管理费
    private String profit;//":"0",							最终利润
    private String addtime;//":""

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

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRuncost() {
        return runcost;
    }

    public void setRuncost(String runcost) {
        this.runcost = runcost;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "ReturnDetailsInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sales='" + sales + '\'' +
                ", cost='" + cost + '\'' +
                ", runcost='" + runcost + '\'' +
                ", profit='" + profit + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
