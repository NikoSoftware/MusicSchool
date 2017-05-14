package cn.sanfast.xmutils.bitmap.loader;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.sanfast.xmutils.bitmap.listener.MediaDownloadListener;
import cn.sanfast.xmutils.bitmap.task.BaseAsyncTask;
import cn.sanfast.xmutils.bitmap.task.DownloadFileTask;
import cn.sanfast.xmutils.bitmap.task.DownloadGifTask;
import cn.sanfast.xmutils.bitmap.task.DownloadImageTask;
import cn.sanfast.xmutils.bitmap.task.DownloadVideoTask;
import cn.sanfast.xmutils.utils.MD5;
import cn.sanfast.xmutils.utils.StringUtil;

/**
 * 异步加载图片频次控制器
 * 避免oom
 * Created by koudejian on 14-2-20.
 */
public class AsyncLoaderTaskManager {

    private final String TAG = "AsyncLoaderTaskManager";
    private static AsyncLoaderTaskManager INSTANCE = null;
    /**
     * 记录所有正在下载或等待下载的任务。
     */
    private HashMap<String, DownloadFileTask> mTaskList = null;
    private final int TOTAL_TASK_NUM = 8;
    private BaseAsyncTask.DownloadFileListener mDownloadFileListener = new BaseAsyncTask.DownloadFileListener() {
        @Override
        public void notifyBeginDownload(String url) {

        }

        @Override
        public void notifyDownloaded(byte[] data, BaseAsyncTask task) {
            remove(task);
            data = null;
        }
    };

    private AsyncLoaderTaskManager() {
        mTaskList = new HashMap<String, DownloadFileTask>();
    }

    /**
     * 移除任务
     *
     * @param downloadImageTask
     */
    private void remove(AsyncTask downloadImageTask) {
        mTaskList.remove(((DownloadFileTask) downloadImageTask).getKey());
    }

    public static synchronized AsyncLoaderTaskManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AsyncLoaderTaskManager();
        }
        return INSTANCE;
    }

    /**
     * 添加任务并自动开始
     *
     * @param downloadImageTask
     */
    public void add(DownloadFileTask downloadImageTask) {
        if (downloadImageTask == null) {
            return;
        }
        mTaskList.put(downloadImageTask.getKey(), downloadImageTask);
        downloadImageTask.setDownloadFileListener(mDownloadFileListener);
        /*
        if(TOTAL_TASK_NUM <= mTaskList.size()){
            Iterator iter = mTaskList.entrySet().iterator();
            while (iter.hasNext()) {
                iter = mTaskList.entrySet().iterator();
                Map.Entry entry = (Map.Entry) iter.next();
                mTaskList.get(entry.getKey()).cancel(true);
                mTaskList.remove(entry.getKey());
                break;
            }
        }
        //*/
        downloadImageTask.execute();
        ////Log.e("koudejian", mTaskList.size() + "==add one task:" + downloadImageTask.toString());
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void clear() {
        if (mTaskList != null) {
            Iterator iter = mTaskList.entrySet().iterator();
            while (iter.hasNext()) {
                iter = mTaskList.entrySet().iterator();
                Map.Entry entry = (Map.Entry) iter.next();
                mTaskList.remove(entry.getKey());
            }
        }
    }

    /**
     * 文件下载任务
     *
     * @param url      路径
     * @param listener
     * @return
     */
    public DownloadFileTask getTask(String url, MediaDownloadListener listener) {
        /**
         * HashMap中已包含该任务
         */
        if (mTaskList.containsKey(MD5.md5Encode(url))) {
            /*return null;*/ // 暂时屏蔽，可以两个线程同时下载一个任务
        }
        DownloadFileTask task = null;
        /**GIF**/
        if (StringUtil.isGifUrl(url)) {
            task = new DownloadGifTask(url, listener);
        }
        /**音频、视频**/
        else if (StringUtil.isVideaUrl(url)) {
            task = new DownloadVideoTask(url, listener);
        }
        /**图片**/
        else {
            task = new DownloadImageTask(url, listener);
        }
        return task;
    }
}
