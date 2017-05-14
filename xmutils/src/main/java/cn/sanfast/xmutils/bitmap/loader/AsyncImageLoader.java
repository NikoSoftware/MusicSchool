package cn.sanfast.xmutils.bitmap.loader;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

import cn.sanfast.xmutils.bitmap.helper.StorageHelper;
import cn.sanfast.xmutils.bitmap.listener.MediaDownloadListener;
import cn.sanfast.xmutils.bitmap.listener.MediaLoadListener;
import cn.sanfast.xmutils.bitmap.model.ImageSize;
import cn.sanfast.xmutils.utils.StringUtil;

/**
 * 异步加载图片信息
 * png, jpg, gif, 视频
 * 1.本地资源使用ImageLoader负责显示
 * 2.网络资源下载到本地，然后由ImageLoader负责显示
 * 3.主要包含视频和gif数据的缓存策略，任务管理
 */
public class AsyncImageLoader {
    private final String TAG = "AsyncImageLoader";
    private String mUrl = null;
    private ImageView mImageView = null;
    private String mImagePath = null;
    private ImageSize mImageSize = null;
    //多媒体加载
    private MediaLoadListener mMediaLoadListener = null;

    public AsyncImageLoader setMediaLoadListener(MediaLoadListener listener) {
        this.mMediaLoadListener = listener;
        return this;
    }

    //异步加载回调
    private MediaDownloadListener mMediaDownloadListener = new MediaDownloadListener() {
        @Override
        public void onDownloadSuccess(String path, Bitmap bitmap) {
            load(path, bitmap);
        }

        @Override
        public void onDownloadFailed(String url) {
            load(null, null);
//            Log.v(TAG, "onDownloadFailed->" + url);
        }
    };

    //*/
    public AsyncImageLoader(String url, ImageView iv, ImageSize imageSize) {
        this(url, iv);
        mImageSize = imageSize;
    }

    public AsyncImageLoader(String url, ImageView iv) {
        this.mUrl = url;
        this.mImageView = iv;
        /**MD5加密的文件路径**/
        this.mImagePath = StorageHelper.getInstance().getUrlFilePath(StorageHelper.DIR_TYPE_TEMP, mUrl);
    }

    public AsyncImageLoader(String url) {
        this.mUrl = url;
        this.mImagePath = StorageHelper.getInstance().getUrlFilePath(StorageHelper.DIR_TYPE_TEMP, mUrl);
    }

    /**
     * 开始下载图片，下载完成自动加载
     */
    public AsyncImageLoader start() {
        if (!StringUtil.isEmpty(mUrl)) {
            /**
             * 是网络资源
             */
            if (StringUtil.isHttpSource(mUrl)) {
                /**本地文件路径，缓存key**/
                String path_size = StorageHelper.getInstance().getUrlFilePath(StorageHelper.DIR_TYPE_TEMP, mUrl, mImageSize);
                /**1. read image cache in HashMap**/
                if (!LoadMediaFromCache(path_size)) {//failed
                    /**2.1. read image from SD card**/
                    if (!LoadImageFromSDcard(path_size)) {//failed
                        /**2.2 缩略图没有时加载全图**/
                        if (!(mImageSize != null && LoadImageFromSDcard(mImagePath))) {
                            /**3. download image form server**/
                            Log.e(TAG, "3.LoadImageFromNet: " + mImagePath);
                            AsyncLoaderTaskManager.getInstance().add(AsyncLoaderTaskManager.getInstance().getTask(mUrl, mMediaDownloadListener));
                        }
                    }
                }
            }
            /**
             * 是本地资源，直接加载
             */
            else if (StringUtil.isLocalSource(mUrl)) {
                ImageLoader.getInstance().loadImage(mUrl, mImageView, mImageSize);
            } else {
//                Log.e(TAG, "error: source url is illegal --> " + mUrl);
            }
        } else {
//            Log.e(TAG, "error: source url is null...");
        }
        return this;
    }

    /**
     * 加载非图片媒体文件
     * gif, 视频
     *
     * @return
     */
    public AsyncImageLoader loader() {
        if (!StringUtil.isEmpty(mUrl)) {
            if (StringUtil.isHttpSource(mUrl)) {//网络
                //本地文件路径
                String path = StorageHelper.getInstance().getMediaUrlFilePath(StorageHelper.DIR_TYPE_TEMP, mUrl);
                //1. read image from SD card
                if (!LoadImageFromSDcard(path)) {//failed
                    AsyncLoaderTaskManager.getInstance().add(AsyncLoaderTaskManager.getInstance().getTask(mUrl, mMediaDownloadListener));
                }
            } else if (StringUtil.isLocalSource(mUrl)) {//本地文件
                if (mMediaLoadListener != null) {
                    mMediaLoadListener.onLoad(mUrl, null);
                }
            } else {
//                Log.v(TAG, "error: source url is illegal-->" + mUrl);
            }
        } else {
//            Log.v(TAG, "error: source url is null...");
        }
        return this;
    }

    /**
     * 加载内存图片
     *
     * @return
     */
    private Boolean LoadMediaFromCache(String path) {
        // cache文件使用ImageSize
        Bitmap bitmap = ImageLoader.getInstance().getBitmapFromLruCache(path);
        if (bitmap != null) {//cache is exist
            if (mImageView != null) {
                mImageView.setImageBitmap(bitmap);
            }
            if (mMediaLoadListener != null) {
                mMediaLoadListener.onLoad(path, bitmap);
            }
            Log.e(TAG, "1.LoadMediaFromCache: " + path);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 加载SD卡图片
     *
     * @return
     */
    private Boolean LoadImageFromSDcard(String path) {
        File temp = new File(path);
        if (temp.exists()) {//read file into memory if file is exist on SDcard
            load(path, null);
            Log.e(TAG, "2.LoadImageFromSDCard: " + path);
            return true;
        }
        return false;
    }

    /**
     * 加载
     *
     * @param path
     * @param bitmap
     */
    private void load(String path, Bitmap bitmap) {
        if (mImageView != null) {
//            Log.e(TAG, "load -> " + path);
            ImageLoader.getInstance().loadImage(path, mImageView, mImageSize);
        } else {
//            Log.e(TAG, "ImageView is null -> " + path);
        }
        if (bitmap == null && path != null) {
            bitmap = ImageLoader.getInstance().decodeSampledBitmapFromResource(path);
        }
        if (mMediaLoadListener != null) {
            mMediaLoadListener.onLoad(path, bitmap);
        }
    }
}
