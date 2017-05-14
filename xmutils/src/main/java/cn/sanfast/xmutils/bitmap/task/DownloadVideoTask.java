package cn.sanfast.xmutils.bitmap.task;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import cn.sanfast.xmutils.bitmap.loader.ImageLoader;
import cn.sanfast.xmutils.bitmap.helper.StorageHelper;
import cn.sanfast.xmutils.bitmap.listener.MediaDownloadListener;

/**
 * Created by koudejian on 15-2-6.
 */
public class DownloadVideoTask extends DownloadFileTask {

    public DownloadVideoTask(String url, MediaDownloadListener listener) {
        super(url, listener);
    }

    @Override
    protected void dealWithFile(byte[] data) {
        //1.存储文件
        String path = StorageHelper.getInstance().getMediaUrlFilePath(StorageHelper.DIR_TYPE_VIDEO, mUrl);
        if (saveFile(path, data)) {
            //2.生成缩略图，并保存
            mBitmap = createVideoThumbnail(path);
            mPath = StorageHelper.getInstance().getUrlFilePath(StorageHelper.DIR_TYPE_VIDEO, mUrl);
            ImageLoader.getInstance().saveBitMap(mBitmap, mPath);
        }
    }

    /**
     * 获取视频的某一帧图片
     *
     * @param filePath
     * @return
     */
    public static Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(2L);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        return bitmap;
    }
}
