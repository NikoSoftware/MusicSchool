package cn.sanfast.xmutils.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * 滚动组件的弹性容器，放ISO回弹效果
 * <p/>
 * Created by wzd on 2016/10/9.
 */
public class StretchRecyclerView extends LinearLayout {

    private final String TAG = StretchRecyclerView.class.getSimpleName();
    /**
     * 容器中的组件
     */
    private View mConvertView;
    private RecyclerView mRecyclerView;
    private int mStart;
    private int mEnd;
    private int mLastY;
    private Scroller mScroller;
    private OnLoadMoreListener mOnLoadMoreListener;
    /**
     * 上拉加载
     */
    private boolean mCanLoadMore = false;
    /**
     * 加载中
     */
    private boolean mIsLoading = false;
    /**
     * 全部加载完毕
     */
    private boolean mIsLoadComplete = false;

    public StretchRecyclerView(Context context) {
        this(context, null);
    }

    public StretchRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StretchRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new RuntimeException(TAG + " can only have one child");
        }
        mConvertView = getChildAt(0);
        // TODO: 2016/10/9 拓展
        if (mConvertView instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) mConvertView;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            View view = getChildAt(0);
            view.layout(l, t, r, b);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStart = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    // 终止动画
                    mScroller.abortAnimation();
                }
                scrollTo(0, (int) ((mLastY - y) * 0.4));
                // 准备上拉加载
                if (!mIsLoadComplete && mCanLoadMore && !mIsLoading) {
                    mIsLoading = true;
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onReady();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mEnd = getScrollY();
                int deltaScrollY = mEnd - mStart;
                /**
                 * 回弹动画，第一二个参数为开始的x，y
                 * 第三个和第四个参数为滚动的距离（注意方向问题）
                 * 第五个参数是回弹时间
                 */
                mScroller.startScroll(0, mEnd, 0, -deltaScrollY, 200);
                // 执行上拉加载
                if (!mIsLoadComplete && mCanLoadMore && mIsLoading) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoading();
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mConvertView instanceof RecyclerView) {
                    // 下拉
                    if (y - mLastY > 0) {
                        if (isRecyclerViewToTop()) {
                            // 滑动到顶部
                            return true;
                        }
                    }
                    // 上拉
                    if (y - mLastY < 0) {
                        if (isRecyclerViewToBottom()) {
                            // 滑动到底部
                            mCanLoadMore = true;
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * RecyclerView滑动到顶部
     *
     * @return boolean
     */
    private boolean isRecyclerViewToTop() {
        if (mRecyclerView != null) {
            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            if (manager == null || manager.getItemCount() == 0) {
                return true;
            }
            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                int firstChildTop = 0;
                // 如果RecyclerView的子控件数量不为0，获取第一个子控件的top
                if (layoutManager.getItemCount() > 0) {
                    View firstVisibleChild = mRecyclerView.getChildAt(0);
                    // 处理item高度超过一屏时的情况
                    if (firstVisibleChild != null && firstVisibleChild.getMeasuredHeight() >= mRecyclerView.getMeasuredHeight()) {
                        if (Build.VERSION.SDK_INT < 14) {
                            return !(ViewCompat.canScrollVertically(mRecyclerView, -1) || mRecyclerView.getScrollY() > 0);
                        } else {
                            return !ViewCompat.canScrollVertically(mRecyclerView, -1);
                        }
                    }
                    // 解决item的topMargin不为0时不能触发下拉刷新
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) firstVisibleChild.getLayoutParams();
                    firstChildTop = firstVisibleChild.getTop() - layoutParams.topMargin
                            - getRecyclerViewItemTopInset(layoutParams) - mRecyclerView.getPaddingTop();
                }
                if (layoutManager.findFirstCompletelyVisibleItemPosition() < 1 && firstChildTop == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * RecyclerView滑动到底部
     *
     * @return boolean
     */
    private boolean isRecyclerViewToBottom() {
        if (mRecyclerView != null) {
            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            if (manager == null || manager.getItemCount() == 0) {
                return false;
            }
            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                View lastVisibleChild = mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1);
                // 处理item高度超过一屏幕时的情况
                if (lastVisibleChild != null && lastVisibleChild.getMeasuredHeight() >= mRecyclerView.getMeasuredHeight()) {
                    if (Build.VERSION.SDK_INT < 14) {
                        return !(ViewCompat.canScrollVertically(mRecyclerView, 1) || mRecyclerView.getScrollY() < 0);
                    } else {
                        return !ViewCompat.canScrollVertically(mRecyclerView, 1);
                    }
                }
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    StretchRecyclerView parentView = getStretchRecyclerView(mRecyclerView);
                    if (parentView != null) {
                        // 处理StretchRecyclerView中findLastCompletelyVisibleItemPosition失效问题
                        View lastCompletelyVisibleChild = layoutManager.getChildAt(layoutManager.findLastCompletelyVisibleItemPosition());
                        if (lastCompletelyVisibleChild == null) {
                            return true;
                        } else {
                            // 0表示x，1表示y
                            int[] location = new int[2];
                            lastCompletelyVisibleChild.getLocationOnScreen(location);
                            int lastChildBottomOnScreen = location[1] + lastCompletelyVisibleChild.getMeasuredHeight();
                            parentView.getLocationOnScreen(location);
                            int stretchRecyclerViewBottomOnScreen = location[1] + parentView.getMeasuredHeight();
                            return lastChildBottomOnScreen <= stretchRecyclerViewBottomOnScreen;
                        }
                    } else {
                        return true;
                    }
                }
            } else if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
                int[] out = layoutManager.findLastCompletelyVisibleItemPositions(null);
                int lastPosition = layoutManager.getItemCount() - 1;
                for (int position : out) {
                    if (position == lastPosition) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 反射方式获取RecyclerView的item的topInset
     *
     * @param layoutParams RecyclerView.LayoutParams
     * @return int
     */
    private int getRecyclerViewItemTopInset(RecyclerView.LayoutParams layoutParams) {
        try {
            Field field = RecyclerView.LayoutParams.class.getDeclaredField("mDecorInsets");
            field.setAccessible(true);
            // 开发者自定义的滚动监听器
            Rect decorInsets = (Rect) field.get(layoutParams);
            return decorInsets.top;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取stretchRecyclerView
     *
     * @param view
     * @return
     */
    private StretchRecyclerView getStretchRecyclerView(View view) {
        ViewParent viewParent = view.getParent();
        while (viewParent != null) {
            if (viewParent instanceof StretchRecyclerView) {
                return (StretchRecyclerView) viewParent;
            }
            viewParent = viewParent.getParent();
        }
        return null;
    }

    public interface OnLoadMoreListener {
        void onReady();

        void onLoading();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    public void pullLoadReset() {
        mCanLoadMore = false;
        mIsLoading = false;
        mIsLoadComplete = false;
    }

    public void pullLoadComplete() {
        mIsLoadComplete = true;
    }

    public void pullLoadEnd() {
        mCanLoadMore = false;
        mIsLoading = false;
    }
}
