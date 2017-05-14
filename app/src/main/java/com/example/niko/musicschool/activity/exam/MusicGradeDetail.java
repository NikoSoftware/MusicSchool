package com.example.niko.musicschool.activity.exam;

import android.view.View;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;
import com.example.niko.musicschool.model.OrderGradeParamModel;

/**
 * Created by niko on 2017/4/7.
 */

public class MusicGradeDetail extends BaseActivity {

    private TextView mTvPayTitle;
    private TextView mTvProjectName1;
    private TextView mTvContent1;
    private TextView mTvProjectName2;
    private TextView mTvContent2;
    private TextView mTvProjectName3;
    private TextView mTvContent3;
    private TextView mTvProjectName4;
    private TextView mTvContent4;
    private TextView mTvProjectName5;
    private TextView mTvContent5;
    private TextView mTvPrice2;
    private OrderGradeParamModel mOrderGradeParamModel;
    private AppbarNormalController mAppbar;


    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_music_details);
    }

    @Override
    protected void initViews() {
        mOrderGradeParamModel = (OrderGradeParamModel) getIntent().getExtras().get("OrderGradeParamModel");

        // appbar
        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));
        mTvPayTitle = (TextView)findViewById(R.id.tv_pay_title);
        mTvProjectName1 = (TextView)findViewById(R.id.tv_project_name1);
        mTvContent1 = (TextView)findViewById(R.id.tv_content1);
        mTvProjectName2 = (TextView)findViewById(R.id.tv_project_name2);
        mTvContent2 = (TextView)findViewById(R.id.tv_content2);
        mTvProjectName3 = (TextView)findViewById(R.id.tv_project_name3);
        mTvContent3 = (TextView)findViewById(R.id.tv_content3);
        mTvProjectName4 = (TextView)findViewById(R.id.tv_project_name4);
        mTvContent4 = (TextView)findViewById(R.id.tv_content4);
        mTvProjectName5 = (TextView)findViewById(R.id.tv_project_name5);
        mTvContent5 = (TextView)findViewById(R.id.tv_content5);

        mTvPrice2 = (TextView)findViewById(R.id.tv_price2);

    }

    @Override
    protected void setupViews() {
        // appbar
        mAppbar.init(R.drawable.selector_btn_back, "考试报名详情", "");
        mAppbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvPayTitle.setText(mOrderGradeParamModel.getName());
        mTvContent1.setText(mOrderGradeParamModel.getGradeStudentModel().getName());
        mTvContent2.setText(mOrderGradeParamModel.getMajorModel().getGrade_major());
        mTvContent3.setText(mOrderGradeParamModel.getMajorModel().getGrade_level());
        mTvContent4.setText(mOrderGradeParamModel.getTime());
        mTvContent5.setText(mOrderGradeParamModel.getTelephone());
        mTvPrice2.setText(mOrderGradeParamModel.getPrice()+"元");


    }
}
