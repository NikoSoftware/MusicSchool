package com.example.niko.musicschool.control.appbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.BaseController;


/**
 * appbar_icon.xml
 * Created by wzd on 2016/7/6.
 */
public class AppbarIconController extends BaseController {

    private View mLeft;
    private View mRight;
    private ImageView mBack;
    private TextView mTitle;
    private ImageView mIcon;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public AppbarIconController(Context context, View view) {
        super(context, view);
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.appbar_icon, null);
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
        mIcon = (ImageView) findViewById(R.id.appbar_icon);
    }

    /**
     * 初始化
     *
     * @param resId  int
     * @param title  String
     * @param iconId int
     * @return this
     */
    public AppbarIconController init(int resId, String title, int iconId) {
        setBackIcon(resId);
        setTitle(title);
        setIcon(iconId);
        return this;
    }

    /**
     * 给返回按钮设置点击事件
     *
     * @param listener View.OnClickListener
     * @return this
     */
    public AppbarIconController setOnBackClickListener(View.OnClickListener listener) {
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
    public AppbarIconController setOnIconClickListener(View.OnClickListener listener) {
        if (mRight != null) {
            mRight.setOnClickListener(listener);
        }
        /*if (mIcon != null) {
            mIcon.setOnClickListener(listener);
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
     * @param iconId int
     */
    public void setIcon(int iconId) {
        if (mIcon != null) {
            mIcon.setImageResource(iconId);
        }
    }

}
