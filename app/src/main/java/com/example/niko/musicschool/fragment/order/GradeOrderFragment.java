package com.example.niko.musicschool.fragment.order;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.adapter.order.OrderAdapter;
import com.example.niko.musicschool.adapter.order.OrderGradeAdapter;
import com.example.niko.musicschool.fragment.BaseFragment;
import com.example.niko.musicschool.model.CourseOrderModel;
import com.example.niko.musicschool.model.GradeStudentModel;
import com.example.niko.musicschool.model.OrderGradeParamModel;
import com.example.niko.musicschool.model.UserModel;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.sanfast.xmutils.dialog.CustomDialog;

/**
 * Created by niko on 2017/3/28.
 */

public class GradeOrderFragment extends BaseFragment {


    private LRecyclerView mLRecyclerView;
    private OrderGradeAdapter mAdapter;
    private int mPage=1;
    private CustomDialog mCustomDialog;

    @Override
    protected View setLayout(LayoutInflater inflater, ViewGroup container) {
        return LayoutInflater.from(mContext).inflate(R.layout.fragment_course, container, false);
    }


    @Override
    protected void initViews() {
        mLRecyclerView = (LRecyclerView)findViewById(R.id.lrecycler_view);
        mAdapter = new OrderGradeAdapter(mContext);
    }

    @Override
    protected void setupViews() {
        final LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mLRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mLRecyclerView.setAdapter(mLRecyclerViewAdapter);
        // mLRecyclerView.setPullRefreshEnabled(true);
        mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_LOADING).show();
        queryOrderList();
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryOrderList();
            }
        });

    }



    private void queryOrderList() {

        BmobQuery<OrderGradeParamModel> query = new BmobQuery<OrderGradeParamModel>();

        query.include("user,gradeStudentModel,majorModel");
        String id =BmobUser.getCurrentUser(UserModel.class).getObjectId();
        query.addWhereEqualTo("user",id);
        query.order("-createdAt");
        query.findObjects(new FindListener<OrderGradeParamModel>() {

            @Override
            public void done(List<OrderGradeParamModel> orderGradeParamModel, BmobException e) {
                mLRecyclerView.refreshComplete(mPage);
                mCustomDialog.dismiss();
                if (e == null) {

                    mAdapter.setData(orderGradeParamModel);

                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });


    }


}
