package cn.sanfast.xmutils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.sanfast.xmutils.dialog.CustomDialog;

/**
 * Created by koudejian on 15-1-22.
 */
public class BaseTask {

    private Context mContext = null;
    protected HttpTask mHttpTask = null;
    protected CustomDialog mCustomDialog = null;
    private boolean mIsNetworkAvailable = false;

    public BaseTask(Context context) {
        mContext = context;
    }

    /**
     * 开启http任务
     *
     * @return boolean
     */
    public boolean start() {
        // 检测网络
        if (isNetworkConnected(mContext)) {
            mIsNetworkAvailable = true;
            if (mHttpTask != null) {
                if (mCustomDialog != null) {
                    mCustomDialog.show();
                }
                mHttpTask.start();
            }
            return true;
        } else {
            // 暂无网络
            final CustomDialog dialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_NO_TITLE);
            dialog.setMessage("当前网络不可用，请检查网络设置");
            dialog.setButton("", null, "确定", null);
            dialog.show();
            return false;
        }
    }

    /**
     * 停止http任务
     */
    public void stop() {
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
        }
        mHttpTask.stop();
    }

    /**
     * 网络状态
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager
                .getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 简单处理json
     *
     * @param str String
     * @return String
     */
    public String getJsonStr(String str) {
        if (str == null) {
            return null;
        }
        String result = str;
        int start = str.indexOf('{');
        int end = str.lastIndexOf('}');
        if (start >= 0 && end > 0) {
            result = str.substring(start, end + 1);
        }
        return result;
    }

    /**
     * 默认不调用
     */
    public void setCustomDialog() {
        mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_LOADING);
    }

    /**
     * 默认不调用
     */
    public void closeCustomDialog() {
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
    }

    /**
     * 默认不调用
     *
     * @param text String
     */
    public void setCustomDialog(String text) {
        mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_LOADING).setLoadingText(text);
    }

    /**
     * 获取任务是否正在运行中
     *
     * @return true:未在运行，false:正在运行
     */
    public boolean isRunning() {
        // 没有网络或者mHttpTask为空
        if (mHttpTask == null || !mIsNetworkAvailable) {
            return false;
        }
        return mHttpTask.isRunning();
    }
}
