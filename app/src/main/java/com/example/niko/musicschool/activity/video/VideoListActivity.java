package com.example.niko.musicschool.activity.video;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.adapter.teacher.MusicTeacherListAdapter;
import com.example.niko.musicschool.adapter.video.VideoListAdapter;
import com.example.niko.musicschool.control.appbar.AppbarIconController;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;
import com.example.niko.musicschool.model.CourseModel;
import com.example.niko.musicschool.model.VideoModel;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by niko on 2017/4/10.
 */

public class VideoListActivity extends BaseActivity {
    private LRecyclerView mLRecyclerView;
    private VideoListAdapter mAdapter;
    private int mPage = 1;
    private AppbarNormalController mAppbar;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_vedio_list);
    }

    @Override
    protected void initViews() {
        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));
        mLRecyclerView = (LRecyclerView)findViewById(R.id.lrecycler_view);
    }

    @Override
    protected void setupViews() {
        mAppbar.init(R.drawable.selector_btn_back, "视频列表", "");
        mAppbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        setCustomDialog();
        mAdapter = new VideoListAdapter(mContext);

        final LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mLRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLRecyclerView.setAdapter(mLRecyclerViewAdapter);
        // mLRecyclerView.setPullRefreshEnabled(true);
        queryList();
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryList();
            }
        });





    }

    /**
     * 请求数据
     */
    private void queryList() {
        BmobQuery<VideoModel> query = new BmobQuery<VideoModel>();


        query.findObjects(new FindListener<VideoModel>() {

            @Override
            public void done(List<VideoModel> videoModels, BmobException e) {
                mLRecyclerView.refreshComplete(mPage);
                closeCustomDialog();
                if (e == null) {
                    mAdapter.setData(videoModels);
                    closeCustomDialog();
                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });




    }
}
