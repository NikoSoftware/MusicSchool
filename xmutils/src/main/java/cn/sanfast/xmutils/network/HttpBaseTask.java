package cn.sanfast.xmutils.network;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.security.KeyStore;

/**
 * Created by koudejian on 15-1-22.
 */
public class HttpBaseTask extends AsyncTask<Void, Void, String> {
    private final String TAG = "HttpBaseTask";
    private final int TIME_MAX_WAIT_OUT_CONNECTION = 10000;
    private final int TIME_OUT_CONNECTION = 10000;
    private final int TIME_OUT_SOCKET = 10000;
    /**
     * 一般get请求
     */
    public static final String HTTP_GET = "GET";
    /**
     * 一般post请求
     */
    public static final String HTTP_POST = "POST";

    public HttpBaseTask(String url, HttpParameter parameter) {
        this.mUrl = url;
        this.mHttpParameter = parameter;
    }

    public HttpBaseTask(String url, HttpParameter parameter, HttpTaskFinishListener httpTaskFinishListener) {
        this(url, parameter);
        setHttpTaskFinishListener(httpTaskFinishListener);
    }

    private HttpTaskFinishListener mHttpTaskFinishListener = null;
    private HttpParameter mHttpParameter = null;
    private String mUrl = null;
    private String mError = null;

    /**
     * @param httpTaskFinishListener
     */
    public void setHttpTaskFinishListener(HttpTaskFinishListener httpTaskFinishListener) {
        mHttpTaskFinishListener = httpTaskFinishListener;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = null;
        DefaultHttpClient client = createHttpClient();
        HttpResponse httpResponse = null;
        try {
            Log.e(TAG, mUrl + mHttpParameter.toString());
            // POST
            if (HTTP_POST.equals(mHttpParameter.getHttpMethod())) {
                HttpPost httpPostRequest = new HttpPost(mUrl);
                HttpEntity entity = null;
                if (mHttpParameter.size() > 0) {
                    entity = new UrlEncodedFormEntity(mHttpParameter.getParas(), HTTP.UTF_8);
                    httpPostRequest.setEntity(entity);
                }
                httpResponse = client.execute(httpPostRequest);
            }
            // GET
            else if (HTTP_GET.equals(mHttpParameter.getHttpMethod())) {
                HttpGet httpGetRequest = new HttpGet(mUrl + mHttpParameter.toString());
                httpResponse = client.execute(httpGetRequest);
            }
            int stateCode = httpResponse.getStatusLine().getStatusCode();
            if (stateCode == HttpStatus.SC_OK) {
                result = EntityUtils.toString(httpResponse.getEntity());
            } else {
                mError = "请求失败，请稍后再试";
                Log.e(TAG, "Error: " + stateCode + ", " + httpResponse.toString());
            }
        } catch (Exception e) {
            mError = "网络连接异常，请检测网络";
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // 不需要回调
        if (mHttpTaskFinishListener == null) {
            return;
        }
        if (result == null || "".equals(result)) {
            mHttpTaskFinishListener.error(mError);
        } else {
            mHttpTaskFinishListener.finish(result);
        }
    }

    /**
     * 获取HttpClient
     *
     * @return DefaultHttpClient
     */
    protected DefaultHttpClient createHttpClient() {
        // sets up parameters
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");
        params.setBooleanParameter("http.protocol.expect-continue", false);
        // 从这里开始是进行下载，使用了多线程执行请求
        // 设置并发数
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(50));
        // 设置连接最大等待时间
        ConnManagerParams.setTimeout(params, TIME_MAX_WAIT_OUT_CONNECTION);
        // 连接超时
        HttpConnectionParams.setConnectionTimeout(params, TIME_OUT_CONNECTION);
        // 请求读取超时
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
            // Log.e(TAG, "https:", e);
        }
        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
        return new DefaultHttpClient(manager, params);
    }
}
