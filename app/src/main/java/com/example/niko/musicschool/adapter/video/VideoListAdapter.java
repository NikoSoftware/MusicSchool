package com.example.niko.musicschool.adapter.video;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.activity.teacher.TeacherActivtiy;
import com.example.niko.musicschool.activity.video.VideoActivity;
import com.example.niko.musicschool.adapter.BaseRecyclerViewAdapter;
import com.example.niko.musicschool.adapter.BaseViewHandle;
import com.example.niko.musicschool.model.CourseModel;
import com.example.niko.musicschool.model.VideoModel;

import java.util.Map;

import cn.sanfast.xmutils.bitmap.loader.AsyncImageLoader;

/**
 * Created by niko on 2017/3/26.
 */

public class VideoListAdapter extends BaseRecyclerViewAdapter<VideoModel> {
    public VideoListAdapter(Context context) {
        super(context);
    }

    @Override
    public void setViewMap(Map<Integer, Integer[]> map) {
        map.put(R.layout.item_video,new Integer[]{
              R.id.rl_contain, R.id.tv_name,R.id.tv_time
        });
    }

    @Override
    public void onBind(BaseViewHandle holder, int position, final VideoModel model, int viewType) {

        ((TextView) holder.getView(R.id.tv_name)).setText(model.getName());
        ((TextView) holder.getView(R.id.tv_time)).setText(model.getCreatedAt());
        ((RelativeLayout) holder.getView(R.id.rl_contain)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("VideoModel",model);

                ((BaseActivity) context).skip(context, VideoActivity.class,bundle);

            }
        });
    }


    @Override
    public int getItemViewsType(int position) {
        return R.layout.item_video;
    }

}
