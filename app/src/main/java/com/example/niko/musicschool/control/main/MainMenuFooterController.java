package com.example.niko.musicschool.control.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.LoginActivity;
import com.example.niko.musicschool.control.BaseController;

import cn.bmob.v3.BmobUser;

/**
 * 侧滑菜单
 * <p/>
 * Created by wzd on 2016/9/22.
 */
public class MainMenuFooterController extends BaseController {

    private View mViewSwitch;
    private TextView mTvSwitch;
    private DrawerLayout mDrawerLayout;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public MainMenuFooterController(Context context, View view) {
        super(context, view);
        initViews();
    }

    /**
     * 初始化操作
     */
    @Override
    protected void initViews() {

        mViewSwitch = findViewById(R.id.view_switch);
        mTvSwitch = (TextView) findViewById(R.id.tv_switch);
    }

    /**
     * setup
     */
    public void setupViews() {
        mViewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();   //清除缓存用户对象
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                ((Activity) mContext).finish();
                closeDrawerLayout();
            }
        });
    }

    /**
     * 拿到DrawerLayout引用
     *
     * @param drawerLayout DrawerLayout
     */
    public void setDrawerLayout(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }

    /**
     * 关闭DrawerLayout
     */
    private void closeDrawerLayout() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }, 400);
        }
    }
}
