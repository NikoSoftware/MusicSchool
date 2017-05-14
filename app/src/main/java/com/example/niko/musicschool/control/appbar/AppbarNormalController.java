package com.example.niko.musicschool.control.appbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.BaseController;

/**
 * appbar_normal.xml
 * Created by wzd on 2016/7/6.
 */
public class AppbarNormalController extends BaseController {

    private View mLeft;
    private View mRight;
    private ImageView mBack;
    private TextView mTitle;
    private TextView mOperation;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public AppbarNormalController(Context context, View view) {
        super(context, view);
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.appbar_normal, null);
        }
        initViews();
    }

    /**
     * 初始化操作
     */
    @Override
    protected void initViews() {
        mLeft = findViewById(R.id.appbar_left);
        mRight = findViewById(R.id.appbar_right);
        mBack = (ImageView) findViewById(R.id.appbar_back);
        mTitle = (TextView) findViewById(R.id.appbar_title);
        mOperation = (TextView) findViewById(R.id.appbar_operation);
    }

    /**
     * 初始化
     *
     * @param resId     int
     * @param title     String
     * @param operation String
     * @return this
     */
    public AppbarNormalController init(int resId, String title, String operation) {
        setBackIcon(resId);
        setTitle(title);
        setOperation(operation);
        return this;
    }

    /**
     * 给返回按钮设置点击事件
     *
     * @param listener View.OnClickListener
     * @return this
     */
    public AppbarNormalController setOnBackClickListener(View.OnClickListener listener) {
        if (mLeft != null) {
            mLeft.setOnClickListener(listener);
        }
        /*if (mBack != null) {
            mBack.setOnClickListener(listener);
        }*/
        return this;
    }

    /**
     * 给操作按钮设置点击事件
     *
     * @param listener View.OnClickListener
     * @return this
     */
    public AppbarNormalController setOnOperationClickListener(View.OnClickListener listener) {
        if (mRight != null) {
            mRight.setOnClickListener(listener);
        }
        /*if (mOperation != null) {
            mOperation.setOnClickListener(listener);
        }*/
        return this;
    }

    /**
     * 设置返回按钮图标
     *
     * @param resId int
     */
    public void setBackIcon(int resId) {
        if (mBack != null) {
            mBack.setImageResource(resId);
        }
    }

    /**
     * 设置标题
     *
     * @param title String
     */
    public void setTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    /**
     * 设置操作
     *
     * @param operation String
     */
    public void setOperation(String operation) {
        if (mOperation != null) {
            mOperation.setText(operation);
        }
    }

}
