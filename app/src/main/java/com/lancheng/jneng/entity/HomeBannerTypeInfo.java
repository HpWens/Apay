package com.lancheng.jneng.entity;

/**
 * Created by AFLF on 2016/7/25.用于首页的banner数据的list
 */
public class HomeBannerTypeInfo {
   // ":[{"name":"一米阳光","linkurl":"http://www.ymiyg.com","imgurl":"/images/pao.jpg"
    private String name;
    private String linkurl;
    private String imgurl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "HomeBannerTypeInfo{" +
                "name='" + name + '\'' +
                ", linkurl='" + linkurl + '\'' +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }
}
