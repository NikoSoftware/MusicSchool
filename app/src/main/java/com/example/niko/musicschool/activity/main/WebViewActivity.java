package com.example.niko.musicschool.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.NoDataController;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;

import cn.sanfast.xmutils.utils.AppUtils;
import cn.sanfast.xmutils.utils.StringUtil;

public class WebViewActivity extends BaseActivity {

    private static final String TAG = "WebView";
    private static final String TEL_PREFIX = "wtai://wp/mc";
    private AppbarNormalController mAppbar;
    private WebView mWebView;
    private NoDataController mNoDataController;
    private String mUrl = null;
    private String mTitle;
    private boolean mIsCache = false;
    private boolean mHasTitle = false;
    private boolean mFailed = false;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        if (intent != null) {
            mUrl = intent.getStringExtra("url");
            mIsCache = intent.getBooleanExtra("iscache", true);
            mHasTitle = intent.getBooleanExtra("hastitle", false);
            if (mHasTitle) {
                mTitle = intent.getStringExtra("title");
            }
        }
    }

    /**
     * 获取activity TAG
     *
     * @return String
     */
    @Override
    protected String getActivityTag() {
        return TAG;
    }

    @Override
    public void initViews() {
        // appbar
        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));
        // views
        mWebView = (WebView) findViewById(R.id.web_view);
        mNoDataController = new NoDataController(mContext, findViewById(R.id.no_data));
    }

    /**
     * 为控件填充内容
     */
    @Override
    protected void setupViews() {
        // appbar
        mAppbar.init(R.drawable.selector_btn_back, "", "");
        mAppbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        if (mHasTitle) {
            mAppbar.setTitle(mTitle);
        }
        // no data
        mNoDataController.init(0, "").setEnable(false).show();
        // webView
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 电话
                if (!StringUtil.isEmpty(url) && url.startsWith(TEL_PREFIX)) {
                    String phone = url.split(";")[1];
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mNoDataController.setTips("加载中，请稍候...").setEnable(false).show();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // 页面加载失败
                mFailed = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mFailed) {
                    mNoDataController.setTips("加载失败").setEnable(false).show();
                } else {
                    mNoDataController.setTips("加载成功").hide();
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReachedMaxAppCacheSize(long requiredStorage, long quota, QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(requiredStorage * 2);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!mHasTitle) {
                    mAppbar.setTitle(title);
                }
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                finish();
            }
        });
        // js支持
        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setAppCacheEnabled(true);
        /*setting.setAppCachePath(getExternalCacheDir().getAbsolutePath());*/
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setPluginState(WebSettings.PluginState.ON);
        setting.setDomStorageEnabled(true);
        setting.setAppCacheMaxSize(1024 * 1024 * 8);
        setting.setAllowFileAccess(true);
        // 设置页面自适应手机屏幕
        /*setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);*/
        // 设置WebView可触摸缩放
        setting.setSupportZoom(true);
        setting.setBuiltInZoomControls(true);
        setting.setDisplayZoomControls(false);
        // network
        boolean net = AppUtils.isNetworkConnected(mContext);
        if (net || !mIsCache) {
            setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        mWebView.loadUrl(mUrl);
    }


    /**
     * 通过WebView的goBack(),goForward()方法设置其前进和后退
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 返回前一个页面
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回键
     */
    private void back() {
        if (mWebView.canGoBack()) {
            // 返回前一个页面
            mWebView.goBack();
        } else {
            finish();
        }
    }

/*
    */
/**
     * 结束
     *//*

    private void finishThis() {
        finishThisActivity(R.anim.activity_enter_none, R.anim.activity_exit_right);
    }
*/

    /**
     * 启动webView，默认打开缓存(默认：没网读取缓存，有网访问网络)
     * 有标题
     *
     * @param context Context
     * @param url     String
     * @param title   String
     */
    public static void startWebView(Context context, String url, String title) {
        startWebView(context, url, title, true);
    }

    /**
     * 启动webView，默认打开缓存(默认：没网读取缓存，有网访问网络)
     * 可以关闭没网访问缓存
     * 有标题
     *
     * @param context String
     * @param url     String
     * @param title   String
     * @param isCache boolean
     */
    public static void startWebView(Context context, String url, String title, boolean isCache) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("iscache", isCache);
        intent.putExtra("hastitle", true);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_exit_none);
        }
    }

    /**
     * 启动WebView，使用网页标题
     *
     * @param context Context
     * @param url     String
     * @param isCache boolean
     */
    public static void startWebView(Context context, String url, boolean isCache) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("iscache", isCache);
        intent.putExtra("hastitle", false);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_exit_none);
        }
    }

    /**
     * 启动WebView
     *
     * @param context Context
     * @param url     String
     */
    public static void startWebView(Context context, String url) {
        startWebView(context, url, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        if (viewGroup != null) {
            viewGroup.removeAllViews();
        }
        if (mWebView != null) {
            mWebView.destroy();
        }
    }
}
