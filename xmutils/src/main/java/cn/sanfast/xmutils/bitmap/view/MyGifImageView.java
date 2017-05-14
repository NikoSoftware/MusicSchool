package cn.sanfast.xmutils.bitmap.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import cn.sanfast.xmutils.bitmap.loader.AsyncImageLoader;
import cn.sanfast.xmutils.bitmap.listener.MediaLoadListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by tyl on 15/1/30.
 */
public class MyGifImageView extends GifImageView {
    private MediaLoadListener mMediaLoadListener = new MediaLoadListener() {
        @Override
        public void onLoad(String path, Bitmap bitmap) {
            try {
                GifDrawable gifDrawable = new GifDrawable(path);
                setImageDrawable(gifDrawable);
                mGifDrawable = gifDrawable;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private GifDrawable mGifDrawable = null;

    public MyGifImageView(Context context) {
        super(context);
    }

    public MyGifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGifImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyGifImageView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
    }

    public void displayGif(String url) {
        new AsyncImageLoader(url).setMediaLoadListener(mMediaLoadListener).loader();
    }

    public void displayGif(String url, int defaultResId) {
        displayGif(url);
        setImageResource(defaultResId);
    }

    public void start() {
        if (null != mGifDrawable) {
            mGifDrawable.start();
        } else {

        }
    }

    public void stop() {
        if (null != mGifDrawable) {
            mGifDrawable.stop();
        }
    }
}

