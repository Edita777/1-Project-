package com.example.twf_final.model;

public class SearchModel {

    private String Name;
    private String desc;

    public SearchModel(String name, String desc) {
        Name = name;
        this.desc = desc;
    }

    public SearchModel() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
