package com.example.niko.musicschool.control.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.BaseController;


/**
 * 侧滑菜单项
 * item_menu.xml
 * <p/>
 * Created by wzd on 2016/6/16.
 */
public class ItemMenuController extends BaseController {

    private ImageView mIcon;
    private TextView mName;
    private ImageView mArrow;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public ItemMenuController(Context context, View view) {
        super(context, view);
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.item_menu, null);
        }
        initViews();
    }

    /**
     * 初始化操作
     */
    @Override
    protected void initViews() {
        mIcon = (ImageView) findViewById(R.id.menu_icon);
        mName = (TextView) findViewById(R.id.menu_name);
        mName.setTextColor(mContext.getResources().getColor(R.color.text_normal));
        mArrow = (ImageView) findViewById(R.id.menu_arrow);
    }

    /**
     * 初始化
     *
     * @param iconId  int
     * @param name    String
     * @param arrowId int
     * @return this
     */
    public ItemMenuController init(int iconId, String name, int arrowId) {
        mIcon.setImageResource(iconId);
        mName.setText(name);
        mArrow.setImageResource(arrowId);
        return this;
    }

    /**
     * 设置点击事件监听
     *
     * @param listener View.OnClickListener
     */
    public void setOnClickListener(View.OnClickListener listener) {
        mView.setOnClickListener(listener);
    }

    public void show() {
        if (mView != null) {
            mView.setVisibility(View.VISIBLE);
        }
    }

    public void hide() {
        if (mView != null) {
            mView.setVisibility(View.GONE);
        }
    }
}
