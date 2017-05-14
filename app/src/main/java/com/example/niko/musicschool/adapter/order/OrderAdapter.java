package com.example.niko.musicschool.adapter.order;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.activity.teacher.TeacherOrderInfoActivtiy;
import com.example.niko.musicschool.adapter.BaseRecyclerViewAdapter;
import com.example.niko.musicschool.adapter.BaseViewHandle;
import com.example.niko.musicschool.model.CourseOrderModel;
import com.example.niko.musicschool.model.OrderInterface;

import java.util.Map;

/**
 * Created by niko on 2017/3/28.
 */

public class OrderAdapter extends BaseRecyclerViewAdapter<CourseOrderModel> {
    public OrderAdapter(Context context) {
        super(context);
    }

    @Override
    public void setViewMap(Map<Integer, Integer[]> map) {

        map.put(R.layout.item_order_manage,new Integer[]{
                R.id.tv_order_title, R.id.tv_order_time,R.id.tv_order_address,
                R.id.tv_order_status,R.id.tv_check_detail,R.id.tv_order_price
        });

    }

    @Override
    public void onBind(BaseViewHandle holder, int position, final CourseOrderModel model, int viewType) {

        ((TextView) holder.getView(R.id.tv_order_title)).setText(model.getName());
        ((TextView) holder.getView(R.id.tv_order_time)).setText("预订时间："+model.getTime());
        ((TextView) holder.getView(R.id.tv_order_address)).setText("编号："+model.getId());
        ((TextView) holder.getView(R.id.tv_order_price)).setText("价格："+model.getPrice()+"元");
        ((TextView) holder.getView(R.id.tv_check_detail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundel = new Bundle();
                bundel.putSerializable("CourseOrderModel",model);
                ((BaseActivity) context).skip(context, TeacherOrderInfoActivtiy.class,bundel);

            }
        });
    }

    @Override
    public int getItemViewsType(int position) {
        return R.layout.item_order_manage;
    }
}
