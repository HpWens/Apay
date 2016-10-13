package com.lancheng.jneng.entity;

/**
 * Created by AFLF on 2016/7/26.新闻列表
 */
public class NewsDataTypeListInfo {

    private String id;
    private String name;
    private String chicknumber;
    private String addtime;
    private String editor;
    private String sourse;
    private String content;
    private String remark;

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

    public String getChicknumber() {
        return chicknumber;
    }

    public void setChicknumber(String chicknumber) {
        this.chicknumber = chicknumber;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getSourse() {
        return sourse;
    }

    public void setSourse(String sourse) {
        this.sourse = sourse;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "NewsDataTypeListInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", chicknumber='" + chicknumber + '\'' +
                ", addtime='" + addtime + '\'' +
                ", editor='" + editor + '\'' +
                ", sourse='" + sourse + '\'' +
                ", content='" + content + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
