package cn.sanfast.xmutils.bitmap.task;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyStore;

import cn.sanfast.xmutils.bitmap.listener.MediaDownloadListener;
import cn.sanfast.xmutils.network.EasySSLSocketFactory;
import cn.sanfast.xmutils.utils.MD5;
import cn.sanfast.xmutils.utils.StringUtil;

/**
 * Created by tyl on 15/1/29.
 */
public abstract class DownloadFileTask extends BaseAsyncTask {
    private final String TAG = "DownloadFileTask";
    protected String mUrl = null;
    protected String mPath = null;
    protected Bitmap mBitmap = null;

    private final int TIME_MAX_WAIT_OUT_CONNECTION = 20000;
    private final int TIME_OUT_CONNECTION = 20000;
    private final int TIME_OUT_SOCKET = 20000;

    /**
     * 执行生命周期doInBackground，请勿操作UI
     */
    protected abstract void dealWithFile(byte[] data);

    protected MediaDownloadListener mMediaDownloadListener = null;

    public DownloadFileTask(String url, MediaDownloadListener listener) {
        mUrl = url;
        mMediaDownloadListener = listener;
    }

    @Override
    protected void onPostExecute(byte[] data) {
        if (null != mDownloadFileListener) {
            mDownloadFileListener.notifyDownloaded(data, DownloadFileTask.this);
        }
        if (mMediaDownloadListener != null) {
            if (!StringUtil.isEmpty(mPath) && mBitmap != null) {
                mMediaDownloadListener.onDownloadSuccess(mPath, mBitmap);
            } else {
                mMediaDownloadListener.onDownloadFailed(mUrl);
            }
        }
    }

    /**
     * 异步下载网络资源
     *
     * @param params
     * @return
     */
    @Override
    protected byte[] doInBackground(Void... params) {
        byte[] data = null;
        DefaultHttpClient androidHttpClient = null;
        try {
            androidHttpClient = createHttpClient();
            HttpGet httpGet;
            if (mUrl.trim().contains(" .jpg")) {
                httpGet = new HttpGet(mUrl.replace(" .jpg", "%20.jpg"));
            } else {
                httpGet = new HttpGet(mUrl);
            }
            if (null != mDownloadFileListener) {
                mDownloadFileListener.notifyBeginDownload(mUrl);
            }
            HttpResponse httpResponse = androidHttpClient.execute(httpGet);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(outputStream);
                data = outputStream.toByteArray();
                outputStream.close();
                /**子类实现对文件的处理方法**/
                dealWithFile(data);
            } else {
                Log.e(TAG, httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (androidHttpClient != null) {
                androidHttpClient.getConnectionManager().shutdown();
            }
        }
        return data;
    }

    public String getKey() {
        return MD5.md5Encode(mUrl);
    }

    /**
     * 获取HttpClient
     * @return DefaultHttpClient
     */
    protected DefaultHttpClient createHttpClient() {
        // sets up parameters
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");
        params.setBooleanParameter("http.protocol.expect-continue", false);
        // 从这里开始是进行下载，使用了多线程执行请求
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(50));// 设置并发数
        // 设置连接最大等待时间
        ConnManagerParams.setTimeout(params, TIME_MAX_WAIT_OUT_CONNECTION);
        /* 连接超时 */
        HttpConnectionParams.setConnectionTimeout(params, TIME_OUT_CONNECTION);
        /* 请求读取超时 */
        HttpConnectionParams.setSoTimeout(params, TIME_OUT_SOCKET);
        // registers schemes for both http and https
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            EasySSLSocketFactory sf = new EasySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registry.register(new Scheme("https", sf, 443));
        } catch (Exception e) {
            //Log.e(TAG, "https:", e);
        }
        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
        return new DefaultHttpClient(manager, params);
    }
}
