package com.example.niko.musicschool.control.grade;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.BaseController;
import com.example.niko.musicschool.model.OrderGradeParamModel;

import cn.sanfast.xmutils.utils.StringUtil;


/**
 * Created by niko on 2016/8/22.
 */
public class MusicGradeTeacherInfoController extends BaseController {


    private EditText mEtTeachName;
    private EditText mEtTeachPhone;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public MusicGradeTeacherInfoController(Context context, View view) {
        super(context, view);
        initViews();
    }

    @Override
    protected void initViews() {

        mEtTeachName =(EditText)findViewById(R.id.et_teach_name);
        mEtTeachPhone =(EditText)findViewById(R.id.et_teach_phone);

    }


    public OrderGradeParamModel getControllerData(OrderGradeParamModel model){

        if(!model.isOk()){
            return model;
        }


        //联系人姓名
        String str;
        str = mEtTeachName.getText().toString().trim();
        if(!TextUtils.isEmpty(str)){
            model.setTeacher_name(str);
        }else{
            model.setOk(false);
            showSnackbar(mEtTeachName,"请填写老师姓名");
            return model;
        }


        //老师手机号
        str = mEtTeachPhone.getText().toString().trim();
        if(!TextUtils.isEmpty(str)){
            if(StringUtil.isPhoneNumber(str)){
                model.setTeacher_telephone(str);
            }else{
                model.setOk(false);
               showSnackbar(mEtTeachPhone,"老师手机号码有误");
                return model;
            }
        }else{
            model.setOk(false);
            showSnackbar(mEtTeachPhone,"请填写老师手机号码");
            return model;
        }


        return model;
    }



}
