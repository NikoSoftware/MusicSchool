package com.example.niko.musicschool.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * ScrollView反弹效果的实现
 * modified by snail at 2014-1-18 01:43:27
 * fix the bug, when touch and up with no moving, animation is still play.
 */
public class StretchScrollView extends ScrollView {

    private IScrollChanged mIScrollChanged = null;
    private IScrollingListener mIScrollingListener = null;

    public interface IScrollChanged {
        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public interface IScrollingListener {
        void onScrolling(int deltaY);
    }

    public void setIScrollingListener(IScrollingListener iScrollingListener) {
        this.mIScrollingListener = iScrollingListener;
    }

    public void setIScrollChanged(IScrollChanged iScrollChanged) {
        this.mIScrollChanged = iScrollChanged;
    }

    private View mInnerView;                        // 孩子View
    private float y;                                // 点击时y坐标
    private final Rect mNormal = new Rect();        // 矩形(这里只是个形式，只是用于判断是否需要动画.)
    private boolean mIsCount = false;               // 是否开始计算

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public StretchScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        //去除底部阴影
        setVerticalFadingEdgeEnabled(false);
    }

    /**
     * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
     * 方法，也应该调用父类的方法，使该方法得以执行.
     * <p/>
     * add by snail.
     * 注意：此处移动为mInnerView视图，视图是选取ScrollView的第一个孩子节点。
     * 所以在编写布局的时候最好让这个孩子节点的高度与ScrollView保持一致。
     * 及该view最好不要使用margin，padding等属性。
     */
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mInnerView = getChildAt(0);
        }
    }

    /**
     * 监听touch
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mInnerView != null) {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 触摸事件
     *
     * @param ev
     */
    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                // 手指松开.
                if (isNeedAnimation()) {
                    animation();
                    mIsCount = false;
                }
                break;
            /***
             * 排除出第一次移动计算，因为第一次无法得知y坐标， 在MotionEvent.ACTION_DOWN中获取不到，
             * 因为此时是MyScrollView的touch事件传递到到了LIstView的孩子item上面.所以从第二次计算开始.
             * 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0. 之后记录准确了就正常执行.
             */
            case MotionEvent.ACTION_MOVE:
                final float preY = y;// 按下时的y坐标
                float nowY = ev.getY();// 时时y坐标
                int deltaY = (int) (preY - nowY);// 滑动距离
                if (!mIsCount) {
                    deltaY = 0; // 在这里要归0.
                }
                y = nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove() && mIScrollingListener == null) {
                    // 初始化头部矩形
                    if (mNormal.isEmpty()) {
                        // 保存正常的布局位置
                        mNormal.set(mInnerView.getLeft(), mInnerView.getTop(), mInnerView.getRight(), mInnerView.getBottom());
                    }
                    // 移动布局
                    mInnerView.layout(mInnerView.getLeft(), mInnerView.getTop() - deltaY / 2,
                            mInnerView.getRight(), mInnerView.getBottom() - deltaY / 2);
                }
                mIsCount = true;
                if (mIScrollingListener != null) {
                    mIScrollingListener.onScrolling(deltaY);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 回缩动画
     */
    public void animation() {
        // 开启移动动画
        if (mInnerView.getTop() != mNormal.top) {
            TranslateAnimation ta = new TranslateAnimation(0, 0, mInnerView.getTop(), mNormal.top);
            ta.setDuration(200);
            mInnerView.startAnimation(ta);
        }
        // 设置回到正常的布局位置
        mInnerView.layout(mNormal.left, mNormal.top, mNormal.right, mNormal.bottom);
        mNormal.setEmpty();
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !mNormal.isEmpty();
    }

    /**
     * 是否需要移动布局 mInnerView.getMeasuredHeight():获取的是控件的总高度
     * <p/>
     * getHeight()：获取的是屏幕的高度
     *
     * @return
     */
    private boolean isNeedMove() {
        int offset = mInnerView.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        // 0是顶部，后面那个是底部
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mIScrollChanged != null) {
            mIScrollChanged.onScrollChanged(l, t, oldl, oldt);
        }
    }
}
