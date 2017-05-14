package com.example.niko.musicschool.activity.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.adapter.order.FragmentAdapter;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;
import com.example.niko.musicschool.fragment.order.CourseOrderFragment;
import com.example.niko.musicschool.fragment.order.GradeOrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niko on 2017/3/28.
 */

public class OrderManageActivity extends BaseActivity {

    private final String TAG = "订单管理";
    private int mPosition = 0;
    private AppbarNormalController mAppbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private boolean isGrade = false;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_order_manage);
    }

    @Override
    protected void initViews() {
        // appbar
        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));
        isGrade = getIntent().getBooleanExtra("isGrade",false);

        // views
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void setupViews() {
        // appbar
        mAppbar.init(R.drawable.selector_btn_back, TAG, "");
        mAppbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });

        // view pager
        String[] tabTitles = new String[]{"课程订单","考试报名" };
        List<Fragment> fragments = new ArrayList<Fragment>();
        // 全部
        fragments.add(new CourseOrderFragment());
        fragments.add(new GradeOrderFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), tabTitles, fragments);
        mViewPager.setAdapter(adapter);
        // tab layout
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);
        if(isGrade){
            mTabLayout.getTabAt(1).select();
        }

    }
}
