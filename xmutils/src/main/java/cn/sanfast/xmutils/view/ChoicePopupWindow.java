package cn.sanfast.xmutils.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import cn.sanfast.xmutils.R;

public class ChoicePopupWindow extends PopupWindow {

    private Activity mActivity;
    private Button mBtnTop;
    private Button mBtnOne;
    private Button mBtnTwo;
    private Button mBtnBottom;
    private ImageView mIvLineOne;
    private ImageView mIvLineTwo;
    private View mView;

    /**
     * 一个选项
     *
     * @param activity
     * @param title
     * @param listener
     */
    public ChoicePopupWindow(Activity activity, String title, OnClickListener listener) {
        super(activity);
        this.mActivity = activity;
        init();
        setButtonsVisibility(View.GONE, View.GONE);
        mBtnTop.setVisibility(View.GONE);
        mView.findViewById(R.id.iv_line_top).setVisibility(View.GONE);
        mBtnBottom.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.selector_popup_window_button_single));
        mBtnBottom.setText(title);
        mBtnBottom.setOnClickListener(listener);
    }

    public ChoicePopupWindow(Activity activity, int colorId, String title, OnClickListener listener) {
        super(activity);
        this.mActivity = activity;
        init();
        setButtonsVisibility(View.GONE, View.GONE);
        mBtnTop.setVisibility(View.GONE);
        mView.findViewById(R.id.iv_line_top).setVisibility(View.GONE);
        mBtnBottom.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.selector_popup_window_button_single));
        mBtnBottom.setText(title);
        mBtnBottom.setTextColor(colorId);
        mBtnBottom.setOnClickListener(listener);
    }

    /**
     * 两个选项
     *
     * @param activity
     * @param titleOne
     * @param listenerOne
     * @param titleTwo
     * @param listenerTwo
     */
    public ChoicePopupWindow(Activity activity, String titleOne, OnClickListener listenerOne, String titleTwo, OnClickListener listenerTwo) {
        super(activity);
        mActivity = activity;
        init();
        setButtonsVisibility(View.GONE, View.GONE);
        mBtnTop.setText(titleOne);
        mBtnTop.setOnClickListener(listenerOne);
        mBtnBottom.setText(titleTwo);
        mBtnBottom.setOnClickListener(listenerTwo);
    }

    public void init() {
        mView = View.inflate(mActivity, R.layout.popup_window_choice, null);
        // 设置出入动画
        this.setAnimationStyle(R.style.PopupWindowAnimation);

        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                recoverAlpha();
            }
        });
        this.setOutsideTouchable(true);
        this.setContentView(mView);
        this.update();
        this.showAtLocation(getContentView(), Gravity.BOTTOM, 0, 0);
        /**设置Activity透明**/
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.6f;
        mActivity.getWindow().setAttributes(lp);

        mView.findViewById(R.id.btn_cancel_top).setOnClickListener(new OnCancelClickListener());
        mView.findViewById(R.id.btn_cancel_bottom).setOnClickListener(new OnCancelClickListener());

        mBtnTop = (Button) mView.findViewById(R.id.btn_top);
        mBtnBottom = (Button) mView.findViewById(R.id.btn_bottom);

        mBtnOne = (Button) mView.findViewById(R.id.btn_one);
        mIvLineOne = (ImageView) mView.findViewById(R.id.iv_line_one);
        mBtnTwo = (Button) mView.findViewById(R.id.btn_two);
        mIvLineTwo = (ImageView) mView.findViewById(R.id.iv_line_two);

    }

    private void setButtonsVisibility(int oneState, int twoState) {
        mBtnOne.setVisibility(oneState);
        mIvLineOne.setVisibility(oneState);
        mBtnTwo.setVisibility(twoState);
        mIvLineTwo.setVisibility(twoState);

    }

    private class OnCancelClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            close();
        }
    }

    /**
     * 恢复Activity的透明效果
     */
    private void recoverAlpha() {
        /**取消Activity的透明效果**/
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 1f;
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

}