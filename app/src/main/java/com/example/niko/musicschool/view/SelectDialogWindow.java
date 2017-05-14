package com.example.niko.musicschool.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.niko.musicschool.R;

import java.util.ArrayList;

import cn.sanfast.xmutils.utils.AppUtils;

/**
 * 购买信息
 * Created by niko on 2016/8/31.
 */
public class SelectDialogWindow extends PopupWindow {

    private final String mTitle;
    private Activity mActivity;
    private EditText mEtName;
    private EditText mEtPhone;
    private OnConfirmListener mListener;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mListData ;
    private TextView mTextTitle;
    private OnConfirmListener mOnConfirmListener=null;

    /**
     * 构造器
     *
     * @param activity
     */
    public SelectDialogWindow(Activity activity, ArrayList<String> mListData, String title) {
        super(activity);
        mActivity = activity;
        this.mListData =mListData;
        this.mTitle =title;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        View view = View.inflate(mActivity, R.layout.pop_select_dialog, null);

        mTextTitle = (TextView) view.findViewById(R.id.tv_title);
        mTextTitle.setText(mTitle);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.pop_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(new RecyclerViewAdapter());
        if(mListData.size()>6){
            mRecyclerView.getLayoutParams().height = AppUtils.dp2px(mActivity, 50)*6;
        }


        // 设置出入动画
        this.setAnimationStyle(R.style.PopupWindowAnimation);
        // 设置宽高
        this.setWidth(AppUtils.getScreenWidth(mActivity) - AppUtils.dp2px(mActivity, 64));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 消失事件
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                recoverAlpha();
            }
        });
        // 外部可点击
        this.setOutsideTouchable(true);
        this.setContentView(view);
        this.update();


    }



    public class RecyclerViewAdapter extends RecyclerView.Adapter{


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_pop_select_dialog,null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            MHolder mHolder = (MHolder) holder;

            mHolder.popSelectItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnConfirmListener!=null){
                        mOnConfirmListener.onConfirm(mListData.get(position),position);
                    }
                    close();
                }
            });
            mHolder.tvContent.setText(mListData.get(position));

        }

        @Override
        public int getItemCount() {
            if(mListData==null){
                return 0;
            }
            return mListData.size();
        }
    }



    public class MHolder extends RecyclerView.ViewHolder{


        private final RelativeLayout popSelectItem;
        private final TextView tvContent;

        public MHolder(View itemView) {
            super(itemView);
            tvContent = (TextView)itemView.findViewById(R.id.tv_content);
            popSelectItem =(RelativeLayout)itemView.findViewById(R.id.pop_select_item);
        }
    }




    /**
     * 取消、关闭
     */
    private View.OnClickListener mOnCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            close();
        }
    };

    /**
     * 恢复Activity的透明效果
     */
    private void recoverAlpha() {
        /**取消Activity的透明效果**/
        float alpha = 1f;
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = alpha;
        if (alpha == 1) {
            // 不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            // 此行代码主要是解决在华为手机上半透明效果无效的bug
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        mActivity.getWindow().setAttributes(lp);
    }

    /**
     * 显示
     */
    public void show() {
        this.showAtLocation(getContentView(), Gravity.CENTER, 0, 0);
        /**设置Activity透明**/
        float alpha = 0.6f;
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = alpha;
        if (alpha == 1) {
            // 不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            // 此行代码主要是解决在华为手机上半透明效果无效的bug
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        mActivity.getWindow().setAttributes(lp);
    }

    /**
     * 关闭，dismiss
     */
    private void close() {
        if (this.isShowing()) {
            this.dismiss();
            recoverAlpha();
        }
    }

    public interface OnConfirmListener {
        void onConfirm(String itemName, int index);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.mOnConfirmListener = listener;
    }

}