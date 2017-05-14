package com.example.niko.musicschool.fragment.picture;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dalong.carview.CardDataItem;
import com.dalong.carview.CardSlidePanel;
import com.example.niko.musicschool.R;
import com.example.niko.musicschool.model.TeacherModel;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 卡片Fragment
 *
 */
@SuppressLint({"HandlerLeak", "NewApi", "InflateParams"})
public class CardFragment extends Fragment {

    private CardSlidePanel.CardSwitchListener cardSwitchListener;

    private List<CardDataItem> dataList = new ArrayList<CardDataItem>();
    public  CardSlidePanel slidePanel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.dalong.carview.R.layout.card_layout, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        slidePanel = (CardSlidePanel) rootView
                .findViewById(com.dalong.carview.R.id.image_slide_panel);
        cardSwitchListener = new CardSlidePanel.CardSwitchListener() {

            @Override
            public void onShow(View cardView,int index) {
                cardView.findViewById(R.id.card_like_icon).setAlpha(0);
                cardView.findViewById(R.id.card_dislike_icon).setAlpha(0);
                Log.d("CardFragment", "正在显示-" + dataList.get(index).userName);
            }

            @Override
            public void onCardVanish(View changedView,int index, int type) {
                Log.d("CardFragment", "正在消失-" + dataList.get(index).userName + " 消失type=" + type);
                switch (type){
                    case 0:
                        Log.d("CardFragment", "不喜欢" );
                        setViewPressed(getActivity().findViewById(R.id.card_left_btn),200);
                        break;
                    case 1:
                        Log.d("CardFragment", "喜欢" );
                        setViewPressed(getActivity().findViewById(R.id.card_right_btn),200);
                        break;
                }
            }

            @Override
            public void onItemClick(View cardView, int index) {
                Log.d("CardFragment", "卡片点击-" + dataList.get(index).userName);
            }

            @Override
            public void onViewPosition(View changedView,float dx, float dy) {
                changedView.findViewById(R.id.card_like_icon).setAlpha(dx);
                changedView.findViewById(R.id.card_dislike_icon).setAlpha(dy);
            }
        };
        slidePanel.setCardSwitchListener(cardSwitchListener);
        prepareDataList();
        slidePanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    public  void setViewPressed(final View view,long time){
        view.setPressed(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setPressed(false);
            }
        },time);

    }

    private void prepareDataList() {

        BmobQuery<TeacherModel> query = new BmobQuery<TeacherModel>();


        query.findObjects(new FindListener<TeacherModel>() {

            @Override
            public void done(List<TeacherModel> courseOrderModel, BmobException e) {
                if (e == null&&courseOrderModel!=null) {

                    for (TeacherModel teacherModel : courseOrderModel) {

                        CardDataItem dataItem = new CardDataItem();
                        dataItem.userName = teacherModel.getTname();
                        dataItem.imagePath = teacherModel.getTimg();
                        dataItem.likeNum = (int) (Math.random() * 10);
                        dataItem.imageNum = (int) (Math.random() * 6);
                        dataItem.subject = teacherModel.getTsubject();
                        dataList.add(dataItem);
                    }

                    slidePanel.fillData(dataList);

                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });


    }

}
