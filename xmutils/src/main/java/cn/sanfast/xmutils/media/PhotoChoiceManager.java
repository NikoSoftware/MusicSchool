package cn.sanfast.xmutils.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import java.io.File;

import cn.sanfast.xmutils.bitmap.helper.StorageHelper;
import cn.sanfast.xmutils.bitmap.utils.BitmapUtil;
import cn.sanfast.xmutils.utils.StringUtil;
import cn.sanfast.xmutils.view.ChoicePopupWindow;

/**
 * 照片选择器
 * <p>
 * Created by wzd on 2016/7/13.
 */
public class PhotoChoiceManager {

    private final String TAG = PhotoChoiceManager.class.getSimpleName();
    private final String IMAGE_TYPE = "image/*";
    private final int REQUEST_IMAGE = 1000;
    private final int REQUEST_PHOTO_ZOOM = 1002;
    private final int REQUEST_PHOTO = 1003;
    private Context mContext;
    private Fragment mFragment;
    private View mView;
    /**
     * 是否需要裁剪图片
     */
    private PhotoZoom mPhotoZoom;
    private String mPhotoPath;
    private String mZoomPath;
    private ChoicePopupWindow mChoicePopupWindow;

    public class PhotoZoom {
        public boolean needZoom = false;
        public int width = 0;
        public int height = 0;

        public PhotoZoom() {
            needZoom = false;
            width = 0;
            height = 0;
        }
    }

    public void setPhotoZoom(int width, int height) {
        mPhotoZoom.needZoom = true;
        mPhotoZoom.width = width;
        mPhotoZoom.height = height;
    }

    /**
     * 多媒体文件选择成功监听
     */
    private MediaSuccessListener mMediaSuccessListener;

    public void setMediaSuccessListener(MediaSuccessListener mediaSuccessListener) {
        mMediaSuccessListener = mediaSuccessListener;
    }

    public interface MediaSuccessListener {
        public void onFileSelected(String filePath, String fileType);
    }

    private OnImageSuccessListener mOnImageSuccessListener;

    public interface OnImageSuccessListener {
        public void onImageSuccess(ImageView imageView, String filePath, String fileType);
    }

    public void setOnIamgeSuccessListener(OnImageSuccessListener onIamgeSuccessListener) {
        mOnImageSuccessListener = onIamgeSuccessListener;
    }

    private ImageView mImageView;

    public void setImageView(ImageView imageView) {
        mImageView = imageView;
    }

    public PhotoChoiceManager(Context context, View view) {
        this.mContext = context;
        this.mView = view;
        initViews();
    }

    public PhotoChoiceManager(Context context, Fragment fragment, View view) {
        this.mContext = context;
        this.mFragment = fragment;
        this.mView = view;
        initViews();
    }

    /**
     * 初始化操作
     */
    protected void initViews() {
        mPhotoZoom = new PhotoZoom();
    }

    /**
     * 多媒体选择框
     */
    public void openMediaDialog() {
        mChoicePopupWindow = new ChoicePopupWindow(
                (Activity) mContext,
                "打开相机",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCamera();
                        mChoicePopupWindow.dismiss();
                    }
                },
                "打开相册",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPhotoAlbum();
                        mChoicePopupWindow.dismiss();
                    }
                });
    }

    public void openCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mPhotoPath = StorageHelper.getInstance().getDirByType(StorageHelper.DIR_TYPE_TEMP) + "/" + System.currentTimeMillis() + ".jpg";
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPhotoPath)));
            if (mFragment != null) {
                mFragment.startActivityForResult(intent, REQUEST_PHOTO);
                /*Log.e(TAG, "fragment.startActivityForResult");*/
                return;
            }
        /*Log.e(TAG, "activity.startActivityForResult");*/
            ((Activity) mContext).startActivityForResult(intent, REQUEST_PHOTO);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void openPhotoAlbum() {
        try {
            Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
            getAlbum.setType(IMAGE_TYPE);
            if (mFragment != null) {
                mFragment.startActivityForResult(getAlbum, REQUEST_IMAGE);
                /*Log.e(TAG, "fragment.startActivityForResult");*/
                return;
            }
        /*Log.e(TAG, "activity.startActivityForResult");*/
            ((Activity) mContext).startActivityForResult(getAlbum, REQUEST_IMAGE);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void startPhotoZoom(Uri uri) {
        String path = BitmapUtil.getRealPathFromURI(mContext, uri);
        if (null == path) {
            Snackbar.make(mView, "无法裁剪图片", Snackbar.LENGTH_SHORT)
                    .setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
            return;
        }

        mZoomPath = StorageHelper.getInstance().getDirByType(StorageHelper.DIR_TYPE_TEMP) + System.currentTimeMillis() + ".jpg";
        Uri zoomUri = Uri.fromFile(new File(mZoomPath));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(path)), IMAGE_TYPE);
        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", mPhotoZoom.width/requestDivisor(mPhotoZoom.width,mPhotoZoom.height));
        intent.putExtra("aspectY", mPhotoZoom.height/requestDivisor(mPhotoZoom.width,mPhotoZoom.height));

        intent.putExtra("outputX", mPhotoZoom.width);
        intent.putExtra("outputY", mPhotoZoom.height);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, zoomUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, REQUEST_PHOTO_ZOOM);
            /*Log.e(TAG, "fragment.startActivityForResult");*/
            return;
        }
        /*Log.e(TAG, "activity.startActivityForResult");*/
        ((Activity) mContext).startActivityForResult(intent, REQUEST_PHOTO_ZOOM);
    }

    /**
     * onActivityResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    if (null != data && null != data.getData()) {
                        if (mPhotoZoom.needZoom) {
                            startPhotoZoom(data.getData());
                        } else {
                            String path = BitmapUtil.getRealPathFromURI(mContext, data.getData());
                            if (mMediaSuccessListener != null && path != null) {
                                mMediaSuccessListener.onFileSelected(path, getFileMimeType(path));
                            }
                            if (mOnImageSuccessListener != null && path != null) {
                                mOnImageSuccessListener.onImageSuccess(mImageView, path, getFileMimeType(path));
                            }
                        }
                    }
                    break;
                case REQUEST_PHOTO_ZOOM:
                    if (null != mMediaSuccessListener) {
                        /*Log.e(TAG, mZoomPath);*/
                        if (null != mZoomPath)
                            mMediaSuccessListener.onFileSelected(mZoomPath, getFileMimeType(mZoomPath));
                        if (mOnImageSuccessListener != null && mZoomPath != null) {
                            mOnImageSuccessListener.onImageSuccess(mImageView, mZoomPath, getFileMimeType(mZoomPath));
                        }
                    }
                    break;
                case REQUEST_PHOTO:
                    if (mPhotoZoom.needZoom) {
                        if (!StringUtil.isEmpty(mPhotoPath)) {
                            startPhotoZoom(Uri.fromFile(new File(mPhotoPath)));
                        }
                    } else {
                        if (mMediaSuccessListener != null) {
                            mMediaSuccessListener.onFileSelected(mPhotoPath, getFileMimeType(mPhotoPath));
                        }
                        if (mOnImageSuccessListener != null && mPhotoPath != null) {
                            mOnImageSuccessListener.onImageSuccess(mImageView, mPhotoPath, getFileMimeType(mPhotoPath));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public static String getFileMimeType(String path) {
        String ret = null;
        if (null != path) {
            ret = MimeTypeMap.getFileExtensionFromUrl(path);
        }
        return ret;
    }

    /**
     * 最大公约数
     * @param m
     * @param n
     * @return
     */
    private int requestDivisor(int m,int n) {
        if (m % n == 0) {
            return n;
        } else {
            return requestDivisor(n,m % n);
        }
    }

}
