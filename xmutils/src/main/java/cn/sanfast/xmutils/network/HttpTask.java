package cn.sanfast.xmutils.network;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by koudejian on 15-1-22.
 * 接口解析
 */

public class HttpTask {
    private final String TAG = "HttpTask";
    private boolean mIsStop = false;
    private HttpBaseTask mHttpBaseTask = null;
    private HttpResponseListener mHttpResponseListener = null;
    private HttpResultListener mHttpResultListener = null;

    public void setHttpResponseListener(HttpResponseListener httpResponseListener) {
        mHttpResponseListener = httpResponseListener;
    }

    public void setHttpResultListener(HttpResultListener httpResultListener) {
        mHttpResultListener = httpResultListener;
    }

    public HttpTask(String url, HttpParameter parameter) {
        mHttpBaseTask = new HttpBaseTask(url, parameter);
        mHttpBaseTask.setHttpTaskFinishListener(new HttpTaskFinishListener() {
            @Override
            public void finish(String str) {
                str = getJsonStr(str);
                Log.e(TAG, str);
                if (mHttpResponseListener != null && !mIsStop) {
                    // 简单解析数据
                    try {
                        JSONObject obj = new JSONObject(str);
                        String code = obj.optString("status");
                        String msg = obj.optString("msg");
                        String data = obj.optString("data");
                        // 访问成功
                        if ("0".equals(code)) {
                            mHttpResponseListener.success(msg, data);
                        } else {
                            mHttpResponseListener.failure(code, msg);
                        }
                    } catch (JSONException e) {
                        // 返回的不是json
                        e.printStackTrace();
                        mHttpResponseListener.error("请求失败，请稍后再试");
                    }
                }
                if (mHttpResultListener != null && !mIsStop) {
                    mHttpResultListener.success(str);
                }
            }

            @Override
            public void error(String obj) {
                // 处理错误
                if (mHttpResponseListener != null) {
                    mHttpResponseListener.error(obj);
                }
                if (mHttpResultListener != null) {
                    mHttpResultListener.error(obj);
                }
            }
        });
    }

    public HttpTask(String url, HttpParameter parameter, HttpResponseListener httpResponseListener) {
        this(url, parameter);
        setHttpResponseListener(httpResponseListener);
    }

    public HttpTask(String url, HttpParameter parameter, HttpResultListener httpResultListener) {
        this(url, parameter);
        setHttpResultListener(httpResultListener);
    }

    public void start() {
        mIsStop = false;
        if (mHttpBaseTask != null && mHttpBaseTask.getStatus() == AsyncTask.Status.PENDING) {
            mHttpBaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void stop() {
        mIsStop = true;
    }

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
     * 获取任务是否正在运行中
     *
     * @return true:在运行，false:未在运行
     */
    public boolean isRunning() {
        if (mHttpBaseTask != null && mHttpBaseTask.getStatus() == AsyncTask.Status.RUNNING) {
            return true;
        }
        return false;
    }
}
