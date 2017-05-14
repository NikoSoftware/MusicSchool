package cn.sanfast.xmutils.bitmap.listener;


import android.graphics.Bitmap;

/**
 *
 * 文件下载完成时监听
 * Created by koudejian on 14-1-14.
 *
 */
public interface MediaDownloadListener {
    /**
     * 返回源文件路径和预览图片
     * 如果本身为图片，则是图片文件和bitmap
     * Gif-> gif文件和第一帧预览图
     * 视频-> 视频文件和预览图
     * @param path
     * @param bitmap
     */
    public void onDownloadSuccess(String path, Bitmap bitmap);

    /**
     * 加载失败回调
     * @param url
     */
    public void onDownloadFailed(String url);
}
