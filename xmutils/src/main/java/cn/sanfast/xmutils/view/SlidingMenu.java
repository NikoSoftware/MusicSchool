package cn.sanfast.xmutils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

import cn.sanfast.xmutils.utils.AppUtils;

/**
 * 侧滑菜单
 * <p/>
 * Created by wzd on 2016/4/7.
 */
public class SlidingMenu extends HorizontalScrollView {

    private Context mContext;
    /**
     * 菜单边距
     */
    private final int MENU_MARGIN = 72;
    private int mScreenWidth;
    private int mMenuWidth;
    private int mHalfMenuWidth;
    private boolean mOnce;
    private boolean mIsOpen;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private View mFilter;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        // 获取屏幕宽度
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 第一次加载，显示设置宽度
         */
        if (!mOnce) {
            // SlidingMenu只能有一个孩子：LinearLayout
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            // wrapper有两个孩子，第一个为menu
            mMenu = (ViewGroup) wrapper.getChildAt(0);
            // wrapper的第二个孩子：content
            mContent = (ViewGroup) wrapper.getChildAt(1);
            // content的第二个孩子：filter
            mFilter = mContent.getChildAt(1);

            // 转成dip
            mMenuWidth = mScreenWidth - dp2px(MENU_MARGIN);
            mHalfMenuWidth = mMenuWidth / 2;

            // 设置菜单宽度
            mMenu.getLayoutParams().width = mMenuWidth;
            // 设置内容宽度
            mContent.getLayoutParams().width = mScreenWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // 将菜单隐藏
            this.scrollTo(mMenuWidth, 0);
            // 浮层透明
            ViewHelper.setAlpha(mFilter, 0);
            mOnce = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                // UP事件时，进行判断，如果显示区域大于菜单宽度的一半，则完全显示，否则隐藏
                int scrollX = getScrollX();
                if (scrollX > mHalfMenuWidth) {
                    this.smoothScrollTo(mMenuWidth, 0);
                    mIsOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    mIsOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (y >= AppUtils.dp2px(mContext, 24 + 52) &&
                        y <= AppUtils.dp2px(mContext, 24 + 52) + 9 * AppUtils.getScreenWidth(mContext) / 16) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        /**
         * 所有动画效果，都在此方法中实现, l == getScrollX(), 即菜单划出的宽度
         * scale: 1.0~0.0
         */
        float scale = l * 1.0f / mMenuWidth;
        if (mOnce) {
            ViewHelper.setTranslationX(mContent, mMenuWidth * (scale - 1));
            ViewHelper.setAlpha(mFilter, (1 - scale) / 2);
            ViewHelper.setScaleX(mContent, 0.88f + scale * 0.12f);
            ViewHelper.setScaleY(mContent, 0.85f + scale * 0.15f);
        }
    }

    /**
     * dp转换为px
     *
     * @param dp float
     * @return int
     */
    private int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 打开菜单
     */
    public void open() {
        if (mIsOpen) {
            return;
        }
        this.smoothScrollTo(0, 0);
        mIsOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void close() {
        if (mIsOpen) {
            this.smoothScrollTo(mMenuWidth, 0);
            mIsOpen = false;
        }
    }

    /**
     * 开启与否
     *
     * @return boolean
     */
    public boolean isOpen() {
        return mIsOpen;
    }

    /**
     * 切换菜单状态
     */
    public void toggle() {
        if (mIsOpen) {
            close();
        } else {
            open();
        }
    }

}
