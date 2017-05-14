package com.example.niko.musicschool.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by niko on 2017/4/7.
 */

public class GradeStudentModel extends BmobObject {

    private String name;
    private String name_spell;
    private String gender;
    private String birth;
    private String idcard;
    private String company_name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_spell() {
        return name_spell;
    }

    public void setName_spell(String name_spell) {
        this.name_spell = name_spell;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
