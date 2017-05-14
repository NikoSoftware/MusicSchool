package com.example.niko.musicschool.activity.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;
import com.example.niko.musicschool.model.VideoModel;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * Created by niko on 2017/3/12.
 */

public class VideoActivity extends AppCompatActivity {


    private Context mContext;
    private JCVideoPlayerStandard mVideoView;
    private VideoModel mVideoModel;
    private AppbarNormalController mAppbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        mVideoModel = (VideoModel)getIntent().getExtras().get("VideoModel");
        mContext =this;
        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));
        mAppbar.init(R.drawable.selector_btn_back, mVideoModel.getName(), "");
        mAppbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mVideoView = (JCVideoPlayerStandard) findViewById(R.id.video_view);
        Log.e("TAG===>",mVideoModel.getUrl());
        mVideoView.setUp(mVideoModel.getUrl(),JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,mVideoModel.getName());

    }





    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
