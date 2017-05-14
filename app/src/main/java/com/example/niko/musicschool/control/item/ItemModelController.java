package com.example.niko.musicschool.control.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.BaseController;


/**
 * 主界面上的子模块入口
 * <p/>
 * Created by wzd on 2016/6/15.
 */
public class ItemModelController extends BaseController {

    private TextView mTitle;
    private ImageView mImage;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public ItemModelController(Context context, View view) {
        super(context, view);
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.item_main_model, null);
        }
        initViews();
    }

    /**
     * 初始化操作
     */
    @Override
    protected void initViews() {
        mTitle = (TextView) findViewById(R.id.model_title);
        mTitle.setTextColor(mContext.getResources().getColor(R.color.white));
        mImage = (ImageView) findViewById(R.id.model_image);
    }

    /**
     * 设置标题
     *
     * @param title String
     * @return this
     */
    public ItemModelController setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    /**
     * 设置图标
     *
     * @param resId int
     * @return this
     */
    public ItemModelController setImage(int resId) {
        mImage.setImageResource(resId);
        return this;
    }

    /**
     * 设置背景
     *
     * @param resId int
     * @return this
     */
    public ItemModelController setBackground(int resId) {
        mView.setBackgroundResource(resId);
        return this;
    }

    /**
     * 设置点击事件
     *
     * @param listener View.OnClickListener
     */
    public void setOnClickListener(View.OnClickListener listener) {
        mView.setOnClickListener(listener);
    }
}
