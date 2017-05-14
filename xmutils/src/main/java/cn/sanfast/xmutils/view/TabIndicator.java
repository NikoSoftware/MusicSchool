package cn.sanfast.xmutils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * tab指示器
 * Created by wzd on 2015/9/29.
 */
public class TabIndicator extends LinearLayout {

    private final String TAG = TabIndicator.class.getSimpleName();
    private Scroller mScroller;
    private int mCurrentPosition;
    private int mTabWidth;

    public TabIndicator(Context context) {
        this(context, null);
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mCurrentPosition = 0;
    }

    /**
     * 由父视图调用用来请求子视图根据偏移值 mScrollX,mScrollY重新绘制
     */
    @Override
    public void computeScroll() {
        // 判断滚动是否完成，如果返回true，表示滚动还未完成
        // 因为前面startScroll，所以只有在startScroll完成时，才会为false
        if (mScroller.computeScrollOffset()) {
            // 产生了动画效果，根据当前值 每次滚动一点
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
    }

    /**
     * 设置滚动的相对便宜
     *
     * @param dx 水平方向的偏移量
     * @param dy 竖直方向的偏移量
     */
    public void smoothScrollBy(int dx, int dy) {
        // 设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 1000);
        // 保证computeScroll()被调用
        invalidate();
    }

    /**
     * 滚动到目标位置
     *
     * @param fx 目标位置X坐标
     * @param fy 目标位置Y坐标
     */
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    /**
     * 根据位置移动
     *
     * @param position int，，取值0 1 2 3 4
     */
    public void smoothScrollTo(int position) {
        if (position < 0 || position > 4) {
            return;
        }
        smoothScrollByPosition(position);
    }

    /**
     * 通过位置移动
     *
     * @param position int
     */
    private void smoothScrollByPosition(int position) {
        if (position == 0) {
            switch (mCurrentPosition) {
                case 0:
                    /**相同位置，不移动**/
                    break;
                case 1:
                    /**左移1位**/
                    smoothScrollBy(mTabWidth, 0);
                    break;
                case 2:
                    /**左移2位**/
                    smoothScrollBy(mTabWidth * 2, 0);
                    break;
                case 3:
                    /**左移3位**/
                    smoothScrollBy(mTabWidth * 3, 0);
                    break;
                case 4:
                    /**左移4位**/
                    smoothScrollBy(mTabWidth * 4, 0);
                    break;
            }
        } else if (position == 1) {
            switch (mCurrentPosition) {
                case 0:
                    /**右移1位**/
                    smoothScrollBy(-mTabWidth, 0);
                    break;
                case 1:
                    /**相同位置，不移动**/
                    break;
                case 2:
                    /**左移1位**/
                    smoothScrollBy(mTabWidth, 0);
                    break;
                case 3:
                    /**左移2位**/
                    smoothScrollBy(mTabWidth * 2, 0);
                    break;
                case 4:
                    /**左移3位**/
                    smoothScrollBy(mTabWidth * 3, 0);
                    break;
            }
        } else if (position == 2) {
            switch (mCurrentPosition) {
                case 0:
                    /**右移2位**/
                    smoothScrollBy(-mTabWidth * 2, 0);
                    break;
                case 1:
                    /**右移1位**/
                    smoothScrollBy(-mTabWidth, 0);
                    break;
                case 2:
                    /**相同位置，不移动**/
                    break;
                case 3:
                    /**左移1位**/
                    smoothScrollBy(mTabWidth, 0);
                    break;
                case 4:
                    /**左移2位**/
                    smoothScrollBy(mTabWidth * 2, 0);
                    break;
            }
        } else if (position == 3) {
            switch (mCurrentPosition) {
                case 0:
                    /**右移3位**/
                    smoothScrollBy(-mTabWidth * 3, 0);
                    break;
                case 1:
                    /**右移2位**/
                    smoothScrollBy(-mTabWidth * 2, 0);
                    break;
                case 2:
                    /**右移1位**/
                    smoothScrollBy(-mTabWidth, 0);
                    break;
                case 3:
                    /**相同位置，不移动**/
                    break;
                case 4:
                    /**左移1位**/
                    smoothScrollBy(mTabWidth, 0);
                    break;
            }
        } else if (position == 4) {
            switch (mCurrentPosition) {
                case 0:
                    /**右移4位**/
                    smoothScrollBy(-mTabWidth * 4, 0);
                    break;
                case 1:
                    /**右移3位**/
                    smoothScrollBy(-mTabWidth * 3, 0);
                    break;
                case 2:
                    /**右移2位**/
                    smoothScrollBy(-mTabWidth * 2, 0);
                    break;
                case 3:
                    /**右移1位**/
                    smoothScrollBy(-mTabWidth, 0);
                    break;
                case 4:
                    /**相同位置，不移动**/
                    break;
            }
        }
        mCurrentPosition = position;
    }

    /**
     * 设置指示器宽度
     *
     * @param width int
     */
    public void setIndicatorWidth(int width) {
        mTabWidth = width;
        View view = getChildAt(0);
        if (view == null) {
            return;
        }
        try {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.width = width;
            view.setLayoutParams(lp);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 设置指示器初始位置
     * 默认为0
     *
     * @param position int 取值 0 1 2 3 4
     */
    public void setIndicatorPosition(int position) {
        int dx = 0;
        int dy = 0;
        mCurrentPosition = position;
        switch (position) {
            case 0:
                dx = 0;
                break;
            case 1:
                dx = -mTabWidth;
                break;
            case 2:
                dx = -mTabWidth * 2;
                break;
            case 3:
                dx = -mTabWidth * 3;
                break;
            case 4:
                dx = -mTabWidth * 4;
                break;
        }
        // 设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 0);
        // 保证computeScroll()被调用
        invalidate();
    }
}
