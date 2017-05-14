package com.example.niko.musicschool.activity.picture;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.control.appbar.AppbarNormalController;
import com.example.niko.musicschool.fragment.picture.CardFragment;

/**
 * Created by niko on 2017/5/11.
 */

public class PictureActivity extends BaseActivity {
    private AppbarNormalController mAppbar;
    private final String TAG = "第一印象";

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_picture);
    }

    @Override
    protected void initViews() {
        // appbar
        mAppbar = new AppbarNormalController(mContext, findViewById(R.id.appbar));

    }

    @Override
    protected void setupViews() {
        // appbar
        mAppbar.init(R.drawable.selector_btn_back, TAG, "");
        mAppbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CardFragment cardFragment = new CardFragment();
        fragmentTransaction.add(R.id.picture_fragment,cardFragment);
        fragmentTransaction.commit();

    }
}
