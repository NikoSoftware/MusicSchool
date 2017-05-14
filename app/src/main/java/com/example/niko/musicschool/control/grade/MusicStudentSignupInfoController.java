package com.example.niko.musicschool.control.grade;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.BaseController;
import com.example.niko.musicschool.model.GradeStudentModel;
import com.example.niko.musicschool.model.OrderGradeParamModel;
import com.example.niko.musicschool.utils.AppTool;
import com.example.niko.musicschool.view.SelectDayPopupWindow;

import java.util.ArrayList;

import cn.sanfast.xmutils.utils.CheckGroup;
import cn.sanfast.xmutils.utils.StringUtil;
import cn.sanfast.xmutils.view.CheckImageView;


/**
 * Created by niko on 2016/8/22.
 */
public class MusicStudentSignupInfoController extends BaseController {


    private EditText mEdStCname;
    private EditText mEdStPname;
    private CheckImageView mRbSexMan;
    private CheckImageView mRbSexWoman;
    private LinearLayout mLlSelectTime;
    private TextView mTvSelectTime;
    private EditText mEdAddress;
    private ArrayList<CheckImageView> radioImageViews;
    private EditText mEdIdCard;
    private SelectDayPopupWindow mSelectDayPopupWindow;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public MusicStudentSignupInfoController(Context context, View view) {
        super(context, view);
        initViews();
    }

    @Override
    protected void initViews() {

        mEdStCname = (EditText)findViewById(R.id.ed_st_cname);
        mEdStPname = (EditText)findViewById(R.id.ed_st_pname);
        AppTool.setOnlyInputPinYin(mContext,mEdStPname);

        mRbSexMan = (CheckImageView)findViewById(R.id.rb_sex_man);
        mRbSexWoman = (CheckImageView)findViewById(R.id.rb_sex_woman);

        mEdIdCard = (EditText)findViewById(R.id.et_idcard);
        AppTool.setOnlyInputIdcard(mContext,mEdIdCard);

        mLlSelectTime = (LinearLayout)findViewById(R.id.ll_select_time);
        mTvSelectTime =(TextView)findViewById(R.id.tv_select_time);
        mEdAddress = (EditText)findViewById(R.id.et_address);


        radioImageViews = new ArrayList<>();
        radioImageViews.add(mRbSexMan);
        radioImageViews.add(mRbSexWoman);
        CheckGroup.InitRadioImageViewGroup(radioImageViews);
    }

    public void setupShow(){
        mLlSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectDayPopupWindow==null)
                    mSelectDayPopupWindow= new SelectDayPopupWindow((Activity) mContext);

                mSelectDayPopupWindow.setOnConfirmListener(new SelectDayPopupWindow.OnConfirmListener() {
                    @Override
                    public void onConfirm(int year, int month, int day) {
                        mTvSelectTime.setText(year+"-"+AppTool.tansformFillTime(month)+"-"+AppTool.tansformFillTime(day));
                    }
                });
                mSelectDayPopupWindow.show();
            }
        });

    }

    public OrderGradeParamModel getControllerData(OrderGradeParamModel model){

        GradeStudentModel gradeStudentModel = new GradeStudentModel();

        //赋值用户名
        String str;
        str = mEdStCname.getText().toString().trim();
        if(!TextUtils.isEmpty(str)){
            gradeStudentModel.setName(str);
        }else{
            model.setOk(false);
            showSnackbar(mTvSelectTime,"请输入用户名");
            return model;
        }

        //赋值拼音
        str = mEdStPname.getText().toString().trim();
        if(!TextUtils.isEmpty(str)){
            gradeStudentModel.setName_spell(str);
        }


        //获取选中的性别
        switch (CheckGroup.getSelectIndex(radioImageViews)){
            case 0:
                gradeStudentModel.setGender(1+"");
                break;
            case 1:
                gradeStudentModel.setGender(2+"");
                break;
        }

        //设置身份证
        str = mEdIdCard.getText().toString().trim();
        if(!TextUtils.isEmpty(str)){
            if(StringUtil.isIdCard(str)) {
                gradeStudentModel.setIdcard(str);
            }else{
                model.setOk(false);
                showSnackbar(mTvSelectTime,"身份证信息有误");
                return model;
            }
        }else{
            model.setOk(false);
            showSnackbar(mTvSelectTime,"请输入身份证");
            return model;
        }


        //设置生日
        str = mTvSelectTime.getText().toString().trim();
        if(!str.equals("请选择")){
            gradeStudentModel.setBirth(str);
        }else{
            model.setOk(false);
            showSnackbar(mTvSelectTime,"请选择出生年月");
            return model;
        }

        //所在学校/单位
        str = mEdAddress.getText().toString().trim();
        if(!TextUtils.isEmpty(str)){
            gradeStudentModel.setCompany_name(str);
        }else{
            model.setOk(false);
           showSnackbar(mTvSelectTime,"请选择所在学校/单位");
            return model;
        }

        model.setGradeStudentModel(gradeStudentModel);

        return model;
    }



}
