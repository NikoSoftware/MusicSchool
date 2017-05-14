package cn.sanfast.xmutils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 抽屉式侧滑菜单辅助ViewGroup
 * <p/>
 * Created by wzd on 2016/4/8.
 */
public class SlidingLinearLayout extends LinearLayout {

    public SlidingLinearLayout(Context context) {
        this(context, null);
    }

    public SlidingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        /**
         * 定制子View的绘制顺序
         */
        if (i == 0) {
            return 1;
        }
        if (i == 1) {
            return 0;
        }
        return super.getChildDrawingOrder(childCount, i);
    }
}
