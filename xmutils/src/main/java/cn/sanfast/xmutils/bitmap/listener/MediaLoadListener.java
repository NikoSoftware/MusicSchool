package cn.sanfast.xmutils.bitmap.listener;

import android.graphics.Bitmap;

/**
 * Created by koudejian on 15-2-7.
 * 多媒体加载成功
 */
public interface MediaLoadListener {
    public void onLoad(String path, Bitmap bitmap);
}
