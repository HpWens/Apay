package com.lancheng.jneng.entity;

/**
 * Created by AFLF on 2016/8/4. 个人详细资料页面
 */
public class AccountTypeInfo {
    private String six;//":"",
    private String telephone;//":"",
    private String id;//":"",							用户编号
    private String username;//":"liujiong",					用户名
    private String name;//":"liujiong",						昵称
    private String rellname;//":"liujiong",					真实姓名
    private String avatar;//":"/images/head.jpg",				头像
    private String email;//":"527559158@qq.com",邮件
    private String qq;//":"527559158",QQ
    private String birthday;//":"2012-2-2",生日
    private String address;//":"address",家族地址
    private String hobby;//":"hobby",爱好
    private String introduction;//":"introduction",个人介绍
    private String moneys;//":1000000,可用金额
    private String logintimes;//:8登录次数

    public String getSix() {
        return six;
    }

    public void setSix(String six) {
        this.six = six;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRellname() {
        return rellname;
    }

    public void setRellname(String rellname) {
        this.rellname = rellname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMoneys() {
        return moneys;
    }

    public void setMoneys(String moneys) {
        this.moneys = moneys;
    }

    public String getLogintimes() {
        return logintimes;
    }

    public void setLogintimes(String logintimes) {
        this.logintimes = logintimes;
    }

    @Override
    public String toString() {
        return "AccountTypeInfo{" +
                "six='" + six + '\'' +
                ", telephone='" + telephone + '\'' +
                ", id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", rellname='" + rellname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", qq='" + qq + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", hobby='" + hobby + '\'' +
                ", introduction='" + introduction + '\'' +
                ", moneys='" + moneys + '\'' +
                ", logintimes='" + logintimes + '\'' +
                '}';
    }
}
