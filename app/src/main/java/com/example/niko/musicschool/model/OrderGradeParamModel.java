package com.example.niko.musicschool.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by niko on 2016/9/26.
 */
public class OrderGradeParamModel extends BmobObject implements OrderInterface,Serializable{


    //判断传递值是否成功的flag
    public boolean ok = true;
    private UserModel user;
    private String grade_institution;
    private String grade_regulations;
    private String contact;
    private String telephone;
    private String address;
    private String teacher_name;
    private String teacher_telephone;
    private String express_method;
    private MajorModel majorModel;
    private GradeStudentModel gradeStudentModel;
    private int price=200;



    public String getExpress_method() {
        return express_method;
    }

    public void setExpress_method(String express_method) {
        this.express_method = express_method;
    }


    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_telephone() {
        return teacher_telephone;
    }

    public void setTeacher_telephone(String teacher_telephone) {
        this.teacher_telephone = teacher_telephone;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getGrade_institution() {
        return grade_institution;
    }

    public void setGrade_institution(String grade_institution) {
        this.grade_institution = grade_institution;
    }


    public String getGrade_regulations() {
        return grade_regulations;
    }

    public void setGrade_regulations(String grade_regulations) {
        this.grade_regulations = grade_regulations;
    }


    public MajorModel getMajorModel() {
        return majorModel;
    }

    public void setMajorModel(MajorModel majorModel) {
        this.majorModel = majorModel;
    }

    public GradeStudentModel getGradeStudentModel() {
        return gradeStudentModel;
    }

    public void setGradeStudentModel(GradeStudentModel gradeStudentModel) {
        this.gradeStudentModel = gradeStudentModel;
    }


    @Override
    public String getName() {
        return majorModel.getGrade_major()+majorModel.getGrade_level()+"报名";
    }

    @Override
    public String getPrice() {
        return price+"";
    }

    @Override
    public String getTime() {
        return this.getCreatedAt();
    }

    @Override
    public String getId() {
        return getObjectId();
    }
}
