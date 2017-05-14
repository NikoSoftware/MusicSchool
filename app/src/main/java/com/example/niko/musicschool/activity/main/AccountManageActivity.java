package com.example.niko.musicschool.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;
import com.example.niko.musicschool.control.item.ItemInfoController;
import com.example.niko.musicschool.model.UserModel;

import cn.sanfast.xmutils.bitmap.utils.Base64Utils;
import cn.sanfast.xmutils.bitmap.view.CircleImageView;
import cn.sanfast.xmutils.media.PhotoChoiceManager;


/**
 * 账户管理
 * <p/>
 * Created by wzd on 2016/8/10.
 */
public class AccountManageActivity extends BaseActivity implements View.OnClickListener{

    private final String TAG = "账户管理";
    private final int REQUEST_NICKNAME = 100;
    private final int REQUEST_TELEPHONE = 200;
    private AppbarNormalController mAppbar;
    private View mViewAvatar;
    private CircleImageView mCivAvatar;
    private ItemInfoController mItemName;
    private ItemInfoController mItemPhone;
    private PhotoChoiceManager mPhotoChoiceManager;
    private View mRootView;
    private UserModel mUserModel;

    /**
     * 为Activity添加布局文件
     */
    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_account_manage);
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
        mRootView = findViewById(R.id.layout_account_manage);
        // appbar
        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));
        // views
        mViewAvatar = findViewById(R.id.view_avatar);
        mCivAvatar = (CircleImageView) findViewById(R.id.civ_avatar);
        mItemName = new ItemInfoController(mContext, findViewById(R.id.view_name));
        mItemPhone = new ItemInfoController(mContext, findViewById(R.id.view_phone));
        // photo
        mPhotoChoiceManager = new PhotoChoiceManager(mContext, mRootView);

        mUserModel =  UserModel.getCurrentUser(UserModel.class);


    }

    /**
     * 为控件填充内容
     */
    @Override
    protected void setupViews() {
        // appbar
        mAppbar.init(R.drawable.selector_btn_back, TAG, "");
        mAppbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // views
        mViewAvatar.setOnClickListener(this);
        mCivAvatar.setOnClickListener(this);
        mItemName.setOnClickListener(this);
        mItemPhone.setOnClickListener(this);
        // photo
        mPhotoChoiceManager.setPhotoZoom(120, 120);
        mPhotoChoiceManager.setMediaSuccessListener(new PhotoChoiceManager.MediaSuccessListener() {
            @Override
            public void onFileSelected(String filePath, String fileType) {
                String avatar = Base64Utils.getUploadFileData(filePath, fileType);
                uploadAvatar(avatar);
            }
        });


        mCivAvatar.loadImageFromURL(mUserModel.getImgpath(), R.drawable.ic_avatar);
        mItemName.init("昵称", mUserModel.getNick());
        mItemPhone.init("手机号", mUserModel.getUsername());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // avatar
            case R.id.civ_avatar:
            case R.id.view_avatar:
                setAvatar();
                break;
            // name
            case R.id.view_name:
                updateNickname();
                break;
            // phone
            case R.id.view_phone:
                break;
            // password
        }
    }

    /**
     * 设置头像
     */
    private void setAvatar() {
        // 相机权限申请

    }

    /**
     * 上传头像
     *
     * @param avatar String
     */
    private void uploadAvatar(String avatar) {



    }

    /**
     * 修改用户名
     */
    private void updateNickname() {
        Intent intent = new Intent(mContext, UpdateNicknameActivity.class);
        intent.putExtra(UpdateNicknameActivity.INTENT_FLAG, mUserModel.getNick());
        startActivityForResult(intent, REQUEST_NICKNAME);
    }


    /**
     * 修改登录密码
     */
    private void updatePassword() {
    /*    Intent intent = new Intent(mContext, ShortcutLoginActivity.class);
        intent.putExtra(ShortcutLoginActivity.INTENT_FLAG, ShortcutLoginActivity.INTENT_FLAG_MODIFY_PASSWORD);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_exit_none);
*/    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // update nickname
            case REQUEST_NICKNAME:
                if (Activity.RESULT_OK == resultCode && data != null) {
                    String nickname = data.getStringExtra(UpdateNicknameActivity.NICKNAME);
                    mItemName.setValue(nickname);
                }
                break;
            // update telephone

            // default, update avatar
            default:
                if (mPhotoChoiceManager != null) {
                    mPhotoChoiceManager.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




}
