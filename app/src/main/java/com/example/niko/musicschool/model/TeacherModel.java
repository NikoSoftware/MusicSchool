package com.example.niko.musicschool.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by niko on 2017/3/26.
 */

public class TeacherModel extends BmobObject implements Serializable{

    private String timg;
    private String tname;
    private String tsex;
    private Integer tage;
    private String tphone;
    private String teducation;
    private Integer tteachage;
    private String tsubject;
    private String tbrief;
    private UserModel tuser;


    public String getTimg() {
        return timg;
    }

    public void setTimg(String timg) {
        this.timg = timg;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTsex() {
        return tsex;
    }

    public void setTsex(String tsex) {
        this.tsex = tsex;
    }

    public Integer getTage() {
        return tage;
    }

    public void setTage(Integer tage) {
        this.tage = tage;
    }

    public String getTphone() {
        return tphone;
    }

    public void setTphone(String tphone) {
        this.tphone = tphone;
    }

    public String getTeducation() {
        return teducation;
    }

    public void setTeducation(String teducation) {
        this.teducation = teducation;
    }

    public Integer getTteachage() {
        return tteachage;
    }

    public void setTteachage(Integer tteachage) {
        this.tteachage = tteachage;
    }

    public String getTsubject() {
        return tsubject;
    }

    public void setTsubject(String tsubject) {
        this.tsubject = tsubject;
    }

    public String getTbrief() {
        return tbrief;
    }

    public void setTbrief(String tbrief) {
        this.tbrief = tbrief;
    }

    public UserModel getTuser() {
        return tuser;
    }

    public void setTuser(UserModel tuser) {
        this.tuser = tuser;
    }
}
