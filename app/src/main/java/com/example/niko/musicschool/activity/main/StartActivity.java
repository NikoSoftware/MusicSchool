package com.example.niko.musicschool.activity.main;

import android.os.Handler;
import android.os.Message;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.db.SharePerefenceTool;
import com.example.niko.musicschool.model.UserModel;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by niko on 2017/3/26.
 */

public class StartActivity extends BaseActivity {

    private final String TAG = "启动页";
    private final int GOTO_LOGIN = 100;
    private final int GOTO_MAIN = 200;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case GOTO_LOGIN:
                    skip(mContext,LoginActivity.class);
                    finish();
                    break;
                case GOTO_MAIN:
                    skip(mContext,MainActivity.class);
                    finish();
                    break;
            }
            return false;
        }
    });


    @Override
    protected void setLayout() {

        setContentView(R.layout.activity_start);

    }

    @Override
    protected void initViews() {
        loadFilterInfo();
    }

    @Override
    protected void setupViews() {

    }

    private void loadFilterInfo(){
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser==null){
            mHandler.sendEmptyMessageDelayed(GOTO_LOGIN,2500);
        }else{
            bmobUser.setPassword(SharePerefenceTool.getPassword(mContext));
            bmobUser.login(new SaveListener<UserModel>() {
                @Override
                public void done(UserModel s, BmobException e) {
                    if (e == null) {
                        mHandler.sendEmptyMessageDelayed(GOTO_MAIN,1000);
                    } else {
                        mHandler.sendEmptyMessageDelayed(GOTO_LOGIN,2500);
                    }
                }
            });

        }
    }


}
