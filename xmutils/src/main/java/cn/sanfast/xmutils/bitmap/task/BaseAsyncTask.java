package cn.sanfast.xmutils.bitmap.task;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by koudejian on 15-2-6.
 */
public abstract class BaseAsyncTask extends AsyncTask<Void, Void, byte[]> {

    public interface DownloadFileListener {
        public void notifyBeginDownload(String url);

        public void notifyDownloaded(byte[] data, BaseAsyncTask task);
    }

    protected DownloadFileListener mDownloadFileListener;

    public void setDownloadFileListener(DownloadFileListener downloadFileListener) {
        this.mDownloadFileListener = downloadFileListener;
    }

    /**
     * 保存文件
     *
     * @param path
     * @param data
     */
    public static boolean saveFile(String path, byte[] data) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(path));
            dataOutputStream.write(data);
            dataOutputStream.close();
//            Log.e("BaseAsyncTask", "saveFile --> " + path);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return false;
    }
}
