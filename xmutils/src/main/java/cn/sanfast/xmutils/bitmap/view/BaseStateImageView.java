package cn.sanfast.xmutils.bitmap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by koudejian on 15-1-31.
 * 两种状态的图片基类
 */
public abstract class BaseStateImageView extends ImageView {
    private boolean mState = false;       // 默认false
    protected int[] mResId = null;      // 两种状态的图片，0:false;1:true

    protected abstract void setResource();

    public BaseStateImageView(Context context) {
        super(context);
        setResource();
        init();
    }

    public BaseStateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setResource();
        init();
    }

    public BaseStateImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setResource();
        init();
    }

    //更新状态图片
    private void init() {
        setImageResource((mState) ? mResId[1] : mResId[0]);
    }

    //设置状态
    protected void setState(boolean state) {
        mState = state;
        init();
    }

    //获取状态
    protected boolean getState() {
        return mState;
    }

}
