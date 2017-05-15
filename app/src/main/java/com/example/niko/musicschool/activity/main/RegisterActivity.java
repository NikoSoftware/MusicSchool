package com.example.niko.musicschool.activity.main;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.db.SharePerefenceTool;
import com.example.niko.musicschool.model.TeacherModel;
import com.example.niko.musicschool.model.UserModel;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.sanfast.xmutils.utils.StringUtil;

/**
 * Created by niko on 2017/3/24.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private View mRootView;
    private EditText mEtPhone;
    private EditText mEtPassword;
    private TextView mBtnLogin;


    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initViews() {
        mRootView = findViewById(R.id.layout_login);
        // views
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (TextView) findViewById(R.id.btn_login);
    }

    @Override
    protected void setupViews() {

        mBtnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 注册
            case R.id.btn_login:

                if(checkRegister()){
                    setCustomDialog();
                    UserModel bu = new UserModel();
                    bu.setUsername(mEtPhone.getText().toString());
                    bu.setPassword(mEtPassword.getText().toString().trim());
                    bu.setNick(mEtPhone.getText().toString());
                    bu.signUp(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser s, BmobException e) {
                            closeCustomDialog();
                            if(e==null){
                                SharePerefenceTool.saveUser(mContext,mEtPassword.getText().toString().trim());
                                showSnackbar("注册成功");
                                skip(mContext,MainActivity.class);
                                finish();
                            }else{
                                showSnackbar("注册失败");
                                e.printStackTrace();
                            }
                        }
                    });



                }

                break;

        }
    }


    /**
     * 核对数据
     * @return
     */
    public boolean checkRegister(){

        String str = mEtPhone.getText().toString().trim();
        if(!StringUtil.isPhoneNumber(str)){

            showSnackbar("请输入手机号");
            return false;
        }
        str = mEtPassword.getText().toString().trim();

        if(str.length()<6){

            showSnackbar("密码不能小于6位");
            return false;
        }

        return true;
    }






}
