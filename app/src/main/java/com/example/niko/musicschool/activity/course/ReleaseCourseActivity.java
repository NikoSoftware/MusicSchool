package com.example.niko.musicschool.activity.course;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.niko.musicschool.JsonModel.SubjectModel;
import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;
import com.example.niko.musicschool.model.CourseModel;
import com.example.niko.musicschool.model.TeacherModel;
import com.example.niko.musicschool.utils.AppTool;
import com.example.niko.musicschool.view.SelectDayPopupWindow;
import com.example.niko.musicschool.view.SelectDialogWindow;
import com.example.niko.musicschool.view.SelectTimePopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by niko on 2017/4/20.
 */

public class ReleaseCourseActivity extends BaseActivity {
    private EditText mEdStCname;
    private EditText mEdAddress;
    private LinearLayout mLlSelectMajor;
    private TextView mTvSelectMajor;
    private LinearLayout mLlSelectTime;
    private TextView mTvSelectTime;
    private LinearLayout mLlStartTime;
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private LinearLayout mLlEndTime;
    private ArrayList<SubjectModel.DataBean> mSubjectList = new ArrayList<SubjectModel.DataBean>();
    private String mMajor;
    private SelectDayPopupWindow mSelectDayPopupWindow;
    private ArrayList<Integer> mTimes;
    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean mMonthAndDay=false;
    private EditText mEdPrice;
    private Button mSignButton;
    private TeacherModel mTeacherModel;
    private AppbarNormalController mAppbar;

    @Override
    protected void setLayout() {
        mTeacherModel = (TeacherModel) getIntent().getExtras().get("Teachermodel");

        setContentView(R.layout.activity_release_course);

    }

    @Override
    protected void initViews() {
        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));
        mEdStCname = (EditText)findViewById(R.id.ed_st_cname);
        mEdAddress = (EditText)findViewById(R.id.ed_adress);
        mEdPrice =(EditText)findViewById(R.id.ed_price);
        AppTool.setOnlyInputIdcard(this,mEdPrice);

        mLlSelectMajor = (LinearLayout)findViewById(R.id.ll_select_major);
        mTvSelectMajor = (TextView)findViewById(R.id.tv_select_major);

        mLlSelectTime = (LinearLayout)findViewById(R.id.ll_select_time);
        mTvSelectTime = (TextView)findViewById(R.id.tv_select_time);

        mLlStartTime = (LinearLayout)findViewById(R.id.ll_start_time);
        mTvStartTime = (TextView)findViewById(R.id.tv_start_time);

        mLlEndTime = (LinearLayout)findViewById(R.id.ll_end_time);
        mTvEndTime = (TextView)findViewById(R.id.tv_end_time);

        mSignButton = (Button)findViewById(R.id.sign_button);

    }

    /**
     * 请求专业数据
     */
    public void requestSubjectDate(){

        SubjectModel subjectModel = new SubjectModel();
        mSubjectList.clear();
        mSubjectList.addAll(subjectModel.getData());

        InitTime();
    }

    private void InitTime(){
        mTimes = new ArrayList<Integer>();
        for (int i = 8; i <=22; i++) {
            mTimes.add(i);
        }

    }



    @Override
    protected void setupViews() {
        // appbar
        mAppbar.init(R.drawable.selector_btn_back, "发布课程", "");
        mAppbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        requestSubjectDate();

        mLlSelectMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    }
                });
                selectDialogWindow.show();

            }
        });

        mLlSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectDayPopupWindow==null)
                    mSelectDayPopupWindow= new SelectDayPopupWindow((Activity) mContext,true);

                mSelectDayPopupWindow.setOnConfirmListener(new SelectDayPopupWindow.OnConfirmListener() {
                    @Override
                    public void onConfirm(int year, int month, int day) {
                        mMonthAndDay = true;
                        mYear = year;
                        mMonth =month;
                        mDay = day;
                        mTvSelectTime.setText(year+"-"+ AppTool.tansformFillTime(month)+"-"+AppTool.tansformFillTime(day));
                    }
                });
                mSelectDayPopupWindow.show();
            }
        });
        mLlStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMonthAndDay){
                    showSnackbar("请先选择年月日");
                    return;
                }

                SelectTimePopupWindow selectTimePopupWindow = new SelectTimePopupWindow(ReleaseCourseActivity.this);
                selectTimePopupWindow.setCanSelectData(mTimes);
                selectTimePopupWindow.setDate(mYear,mMonth,mDay);
                selectTimePopupWindow.setOnConfirmListener(new SelectTimePopupWindow.OnConfirmListener() {
                    @Override
                    public void onConfirm(List<Integer> list) {
                        mTvStartTime.setText(list.get(0).intValue()+":00");
                    }
                });
                selectTimePopupWindow.show();

            }
        });
        mLlEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMonthAndDay){
                    showSnackbar("请先选择年月日");
                    return;
                }
                SelectTimePopupWindow selectTimePopupWindow = new SelectTimePopupWindow(ReleaseCourseActivity.this);
                selectTimePopupWindow.setCanSelectData(mTimes);
                selectTimePopupWindow.setDate(mYear,mMonth,mDay);
                selectTimePopupWindow.setOnConfirmListener(new SelectTimePopupWindow.OnConfirmListener() {
                    @Override
                    public void onConfirm(List<Integer> list) {
                        mTvEndTime.setText(list.get(0).intValue()+":00");
                    }
                });
                selectTimePopupWindow.show();
            }
        });

        mSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRelase();
            }
        });

    }

    private void requestRelase() {

        CourseModel courseModel = new CourseModel();

        String str = mEdStCname.getText().toString().trim();
        if(!TextUtils.isEmpty(str)){
            courseModel.setCname(str);
        }else{
            showSnackbar("请输入课程名称");
            return;
        }

         str = mEdAddress.getText().toString().trim();
        if(!TextUtils.isEmpty(str)){
            courseModel.setCaddress(str);
        }else{
            showSnackbar("请输入教学地址");
            return;
        }

        str = mEdPrice.getText().toString().trim();
        if(!TextUtils.isEmpty(str)){
            try {
                courseModel.setCprice(Float.parseFloat(str));
            }catch (Exception e){
                showSnackbar("价格有误");
                return;
            }
        }else{
            showSnackbar("请输入价格");
            return;
        }

        str = mTvSelectMajor.getText().toString().trim();
        if(!"请选择".equals(str)){
            courseModel.setCsubject(str);
        }else{
            showSnackbar("选择对应专业");
            return;
        }

        str = mTvSelectTime.getText().toString().trim();
        if(!"请选择".equals(str)){
            courseModel.setCdate(str);
        }else{
            showSnackbar("选择教学日期");
            return;
        }

        str = mTvStartTime.getText().toString().trim();
        if(!"请选择".equals(str)){
        }else{
            showSnackbar("选择起始时间");
            return;
        }

        str = mTvEndTime.getText().toString().trim();
        if(!"请选择".equals(str)){

            courseModel.setCtime(mTvStartTime.getText().toString().trim()+"-"+str);
        }else{
            showSnackbar("选择结束时间");
            return;
        }

        courseModel.setCimg(mTeacherModel.getTimg());
        courseModel.setCteacher(mTeacherModel);
        setCustomDialog();
        courseModel.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                mCustomDialog.dismiss();
                if(e==null){
                    showSnackbar("发布成功");
                    finish();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }

            }
        });

    }


}
