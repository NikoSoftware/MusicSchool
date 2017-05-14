package com.example.niko.musicschool.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by niko on 2017/3/28.
 */

public class CourseOrderModel extends BmobObject implements OrderInterface,Serializable{

    private CourseModel courseModel;

    private String cname;

    private String cphone;

    private UserModel userModel;


    public CourseModel getCourseModel() {
        return courseModel;
    }

    public void setCourseModel(CourseModel courseModel) {
        this.courseModel = courseModel;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCphone() {
        return cphone;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public String getName() {
        return courseModel.getCname();
    }

    @Override
    public String getPrice() {
        return courseModel.getCprice()+"";
    }

    @Override
    public String getTime() {
        return getCreatedAt();
    }

    @Override
    public String getId() {
        return getObjectId();
    }
}
