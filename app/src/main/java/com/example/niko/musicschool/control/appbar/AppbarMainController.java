package com.example.niko.musicschool.control.appbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.BaseController;

import cn.sanfast.xmutils.bitmap.view.CircleImageView;

/**
 * appbar_main.xml
 * Created by wzd on 2016/7/6.
 */
public class AppbarMainController extends BaseController {

    private View mLeft;
    private View mRight;
    private CircleImageView mAvatar;
    private TextView mTitle;
    private ImageView mMore;
    private OnAvatarClickListener mListener;

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public AppbarMainController(Context context, View view) {
        super(context, view);
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.appbar_main, null);
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
        mAvatar = (CircleImageView) findViewById(R.id.appbar_avatar);
        mTitle = (TextView) findViewById(R.id.appbar_title);
        mMore = (ImageView) findViewById(R.id.appbar_more);
        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onAvatarClick();
                }
            }
        });
    }

    /**
     * 初始化
     *
     * @param avatar String
     * @param title  String
     * @param resId  int
     * @return this
     */
    public AppbarMainController init(String avatar, String title, int resId) {
        setAvatar(avatar);
        setTitle(title);
        setMenuIcon(resId);
        return this;
    }

    /**
     * 给头像设置点击事件
     *
     * @param listener View.OnClickListener
     * @return this
     */
    public AppbarMainController setOnAvatarClickListener(View.OnClickListener listener) {
        if (mLeft != null) {
            mLeft.setOnClickListener(listener);
        }
        /*if (mAvatar != null) {
            mAvatar.setOnClickListener(listener);
        }*/
        return this;
    }

    /**
     * 给菜单按钮设置点击事件
     *
     * @param listener View.OnClickListener
     * @return this
     */
    public AppbarMainController setOnMenuClickListener(View.OnClickListener listener) {
        if (mRight != null) {
            mRight.setOnClickListener(listener);
        }
        /*if (mMore != null) {
            mMore.setOnClickListener(listener);
        }*/
        return this;
    }

    /**
     * 新消息
     *
     * @param isNew boolean
    */
    public void setNewMessage(boolean isNew) {
        mMore.setSelected(isNew);
    }

    /**
     * 设置头像
     *
     * @param avatar String
     */
    public void setAvatar(String avatar) {
        if (mAvatar != null) {
            mAvatar.loadImageFromURL(avatar, R.drawable.ic_avatar);
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
     * 设置菜单图标
     *
     * @param resId int
     */
    public void setMenuIcon(int resId) {
        if (mMore != null) {
            mMore.setImageResource(resId);
        }
    }

    public interface OnAvatarClickListener {
        public void onAvatarClick();
    }

    public void setOnAvatarClickListener(OnAvatarClickListener listener) {
        this.mListener = listener;
    }

}
