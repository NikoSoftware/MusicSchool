package com.example.niko.musicschool.adapter.teacher;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.activity.teacher.TeacherActivtiy;
import com.example.niko.musicschool.adapter.BaseRecyclerViewAdapter;
import com.example.niko.musicschool.adapter.BaseViewHandle;
import com.example.niko.musicschool.model.CourseModel;

import java.util.Map;

import cn.sanfast.xmutils.bitmap.loader.AsyncImageLoader;

/**
 * Created by niko on 2017/3/26.
 */

public class MusicTeacherListAdapter extends BaseRecyclerViewAdapter<CourseModel> {
    public MusicTeacherListAdapter(Context context) {
        super(context);
    }

    @Override
    public void setViewMap(Map<Integer, Integer[]> map) {
        map.put(R.layout.item_teacher,new Integer[]{
              R.id.civ_avatar, R.id.tv_teacher,R.id.tv_type,R.id.tv_description,
                R.id.tv_price, R.id.tv_distance, R.id.rl_contain
        });
    }

    @Override
    public void onBind(BaseViewHandle holder, int position, final CourseModel model, int viewType) {

        new AsyncImageLoader(model.getCimg(), ((ImageView) holder.getView(R.id.civ_avatar))).start();
        Log.e("Cimg" , model.getCimg().toString());
        ((TextView) holder.getView(R.id.tv_teacher)).setText(model.getCname());
        ((TextView) holder.getView(R.id.tv_description)).setText(model.getCsubject());
        ((TextView) holder.getView(R.id.tv_price)).setText(model.getCprice()+"元");
        ((TextView) holder.getView(R.id.tv_distance)).setText(model.getCaddress());
        ((RelativeLayout) holder.getView(R.id.rl_contain)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("CourseModel",model);
                ((BaseActivity) context).skip(context, TeacherActivtiy.class,bundle);

            }
        });

    }

    @Override
    public int getItemViewsType(int position) {
        return R.layout.item_teacher;
    }
}
