package cn.sanfast.xmutils.network;

/**
 * Http数据返回接口
 * <p/>
 * Created by koudejian on 15-1-22.
 * Update by wzd on 16-6-3.
 */
public interface HttpResultListener {

    void success(String result);

    void error(String message);

}
