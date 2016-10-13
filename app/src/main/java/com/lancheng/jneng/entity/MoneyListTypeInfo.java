package com.lancheng.jneng.entity;

/**
 * Created by AFLF on 2016/7/28.资金列表的list
 */
public class MoneyListTypeInfo {
    private String id;
    private String name;
    private String way;
    private String type;
    private String price;
    private String addtime;

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

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "MoneyListTypeInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", way='" + way + '\'' +
                ", type='" + type + '\'' +
                ", price='" + price + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
