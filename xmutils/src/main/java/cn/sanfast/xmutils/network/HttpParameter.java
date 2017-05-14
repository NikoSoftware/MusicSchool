package cn.sanfast.xmutils.network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koudejian on 15-1-22.
 * http参数
 */
public class HttpParameter {
    private String mMethod = null;              //Http请求的method(get or post)
    public List<NameValuePair> mParams = null;

    /**
     * http参数
     * @param method HttpBaseTask.HTTP_GET|HttpBaseTask.HTTP_POST
     */
    public HttpParameter(String method){
        this.mParams = new ArrayList<NameValuePair>();
        this.mMethod = method;
    }

    /**
     * 添加参数
     * @param key
     * @param value
     */
    public void add(String key,String value){
        if(key!=null && key.length() != 0){        //key is not null
            this.mParams.add(new BasicNameValuePair(key,value));
        }
    }

    /**
     * 获取get参数
     * @return
     */
    public String toString(){
        String result = "";
        int length = mParams.size();
        for(int i = 0; i < length; i++){
            result += ((i == 0) ? "?" : "&") + mParams.get(i).getName() + "=" + mParams.get(i).getValue();
        }
        return result;
    }

    /**
     * 返回post参数
     * @return
     */
    public List<NameValuePair> getParas(){
        return this.mParams;
    }

    /**
     * 参数个数
     * @return
     */
    public int size(){
        return this.mParams.size();
    }

    /**
     * http类型
     * @return
     */
    public String getHttpMethod(){
        return this.mMethod;
    }
}
