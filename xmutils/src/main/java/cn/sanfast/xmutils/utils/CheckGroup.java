package cn.sanfast.xmutils.utils;

import android.view.View;

import java.util.ArrayList;

import cn.sanfast.xmutils.view.CheckImageView;

/**
 * Created by niko on 2016/8/23.
 */
public class CheckGroup {



    /**
    * radiobutton group 设计
    * @auther niko
    * @time 2016/8/23 18:15
    */
    public static void InitRadioImageViewGroup(final ArrayList<CheckImageView> radioImageViews ){
        if(radioImageViews.isEmpty()){
            return;
        }
        Boolean isNotSelectOnly = true;
        for(int i=0;i<radioImageViews.size();i++){
            final CheckImageView radioImageView = radioImageViews.get(i);
            if(radioImageView.get()==true){
                isNotSelectOnly =false;
            }

            radioImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!radioImageView.get()){
                        noSelectAll(radioImageViews);
                        radioImageView.set(true);
                    }
                }
            });

        }

        if(isNotSelectOnly){
            radioImageViews.get(0).set(true);
        }
    }


    /**
    * 所有按钮都不选中
    * @auther niko
    * @time 2016/8/23 18:17
    */
    public static void noSelectAll(ArrayList<CheckImageView> radioImageViews){

        for(CheckImageView radioImageView:radioImageViews){
            radioImageView.set(false);
        }
    }

    public static int getSelectIndex(ArrayList<CheckImageView> radioImageViews) {

        for (int i = 0; i < radioImageViews.size(); i++) {

            if(radioImageViews.get(i).get()){
                return i;
            }
        }


        return -1;
    }

    /**
     * 单一选中
     * @param checkImageView
     */
    public static void initSingleRadioImageView(final CheckImageView checkImageView){
        checkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkImageView.get()){
                    checkImageView.set(false);
                }else {
                    checkImageView.set(true);
                }
            }
        });


    }


}
