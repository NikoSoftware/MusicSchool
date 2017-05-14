package cn.sanfast.xmutils.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 可循环滑动的ViewPager
 * <p/>
 * Created by wzd on 2016/6/20.
 */
public class CycleViewPager extends ViewPager {

    private InnerPagerAdapter mAdapter;

    public CycleViewPager(Context context) {
        super(context);
        setOnPageChangeListener(null);
    }

    public CycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPageChangeListener(null);
    }

    @Override
    public void setAdapter (PagerAdapter adapter) {
        mAdapter = new InnerPagerAdapter(adapter);
        super.setAdapter(mAdapter);
        setCurrentItem(1);
    }

    @Override
    public void setOnPageChangeListener (OnPageChangeListener listener) {
        super.setOnPageChangeListener(new InnerOnPageChangeListener(listener));
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(new InnerOnPageChangeListener(listener));
    }

    /**
     * 内部滑动监听
     */
    private class InnerOnPageChangeListener implements OnPageChangeListener {

        private OnPageChangeListener listener;
        private int position;

        public InnerOnPageChangeListener(OnPageChangeListener listener) {
            this.listener = listener;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (null != listener) {
                listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            this.position = position;
            if (null != listener) {
                listener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (null != listener) {
                listener.onPageScrollStateChanged(state);
            }
            if (ViewPager.SCROLL_STATE_IDLE == state) {
                // 最后一位
                if (position == mAdapter.getCount() - 1) {
                    setCurrentItem(1, false);
                } else if (position == 0) {
                    setCurrentItem(mAdapter.getCount() - 2, false);
                }
            }
        }
    }

    /**
     * 内部适配器
     */
    private class InnerPagerAdapter extends PagerAdapter {

        private PagerAdapter adapter;

        public InnerPagerAdapter(PagerAdapter adapter) {
            this.adapter = adapter;
            this.adapter.registerDataSetObserver(new DataSetObserver() {

                @Override
                public void onChanged() {
                    notifyDataSetChanged();
                }

                @Override
                public void onInvalidated() {
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getCount() {
            return adapter.getCount() + 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return adapter.isViewFromObject(view, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == 0) {
                position = adapter.getCount() - 1;
            } else if (position == adapter.getCount() + 1) {
                position = 0;
            } else {
                position -= 1;
            }
            return adapter.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            adapter.destroyItem(container, position, object);
        }
    }
}
