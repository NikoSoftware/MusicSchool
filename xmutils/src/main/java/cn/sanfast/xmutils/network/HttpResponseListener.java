package cn.sanfast.xmutils.network;

/**
 * Http数据返回接口
 * <p/>
 * Created by koudejian on 15-1-22.
 * Update by wzd on 16-6-3.
 */
public interface HttpResponseListener {

    void success(String message, String data);

    void failure(String status, String message);

    void error(String message);
}
