package com.example.niko.musicschool.control.grade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.BaseController;
import com.example.niko.musicschool.model.OrderGradeParamModel;

import java.util.ArrayList;

import cn.sanfast.xmutils.utils.CheckGroup;
import cn.sanfast.xmutils.utils.StringUtil;
import cn.sanfast.xmutils.view.CheckImageView;


/**
 * Created by niko on 2016/8/22.
 */
public class ContactInfoController extends BaseController {


    private LinearLayout mLlSelectAddress;
    private EditText mTvContactName;
    private EditText mTvContactPhone;
    private EditText mTvContactAddress;
    private ArrayList<CheckImageView> checkList =null;
    private String mName;
    private String mAddress;
    private String mMobile;
    private String mArea;
    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public ContactInfoController(Context context, View view) {
        super(context, view);
        //绝对不要忘了初始化
        initViews();
    }

    @Override
    protected void initViews() {

       mLlSelectAddress = (LinearLayout)findViewById(R.id.ll_select_address);
        mTvContactName = (EditText)findViewById(R.id.et_contact_name);
        mTvContactPhone = (EditText)findViewById(R.id.et_contact_phone);
        mTvContactAddress= (EditText)findViewById(R.id.et_contact_address);

        CheckImageView checkImageView1 = (CheckImageView)findViewById(R.id.civ_check_1);
        CheckImageView checkImageView2 = (CheckImageView)findViewById(R.id.civ_check_2);

       checkList= new ArrayList<CheckImageView>();
        checkList.add(checkImageView1);
        checkList.add(checkImageView2);
        CheckGroup.InitRadioImageViewGroup(checkList);


    }


    /**
     * 设置view数据
     */
    public void setupViews(){

        mLlSelectAddress.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

            }
        });

    }

/*
    public void setSelectData(String name,String address,String mobile,String province,String city,String area){
        mTvContactName.setText(name);
        mTvContactPhone.setText(mobile);
        mTvContactAddress.setText(address);
        this.mName =name;
        this.mAddress =address;
        this.mMobile = mobile;
        this.mArea = area;
    }*/




    public OrderGradeParamModel getControllerData(OrderGradeParamModel model){

        if(!model.isOk()){
            return model;
        }
        mName= mTvContactName.getText().toString();
        if(TextUtils.isEmpty(mName)){
            model.setOk(false);
            showSnackbar(mTvContactName,"请填写联系人信息");
            return model;
        }
        model.setContact(mName);
        mMobile = mTvContactPhone.getText().toString();
        if(TextUtils.isEmpty(mMobile)){
            model.setOk(false);
            showSnackbar(mTvContactName,"请填写联系人手机号");
            return model;
        }
        if(!StringUtil.isPhoneNumber(mMobile)){
            model.setOk(false);
            showSnackbar(mTvContactName,"联系人手机号有误");
            return model;
        }
        model.setTelephone(mMobile);

        mAddress = mTvContactAddress.getText().toString();
        if(TextUtils.isEmpty(mAddress)){
            model.setOk(false);
            showSnackbar(mTvContactName,"请填写邮寄地址");
            return model;
        }
        model.setAddress(mAddress);


        //邮递方式
        switch (CheckGroup.getSelectIndex(checkList)){
            case 0:
                model.setExpress_method(0+"");
                break;
            case 1:
                model.setExpress_method(1+"");
                break;
        }


        return model;
    }





}
