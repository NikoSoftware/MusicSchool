package com.example.niko.musicschool.activity.main;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.appbar.AppbarMainController;
import com.example.niko.musicschool.control.main.MainContentController;
import com.example.niko.musicschool.control.main.MainMenuFooterController;
import com.example.niko.musicschool.control.main.MainMenuHeaderController;
import com.example.niko.musicschool.eventbus.NickNameEvent;
import com.example.niko.musicschool.tool.permission.PermissionFail;
import com.example.niko.musicschool.tool.permission.PermissionHelper;
import com.example.niko.musicschool.tool.permission.PermissionSucceed;
import com.nineoldandroids.view.ViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {

    public static final int REQUECT_CODE_SDCARD = 2;
    private final String TAG = "主界面";
    public static final String RECEIVER_UPDATE_PERSONAL_INFO = "receiver_update_personal_info";
    private AppbarMainController mAppbar;
    private DrawerLayout mDrawerLayout;
    private View mViewContent;
    private MainMenuHeaderController mMenuHeader;
    private MainMenuFooterController mMenuFooter;
    private MainContentController mContent;


    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {

        EventBus.getDefault().register(this);
        // appbar
        mAppbar = new AppbarMainController(mContext, findViewById(R.id.appbar));
        // 侧滑菜单
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // header view
        NavigationView navigationViewHeader = (NavigationView) findViewById(R.id.navigation_view_header);
        View header = navigationViewHeader.getHeaderView(0);
        if (header == null) {
            header = navigationViewHeader.inflateHeaderView(R.layout.activity_main_menu_header);
        }
        mMenuHeader = new MainMenuHeaderController(mContext, header);
        mMenuHeader.setDrawerLayout(mDrawerLayout);
        // footer view
        mMenuFooter = new MainMenuFooterController(mContext, findViewById(R.id.navigation_view_footer));
        mMenuFooter.setDrawerLayout(mDrawerLayout);
        // content
        mViewContent = findViewById(R.id.layout_main);
        mContent = new MainContentController(mContext, mViewContent);
        /**
         * 运行时权限
         */
        PermissionHelper.requestPermission(MainActivity.this, REQUECT_CODE_SDCARD,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},"请开启内存信息读写权限");
    }

    /**
     * 更新名字
     * @param nickNameEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateNickName(NickNameEvent nickNameEvent){

        mMenuHeader.UpdateNickName(nickNameEvent);
    }


    @Override
    protected void setupViews() {
        mAppbar.init("", getResources().getString(R.string.app_name), R.drawable.selector_btn_message);
        mAppbar.setOnAvatarClickListener(new AppbarMainController.OnAvatarClickListener() {
            @Override
            public void onAvatarClick() {
                if (mDrawerLayout != null) {
                    if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            }
        });
        mAppbar.setOnMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });



        // DrawerListener
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                float scale = 0.1f * (1.0f - slideOffset) + 0.9f;
                ViewHelper.setScaleX(mViewContent, scale);
                ViewHelper.setScaleY(mViewContent, scale);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });




        mMenuHeader.setupViews();
        mMenuFooter.setupViews();
        mContent.setupViews();

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionHelper.requestPermissionsResult(this,requestCode,permissions);
    }

    /**
     * 运行时权限成功时调用
     */
    @PermissionSucceed(requestCode = REQUECT_CODE_SDCARD)
    private void callPhone() {
     //   Toast.makeText(mContext,"申请权限成功",Toast.LENGTH_SHORT).show();
    }


    /**
     * 运行时权限失败时调用
     */
    @PermissionFail(requestCode = REQUECT_CODE_SDCARD)
    private void failPermission(){

       // Toast.makeText(mContext,"申请权限失败",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
