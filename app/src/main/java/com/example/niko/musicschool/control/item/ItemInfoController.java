package com.example.niko.musicschool.control.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.BaseController;


/**
 * 账户管理
 * item_info.xml
 * <p/>
 * Created by wzd on 2016/8/11.
 */
public class ItemInfoController extends BaseController {

    private TextView mLeft;
    private TextView mRight;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public ItemInfoController(Context context, View view) {
        super(context, view);
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.item_info, null);
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
    }

    /**
     * 初始化
     *
     * @param name  String
     * @param value String
     */
    public void init(String name, String value) {
        if (mLeft != null) {
            mLeft.setText(name);
        }
        setValue(value);
    }

    /**
     * 设置值
     *
     * @param value String
     */
    public void setValue(String value) {
        if (mRight != null) {
            mRight.setText(value);
        }
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
