package com.lancheng.jneng.entity;

/**
 * Created by AFLF on 2016/9/6.获取新闻分类id的list泪
 */
public class CommunityIdTypeInfo {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CommunityIdTypeInfo{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
