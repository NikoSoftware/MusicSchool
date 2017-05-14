package com.example.niko.musicschool.view;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.utils.AppTool;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import cn.sanfast.xmutils.utils.AppUtils;

/**
 * 购买信息
 * Created by niko on 2016/8/31.
 */
public class SelectDayPopupWindow extends PopupWindow {

    private Activity mActivity;
    private OnConfirmListener mListener;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mWeek;
    private boolean mIsMin =false;

    /**
     * 构造器
     *
     * @param activity
     */
    public SelectDayPopupWindow(Activity activity) {
        super(activity);
        mActivity = activity;
        init();
    }


    /**
     * 构造器
     *
     * @param activity
     */
    public SelectDayPopupWindow(Activity activity,boolean isMin) {
        super(activity);
        mActivity = activity;
        mIsMin= isMin;
        init();
    }


    /**
     * 初始化
     */
    private void init() {
        View view = View.inflate(mActivity, R.layout.popup_window_time_select, null);
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
        View btnConfirm = view.findViewById(R.id.btn_confirm);
        View btnCancel = view.findViewById(R.id.btn_cancel);
        final TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
        final TextView tvWeek = (TextView) view.findViewById(R.id.tv_week);

        // on click
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.dp_time);
        if(mIsMin){
            datePicker.setMinDate(System.currentTimeMillis());
        }else {
            datePicker.setMaxDate(System.currentTimeMillis());
        }
        datePicker.setCalendarViewShown(false);
        setDatePickerDividerColor(datePicker);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //初始化年月日
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        tvDate.setText(mYear + "年" + AppTool.tansformFillTime(mMonth) + "月" + AppTool.tansformFillTime(mDay) + "日");

        tvWeek.setText(TransformWeek(calendar.get(Calendar.DAY_OF_WEEK) - 1));

        datePicker.init(mYear, mMonth - 1, mDay,
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                        mYear = i;
                        mMonth = i1 + 1;
                        mDay = i2;

                        tvDate.setText(mYear + "年" + AppTool.tansformFillTime(mMonth) + "月" + AppTool.tansformFillTime(mDay) + "日");
                        calendar.clear();
                        calendar.set(i, i1, i2);
                        tvWeek.setText(TransformWeek(calendar.get(Calendar.DAY_OF_WEEK) - 1));
                        mWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

                    }
                });

        btnCancel.setOnClickListener(mOnCancelListener);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onConfirm(mYear, mMonth, mDay);
                }
                close();
            }
        });
    }


    /**
     * 获得周
     *
     * @return
     */
    public String getWeek() {
        return TransformWeek(mWeek);
    }


    public String TransformWeek(int week) {

        String result = null;
        switch (week) {
            case 0:
                result = "周日";
                break;
            case 1:
                result = "周一";
                break;
            case 2:
                result = "周二";
                break;
            case 3:
                result = "周三";
                break;
            case 4:
                result = "周四";
                break;
            case 5:
                result = "周五";
                break;
            case 6:
                result = "周六";
                break;
            default:
                result = week + "";
                break;

        }
        return result;
    }


    /**
     * 设置时间选择器的分割线颜色
     *
     * @param datePicker
     */
    private void setDatePickerDividerColor(DatePicker datePicker) {
        // Divider changing:

        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);

        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(mActivity.getResources().getColor(R.color.colorPrimary)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
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
        void onConfirm(int year, int month, int day);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        mListener = listener;
    }

}