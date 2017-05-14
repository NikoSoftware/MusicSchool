package com.example.niko.musicschool.control.grade;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.niko.musicschool.JsonModel.SubjectLevelModel;
import com.example.niko.musicschool.JsonModel.SubjectModel;
import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.BaseController;
import com.example.niko.musicschool.model.MajorModel;
import com.example.niko.musicschool.model.OrderGradeParamModel;
import com.example.niko.musicschool.view.SelectDialogWindow;

import java.util.ArrayList;

import cn.sanfast.xmutils.network.HttpResponseListener;

/**
 * Created by niko on 2016/8/22.
 */
public class MusicStudentMajorInfoController extends BaseController {

    
    private LinearLayout mLlSelectMajor;
    private TextView mTvSelectMajor;
    private LinearLayout mLlSelectClass;
    private TextView mTvSelectClass;
    private ArrayList<SubjectModel.DataBean> mSubjectList = new ArrayList<SubjectModel.DataBean>();
    private ArrayList<SubjectLevelModel.DataBean> mSubjectLevelList = new ArrayList<SubjectLevelModel.DataBean>();
    private String mMajor=null;
    private String mMajorLevel = null;
    private EditText mEdContent1;
    private EditText mEdContent2;
    private EditText mEdContent3;
    private EditText mEdContent4;


    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public MusicStudentMajorInfoController(Context context, View view) {
        super(context, view);
        initViews();
    }

    @Override
    protected void initViews() {


        mLlSelectMajor = (LinearLayout)findViewById(R.id.ll_select_major);
        mTvSelectMajor =(TextView)findViewById(R.id.tv_select_major);
        mLlSelectClass = (LinearLayout)findViewById(R.id.ll_select_class);
        mTvSelectClass =(TextView)findViewById(R.id.tv_select_class);

        mEdContent1 = (EditText)findViewById(R.id.et_content1);
        mEdContent2 = (EditText)findViewById(R.id.et_content2);
        mEdContent3 = (EditText)findViewById(R.id.et_content3);
        mEdContent4 = (EditText)findViewById(R.id.et_content4);

    }

    /**
     * 请求专业数据
     */
    public void requestSubjectDate(){

        SubjectModel subjectModel = new SubjectModel();
        mSubjectList.clear();
        mSubjectList.addAll(subjectModel.getData());


    }


    /**
     * 请求专业等级数据
     */
    public void requestSubjectLevelDate(){

        SubjectLevelModel subjectLevelModel = new SubjectLevelModel();
        mSubjectLevelList.clear();
        mSubjectLevelList.addAll(subjectLevelModel.getData());

    }



    /**
     *显示数据
     *
     */
    public void setupShow(){
        //先请求专业列表
        requestSubjectDate();

        /**
         * 专业选择
         */
        mLlSelectMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> strList = new ArrayList<String>();
                for (SubjectModel.DataBean dataBean : mSubjectList) {
                    strList.add(dataBean.getName());
                }

                SelectDialogWindow selectDialogWindow = new SelectDialogWindow((Activity) mContext,strList,"报考专业");
                selectDialogWindow.setOnConfirmListener(new SelectDialogWindow.OnConfirmListener() {
                    @Override
                    public void onConfirm(String itemName, int index) {
                        mTvSelectMajor.setText(itemName);
                        mMajor=mSubjectList.get(index).getName();

                        mTvSelectClass.setText("请选择");
                        mMajorLevel =null;
                        requestSubjectLevelDate();
                    }
                });
                selectDialogWindow.show();
            }
        });

        /**
         * 专业级别选择
         */

        mLlSelectClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTvSelectMajor.getText().equals("请选择")){
                    showSnackbar(mLlSelectMajor,"请先选择专业");
                    return;
                }

                ArrayList<String> strList = new ArrayList<String>();
                for (SubjectLevelModel.DataBean dataBean : mSubjectLevelList) {
                    strList.add(dataBean.getName());
                }
                SelectDialogWindow selectDialogWindow = new SelectDialogWindow((Activity) mContext,strList,"报考级别");
                selectDialogWindow.setOnConfirmListener(new SelectDialogWindow.OnConfirmListener() {
                    @Override
                    public void onConfirm(String itemName, int index) {
                        mTvSelectClass.setText(itemName);
                        mMajorLevel = mSubjectLevelList.get(index).getName();
                    }
                });

                selectDialogWindow.show();
            }
        });

    }

    public OrderGradeParamModel getControllerData(OrderGradeParamModel model){

        MajorModel majorModel = new MajorModel();

        if(!model.isOk()){
            return model;
        }

        if(mMajor!=null){
            majorModel.setGrade_major(mMajor);
        }else{
            model.setOk(false);
            showSnackbar(mTvSelectClass,"请选择报考专业");
            return model;
        }

        if(mMajorLevel!=null){
            majorModel.setGrade_level(mMajorLevel);
        }else{
            model.setOk(false);
            showSnackbar(mTvSelectClass,"请选择报考级别");
            return model;
        }
        boolean status = false;

        String str =mEdContent1.getText().toString();
        if(!TextUtils.isEmpty(str)){
            status =true;
            majorModel.setSong1(str);
        }

        str =mEdContent2.getText().toString();
        if(!TextUtils.isEmpty(str)){
            status =true;
            majorModel.setSong2(str);
        }

        str =mEdContent3.getText().toString();
        if(!TextUtils.isEmpty(str)){
            status =true;
            majorModel.setSong3(str);
        }

        str =mEdContent4.getText().toString();
        if(!TextUtils.isEmpty(str)){
            status =true;
            majorModel.setSong4(str);
        }
        if(!status){
            model.setOk(false);
            showSnackbar(mTvSelectClass,"参赛曲目，至少填写一个。");
            return model;
        }

        model.setMajorModel(majorModel);

        return model;
    }








}
