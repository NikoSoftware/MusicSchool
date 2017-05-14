package com.example.niko.musicschool.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;
import com.example.niko.musicschool.eventbus.NickNameEvent;
import com.example.niko.musicschool.model.UserModel;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.sanfast.xmutils.utils.StringUtil;
import cn.sanfast.xmutils.view.ClearEditText;


/**
 * 修改昵称
 * <p/>
 * Created by wzd on 2016/8/11.
 */
public class UpdateNicknameActivity extends BaseActivity {

    private final String TAG = "修改昵称";
    public static final String INTENT_FLAG = "intent_flag";
    public static final String NICKNAME = "nickname";
    private AppbarNormalController mAppbar;
    private ClearEditText mClearEditText;
    private String mName;
    private View mRootView;

    /**
     * 为Activity添加布局文件
     */
    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_update_nickname);
        Intent intent = getIntent();
        if (intent != null) {
            mName = intent.getStringExtra(INTENT_FLAG);
        }
        if(mName==null){
            mName = "";
        }
        if (StringUtil.isEmpty(mName)) {
            mName = "";
        }

    }

    /**
     * 获取activity TAG
     *
     * @return String
     */
    @Override
    protected String getActivityTag() {
        return TAG;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mRootView = findViewById(R.id.layout_update_nickname);
        // appbar
        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));
        // view
        mClearEditText = (ClearEditText) findViewById(R.id.cet_nickname);
    }

    /**
     * 为控件填充内容
     */
    @Override
    protected void setupViews() {
        // appbar
        mAppbar.init(R.drawable.selector_btn_back, TAG, "确定");
        mAppbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });
        mAppbar.setOnOperationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
        // view
        mClearEditText.setText(mName);
    }

    /**
     * 确定
     */
    private void confirm() {
        final String nickname = mClearEditText.getText().toString().trim();
        Intent intent = getIntent();
        if (!StringUtil.isEmpty(nickname)) {
            intent.putExtra(NICKNAME, nickname);
            setResult(Activity.RESULT_OK, intent);
        }
        if (!StringUtil.isEmpty(nickname) && nickname.equals(mName)) {
            // 未修改
            finish();
        } else {
            // 已修改
            setCustomDialog("修改中");

            final UserModel newUser = BmobUser.getCurrentUser(UserModel.class);
            newUser.setNick(nickname);
            newUser.update(new UpdateListener() {
                               @Override
                               public void done(BmobException e) {
                                   closeCustomDialog();
                                   if(e==null){

                                       NickNameEvent nickNameEvent = new NickNameEvent();
                                       nickNameEvent.setNickName(nickname);
                                       EventBus.getDefault().post(nickNameEvent);
                                       showSnackbar("更新用户信息成功");
                                       finish();
                                   }else{
                                       e.printStackTrace();
                                       showSnackbar("更新用户信息失败");
                                   }
                               }
                           }
            );
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
