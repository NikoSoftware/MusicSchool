package com.example.niko.musicschool.activity.main;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.db.SharePerefenceTool;
import com.example.niko.musicschool.model.UserModel;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.sanfast.xmutils.utils.StringUtil;

/**
 * Created by niko on 2017/3/24.
 */

public class LoginActivity extends BaseActivity  implements View.OnClickListener {

    private View mRootView;
    private EditText mEtPhone;
    private EditText mEtPassword;
    private TextView mBtnLogin;
    private TextView mBtnShortcutLogin;
    private TextView mBtnFindBackPassword;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initViews() {
        mRootView = findViewById(R.id.layout_login);
        // views
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (TextView) findViewById(R.id.btn_login);
        mBtnShortcutLogin = (TextView)findViewById(R.id.tv_shortcut_login);
        mBtnFindBackPassword =  (TextView)findViewById(R.id.tv_find_back_password);
        mBtnFindBackPassword.setVisibility(View.GONE);

    }

    @Override
    protected void setupViews() {

        mBtnLogin.setOnClickListener(this);
        mBtnShortcutLogin.setOnClickListener(this);
        mBtnFindBackPassword.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 登录
            case R.id.btn_login:

                if(checkRegister()) {
                    UserModel bu = new UserModel();
                    bu.setUsername(mEtPhone.getText().toString().trim());
                    bu.setPassword(mEtPassword.getText().toString().trim());
                    bu.login(new SaveListener<UserModel>() {
                        @Override
                        public void done(UserModel s, BmobException e) {
                            if (e == null) {
                                SharePerefenceTool.saveUser(mContext,mEtPassword.getText().toString().trim());
                                skip(mContext, MainActivity.class);
                                finish();
                            } else {
                                showSnackbar("登录失败");
                                e.printStackTrace();
                            }
                        }
                    });
                }


                break;
            // 注册
            case R.id.tv_shortcut_login:
                skip(mContext,RegisterActivity.class);
                break;
            // 找回密码
            case R.id.tv_find_back_password:

                break;

        }
    }



    /**
     * 核对数据
     * @return
     */
    public boolean checkRegister(){

        String str = mEtPhone.getText().toString();
        if(!StringUtil.isPhoneNumber(str)){

            showSnackbar("请输入手机号");
            return false;
        }
        str = mEtPassword.getText().toString();

        if(str.length()<6){

            showSnackbar("密码不能小于6位");
            return false;
        }

        return true;
    }


}
