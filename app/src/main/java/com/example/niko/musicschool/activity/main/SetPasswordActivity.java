package com.example.niko.musicschool.activity.main;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;

/**
 * Created by niko on 2017/3/24.
 */

public class SetPasswordActivity extends BaseActivity {
    private View mRootView;
    private AppbarNormalController mAppbar;
    private TextView mTvSetPassword;
    private EditText mEtPassword;
    private EditText mEtPasswordConfirm;
    private TextView mBtnDone;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_set_password);
    }

    @Override
    protected void initViews() {
        mRootView = findViewById(R.id.layout_set_password);
        // appbar
        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));
        // views
        mTvSetPassword = (TextView) findViewById(R.id.tv_set_password);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtPasswordConfirm = (EditText) findViewById(R.id.et_password_confirm);
        mBtnDone = (TextView) findViewById(R.id.btn_done);
    }

    @Override
    protected void setupViews() {


    }
}
