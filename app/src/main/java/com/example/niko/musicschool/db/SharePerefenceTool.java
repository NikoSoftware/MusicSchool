package com.example.niko.musicschool.db;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by niko on 2017/5/14.
 */

public class SharePerefenceTool {


    /**
     * 储存密码
     * @param context
     * @param password
     */
    public static void saveUser(Context context,String password){

        SharedPreferences sharedPreferences =context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器

        editor.putString("password", password);

        editor.commit();//提交修改
    }

    /**
     * 获取密码
     * @param context
     * @return
     */
    public static String getPassword(Context context){
        SharedPreferences sharedPreferences =context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getString("password","");
    }




}
