package cn.sanfast.xmutils.bitmap.model;

/**
 * 网络图片描述
 * Created by koudejian on 14-1-14.
 */
public class ImageModel {
    private int mHeight = 0;
    private int mWidth = 0;
    private String mUrl = "";
    //是否需要裁剪
    private boolean mIsNeedCut = false;

    public ImageModel(String url) {
        this.mUrl = url;
        this.mIsNeedCut = false;
    }

    public ImageModel(String url, int width, int height) {
        this.mUrl = url;
        this.mWidth = width;
        this.mHeight = height;
        this.mIsNeedCut = true;
    }

    //get methods
    public int getHeight() {
        return this.mHeight;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public boolean isCut() {
        return this.mIsNeedCut;
    }
}
