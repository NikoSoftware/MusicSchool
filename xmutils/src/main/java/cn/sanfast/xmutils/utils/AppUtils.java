package cn.sanfast.xmutils.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

/**
 * 设备尺寸换算、网络状态获取
 * Created by wzd on 2015/8/31.
 */
public final class AppUtils {

    /**
     * 屏幕dip与px换算
     *
     * @param context Context
     * @param dp      dip
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px与屏幕dip换算
     *
     * @param context Context
     * @param px      px
     * @return
     */
    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context Context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context Context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取网络连接状态
     *
     * @param context Context
     * @return true：已连接，false：未连接
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = cm.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取设备DEVICE_ID
     *
     * @param context Context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
    }

    /**
     * 返回当前SDK版本号
     *
     * @return int
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 隐藏系统NavigationBar
     *
     * @param activity Activity
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void hideNavigationBar(Activity activity) {
        if (getSDKVersion() >= 11) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    /**
     * 价格转换，分化元，小数两位，四舍五入
     *
     * @param price String
     * @return String
     */
    public static String priceFormat(String price) {
        if (NumberUtils.isNumber(price)) {
            double priceD = Double.valueOf(price);
            BigDecimal decimal = new BigDecimal(Double.toString(priceD / 100));
            price = String.valueOf(decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        return price;
    }

}
