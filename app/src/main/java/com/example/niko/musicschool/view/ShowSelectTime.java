package com.example.niko.musicschool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 点击选择时间点控件
 * Created by niko on 2016/9/8.
 */


public class ShowSelectTime extends View implements View.OnTouchListener {

    public int mViewWidth;
    public int mViewHeight;

    public List<TimeMode> mListTime = new ArrayList<>();
    private Paint mTextPaint;
    private Paint mCellPaint;
    private Paint mCellPaintFrame;
    private TimePaintOption mSelectedOption;
    private TimePaintOption mCanSelectOption;
    private TimePaintOption mNoSelectOption;
    private int mCellWidth;
    private int mSize;
    private float mDensity;
    private OnTimeSelectedListener onTimeSelectedListener;
    private List<Integer> mList = new ArrayList<Integer>();

    public ShowSelectTime(Context context) {
        super(context);
        init();
    }

    public ShowSelectTime(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShowSelectTime(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mDensity = getResources().getDisplayMetrics().density;
        mTextPaint = new Paint();
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mCellPaint = new Paint();
        mCellPaint.setAntiAlias(true);
        mCellPaint.setStyle(Paint.Style.FILL);

        mCellPaintFrame = new Paint();
        mCellPaintFrame.setAntiAlias(true);
        mCellPaintFrame.setStyle(Paint.Style.STROKE);
        mCellPaintFrame.setStrokeWidth(2);

        // 不可选
        mNoSelectOption = new TimePaintOption();
        mNoSelectOption.textColor = Color.parseColor("#90A1AC");
        mNoSelectOption.bgColor = Color.parseColor("#EFF4F6");
        mNoSelectOption.fontSize = 15 * mDensity;
        mNoSelectOption.height = (int) (48 * mDensity);
        // 可选
        mCanSelectOption = new TimePaintOption();
        mCanSelectOption.textColor = Color.parseColor("#3CCD9C");
        mCanSelectOption.bgColor = Color.parseColor("#FFFFFF");
        mCanSelectOption.fontSize = 15 * mDensity;
        mCanSelectOption.height = (int) (int) (48 * mDensity);
        // 已选
        mSelectedOption = new TimePaintOption();
        mSelectedOption.textColor = Color.parseColor("#F88F00");
        mSelectedOption.bgColor = Color.parseColor("#FFFFFF");
        mSelectedOption.fontSize = 15 * mDensity;
        mSelectedOption.height = (int) (48 * mDensity);

        List<TimeMode> textList = new ArrayList<>();
        for (int i = 8; i < 23; i++) {

            TimeMode timeModel = new TimeMode();
            timeModel.setTime(i);
            timeModel.setStatus(TimeMode.NORMAL);
            textList.add(timeModel);

        }

        setTime(textList);

        setOnTouchListener(this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightResult = mViewHeight;
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.AT_MOST) {
            heightResult = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getMode(heightMeasureSpec));
        } else {
            heightResult = MeasureSpec.makeMeasureSpec(mViewHeight, MeasureSpec.AT_MOST);
        }
        setMeasuredDimension(widthMeasureSpec, heightResult);
        super.onMeasure(widthMeasureSpec, heightResult);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //预留两个cellwidth间隔
        this.mCellWidth = this.mViewWidth / 6;
        int spaceWidth = (mCellWidth - 4) / 4;
        this.mSize = mListTime.size();
        Paint.FontMetricsInt fontMetrics = null;
        TimePaintOption option = null;
        TimeMode timeMode = null;

        for (int i = 0; i < mSize; i++) {
            timeMode = mListTime.get(i);

            if (timeMode.status == TimeMode.NORMAL) {
                option = mNoSelectOption;
            } else if (timeMode.status == TimeMode.CAN_SELECTED) {
                option = mCanSelectOption;
            } else {
                option = mSelectedOption;
            }

            int left = (i % 5 == 0) ? 2 : (i % 5 * spaceWidth) + i % 5 * mCellWidth;
            int top = (i / 5 == 0) ? 2 : (i / 5 * spaceWidth) + i / 5 * option.height;
            int right = left + mCellWidth - ((i % 5 == 4) ? 2 : 0);
            int bottom = top + option.height;

            Rect rect = new Rect(left, top, right, bottom);
            RectF rectF = new RectF(rect);
            mCellPaint.setColor(option.bgColor);
            canvas.drawRoundRect(rectF, 8, 8, mCellPaint);
            if (timeMode.status != TimeMode.NORMAL) {
                mCellPaintFrame.setColor(option.textColor);
                canvas.drawRoundRect(rectF, 8, 8, mCellPaintFrame);
            }

            //绘制字体
            mTextPaint.setTextSize(option.fontSize);
            mTextPaint.setColor(option.textColor);
            fontMetrics = mTextPaint.getFontMetricsInt();
            int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;

            int timeLocation = baseline - fontMetrics.bottom * 2;
            int textLocation = rect.bottom - fontMetrics.bottom * 5 / 3;

            canvas.drawText(timeMode.getStrTime(), rect.centerX(), timeLocation, mTextPaint);
            if (timeMode.status == TimeMode.NORMAL) {
                canvas.drawText("不可选", rect.centerX(), textLocation, mTextPaint);
            } else if (timeMode.status == TimeMode.CAN_SELECTED) {
                canvas.drawText("可选", rect.centerX(), textLocation, mTextPaint);
            } else if (TimeMode.SELECTED == timeMode.status) {
                canvas.drawText("已选", rect.centerX(), textLocation, mTextPaint);
            } else if (TimeMode.RESERVED == timeMode.status) {
                canvas.drawText("已预订", rect.centerX(), textLocation, mTextPaint);
            }
        }

        super.onDraw(canvas);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();

                int spaceWidth = (mCellWidth - 4) / 4;

                for (int i = 0; i < this.mSize; i++) {
                    TimeMode timeMode = mListTime.get(i);
                    int left = (i % 5 == 0) ? 2 : (i % 5 * spaceWidth) + i % 5 * mCellWidth;
                    int top = (i / 5 == 0) ? 2 : (i / 5 * spaceWidth) + i / 5 * mCanSelectOption.height;
                    int right = left + mCellWidth - ((i % 5 == 4) ? 2 : 0);
                    int bottom = top + mCanSelectOption.height;

                    Rect rect = new Rect(left, top, right, bottom);
                    RectF rectF = new RectF(rect);

                    if (rectF.contains(x, y)) {
                        boolean flag = false;
                        switch (timeMode.getStatus()) {
                            case TimeMode.CAN_SELECTED:
                                /**
                                 * 设置只能单选 mList.clear();
                                 */
                                for (TimeMode mode : mListTime) {
                                    mode.setStatus(TimeMode.CAN_SELECTED);
                                }

                                timeMode.setStatus(TimeMode.SELECTED);
                                if (!mList.contains(timeMode.getTime())) {

                                    mList.add(timeMode.getTime());
                                }
                                invalidate();
                                flag = true;
                                break;
                            case TimeMode.SELECTED:
                                timeMode.setStatus(TimeMode.CAN_SELECTED);
                                if (mList.contains(timeMode.getTime())) {
                                    mList.remove(Integer.valueOf(timeMode.getTime()));
                                }
                                invalidate();
                                flag = true;
                                break;

                        }
                        if (onTimeSelectedListener != null) {
                            onTimeSelectedListener.onSelected(mList);
                        }
                        if (flag) {
                            return true;
                        }
                    }
                }

        }
        return false;
    }

    public List<Integer> getSelectedList() {
        return mList;
    }

    public interface OnTimeSelectedListener {

        void onSelected(List<Integer> list);

    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {

        this.onTimeSelectedListener = onTimeSelectedListener;
    }

    private void setTime(List<TimeMode> listTime) {
        mListTime.clear();
        mListTime.addAll(listTime);
        mViewHeight = mNoSelectOption.height * 3 + mNoSelectOption.height / 2 + 10;
    }

    /**
     * 设置可选时间点
     *
     * @param list List
     */
    public void setCanSelectData(List<Integer> list) {
        if (list == null) {
            return;
        }
        for (TimeMode timeMode : mListTime) {
            if (list.contains(timeMode.getTime())) {
                timeMode.setStatus(TimeMode.CAN_SELECTED);
            }
        }
        postInvalidate();
    }

    /**
     * 设置已选时间点
     *
     * @param list List
     */
    public void setSelectedData(List<Integer> list) {
        if (list == null) {
            return;
        }
        mList = list;
        for (TimeMode timeMode : mListTime) {
            if (list.contains(timeMode.getTime())) {
                timeMode.setStatus(TimeMode.SELECTED);
            }
        }
        postInvalidate();
    }

    /**
     * 设置已预订时间点
     *
     * @param list List
     */
    public void setReservedData(List<Integer> list) {
        if (list == null) {
            return;
        }
        if (mList == null) {
            mList = list;
        } else {
            mList.addAll(list);
        }
        for (TimeMode timeMode : mListTime) {
            if (list.contains(timeMode.getTime())) {
                timeMode.setStatus(TimeMode.RESERVED);
            }
        }
        postInvalidate();
    }

    public class TimePaintOption {

        public int textColor;
        public int bgColor;
        public float fontSize;
        public int height;
        public int type;

    }

    public class TimeMode {

        public static final int NORMAL = 0;
        public static final int CAN_SELECTED = 1;
        public static final int SELECTED = 2;
        public static final int RESERVED = 3;
        public int status;
        public int time;
        public String strTime;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
            setStrTime(time + ":00");
        }

        public String getStrTime() {
            return strTime;
        }

        public void setStrTime(String strTime) {
            this.strTime = strTime;
        }
    }


}
