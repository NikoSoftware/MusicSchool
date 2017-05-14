package cn.sanfast.xmutils.bitmap.task;

import java.io.IOException;

import cn.sanfast.xmutils.bitmap.helper.StorageHelper;
import cn.sanfast.xmutils.bitmap.listener.MediaDownloadListener;
import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by koudejian on 15-2-6.
 */
public class DownloadGifTask extends DownloadFileTask {

    @Override
    protected void dealWithFile(byte[] data) {
        //1.存储文件
        mPath = StorageHelper.getInstance().getMediaUrlFilePath(StorageHelper.DIR_TYPE_GIF, mUrl);
        if (saveFile(mPath, data)) {

            String path = StorageHelper.getInstance().getUrlFilePath(StorageHelper.DIR_TYPE_GIF, mUrl);
            saveFile(path, data);
            //*//此段代码有问题，gif抽取出的图片貌似不能缓存
            //2.生成缩略图，并保存
            GifDrawable gifDrawable = null;
            try {
                gifDrawable = new GifDrawable(data);
                gifDrawable.seekTo(2);
                mBitmap = gifDrawable.getCurrentFrame();
//                String path = StorageHelper.getInstance().getUrlFilePath(StorageHelper.DIR_TYPE_TEMP, mUrl);
//                ImageLoader.getInstance().saveBitMap(mBitmap, path);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (gifDrawable != null) {
                    gifDrawable.recycle();
                    gifDrawable = null;
                }
            }
            //*/
        }
    }

    public DownloadGifTask(String url, MediaDownloadListener listener) {
        super(url, listener);
    }
}
