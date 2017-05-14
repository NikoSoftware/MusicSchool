package com.example.niko.musicschool.control.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.niko.musicschool.R;
import com.example.niko.musicschool.activity.exam.MusicSignUpActivtiy;
import com.example.niko.musicschool.activity.main.BaseActivity;
import com.example.niko.musicschool.activity.main.WebViewActivity;
import com.example.niko.musicschool.activity.order.OrderManageActivity;
import com.example.niko.musicschool.activity.picture.PictureActivity;
import com.example.niko.musicschool.activity.teacher.MusicTeachingActivity;
import com.example.niko.musicschool.activity.video.VideoListActivity;
import com.example.niko.musicschool.control.BaseController;
import com.example.niko.musicschool.control.item.ItemModelController;
import com.example.niko.musicschool.model.BannerModel;

import java.util.ArrayList;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.sanfast.xmutils.bitmap.listener.MediaLoadListener;
import cn.sanfast.xmutils.bitmap.loader.AsyncImageLoader;
import cn.sanfast.xmutils.network.HttpResponseListener;
import cn.sanfast.xmutils.utils.AppUtils;
import cn.sanfast.xmutils.utils.StringUtil;
/**
 * 主界面
 * <p/>
 * Created by wzd on 2016/6/16.
 */
public class MainContentController extends BaseController {

    private BGABanner mBanner;
    private ItemModelController mVideo;
    private ItemModelController mMusicTeaching;
    private ItemModelController mMusicKinds;
    private ItemModelController mEducatingRoom;
    private ItemModelController mMusicLevel;
    private ItemModelController mMusicCompetition;

    private String p1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490440294846&di=b144071d10f13692a9f5c9b2ffe4788d&imgtype=0&src=http%3A%2F%2Fpic22.photophoto.cn%2F20120219%2F0039039177997088_b.jpg";
    private String p2 ="https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1490430727&di=1c259ff5eff1fd7b68d9e14b31deb14b&src=http://img1.juimg.com/150117/330522-15011GT54180.jpg";
    private String p3 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490440855651&di=d041639fc5ac1c13c4430b2143fcdfe4&imgtype=0&src=http%3A%2F%2Fst.depositphotos.com%2F1055089%2F627%2Fv%2F950%2Fdepositphotos_6279700-Green-music-theme.jpg";

    private String[] imgs = {p1,p2,p3};

    private String url1 = "https://github.com/NikoSoftware";

    private String url2 = "https://github.com/NikoSoftware/MusicSchool";
    private String url3 = "https://github.com/NikoSoftware";

    private String[] urls = {url1,url2,url3};

    /**
     * 构造器
     *
     * @param context Context
     * @param view    View
     */
    public MainContentController(Context context, View view) {
        super(context, view);
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.activity_main_content, null);
        }
        initViews();
    }

    /**
     * 初始化操作
     */
    @Override
    protected void initViews() {
        // banner
        mBanner = (BGABanner) findViewById(R.id.banner);
        // 设置banner高度
        ViewGroup.LayoutParams lp = mBanner.getLayoutParams();
        lp.width = AppUtils.getScreenWidth(mContext);
        lp.height = 9 * AppUtils.getScreenWidth(mContext) / 16;
        mBanner.setLayoutParams(lp);
        // model
        mVideo = new ItemModelController(mContext, findViewById(R.id.model_video));
        mMusicTeaching = new ItemModelController(mContext, findViewById(R.id.model_music_teaching));
        mMusicKinds = new ItemModelController(mContext, findViewById(R.id.model_music_kinds));
        mEducatingRoom = new ItemModelController(mContext, findViewById(R.id.model_educating_room));
        mMusicLevel = new ItemModelController(mContext, findViewById(R.id.model_music_level));
        mMusicCompetition = new ItemModelController(mContext, findViewById(R.id.model_music_competition));
    }

    public void setupViews() {
        // banner
        mBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                final ImageView iv = (ImageView) view;
                if (model != null && model instanceof BannerModel) {
                    String url = ((BannerModel) model).getImg();
                    new AsyncImageLoader(url, iv).setMediaLoadListener(new MediaLoadListener() {
                        @Override
                        public void onLoad(String path, Bitmap bitmap) {
                            Drawable drawable = iv.getDrawable();
                            if (drawable == null) {
                                iv.setImageBitmap(bitmap);
                            }
                        }
                    }).start();
                }
            }
        });
        mBanner.setOnItemClickListener(new BGABanner.OnItemClickListener() {
            @Override
            public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {

                WebViewActivity.startWebView(mContext,urls[position]);
            }
        });

        ArrayList<BannerModel> listModel = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++) {
            BannerModel bannerModel = new BannerModel();
            bannerModel.setId(""+i);
            bannerModel.setImg(imgs[i]);
            listModel.add(bannerModel);
        }



        mBanner.setData(listModel, null);



        // 卓儿视频
        mVideo.setTitle("卓儿视频")
                .setImage(R.drawable.ic_zhuoer_video)
                .setBackground(R.drawable.selector_video)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ((BaseActivity) mContext).skip(mContext, VideoListActivity.class);


                    }
                });
        // 音乐教学
        mMusicTeaching.setTitle("音乐教学")
                .setImage(R.drawable.ic_zhuoer_music_teaching)
                .setBackground(R.drawable.selector_music_teaching)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((BaseActivity) mContext).skip(mContext, MusicTeachingActivity.class);
                    }
                });
        // 卓儿音乐汇
        mMusicKinds.setTitle("课程订单")
                .setImage(R.drawable.ic_zhuoer_music_kinds)
                .setBackground(R.drawable.selector_music_kinds)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((BaseActivity) mContext).skip(mContext, OrderManageActivity.class);
                         }
                });
        // 卓儿悦教空间
        mEducatingRoom.setTitle("第一印象")
                .setImage(R.drawable.ic_zhuoer_educating_room)
                .setBackground(R.drawable.selector_educating_room)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((BaseActivity) mContext).skip(mContext, PictureActivity.class);
                    }
                });
        // 音乐考级
        mMusicLevel.setTitle("音乐考级")
                .setImage(R.drawable.ic_zhuoer_music_level)
                .setBackground(R.drawable.selector_music_level)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((BaseActivity) mContext).skip(mContext, MusicSignUpActivtiy.class);
                    }
                });
        // 音乐赛事
        mMusicCompetition.setTitle("考级订单")
                .setImage(R.drawable.ic_zhuoer_music_competition)
                .setBackground(R.drawable.selector_music_competition)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,OrderManageActivity.class);
                        intent.putExtra("isGrade",true);
                        startActivity(intent);
                    //    ((BaseActivity) mContext).skip(mContext, OrderManageActivity.class);
                    }
                });





    }




}
