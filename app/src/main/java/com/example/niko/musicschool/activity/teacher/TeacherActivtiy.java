package com.example.niko.musicschool.activity.teacher;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;
import com.example.niko.musicschool.model.CourseModel;
import com.example.niko.musicschool.model.CourseOrderModel;
import com.example.niko.musicschool.model.UserModel;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.sanfast.xmutils.bitmap.loader.AsyncImageLoader;
import cn.sanfast.xmutils.bitmap.view.CircleImageView;
import cn.sanfast.xmutils.utils.AppUtils;
import cn.sanfast.xmutils.utils.StringUtil;

/**
 * Created by niko on 2017/3/26.
 */

public class TeacherActivtiy extends BaseActivity {
    private AppbarNormalController mAppbar;
    private View mViewTeacherInfo;
    private ImageView mIvImage;
    private CircleImageView mAvatar;
    private TextView mTvName;
    private ImageView mIvGender;
    private ImageView mIvLevel;
    private TextView mTvType;
    private TextView mTvAddress;
    private ImageView mIvArrow;
    private ImageView mIvCollection;
    private TextView mTvDescription;
    private CourseModel model;
    private TextView mtvCourse;
    private Button mButton;
    private EditText mEtName;
    private EditText mEtPhone;
    private String mName;
    private String mPhone;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_teacher);
    }

    @Override
    protected void initViews() {

        model = (CourseModel) getIntent().getExtras().get("CourseModel");


        // appbar
        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));

        mViewTeacherInfo = findViewById(R.id.view_teacher_info);
        ViewGroup.LayoutParams lp = mViewTeacherInfo.getLayoutParams();
        lp.height = AppUtils.getScreenWidth(mContext) / 25 * 12;
        mViewTeacherInfo.setLayoutParams(lp);
        // views
        mIvImage = (ImageView) findViewById(R.id.iv_image);
        mAvatar = (CircleImageView) findViewById(R.id.civ_avatar);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mIvGender = (ImageView) findViewById(R.id.iv_gender);
        mIvLevel = (ImageView) findViewById(R.id.iv_level);
        mTvType = (TextView) findViewById(R.id.tv_type);
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mIvArrow = (ImageView) findViewById(R.id.iv_arrow_right);
        mtvCourse= (TextView) findViewById(R.id.tv_course);
        mTvDescription = (TextView) findViewById(R.id.tv_description);
        mButton = (Button)findViewById(R.id.sign_button);
        mEtName = (EditText)findViewById(R.id.et_st_order_name);
        mEtPhone = (EditText)findViewById(R.id.et_st_order_phone);



    }

    @Override
    protected void setupViews() {
        mAppbar.init(R.drawable.selector_btn_back, "课程预订", "");
        mAppbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new AsyncImageLoader(model.getCimg(), mAvatar).start();
        String firstName = model.getCteacher().getTname();
        if (!StringUtil.isEmpty(firstName)) {
            firstName += "老师";
        }
        mTvName.setText(firstName);

        if ("男".equals(model.getCteacher().getTsex())) {
            mIvGender.setImageResource(R.drawable.ic_male);
        } else {
            mIvGender.setImageResource(R.drawable.ic_female);
        }
        mTvType.setText(model.getCsubject());
        String address = model.getCteacher().getTphone();
        if (!StringUtil.isEmpty(address)) {
            mTvAddress.setText(address);
        }
        mTvDescription.setText(model.getCteacher().getTbrief());

        mtvCourse.setText("课程名："+model.getCname()+"\n开课时间："
                +model.getCdate()+" "+model.getCtime()+"\n地址："+model.getCaddress()+
                "\n对应专业："+model.getCsubject());

        mButton.setText("("+model.getCprice()+")元"+"预订");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInfo()){

                    CourseOrderModel courseOrderModel = new CourseOrderModel();
                    courseOrderModel.setCourseModel(model);
                    courseOrderModel.setCname(mName);
                    courseOrderModel.setCphone(mPhone);
                    courseOrderModel.setUserModel(BmobUser.getCurrentUser(UserModel.class));

                    courseOrderModel.save(new SaveListener<String>() {

                        @Override
                        public void done(String objectId, BmobException e) {
                            if(e==null){
                                showSnackbar("预订成功：");
                                finish();
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });


                }
            }
        });
    }



    public boolean checkInfo(){

        mName = mEtName.getText().toString().trim();
        if(StringUtil.isEmpty(mName)){
            showSnackbar("请填写学生姓名");
            return false;
        }

        mPhone = mEtPhone.getText().toString().trim();
        if(StringUtil.isEmpty(mPhone)){
            showSnackbar("请填写学生姓名");
            return false;
        }else if(!StringUtil.isPhoneNumber(mPhone)){

            showSnackbar("手机号有误");
            return false;
        }

       return true;
    }



}
