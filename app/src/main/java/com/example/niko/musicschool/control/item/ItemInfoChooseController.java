package com.example.niko.musicschool.control.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.BaseController;


/**
 * item
 * item_info_choose.xml
 * <p/>
 * Created by wzd on 2016/8/11.
 */
public class ItemInfoChooseController extends BaseController {

    private TextView mLeft;
    private TextView mRight;
    private ImageView mArrow;
    private View mLine;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public ItemInfoChooseController(Context context, View view) {
        super(context, view);
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.item_info_choose, null);
        }
        initViews();
    }

    /**
     * 初始化操作
     */
    @Override
    protected void initViews() {
        mLeft = (TextView) findViewById(R.id.tv_left);
        mRight = (TextView) findViewById(R.id.tv_right);
        mArrow = (ImageView) findViewById(R.id.iv_arrow_right);
        mLine = findViewById(R.id.view_line);
    }

    /**
     * 初始化
     *
     * @param name  String
     * @param value String
     */
    public ItemInfoChooseController init(String name, String value) {
        if (mLeft != null) {
            mLeft.setText(name);
        }
        setValue(value);
        return this;
    }

    /**
     * 设置值
     *
     * @param value String
     */
    public ItemInfoChooseController setValue(String value) {
        if (mRight != null) {
            mRight.setText(value);
        }
        return this;
    }

    /**
     * 获取值
     *
     * @return String
     */
    public String getValue() {
        if (mRight == null) {
            return "";
        }
        return mRight.getText().toString().trim();
    }

    /**
     * 显示箭头
     *
     * @return this
     */
    public ItemInfoChooseController showArrow() {
        if (mArrow != null) {
            mArrow.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 隐藏箭头
     *
     * @return this
     */
    public ItemInfoChooseController hideArrow() {
        if (mArrow != null) {
            mArrow.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 显示线
     *
     * @return this
     */
    public ItemInfoChooseController showLine() {
        if (mLine != null) {
            mLine.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 隐藏线
     *
     * @return this
     */
    public ItemInfoChooseController hideLine() {
        if (mLine != null) {
            mLine.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 显示
     */
    public void show() {
        if (mView != null) {
            mView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏
     */
    public void hide() {
        if (mView != null) {
            mView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置点击事件
     *
     * @param listener View.OnClickListener
     */
    public void setOnClickListener(View.OnClickListener listener) {
        if (listener != null && mView != null) {
            mView.setOnClickListener(listener);
        }
    }
}
