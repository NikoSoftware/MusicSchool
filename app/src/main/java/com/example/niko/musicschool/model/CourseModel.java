package com.example.niko.musicschool.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by niko on 2017/3/26.
 */

public class CourseModel extends BmobObject implements Serializable{

    private String cname;
    private String cimg;
    private TeacherModel cteacher;
    private String cdate;
    private String ctime;
    private String csubject;
    private float cprice;
    private String caddress;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCimg() {
        return cimg;
    }

    public void setCimg(String cimg) {
        this.cimg = cimg;
    }

    public TeacherModel getCteacher() {
        return cteacher;
    }

    public void setCteacher(TeacherModel cteacher) {
        this.cteacher = cteacher;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getCsubject() {
        return csubject;
    }

    public void setCsubject(String csubject) {
        this.csubject = csubject;
    }

    public float getCprice() {
        return cprice;
    }

    public void setCprice(float cprice) {
        this.cprice = cprice;
    }

    public String getCaddress() {
        return caddress;
    }

    public void setCaddress(String caddress) {
        this.caddress = caddress;
    }
}
