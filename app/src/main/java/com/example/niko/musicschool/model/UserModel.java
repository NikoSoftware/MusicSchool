package com.example.niko.musicschool.model;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by niko on 2017/3/25.
 */

public class UserModel extends BmobUser implements Serializable {

    private String imgpath;
    private String nick;

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
