package cn.sanfast.xmutils.bitmap.task;

import android.graphics.BitmapFactory;

import cn.sanfast.xmutils.bitmap.helper.StorageHelper;
import cn.sanfast.xmutils.bitmap.listener.MediaDownloadListener;
import cn.sanfast.xmutils.bitmap.utils.BitmapUtil;

/**
 * Created by koudejian on 15-2-6.
 */
public class DownloadImageTask extends DownloadFileTask {

    private final String TAG = "DownloadImageTask";

    @Override
    protected void dealWithFile(byte[] data) {
        /**
         * 1. 存储文件
         */
        mPath = StorageHelper.getInstance().getUrlFilePath(StorageHelper.DIR_TYPE_TEMP, mUrl);
        /**保存文件到SD卡**/
        if (saveFile(mPath, data)) {
            /**
             * 2. 生成缩略图，并保存
             */
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            options.inSampleSize = BitmapUtil.computeSampleSize(options, -1, 1280 * 720);
            options.inJustDecodeBounds = false;
            mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
//            if(mBitmap == null){//缩略图生成失败时
//                mBitmap = BitmapFactory.decodeFile(mPath, getBitmapOption(2));
//            }
        }
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public DownloadImageTask(String url, MediaDownloadListener listener) {
        super(url, listener);
    }
}
