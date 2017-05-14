package cn.sanfast.xmutils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import cn.sanfast.xmutils.R;

public class CustomDialog {

    public final static String TAG = "CustomDialog";

    /**
     * 等待对话框，可以取消
     */
    public static final int DIALOG_THEME_LOADING_CANCEL = 0;
    /**
     * 等待对话框，不可以取消
     */
    public static final int DIALOG_THEME_LOADING = 1;

    /**
     * 确认对话框，可以取消
     */
    public static final int DIALOG_THEME_WITH_TITLE_CANCEL = 2;
    /**
     * 确认对话框，不可以取消
     */
    public static final int DIALOG_THEME_WITH_TITLE = 3;

    /**
     * 操作对话框，可以取消
     */
    public static final int DIALOG_THEME_NO_TITLE_CANCEL = 4;
    /**
     * 操作对话框，不可以取消
     */
    public static final int DIALOG_THEME_NO_TITLE = 5;

    private boolean mIsCanCancel;
    private int mLayout;
    private int mStyle;
    private Dialog mDialog = null;
    private final Context mContext;
    private final int mTheme;
    private OnCustomDialogListener mListener;

    /**
     * 构造器，实例化一个CustomDialog对象
     *
     * @param context Context
     * @param theme   int
     */
    public CustomDialog(Context context, int theme) {
        this.mContext = context;
        this.mTheme = theme;
        init(theme);
    }

    /**
     * 初始化
     *
     * @param theme int
     */
    private void init(int theme) {
        // 是否可取消
        mIsCanCancel = (theme % 2) == 0;
        // 确定style
        if (mIsCanCancel) {
            mStyle = R.style.DialogThemeCancel;
        } else {
            mStyle = R.style.DialogTheme;
        }
        // 创建一个对话框
        mDialog = new Dialog(mContext, mStyle) {

            @Override
            protected void onStop() {
                super.onStop();
                if (mListener != null) {
                    mListener.onFinish();
                }
            }

            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (mListener != null) {
                    mListener.onKeyDown();
                }
                return super.onKeyDown(keyCode, event);
            }

        };
        // 确定布局
        switch (theme) {
            // 操作对话框
            case DIALOG_THEME_NO_TITLE:
            case DIALOG_THEME_NO_TITLE_CANCEL:
                mLayout = R.layout.dialog_no_title;
                break;
            // 有标题对话框
            case DIALOG_THEME_WITH_TITLE:
            case DIALOG_THEME_WITH_TITLE_CANCEL:
                mLayout = R.layout.dialog_with_title;
                break;
            // 等待对话框
            case DIALOG_THEME_LOADING:
            case DIALOG_THEME_LOADING_CANCEL:
                mLayout = R.layout.dialog_loading;
                break;
        }
        mDialog.setContentView(mLayout);
    }

    /**
     * 设置加载时显示的信息
     *
     * @param text String
     * @return this
     */
    public CustomDialog setLoadingText(String text) {
        if (mTheme == DIALOG_THEME_LOADING || mTheme == DIALOG_THEME_LOADING_CANCEL) {
            TextView loading = (TextView) mDialog.findViewById(R.id.dialog_loading_text);
            if (loading != null) {
                loading.setText(text);
            }
        }
        return this;
    }

    /**
     * 设置标题
     *
     * @param title String
     * @return this
     */
    public CustomDialog setTitle(String title) {
        if (mTheme == DIALOG_THEME_WITH_TITLE || mTheme == DIALOG_THEME_WITH_TITLE_CANCEL) {
            TextView textView = (TextView) mDialog.findViewById(R.id.dialog_title);
            if (textView != null) {
                textView.setText(title);
            }
        }
        return this;
    }

    /**
     * 设置提示信息
     *
     * @param message String
     * @return this
     */
    public CustomDialog setMessage(String message) {
        if (mTheme == DIALOG_THEME_LOADING || mTheme == DIALOG_THEME_LOADING_CANCEL) {
            return this;
        }
        TextView textView = (TextView) mDialog.findViewById(R.id.dialog_message);
        if (textView != null) {
            textView.setText(message);
        }
        return this;
    }

    /**
     * 设置点击事件
     *
     * @param cancel          String
     * @param cancelListener  View.OnClickListener
     * @param confirm         String
     * @param confirmListener View.OnClickListener
     * @return this
     */
    public CustomDialog setButton(String cancel, final View.OnClickListener cancelListener, String confirm, final View.OnClickListener confirmListener) {
        if (mTheme == DIALOG_THEME_LOADING || mTheme == DIALOG_THEME_LOADING_CANCEL) {
            return this;
        }
        // cancel
        TextView btnCancel = (TextView) mDialog.findViewById(R.id.dialog_btn_cancel);
        if (btnCancel != null) {
            btnCancel.setText(cancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (cancelListener != null) {
                        cancelListener.onClick(view);
                    }
                }
            });
        }
        // confirm
        TextView btnConfirm = (TextView) mDialog.findViewById(R.id.dialog_btn_confirm);
        if (btnConfirm != null) {
            btnConfirm.setText(confirm);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (confirmListener != null) {
                        confirmListener.onClick(view);
                    }
                }
            });
        }
        return this;

    }

    /**
     * 设置Dialog事件
     *
     * @param listener OnCustomDialogListener
     * @return this
     */
    public CustomDialog setOnCustomDialogListener(OnCustomDialogListener listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * 显示Dialog
     *
     * @return this
     */
    public CustomDialog show() {
        try {
            mDialog.show();
        } catch (Exception e) {
            Log.e(TAG, "dialog err", e);
        }
        return this;
    }

    /**
     * 关闭Dialog
     */
    public void dismiss() {
        try {
            mDialog.dismiss();
        } catch (Exception e) {
            Log.e(TAG, "dialog err", e);
        }
    }

    /**
     * 取消Dialog
     */
    public void cancel() {
        try {
            mDialog.cancel();
        } catch (Exception e) {
            Log.e(TAG, "dialog err", e);
        }
    }

    /**
     * Dialog是否正在显示
     *
     * @return boolean
     */
    public boolean isShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }
        return false;
    }

    /**
     * 对话框事件
     */
    public abstract class OnCustomDialogListener {

        public void onFinish() {

        }

        public void onKeyDown() {

        }
    }

}
