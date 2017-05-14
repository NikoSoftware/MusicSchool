package com.example.niko.musicschool.control.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.course.ReleaseCourseActivity;
import com.example.niko.musicschool.activity.main.AccountManageActivity;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.activity.order.OrderManageActivity;
import com.example.niko.musicschool.activity.order.TeacherOrderListActivity;
import com.example.niko.musicschool.control.BaseController;
import com.example.niko.musicschool.control.item.ItemMenuController;
import com.example.niko.musicschool.eventbus.NickNameEvent;
import com.example.niko.musicschool.model.TeacherModel;
import com.example.niko.musicschool.model.UserModel;
import com.example.niko.musicschool.model.VideoModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.sanfast.xmutils.bitmap.loader.AsyncImageLoader;
import cn.sanfast.xmutils.bitmap.loader.ImageLoader;
import cn.sanfast.xmutils.bitmap.view.CircleImageView;
import cn.sanfast.xmutils.utils.StringUtil;

/**
 * 侧滑菜单
 * <p>
 * Created by wzd on 2016/6/16.
 */
public class MainMenuHeaderController extends BaseController {

    private  UserModel mUserModel;
    private View mAccountView;
    private CircleImageView mCivAvatar;
    private TextView mTvName;
    private TextView mTvPhone;
    private ItemMenuController mSchoolchild;
    private ItemMenuController mCollection;
    private ItemMenuController mOrder;
    private ItemMenuController mSignIn;
    private ItemMenuController mWallet;
    private ItemMenuController mAddress;
    private ItemMenuController mSetting;
    private DrawerLayout mDrawerLayout;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public MainMenuHeaderController(Context context, View view) {
        super(context, view);
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.activity_main_menu_header, null);
        }
        initViews();

        mUserModel =  UserModel.getCurrentUser(UserModel.class);

    }

    /**
     * 初始化操作
     */
    @Override
    protected void initViews() {
        // header
        mAccountView = findViewById(R.id.view_menu_header);
        mCivAvatar = (CircleImageView) findViewById(R.id.civ_menu_avatar);
        mTvName = (TextView) findViewById(R.id.tv_user_name);
        mTvPhone = (TextView) findViewById(R.id.tv_user_phone);
        // item
        mSchoolchild = new ItemMenuController(mContext, findViewById(R.id.menu_schoolchild));
        mCollection = new ItemMenuController(mContext, findViewById(R.id.menu_collection));
        mOrder = new ItemMenuController(mContext, findViewById(R.id.menu_order));
        mSignIn = new ItemMenuController(mContext, findViewById(R.id.menu_sign_in));
        mWallet = new ItemMenuController(mContext, findViewById(R.id.menu_wallet));
        mAddress = new ItemMenuController(mContext, findViewById(R.id.menu_address));
        mSetting = new ItemMenuController(mContext, findViewById(R.id.menu_setting));
    }


    public void UpdateNickName(NickNameEvent nickNameEvent){

        mTvName.setText(nickNameEvent.getNickName());

    }

    /**
     * setup
     */
    public void setupViews() {
       updatePersonalInfo();
        // 账户管理
         mAccountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AccountManageActivity.class);
                gotoCertainPage(intent);
            }
        });
       // 学生信息
        mSchoolchild.init(R.drawable.ic_schoolchild, "被订购课程", R.drawable.ic_arrow_right)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ((BaseActivity) mContext).setCustomDialog();
                        BmobQuery<TeacherModel> query = new BmobQuery<TeacherModel>();
                        query.addWhereEqualTo("tuser", mUserModel);
                        query.include("tuser");

                        query.findObjects(new FindListener<TeacherModel>() {

                            @Override
                            public void done(List<TeacherModel> videoModels, BmobException e) {
                                ((BaseActivity) mContext).mCustomDialog.dismiss();

                                if (e == null && videoModels != null && videoModels.size() != 0) {

                                    ((BaseActivity) mContext).skip(mContext, TeacherOrderListActivity.class);

                                } else if (videoModels != null && videoModels.size() == 0) {

                                    ((BaseActivity) mContext).showSnackbar("你不是老师，请联系审核!");
                                } else if (e != null) {

                                    Log.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());

                                }

                            }
                        });
                    }
                });
        // 我的收藏
        mCollection.init(R.drawable.ic_collection, "发布课程", R.drawable.ic_arrow_right)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((BaseActivity) mContext).setCustomDialog();
                        BmobQuery<TeacherModel> query = new BmobQuery<TeacherModel>();
                        query.addWhereEqualTo("tuser", mUserModel);
                        query.include("tuser");

                        query.findObjects(new FindListener<TeacherModel>() {

                            @Override
                            public void done(List<TeacherModel> videoModels,BmobException e) {
                                ((BaseActivity) mContext).mCustomDialog.dismiss();

                                if(e==null&& videoModels!=null&&videoModels.size()!=0){
                                    Bundle bundle =new Bundle();
                                    bundle.putSerializable("Teachermodel",videoModels.get(0));

                                    ((BaseActivity) mContext).skip(mContext, ReleaseCourseActivity.class,bundle);

                                }else if(videoModels!=null&&videoModels.size()==0) {

                                    ((BaseActivity) mContext).showSnackbar("你不是老师，请联系审核!");
                                }else if(e!=null){

                                    Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());

                                }

                            }

                        });


                    }
                });
        // 我的订单
        mOrder.init(R.drawable.ic_order, "我的订单", R.drawable.ic_arrow_right)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, OrderManageActivity.class);
                        gotoCertainPage(intent);

                    }
                });
        // 我的报名
        mSignIn.init(R.drawable.ic_sign_in, "我的报名", R.drawable.ic_arrow_right)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, OrderManageActivity.class);
                        gotoCertainPage(intent);
                    }
                });
/*        // 钱包管理
        mWallet.init(R.drawable.ic_wallet, "钱包管理", R.drawable.ic_arrow_right)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/
/*        // 地址管理
        mAddress.init(R.drawable.ic_address, "地址管理", R.drawable.ic_arrow_right)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });*/
/*        // 设置
        mSetting.init(R.drawable.ic_setting, "设置", R.drawable.ic_arrow_right)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });*/
    }

    /**
     * 设置头像
     */
    public void setAvatar(String avatar) {
        if (mCivAvatar != null) {
            mCivAvatar.loadImageFromURL(avatar, R.drawable.ic_avatar);
        }
    }

    /**
     * 更新个人信息
     */
    public void updatePersonalInfo() {

        if(mUserModel.getImgpath()==null){
            mCivAvatar.setImageResource(R.drawable.ic_avatar);
        }else{
            new AsyncImageLoader(mUserModel.getImgpath(),mCivAvatar).start();
        }
        mTvName.setText(mUserModel.getNick());
        mTvPhone.setText(mUserModel.getUsername());

    }

    /**
     * 去到指定页面
     *
     * @param intent Intent
     */
    private void gotoCertainPage(Intent intent) {
        startActivity(intent);
        // 关闭DrawerLayout
        closeDrawerLayout();
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
