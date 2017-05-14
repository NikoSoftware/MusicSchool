package com.example.niko.musicschool.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.example.niko.musicschool.activity.main.LoginActivity;

import cn.sanfast.xmutils.dialog.CustomDialog;


/**
 * 控制器基类
 * Created by wzd on 2015/9/7.
 */
public abstract class BaseController {

    /**
     * Context
     */
    protected Context mContext;
    /**
     * Fragment
     */
    protected Fragment mFragment;
    /**
     * View
     */
    protected View mView;
    /**
     * Activity root view
     */
    protected View mRootView;
    public CustomDialog mCustomDialog;

    /**
     * 初始化操作
     */
    protected abstract void initViews();

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public BaseController(Context context, View view) {
        this.mContext = context;
        this.mView = view;
    }

    /**
     * View.findViewById(int id)
     *
     * @param resId int
     * @return View
     */
    public View findViewById(int resId) {
        View view = null;
        if (mView != null) {
            view = mView.findViewById(resId);
        }
        return view;
    }

    /**
     * 启动Activity
     *
     * @param intent Intent
     */
    public void startActivity(Intent intent) {
        if (mContext != null && intent != null) {
            mContext.startActivity(intent);
        }
    }

    /**
     * 启动Activity
     *
     * @param intent      Intent
     * @param requestCode int
     */
    public void startActivityForResult(Intent intent, int requestCode) {
        if (mContext != null && intent != null) {
            ((Activity) mContext).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 设置Fragment
     *
     * @param fragment Fragment
     */
    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    /**
     * 显示提示信息
     *
     * @param msg String
     */
    public void showToast(String msg) {
        if (msg != null && !"".equals(msg) && !"null".equals(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示提示信息
     *
     * @param view View
     * @param msg  String
     */
    public void showSnackbar(View view, String msg) {
        if (view != null && msg != null && !"".equals(msg) && !"null".equals(msg)) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                    .setAction("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .show();
        }
    }

    /**
     * 登录失效
     *
     * @param msg String
     */
    protected void loginTimeOut(String msg) {
        CustomDialog dialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_NO_TITLE);
        dialog.setMessage(msg);
        dialog.setButton(
                "", null, "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 去登陆界面
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                    }
                });
        dialog.show();
    }

    /**
     * 返回view
     *
     * @return
     */
    public View getView() {
        return mView;
    }


    /**
     * 默认不调用
     */
    public void setCustomDialog() {
        mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_LOADING);
    }

    /**
     * 默认不调用
     */
    public void closeCustomDialog() {
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
    }

    /**
     * 默认不调用
     *
     * @param text String
     */
    public void setCustomDialog(String text) {
        mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_LOADING).setLoadingText(text);
    }


}
