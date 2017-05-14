package cn.sanfast.xmutils.bitmap.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import cn.sanfast.xmutils.bitmap.helper.StorageHelper;
import cn.sanfast.xmutils.bitmap.model.ImageSize;
import cn.sanfast.xmutils.bitmap.task.DownloadVideoTask;
import cn.sanfast.xmutils.bitmap.zoom.MiniBitmap;
import cn.sanfast.xmutils.utils.MD5;
import cn.sanfast.xmutils.utils.StringUtil;

/**
 * 本地图片加载
 */
public class ImageLoader {
    private final String TAG = "ImageLoader";
    /**
     * 图片缓存的核心类
     * 所有图片的缓存核心
     */
    private LruCache<String, SoftReference<Bitmap>> mLruCache;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    /**
     * 线程池的线程数量，默认为1
     */
    private int mThreadCount = 1;
    /**
     * 队列的调度方式
     */
    private Type mType = Type.LIFO;
    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTasks;
    /**
     * 轮询的线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHander;

    /**
     * 运行在UI线程的handler，用于给ImageView设置图片
     */
    private Handler mHandler;

    /**
     * 引入一个值为1的信号量，防止mPoolThreadHander未初始化完成
     */
    private volatile Semaphore mSemaphore = new Semaphore(0);

    /**
     * 引入一个值为1的信号量，由于线程池内部也有一个阻塞线程，防止加入任务的速度过快，使LIFO效果不明显
     */
    private volatile Semaphore mPoolSemaphore;

    private static ImageLoader mInstance;

    /**
     * 队列的调度方式
     *
     * @author zhy
     */
    public enum Type {
        FIFO, LIFO
    }


    /**
     * 单例获得该实例对象
     *
     * @return
     */
    public static ImageLoader getInstance() {

        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(3, Type.LIFO);
                }
            }
        }
        return mInstance;
    }

    private ImageLoader(int threadCount, Type type) {
        init(threadCount, type);
    }

    private void init(int threadCount, Type type) {
        // loop thread
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();

                mPoolThreadHander = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        mThreadPool.execute(getTask());
                        try {
                            mPoolSemaphore.acquire();
                        } catch (InterruptedException e) {
                        }
                    }
                };
                // 释放一个信号量
                mSemaphore.release();
                Looper.loop();
            }
        };
        mPoolThread.start();

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mLruCache = new LruCache<String, SoftReference<Bitmap>>(cacheSize);

        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mPoolSemaphore = new Semaphore(threadCount);
        mTasks = new LinkedList<Runnable>();
        mType = type == null ? Type.LIFO : type;

    }

    /**
     * 加载图片
     *
     * @param path
     * @param imageView
     */
    public void loadImage(String path, ImageView imageView) {
        loadImage(path, imageView, null);
    }

    /**
     * 加载指定尺寸的图片
     *
     * @param path
     * @param imageView
     * @param imageSize
     */
    public void loadImage(final String path, final ImageView imageView, final ImageSize imageSize) {
        if (imageView == null || path == null) {
//            Log.e(TAG, "load Image null");
            return;
        }
        // set tag
        imageView.setTag(path);
        // UI线程
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                    ImageView imageView = holder.imageView;
                    Bitmap bm = holder.bitmap;
                    String path = holder.path;
                    if (path.equals(imageView.getTag()) && bm != null) {
//                      //Log.e.e(TAG, "loadImage -> " + imageView.getId() + "===" + path);
                        imageView.setImageBitmap(bm);
                    }
                }
            };
        }
        final String new_path = StorageHelper.getInstance().getUrlFilePath(StorageHelper.DIR_TYPE_TEMP, path, imageSize);
        final Bitmap bm = getBitmapFromLruCache(new_path);
        if (bm != null) {
            ImgBeanHolder holder = new ImgBeanHolder();
            holder.bitmap = bm;
            holder.imageView = imageView;
            holder.path = path;
            Message message = Message.obtain();
            message.obj = holder;
            mHandler.sendMessage(message);
        } else {
            addTask(new Runnable() {
                @Override
                public void run() {
                    String cache_path = path;
                    Bitmap bitmap = null;
                    try {
//                      bitmap = BitmapFactory.decodeFile(path, getBitmapOption(2));
                        ImageSize tempImageSize = getImageViewWidth(imageView);
                        if (StringUtil.isVideaUrl(path)) {//视频，本地图片
                            bitmap = DownloadVideoTask.createVideoThumbnail(path);
                        } else {
                            bitmap = decodeSampledBitmapFromResource(path, tempImageSize.width, tempImageSize.height);
                        }
                        if (imageSize != null) {
                            cache_path = new_path;
                            bitmap = MiniBitmap.cutBitmap(bitmap, imageSize.width, imageSize.height);
                            ImageLoader.getInstance().saveBitMap(bitmap, new_path);
                        }
                        if (bitmap != null) {
                            ImageLoader.getInstance().addBitmapToLruCache(cache_path, bitmap);
                        }
                    } catch (Exception e) {
                        //Log.eog.e(TAG, e.toString());
                    }
                    ImgBeanHolder holder = new ImgBeanHolder();
                    holder.bitmap = bitmap;
                    holder.imageView = imageView;
                    holder.path = path;
                    Message message = Message.obtain();
                    message.obj = holder;
                    mHandler.sendMessage(message);
                    mPoolSemaphore.release();
                }
            });
        }

    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    /**
     * 添加一个任务
     *
     * @param runnable
     */
    private synchronized void addTask(Runnable runnable) {
        try {
            // 请求信号量，防止mPoolThreadHander为null
            if (mPoolThreadHander == null)
                mSemaphore.acquire();
        } catch (InterruptedException e) {
        }
        mTasks.add(runnable);

        mPoolThreadHander.sendEmptyMessage(0x110);
    }

    /**
     * 取出一个任务
     *
     * @return
     */
    private synchronized Runnable getTask() {
        if (mType == Type.FIFO) {
            return mTasks.removeFirst();
        } else if (mType == Type.LIFO) {
            return mTasks.removeLast();
        }
        return null;
    }

    /**
     * 单例获得该实例对象
     *
     * @return
     */
    public static ImageLoader getInstance(int threadCount, Type type) {

        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }


    /**
     * 根据ImageView获得适当的压缩的宽和高
     *
     * @param imageView
     * @return
     */
    private ImageSize getImageViewWidth(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        final DisplayMetrics displayMetrics = imageView.getContext()
                .getResources().getDisplayMetrics();
        final LayoutParams params = imageView.getLayoutParams();

        int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView
                .getWidth(); // Get actual image width
        if (width <= 0)
            width = params.width; // Get layout width parameter
        if (width <= 0)
            width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
        if (width <= 0)
            width = displayMetrics.widthPixels;
        int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView
                .getHeight(); // Get actual image height
        if (height <= 0)
            height = params.height; // Get layout height parameter
        if (height <= 0)
            height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check
        if (height <= 0)
            height = displayMetrics.heightPixels;
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;

    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     */
    public Bitmap getBitmapFromLruCache(String key) {
        key = MD5.md5Encode(key);
        Bitmap temp = null;
        SoftReference<Bitmap> softBitmap = mLruCache.get(key);
        if (softBitmap != null) {
            temp = softBitmap.get();
        }
        //Log.e/Log.e("koudejian", "mLruCache get size->" + mLruCache.size() + "===" + key + "===" + temp );
        return temp;
    }

    /**
     * 往LruCache中添加一张图片
     *
     * @param key
     * @param bitmap
     */
    public void addBitmapToLruCache(String key, Bitmap bitmap) {
        key = MD5.md5Encode(key);
        //Log.e //Log.e("koudejian", "mLruCache put size->" + mLruCache.size() + "===" + key + "===" + bitmap);
        if (getBitmapFromLruCache(key) == null) {
            if (bitmap != null) {
                SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
                mLruCache.put(key, softBitmap);
            }
        }
    }

    /**
     * 计算inSampleSize，用于压缩图片
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // 源图片的宽度
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
//        inSampleSize = BitmapUtil.computeSampleSize(options, -1, reqWidth * reqHeight);
        return inSampleSize;
    }

    /**
     * 根据计算的inSampleSize，得到压缩后图片
     *
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSampledBitmapFromResource(String pathName,
                                                  int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        try {
            // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
//            Log.e(TAG, pathName + "===1==" + reqWidth + "====" + reqHeight);
            BitmapFactory.decodeFile(pathName, options);
            // 调用上面定义的方法计算inSampleSize值
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);
            // 使用获取到的inSampleSize值再次解析图片
            options.inJustDecodeBounds = false;
//            Log.e(TAG, pathName + "===2==" + reqWidth + "====" + reqHeight);
            bitmap = BitmapFactory.decodeFile(pathName, options);
            if (bitmap != null) {
//                Log.e(TAG, bitmap.getHeight() + "," + bitmap.getWidth());
            }
        } catch (OutOfMemoryError error) {
            Log.e(TAG, error.toString());
        }
        return bitmap;
    }

    public Bitmap decodeSampledBitmapFromResource(String pathName) {
        Bitmap bitmap = null;
        try {
            // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
//        Log.e("koudjiean", pathName + "===1==" + reqWidth + "====" +  reqHeight);
            BitmapFactory.decodeFile(pathName, options);
            // 调用上面定义的方法计算inSampleSize值
            options.inSampleSize = 2;
            // 使用获取到的inSampleSize值再次解析图片
            options.inJustDecodeBounds = false;
//            Log.e("koudjiean", pathName + "===2==" + reqWidth + "====" + reqHeight);
            bitmap = BitmapFactory.decodeFile(pathName, options);
            if (bitmap != null) {
                //Log.e("koudejian", bitmap.getHeight() + "," + bitmap.getWidth());
            }
        } catch (OutOfMemoryError error) {
            Log.e(TAG, error.toString());
        }
        return bitmap;
    }

    private class ImgBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    /**
     * 反射获得ImageView设置的最大宽度和高度
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
        }
        return value;
    }

    /**
     * 保存图片
     *
     * @param bitmap
     * @param fileName
     * @throws IOException
     */
    public void saveBitMap(Bitmap bitmap, String fileName) {
        File myFile = new File(fileName);
        if (myFile.exists()) {
            return;
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myFile));
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                Log.e(TAG, "saveBitMap:" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
