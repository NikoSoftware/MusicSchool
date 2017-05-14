package cn.sanfast.xmutils.view;

import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import java.lang.reflect.Field;

import cn.sanfast.xmutils.R;

/**
 * RecyclerView，
 * 仿ISO回弹效果，
 * 实现上拉加载与下拉刷新。
 * <p/>
 * Created by wzd on 2016/10/9.
 */
public class XRecyclerView extends LinearLayout {

    private final String TAG = XRecyclerView.class.getSimpleName();
    private final int DURATION_SUCCESS = 800;
    private final int DURATION = 300;
    private final int RESET_HEADER = 1;
    private final int RESET_ROOT = 2;
    /**
     * header
     */
    private View mHeaderView;
    private ImageView mHeaderViewIcon;
    private ProgressBar mHeaderViewProgress;
    private TextView mHeaderViewText;
    private int mHeaderViewHeight;
    private boolean mIsRefreshing = false;
    private boolean mRefreshEnable = false;
    private int mRefreshCount = 0;
    private boolean mIsNeedSleep = false;
    /**
     * content
     */
    private View mConvertView;
    private RecyclerView mRecyclerView;
    /**
     * footer
     */
    private View mFooterView;
    private ProgressBar mFooterViewProgress;
    private TextView mFooterViewText;
    private int mFooterViewHeight;
    private boolean mIsLoading = false;
    private boolean mLoadMoreEnable = false;
    private boolean mIsLoadComplete = false;
    private int mLoadMoreCount = 0;

    private int mStart;
    private int mEnd;
    private int mLastY;
    private Scroller mScroller;
    private OnActionListener mOnActionListener;
    private Context mContext;
    private int mTouchSlop;
    private OnScrollingListener mOnScrollingListener;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case RESET_HEADER:
                    resetHeaderViewHeight(message.arg1);
                    break;
                case RESET_ROOT:
                    mScroller.startScroll(0, getFooterViewHeight(), 0, -getFooterViewHeight(), DURATION);
                    postInvalidate();
                    break;
            }
            return false;
        }
    });

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        // header
        initHeaderView(context);
        // recycler view
        initRecyclerView(context);
        // footer
        initFooterView(context);
        // scroller
        mScroller = new Scroller(context);
        // orientation
        setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * 初始化 header
     *
     * @param context Context
     */
    private void initHeaderView(Context context) {
        mHeaderView = View.inflate(context, R.layout.layout_view_header_refresh, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        mHeaderView.setLayoutParams(lp);
        mHeaderViewIcon = (ImageView) mHeaderView.findViewById(R.id.header_refresh_icon);
        mHeaderViewProgress = (ProgressBar) mHeaderView.findViewById(R.id.header_refresh_progress_bar);
        mHeaderViewText = (TextView) mHeaderView.findViewById(R.id.header_refresh_text);
        mHeaderViewHeight = dp2px(context, 52);
        mIsRefreshing = false;
        mRefreshEnable = false;
        mRefreshCount = 0;
    }

    /**
     * 初始化 RecyclerView
     *
     * @param context Context
     */
    private void initRecyclerView(Context context) {
        mConvertView = View.inflate(context, R.layout.layout_recycler_view_linear_vertical, null);
        if (mConvertView instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) mConvertView;
            mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        }
    }

    /**
     * 初始化 Footer
     *
     * @param context Context
     */
    private void initFooterView(Context context) {
        mFooterView = View.inflate(context, R.layout.layout_view_footer_load_more, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        mFooterView.setLayoutParams(lp);
        mFooterViewProgress = (ProgressBar) mFooterView.findViewById(R.id.footer_load_more_progress_bar);
        mFooterViewText = (TextView) mFooterView.findViewById(R.id.footer_load_more_text);
        mFooterViewHeight = dp2px(context, 52);
        mIsLoading = false;
        mLoadMoreEnable = false;
        mIsLoadComplete = false;
        mLoadMoreCount = 0;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addView(mHeaderView);
        addView(mConvertView);
        addView(mFooterView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            View view = getChildAt(1);
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
                if (mLoadMoreCount == 1 && mIsLoading && !mIsRefreshing) {
                    scrollTo(0, (int) ((mLastY - y + mFooterViewHeight * 2.5) * 0.4));
                } else {
                    scrollTo(0, (int) ((mLastY - y) * 0.4));
                }
                // 下拉刷新
                if (y - mLastY > 0) {
                    if (mRefreshEnable && !mIsRefreshing && !mIsLoading) {
                        updateHeaderViewHeight((int) ((y - mLastY) * 0.8));
                    }
                }
                // 上拉加载
                else if (y - mLastY < 0 && mRecyclerView.getMeasuredHeight() > getMeasuredHeight() - mFooterViewHeight) {
                    if (mLoadMoreEnable && !mIsRefreshing && !mIsLoading && !mIsLoadComplete) {
                        updateFooterViewHeight((int) ((mLastY - y) * 0.8));
                    }
                }
                if (Math.abs(y - mLastY) > mTouchSlop && mOnScrollingListener != null) {
                    mOnScrollingListener.onScrolling();
                }
                break;
            case MotionEvent.ACTION_UP:
                mEnd = getScrollY();
                int deltaScrollY = mEnd - mStart;
                if (mIsRefreshing && !mIsLoading) {
                    mIsNeedSleep = true;
                    actionRefresh();
                } else if (!mIsRefreshing && getHeaderViewHeight() > 0) {
                    mIsNeedSleep = false;
                    new ResetHeaderThread().start();
                }
                if (mIsLoading && !mIsRefreshing) {
                    actionLoadMore();
                    deltaScrollY -= getFooterViewHeight();
                }
                /**
                 * 回弹动画，第一二个参数为开始的x，y
                 * 第三个和第四个参数为滚动的距离（注意方向问题）
                 * 第五个参数是回弹时间
                 */
                mScroller.startScroll(0, mEnd, 0, -deltaScrollY, DURATION);
                break;
        }
        postInvalidate();
        return true;
    }

    /**
     * 更新HeaderView高度
     *
     * @param height int
     */
    private void updateHeaderViewHeight(int height) {
        if (height < 0) {
            return;
        }
        // 下拉过程
        if (height < mHeaderViewHeight) {
            mHeaderViewProgress.setVisibility(GONE);
            mHeaderViewIcon.setVisibility(VISIBLE);
            mHeaderViewIcon.setImageResource(R.drawable.ic_arrow_long_down);
            mHeaderViewText.setText("下拉刷新");
            mIsRefreshing = false;
        } else if (height > mHeaderViewHeight * 1.5) {
            height = (int) (mHeaderViewHeight * 1.5);
            mHeaderViewProgress.setVisibility(GONE);
            mHeaderViewIcon.setVisibility(VISIBLE);
            mHeaderViewIcon.setImageResource(R.drawable.ic_arrow_long_up);
            mHeaderViewText.setText("松开刷新");
            mIsRefreshing = true;
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        mHeaderView.setLayoutParams(lp);
    }

    /**
     * HeaderView高度
     *
     * @return int
     */
    private int getHeaderViewHeight() {
        LinearLayout.LayoutParams lp = (LayoutParams) mHeaderView.getLayoutParams();
        return lp.height;
    }

    /**
     * 更新FooterView高度
     *
     * @param height int
     */
    private void updateFooterViewHeight(int height) {
        // 上拉过程
        if (height < mFooterViewHeight) {
            mFooterViewProgress.setVisibility(GONE);
            mFooterViewText.setText("上拉加载更多");
            mIsLoading = false;
        } else if (height > mFooterViewHeight * 1.5) {
            height = (int) (mFooterViewHeight * 1.5);
            mFooterViewProgress.setVisibility(GONE);
            mFooterViewText.setText("松开加载更多");
            mIsLoading = true;
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        mFooterView.setLayoutParams(lp);
    }

    /**
     * FooterView高度
     *
     * @return int
     */
    private int getFooterViewHeight() {
        LinearLayout.LayoutParams lp = (LayoutParams) mFooterView.getLayoutParams();
        return lp.height;
    }

    /**
     * 刷新
     */
    private void actionRefresh() {
        mRefreshCount++;
        if (mRefreshCount > 1) {
            mRefreshCount = 1;
            return;
        }
        if (mIsRefreshing) {
            mHeaderViewIcon.setVisibility(GONE);
            mHeaderViewProgress.setVisibility(VISIBLE);
            mHeaderViewText.setText("正在刷新...");
        }
        if (mRefreshEnable && mIsRefreshing && !mIsLoading) {
            if (!isNetworkConnected(mContext)) {
                networkDisable();
            }
            if (mOnActionListener != null) {
                mOnActionListener.actionRefresh();
                resetLoadMore();
            }
        }
    }

    /**
     * 加载
     */
    private void actionLoadMore() {
        mLoadMoreCount++;
        if (mLoadMoreCount > 1) {
            mLoadMoreCount = 1;
            return;
        }
        if (mIsLoading) {
            mFooterViewProgress.setVisibility(VISIBLE);
            mFooterViewText.setText("正在加载...");
        }
        if (mLoadMoreEnable && mIsLoading && !mIsLoadComplete && !mIsRefreshing) {
            if (!isNetworkConnected(mContext)) {
                networkDisable();
            }
            if (mOnActionListener != null) {
                mOnActionListener.actionLoadMore();
            }
        }
    }

    /**
     * 重置HeaderView高度
     */
    private void resetHeaderViewHeight(int height) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        mHeaderView.setLayoutParams(lp);
    }

    /**
     * dip转px
     *
     * @param context Context
     * @param dp      int
     * @return int
     */
    private int dp2px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
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
            int firstChildTop = 0;
            // 如果RecyclerView的子控件数量不为0，获取第一个子控件的top
            if (manager.getItemCount() > 0) {
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
            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                if (layoutManager.findFirstCompletelyVisibleItemPosition() < 1 && firstChildTop == 0) {
                    return true;
                }
            } else if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
                int first = layoutManager.findFirstCompletelyVisibleItemPositions(new int[layoutManager.getSpanCount()])[0];
                if (first < 1 && firstChildTop == 0) {
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
                    XRecyclerView parentView = getStretchRecyclerView(mRecyclerView);
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
    private XRecyclerView getStretchRecyclerView(View view) {
        ViewParent viewParent = view.getParent();
        while (viewParent != null) {
            if (viewParent instanceof XRecyclerView) {
                return (XRecyclerView) viewParent;
            }
            viewParent = viewParent.getParent();
        }
        return null;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 能否下拉刷新
     *
     * @param enable boolean
     */
    public void setRefreshEnable(boolean enable) {
        mRefreshEnable = enable;
    }

    /**
     * 能否上拉加载
     *
     * @param enable boolean
     */
    public void setLoadMoreEnable(boolean enable) {
        mLoadMoreEnable = enable;
    }

    /**
     * 下拉刷新结束
     */
    public void setRefreshFinish() {
        if (mIsRefreshing) {
            mIsRefreshing = false;
            mRefreshCount--;
            mHeaderViewIcon.setVisibility(GONE);
            mHeaderViewProgress.setVisibility(GONE);
            mHeaderViewText.setText("刷新成功");
            new ResetHeaderThread().start();
        }
    }

    /**
     * 暂无网络
     */
    private void networkDisable() {
        if (mIsRefreshing) {
            mIsRefreshing = false;
            mRefreshCount--;
            mHeaderViewIcon.setVisibility(GONE);
            mHeaderViewProgress.setVisibility(GONE);
            mHeaderViewText.setText("暂无网络");
            new ResetHeaderThread().start();
        }
        if (mIsLoading) {
            mIsLoading = false;
            mLoadMoreCount--;
            mFooterViewProgress.setVisibility(GONE);
            mFooterViewText.setText("暂无网络");
            mHandler.sendEmptyMessageDelayed(RESET_ROOT, DURATION_SUCCESS);
        }
    }

    /**
     * 上拉加载结束
     */
    public void setLoadMoreFinish() {
        if (mIsLoading) {
            mIsLoading = false;
            mLoadMoreCount--;
            mFooterViewProgress.setVisibility(GONE);
            mFooterViewText.setText("加载成功");
            mHandler.sendEmptyMessageDelayed(RESET_ROOT, DURATION_SUCCESS);
        }
    }

    /**
     * 上拉加载或者下拉刷新结束
     */
    public void setActionFinish() {
        setRefreshFinish();
        setLoadMoreFinish();

    }

    /**
     * 全部加载完毕
     */
    public void setLoadMoreComplete() {
        if (mIsLoading) {
            mIsLoading = false;
            mLoadMoreCount--;
            mIsLoadComplete = true;
            mFooterViewProgress.setVisibility(GONE);
            mFooterViewText.setText("已全部加载完毕");
            mHandler.sendEmptyMessageDelayed(RESET_ROOT, DURATION_SUCCESS);
        }
    }

    /**
     * 重置上拉加载
     */
    public void resetLoadMore() {
        mIsLoading = false;
        mLoadMoreCount = 0;
        mIsLoadComplete = false;
        mFooterViewProgress.setVisibility(GONE);
        mFooterViewText.setText("上拉加载更多");
    }

    public void setOnScrollingListener(OnScrollingListener onScrollingListener) {
        mOnScrollingListener = onScrollingListener;
    }

    public interface OnScrollingListener {
        void onScrolling();
    }

    public interface OnActionListener {
        /**
         * 下拉刷新
         */
        void actionRefresh();

        /**
         * 上拉加载
         */
        void actionLoadMore();
    }

    /**
     * 设置监听
     *
     * @param listener OnActionListener
     */
    public void setOnActionListener(OnActionListener listener) {
        mOnActionListener = listener;
    }


    /**
     * 刷新完成
     */
    private class ResetHeaderThread extends Thread {

        private int i = 0;

        @Override
        public void run() {
            if (mIsNeedSleep) {
                try {
                    Thread.sleep(DURATION_SUCCESS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (i < 20) {
                try {
                    int height = getHeaderViewHeight();
                    if (height <= 0) {
                        break;
                    }
                    Thread.sleep(10);
                    Message message = new Message();
                    message.what = RESET_HEADER;
                    message.arg1 = (int) (height - (float) (i + 1) / 20 * height);
                    if (mHandler != null) {
                        mHandler.sendMessage(message);
                    }
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = cm.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
