package cn.sanfast.xmutils.bitmap.model;

import android.graphics.Bitmap;

import java.io.Serializable;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by tyl on 15/1/29.
 */
public class CacheObject implements Serializable {

    private Object mObject;

    public CacheObject(Object object) {
        mObject = object;
    }

    public void setObject(Object object) {
        mObject = object;
    }

    public Object getObject() {
        return mObject;
    }

    public int getObjectSize() {
        int size = 1;

        if (mObject instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) mObject;
            size = bitmap.getRowBytes() / bitmap.getHeight();
        } else if (mObject instanceof GifDrawable) {
            size = (int) ((GifDrawable) mObject).getAllocationByteCount();
        }

        return size;
    }
}
