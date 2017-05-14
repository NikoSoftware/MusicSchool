package com.example.niko.musicschool.control;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niko.musicschool.R;


/**
 * 没有数据
 * layout_no_data.xml
 * <p/>
 * Created by wzd on 2016/8/11.
 */
public class NoDataController extends BaseController {

    private OnReloadListener mListener;
    private ImageView mImageView;
    private TextView mTextView;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public NoDataController(Context context, View view) {
        super(context, view);
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.layout_no_data, null);
        }
        initViews();
    }

    /**
     * 初始化操作
     */
    @Override
    protected void initViews() {
        mImageView = (ImageView) findViewById(R.id.iv_no_data);
        mTextView = (TextView) findViewById(R.id.tv_no_data);
        if (mView != null) {
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("傻逼", "你点的是我...");
                    if (mListener != null) {
                        mListener.onReload();
                    }
                }
            });
        }
    }

    /**
     * 初始化
     *
     * @param resId int
     * @param tips  String
     * @return this
     */
    public NoDataController init(int resId, String tips) {
        setIcon(resId);
        setTips(tips);
        hide();
        return this;
    }

    /**
     * 设置图标
     *
     * @param resId int
     */
    public NoDataController setIcon(int resId) {
        if (mImageView != null) {
            mImageView.setImageResource(resId);
        }
        return this;
    }

    /**
     * 设置提示文案
     *
     * @param tips String
     */
    public NoDataController setTips(String tips) {
        if (mTextView != null) {
            mTextView.setText(tips);
        }
        return this;
    }

    /**
     * 设置是否可点击
     */
    public NoDataController setEnable(boolean enable) {
        if (mView != null) {
            mView.setEnabled(enable);
        }
        return this;
    }

    /**
     * 设置高度
     *
     * @param height int
     */
    public NoDataController setHeight(int height) {
        ViewGroup.LayoutParams lp = mView.getLayoutParams();
        lp.height = height;
        mView.setLayoutParams(lp);
        return this;
    }

    /**
     * 显示
     */
    public void show() {
        mView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏
     */
    public void hide() {
        mView.setVisibility(View.GONE);
    }

    public interface OnReloadListener {
        void onReload();
    }

    public void setOnReloadListener(OnReloadListener listener) {
        mListener = listener;
    }

}
