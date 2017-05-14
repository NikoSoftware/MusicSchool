package cn.sanfast.xmutils.media;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.File;

import cn.sanfast.xmutils.view.ChoicePopupWindow;

/**
 * 视频选择管理
 * <p/>
 * Created by wzd on 2016/11/11.
 */
public class VideoChoiceManager {

    private final String TAG = VideoChoiceManager.class.getSimpleName();
    private final int VIDEO_SHOOT = 1023;
    private final int VIDEO_CHOOSE = 1025;
    private Context mContext;
    private View mView;
    private ChoicePopupWindow mChoicePopupWindow = null;
    private OnVideoChoseListener mOnVideoChoseListener = null;

    public VideoChoiceManager(Context context, View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 操作弹窗
     */
    public void openVideoDialog() {
        mChoicePopupWindow = new ChoicePopupWindow(
                (Activity) mContext,
                "拍摄视频",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shootVideo();
                        mChoicePopupWindow.dismiss();
                    }
                },
                "选择视频",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseVideo();
                        mChoicePopupWindow.dismiss();
                    }
                });
    }

    /**
     * 拍摄视频
     */
    public void shootVideo() {
        /**
         * MediaStore.EXTRA_OUTPUT：设置媒体文件的保存路径。
         * MediaStore.EXTRA_VIDEO_QUALITY：设置视频录制的质量，0为低质量，1为高质量。
         * MediaStore.EXTRA_DURATION_LIMIT：设置视频最大允许录制的时长，单位为毫秒。
         * MediaStore.EXTRA_SIZE_LIMIT：指定视频最大允许的尺寸，单位为byte。
         */
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        ((Activity) mContext).startActivityForResult(intent, VIDEO_SHOOT);
    }

    /**
     * 选择视频
     */
    public void chooseVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        ((Activity) mContext).startActivityForResult(intent, VIDEO_CHOOSE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_SHOOT && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(mContext.getContentResolver(), id, MediaStore.Video.Thumbnails.MICRO_KIND, null);
                cursor.close();
                if (mOnVideoChoseListener != null) {
                    mOnVideoChoseListener.onVideoChose(filePath, bitmap);
                }
            }
        } else if (requestCode == VIDEO_CHOOSE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            File file = getFileByUri(uri);
            String filePath = file.getAbsolutePath();
            // 实例化MediaMetadataRetriever对象
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(filePath);
            // 获得视频第一帧的Bitmap对象
            Bitmap bitmap = mmr.getFrameAtTime();
            if (mOnVideoChoseListener != null) {
                mOnVideoChoseListener.onVideoChose(filePath, bitmap);
            }
        }

    }

    /**
     * 通过Uri获取文件
     *
     * @param uri Uri
     * @return File
     */
    private File getFileByUri(Uri uri) {
        if (uri == null) {
            return null;
        }
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = mContext.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(")
                        .append(MediaStore.Images.ImageColumns.DATA)
                        .append("=")
                        .append("'" + path + "'")
                        .append(")");
                Cursor cur = cr.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA},
                        buff.toString(),
                        null,
                        null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    Log.e(TAG, "temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
            return new File(path);
        } else {
            Log.e(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }

    public void setOnVideoChoseListener(OnVideoChoseListener listener) {
        mOnVideoChoseListener = listener;
    }

    public interface OnVideoChoseListener {
        void onVideoChose(String filePath, Bitmap bitmap);
    }

}
