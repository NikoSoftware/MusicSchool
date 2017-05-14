package com.example.niko.musicschool.activity.exam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;
import com.example.niko.musicschool.control.grade.ContactInfoController;
import com.example.niko.musicschool.control.grade.MusicGradeTeacherInfoController;
import com.example.niko.musicschool.control.grade.MusicStudentMajorInfoController;
import com.example.niko.musicschool.control.grade.MusicStudentSignupInfoController;
import com.example.niko.musicschool.model.OrderGradeParamModel;
import com.example.niko.musicschool.model.UserModel;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.sanfast.xmutils.network.HttpResponseListener;
import cn.sanfast.xmutils.utils.CheckGroup;
import cn.sanfast.xmutils.view.CheckImageView;

/**
 * 音乐考级报名表类
 * Created by niko on 2016/8/19.
 */
public class MusicSignUpActivtiy extends BaseActivity {

    public final static int RESULT_CODE =1230;
    private final String TAG="音乐考级报名表";
    private AppbarNormalController mAppbar;
    private Button mSignButton;
    private ContactInfoController mMusicContactsInfoController;
    private MusicStudentSignupInfoController mMusicStudentSignupInfoController;
    private MusicStudentMajorInfoController mMusicStudentMajorInfoController;
    private MusicGradeTeacherInfoController mMusicGradeTeacherInfoController;
    private CheckImageView mRbAgree;
    private TextView mTvProvision;
    private int status=0;


    @Override
    protected void setLayout() {

        setContentView(R.layout.activity_music_signup);

    }

    @Override
    protected String getActivityTag() {
        return TAG;
    }

    @Override
    protected void initViews() {

        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));
        mRbAgree = (CheckImageView)findViewById(R.id.rb_agree);
        CheckGroup.initSingleRadioImageView(mRbAgree);
        mSignButton = (Button)findViewById(R.id.sign_button);
        mTvProvision = (TextView)findViewById(R.id.tv_provision);
        mMusicStudentSignupInfoController = new MusicStudentSignupInfoController(mContext,findViewById(R.id.layout_student_signup_info));
        mMusicStudentMajorInfoController = new MusicStudentMajorInfoController(mContext,findViewById(R.id.layout_student_major_info));
        mMusicContactsInfoController =new ContactInfoController(mContext,findViewById(R.id.layout_contact_info));
        mMusicGradeTeacherInfoController = new MusicGradeTeacherInfoController(mContext,findViewById(R.id.layout_grade_teacher_info));

    }

    @Override
    protected void setupViews() {
        mAppbar.init(R.drawable.selector_btn_back,"填写报名表",null);
        mAppbar.setOnBackClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrderDate();
            }
        });
        mMusicStudentSignupInfoController.setupShow();
        mMusicStudentMajorInfoController.setupShow();
        mMusicContactsInfoController.setupViews();
        mTvProvision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    public void submitOrderDate(){
        final OrderGradeParamModel orderGradeParamModel = new OrderGradeParamModel();
        if(!mRbAgree.get()){
            showSnackbar(mRbAgree,"请先同意协议");
            return;
        }

        mMusicStudentSignupInfoController.getControllerData(orderGradeParamModel);
        mMusicStudentMajorInfoController.getControllerData(orderGradeParamModel);
        mMusicContactsInfoController.getControllerData(orderGradeParamModel);
        mMusicGradeTeacherInfoController.getControllerData(orderGradeParamModel);
        if(!orderGradeParamModel.isOk()){
            return;
        }
        orderGradeParamModel.setUser(BmobUser.getCurrentUser(UserModel.class));
        status =0;
        setCustomDialog();
        orderGradeParamModel.getGradeStudentModel().save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    status++;
                    saveInfo(orderGradeParamModel);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }

            }
        });

        orderGradeParamModel.getMajorModel().save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    status++;
                    saveInfo(orderGradeParamModel);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }

            }
        });


    }


    /**
     * 请求数据
     */
    public void saveInfo(final OrderGradeParamModel orderGradeParamModel){

        if(status<2){
            return;
        }
        orderGradeParamModel.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                closeCustomDialog();
                if(e==null){
                    Bundle bundel = new Bundle();
                    bundel.putSerializable("OrderGradeParamModel",orderGradeParamModel);
                    skip(mContext, MusicGradeDetail.class,bundel);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


    }



    public void showSnackbar(View view, String msg) {
        super.showSnackbar(msg);
    }


}
