package com.example.niko.musicschool.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by niko on 2017/4/7.
 */

public class MajorModel extends BmobObject {

    private String grade_major;
    private String grade_level;
    private String song1;
    private String song2;
    private String song3;
    private String song4;

    public String getGrade_major() {
        return grade_major;
    }

    public void setGrade_major(String grade_major) {
        this.grade_major = grade_major;
    }

    public String getGrade_level() {
        return grade_level;
    }

    public void setGrade_level(String grade_level) {
        this.grade_level = grade_level;
    }

    public String getSong1() {
        return song1;
    }

    public void setSong1(String song1) {
        this.song1 = song1;
    }

    public String getSong2() {
        return song2;
    }

    public void setSong2(String song2) {
        this.song2 = song2;
    }

    public String getSong3() {
        return song3;
    }

    public void setSong3(String song3) {
        this.song3 = song3;
    }

    public String getSong4() {
        return song4;
    }

    public void setSong4(String song4) {
        this.song4 = song4;
    }
}
