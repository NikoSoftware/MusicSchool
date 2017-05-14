package cn.sanfast.xmutils.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.math.BigDecimal;

/**
 * 缓存管理
 * <p/>
 * Created by wzd on 2016/9/22.
 */
public final class CacheUtil {

    public static final String APP_DIR = "/Zhuoer/";

    /**
     * 缓存大小
     *
     * @param context Context
     * @return String
     */
    public static String getCacheSizeFormat(Context context) {
        return sizeFormat(getCacheSize(context));
    }

    /**
     * 缓存大小
     * @param context Context
     * @return long
     */
    public static long getCacheSize(Context context) {
        long size = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            size += getFolderSize(context.getExternalCacheDir());
            File file = new File(Environment.getExternalStorageDirectory().toString() + APP_DIR);
            size += getFolderSize(file);
        }
        return size;
    }

    /**
     * 清空缓存
     *
     * @param context
     */
    public static void cleanAllCache(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanSDCardCache();
    }

    /**
     * 清空内部缓存
     *
     * @param context Context
     */
    public static void cleanInternalCache(Context context) {
        File file = context.getCacheDir();
        if (file != null) {
            deleteFolderFile(file.getAbsolutePath(), true);
        }
    }

    /**
     * 清空外部缓存
     *
     * @param context Context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                deleteFolderFile(file.getAbsolutePath(), true);
            }
        }
    }

    /**
     * 清空自定义缓存
     */
    public static void cleanSDCardCache() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFolderFile(Environment.getExternalStorageDirectory().toString() + APP_DIR, true);
        }
    }

    /**
     * 获取文件大小
     *
     * @param file File
     * @return long
     */
    public static long getFolderSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        Log.e("CacheUtil", "path " + file.getAbsolutePath());
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            Log.e("CacheUtil", e.toString());
        }
        return size;
    }

    /**
     * 单位转换
     *
     * @param size double
     * @return String
     */
    public static String sizeFormat(double size) {
        /**Byte**/
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            BigDecimal resultB = new BigDecimal(Double.toString(kiloByte));
            return resultB.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " KB";
        }
        /**KB**/
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal resultKB = new BigDecimal(Double.toString(kiloByte));
            return resultKB.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " KB";
        }
        /**MB**/
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal resultMB = new BigDecimal(Double.toString(megaByte));
            return resultMB.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " MB";
        }
        /**GB**/
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal resultGB = new BigDecimal(Double.toString(gigaByte));
            return resultGB.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " GB";
        }
        /**TB**/
        BigDecimal resultTB = new BigDecimal(Double.toString(teraBytes));
        return resultTB.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " TB";
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param filePath String
     * @param delete   boolean
     */
    public static void deleteFolderFile(String filePath, boolean delete) {
        if (filePath == null || "".equals(filePath) || "null".equals(filePath)) {
            return;
        }
        try {
            File file = new File(filePath);
            // 如果下面还有文件
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (delete) {
                // 如果是文件，删除
                if (!file.isDirectory()) {
                    file.delete();
                }
                // 目录
                else {
                    // 目录下没有文件或者目录，删除
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("CacheUtil", e.toString());
        }
    }

}
