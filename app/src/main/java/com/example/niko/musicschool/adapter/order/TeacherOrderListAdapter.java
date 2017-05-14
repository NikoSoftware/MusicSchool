package com.example.niko.musicschool.adapter.order;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.activity.video.VideoActivity;
import com.example.niko.musicschool.adapter.BaseRecyclerViewAdapter;
import com.example.niko.musicschool.adapter.BaseViewHandle;
import com.example.niko.musicschool.model.CourseModel;
import com.example.niko.musicschool.model.CourseOrderModel;
import com.example.niko.musicschool.model.VideoModel;

import java.util.Map;

/**
 * Created by niko on 2017/3/26.
 */

public class TeacherOrderListAdapter extends BaseRecyclerViewAdapter<CourseOrderModel> {
    public TeacherOrderListAdapter(Context context) {
        super(context);
    }

    @Override
    public void setViewMap(Map<Integer, Integer[]> map) {
        map.put(R.layout.item_teacher_order,new Integer[]{
              R.id.tv_content
        });
    }

    @Override
    public void onBind(BaseViewHandle holder, int position, CourseOrderModel courseOrderModel, int viewType) {

        CourseModel model = courseOrderModel.getCourseModel();
        ((TextView) holder.getView(R.id.tv_content)).
                setText("学生名称："+courseOrderModel.getCname()+
                        "\n学生电话："+courseOrderModel.getCphone()
                        + "\n课程名："+model.getCname()+"\n开课时间："
                +model.getCdate()+" "+model.getCtime()+"\n地址："+model.getCaddress()+
                "\n对应专业："+model.getCsubject()+ "\n价格："+model.getCprice()+"元");


    }


    @Override
    public int getItemViewsType(int position) {
        return R.layout.item_teacher_order;
    }

}
