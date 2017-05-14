package cn.sanfast.xmutils.view;

import android.content.Context;
import android.util.AttributeSet;

import cn.sanfast.xmutils.R;
import cn.sanfast.xmutils.bitmap.view.BaseStateImageView;

/**
 * 单选框，勾
 * <p/>
 * Created by wzd on 2016/8/4.
 */
public class CheckImageView extends BaseStateImageView {

    public CheckImageView(Context context) {
        super(context);
    }

    public CheckImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void setResource() {
        mResId = new int[]{R.drawable.ic_check_normal, R.drawable.ic_check_checked};
    }

    /**
     * 获取状态
     *
     * @return
     */
    public boolean get() {
        return getState();
    }

    /**
     * 设置状态
     *
     * @param isChoice
     */
    public void set(boolean isChoice) {
        setState(isChoice);
    }
}
