package com.example.niko.musicschool.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.widget.EditText;

import com.example.niko.musicschool.R;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sanfast.xmutils.dialog.CustomDialog;


/**
 * Created by niko on 2016/9/23.
 */
public class AppTool {

    /**
     * 颜色条
     */
    public static final int[] colorList ={R.color.colorPrimary, R.color.colorPrimaryDark,
            android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light};
    /**
     * 限制只能输入字母和数字，默认弹出英文输入法
     */

    public static void setOnlyInputPinYin(final Context context, EditText editText){
        editText.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }
            @Override
            protected char[] getAcceptedChars() {
                char[] data = "qwertyuiopasdfghjklzxcvbnm ".toCharArray();
                return data;
            }
        });
    }


    /**
     * 限制输入字符
     * @param context
     * @param editText
     */
    public static void setOnlyInputEngLish(final Context context, EditText editText){
        editText.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }
            @Override
            protected char[] getAcceptedChars() {
                char[] data = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM ".toCharArray();
                return data;
            }
        });
    }


    /**
     * 限制输入字符
     * @param context
     * @param editText
     */
    public static void setOnlyInputIdcard(final Context context, EditText editText){
        editText.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_NUMBER;
            }
            @Override
            protected char[] getAcceptedChars() {
                char[] data = "0123456789Xx".toCharArray();
                return data;
            }
        });
    }


    /**
     * 限制只能输入非负数价格，默认弹出数字输入法
     */

    public static void setOnlyActivePrice(final Context context, EditText editText){
        editText.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_NUMBER;
            }
            @Override
            protected char[] getAcceptedChars() {
                char[] data = ".0123456789".toCharArray();
                return data;
            }
        });
    }


    /**
     * 将1，2,3 转换为  01，02，03
     * @param time
     * @return
     */
    public static String tansformFillTime(int time){

        return time < 10 ? "0"+time : time+"";
    }

    /**
     * 分转为元
     * @param money
     * @return
     */
    public static String centToYuan(String money){

        if(money == null||money.length()==0||(money.length()==1&&money.equals("0"))){
            return "0";
        }
        if(money.length()==1){
            return "0.0"+money;
        }
        if(money.length()==2){
            return "0."+money;
        }
        StringBuilder strBuilder = new StringBuilder(money);
        if(strBuilder.substring(strBuilder.length()-2,strBuilder.length()).equals("00")){
            return strBuilder.substring(0,strBuilder.length()-2);
        }else {
            strBuilder.insert(strBuilder.length() - 2, ".");
        }
        return strBuilder.toString();
    }

    /**
     * 分转万
     * @param money
     * @return
     */
    public static String centToWan(String money){
        return money.length() <6 ? money : centToYuan(money).substring(0,money.length()-6)+"万";
    }

    public static String yuanToCent(String money){
        float  price= Float.parseFloat(money);
        return String.valueOf((int) (price*100)) ;
    }

    /**
     * 提示弹窗
     * @param mContext
     * @param title
     * @param message
     */
    public static void hintWindows(Context mContext,String title,String message){

        CustomDialog dialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_WITH_TITLE_CANCEL);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton("", null, "确定", null);
        dialog.show();

    }


    /**
     *将年月日转换为具体时间
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static long YMDtoTime(int year,int month,int day){
        Date date = new Date();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdformat.parse(year+"-"+month+"-"+day);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long YMDtoTime(String time){
        Date date = new Date();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdformat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     *将年月日转换为具体时间
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static long YMDTtoTime(int year,int month,int day,String time){
        Date date = new Date();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            date = sdformat.parse(year+"-"+month+"-"+day+" "+time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return 0;
    }



    /**
     * 通过bitmap 获取base64
     * @param bitmap
     * @return
     */
    public static String getBase64ByBitmap(Bitmap bitmap){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte[] bytes=os.toByteArray();
        return  Base64.encodeToString(bytes, Base64.NO_WRAP);
    }


    /**
     * Base64转换为bitmap
     * @param str
     * @return
     */
    public static Bitmap getBitmapByBase(String str){
        byte[] bytes;
        bytes=Base64.decode(str, 0);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 2016-05-06 转 2016年05月06日
     * @param time
     * @return
     */
    public static String YMDtoChinese(String time){
        String[] str = time.split("-");
       return  str[0]+"年"+str[1]+"月"+str[2]+"日";
    }

    /**
     * 捕获转型异常
     * @param data
     * @param initialize
     * @return
     */
    public static int integerParse(String data,int initialize){

        int result;
        if(data==null){
            return initialize;
        }
        try {
            result = Integer.parseInt(data);
        }catch (Exception e){
            return initialize;
        }

        return result;
    }

}
