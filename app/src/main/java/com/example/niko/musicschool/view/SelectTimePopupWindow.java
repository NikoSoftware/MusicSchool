package com.example.niko.musicschool.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.niko.musicschool.R;

import java.util.ArrayList;
import java.util.List;

import cn.sanfast.xmutils.utils.AppUtils;

/**
 * 选择时间点
 * <p>
 * Created by niko on 2016/9/10
 */
public class SelectTimePopupWindow extends PopupWindow {

    private Activity mActivity;
    private TextView mTvDate;
    private ShowSelectTime mSelectTimeView;
    private TextView mTvSelectedTime;
    private OnConfirmListener mListener;
    private List<Integer> mList;
    private int mMonthIndex;
    private int mDay;

    /**
     * 构造器
     *
     * @param activity
     */
    public SelectTimePopupWindow(Activity activity) {
        super(activity);
        mActivity = activity;
        mList = new ArrayList<Integer>();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        View view = View.inflate(mActivity, R.layout.popup_window_select_time, null);
        // 设置出入动画
        this.setAnimationStyle(R.style.PopupWindowAnimation);
        // 设置宽高
        this.setWidth(AppUtils.getScreenWidth(mActivity) - AppUtils.dp2px(mActivity, 32));
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
        mTvDate = (TextView) view.findViewById(R.id.tv_date);
        mTvSelectedTime = (TextView) view.findViewById(R.id.tv_selected_time);
        mTvSelectedTime.setText("已选：0 小时");
        mSelectTimeView = (ShowSelectTime) view.findViewById(R.id.show_time);
        mSelectTimeView.setOnTimeSelectedListener(new ShowSelectTime.OnTimeSelectedListener() {
            @Override
            public void onSelected(List<Integer> list) {
                if (list != null) {
                    mTvSelectedTime.setText("已选：" +(list.size()>0 ? "1":"0") + " 小时");
                }
            }
        });
        View btnConfirm = view.findViewById(R.id.btn_confirm);
        View btnCancel = view.findViewById(R.id.btn_cancel);
        // on click
        btnCancel.setOnClickListener(mOnCancelListener);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onConfirm(mSelectTimeView.getSelectedList());
                }
                close();
            }
        });
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
        void onConfirm(List<Integer> list);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        mListener = listener;
    }

    /**
     * 重置数据，每一次必须调用！！！
     */
    public void reset(int monthIndex, int day) {
        // 同一天，不重置数据
        if (monthIndex == mMonthIndex && day == mDay) {
            return;
        }
        mMonthIndex = monthIndex;
        mDay = day;
        if (mList == null) {
            mList = new ArrayList<Integer>();
        } else {
            mList.clear();
        }
    }

    /**
     * 设置日期
     * @param year
     * @param month
     * @param day
     */
    public void setDate(int year, int month, int day) {
        mTvDate.setText(year + "年" + (month < 10 ? "0" + month : month) + "月" + (day < 10 ? "0" + day : day) + "日");
    }

    /**
     * 设置可选项
     *
     * @param list List
     */
    public void setCanSelectData(List<Integer> list) {
        mSelectTimeView.setCanSelectData(list);
    }

    /**
     * 设置已选项
     *
     * @param list List
     */
    public void setSelectedData(List<Integer> list) {
        mSelectTimeView.setSelectedData(list);
        mTvSelectedTime.setText("已选：" + mSelectTimeView.getSelectedList().size() + " 小时");
    }

}