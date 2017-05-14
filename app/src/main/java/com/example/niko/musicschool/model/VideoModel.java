package com.example.niko.musicschool.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by niko on 2017/4/10.
 */

public class VideoModel extends BmobObject implements Serializable {

    private String name;
    private String url;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
