package cn.sanfast.xmutils.bitmap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.sanfast.xmutils.bitmap.listener.MediaLoadListener;
import cn.sanfast.xmutils.bitmap.loader.AsyncImageLoader;
import cn.sanfast.xmutils.bitmap.model.ImageSize;

/**
 * 用于异步加载图片
 *
 * @author koudejian
 */
public class BaseSyncImageView extends ImageView {

    public BaseSyncImageView(Context context) {
        super(context);
    }

    public BaseSyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 无参数
     *
     * @param url
     */
    public void loadImageFromURL(String url) {
        loadImageFromURL(url, null, null);
    }

    /**
     * 默认图片
     *
     * @param url
     * @param defaultResourceId
     */
    public void loadImageFromURL(String url, int defaultResourceId) {
        loadImageFromURL(url, null, null, defaultResourceId);
    }

    /**
     * 图片大小
     *
     * @param url
     * @param imageSize
     */
    public void loadImageFromURL(String url, ImageSize imageSize) {
        loadImageFromURL(url, null, imageSize);
    }

    /**
     * 回调
     *
     * @param url
     * @param listener
     */
    public void loadImageFromURL(String url, MediaLoadListener listener) {
        loadImageFromURL(url, listener, null);
    }

    /**
     * 图片大小，默认图片
     *
     * @param url
     * @param imageSize
     * @param defaultResourceId
     */
    public void loadImageFromURL(String url, ImageSize imageSize, int defaultResourceId) {
        this.setImageResource(defaultResourceId);
        loadImageFromURL(url, imageSize);
    }

    /**
     * 回调，默认图片
     *
     * @param url
     * @param listener
     * @param defaultResourceId
     */
    public void loadImageFromURL(String url, MediaLoadListener listener, int defaultResourceId) {
        this.setImageResource(defaultResourceId);
        loadImageFromURL(url, listener);
    }

    /**
     * 回调，默认图片，图片大小
     *
     * @param url
     * @param listener
     * @param imageSize
     * @param defaultResourceId
     */
    public void loadImageFromURL(String url, MediaLoadListener listener, ImageSize imageSize, int defaultResourceId) {
        this.setImageResource(defaultResourceId);
        loadImageFromURL(url, listener, imageSize);
    }

    /**
     * 图片回调，图片大小
     *
     * @param url
     * @param listener
     * @param imageSize
     */
    public void loadImageFromURL(String url, MediaLoadListener listener, ImageSize imageSize) {
        if (url == null || url.equals("")) {
            return;
        }
        new AsyncImageLoader(url, this, imageSize).setMediaLoadListener(listener).start();
    }

}
