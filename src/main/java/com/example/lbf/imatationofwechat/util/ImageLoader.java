package com.example.lbf.imatationofwechat.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by lbf on 2016/7/12.
 */
public class ImageLoader {
    private static final int THREAD_COUNT = 1;
    public static final int TYPE_FIFO = 1;
    public static final int TYPE_LIFO = 2;
    private static ImageLoader loader;
    private ExecutorService mThreadPool;
    private Handler mHandler;
    private Handler poolThreadHandler;
    private LinkedList<Runnable> mTasks;
    private Semaphore initSemaphore;
    private Semaphore taskSemaphore;
    private int type;
    LruCache<String,Bitmap> mLruCache;
    private ImageLoader(int threadCount, int type) {
        this.type = type;
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        initSemaphore = new Semaphore(1);
        taskSemaphore = new Semaphore(1);
        mTasks = new LinkedList<>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        mLruCache = new LruCache<String,Bitmap>(maxMemory/4){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
        try {
            initSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                poolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        try {
                            taskSemaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mThreadPool.execute(getTask());
                    }
                };
                initSemaphore.release();
                Looper.loop();
            }
        }).start();
    }

    private Runnable getTask() {
        if(type == TYPE_FIFO){
            return mTasks.removeFirst();
        }else if(type == TYPE_LIFO){
            return mTasks.removeLast();
        }
        return null;
    }

    public static ImageLoader getInstance(){
        if(loader == null){
            synchronized (ImageLoader.class){
                if(loader == null){
                    loader = new ImageLoader(THREAD_COUNT,TYPE_LIFO);
                }
            }
        }
        return loader;
    }

    private synchronized void addTask(Runnable task){
        mTasks.addLast(task);
        if(poolThreadHandler == null){
            try {
                initSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        poolThreadHandler.sendEmptyMessage(0);
    }

    public void loadImage(final String path, final ImageView imageView){
        imageView.setTag(path);
        Bitmap bitmap = mLruCache.get(path);
        if(mHandler == null){
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    ImageBean bean = (ImageBean) msg.obj;
                    ImageView mImageView = bean.imageView;
                    Bitmap mBitmap = bean.bitmap;
                    String path = bean.path;
                    if(path.equals(mImageView.getTag())){
                        if(mBitmap!=null){
//                            LogUtil.i("mImageView.width"+mImageView.getWidth());
//                            LogUtil.i("bitmap.width"+mBitmap.getWidth());
//                            LogUtil.i("mImageView.height"+mImageView.getHeight());
//                            LogUtil.i("bitmap.height"+mBitmap.getHeight());
                            mImageView.setImageBitmap(mBitmap);
                        }

                    }
                }
            };
        }
        final Message message = Message.obtain();
        if(bitmap == null){
            addTask(new Runnable() {
                @Override
                public void run() {
                    ImageSize imageSize = getImageSize(imageView);
                    Bitmap bitmap = decodeBitmapFromPath(path,imageSize.width,imageSize.height);
                    if(bitmap !=null){
                        mLruCache.put(path,bitmap);
                    }else{
                        LogUtil.i("***bitmap = null***");
                    }

                    ImageBean bean = new ImageBean(imageView,path,bitmap);
                    message.obj = bean;
                    mHandler.sendMessage(message);
                    taskSemaphore.release();
                }
            });
        }else{
            ImageBean bean = new ImageBean(imageView,path,bitmap);
            message.obj = bean;
            mHandler.sendMessage(message);
        }
    }

    private ImageSize getImageSize(ImageView imageView) {
        ImageSize imageSize;
        DisplayMetrics metrics = imageView.getResources().getDisplayMetrics();
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int width = imageView.getWidth();
        if(width <= 0){
            width = params.width;
        }
        if(width <= 0){
            width = getFieldValue(imageView,"mMaxWidth");
        }
        if(width<=0){
            width = metrics.widthPixels;
        }
        int height = imageView.getHeight();
        if(height <= 0){
            height = params.height;
        }
        if(height <= 0){
            height = getFieldValue(imageView,"mMaxHeight");
        }
        if(height<=0){
            height = metrics.heightPixels;
        }
        imageSize = new ImageSize(width,height);
        LogUtil.i("WIDTH = "+width);
        LogUtil.i("HEIGHT = "+height);
        return imageSize;
    }

    private int getFieldValue(ImageView imageView, String name) {
        int result = 0;
        try {
            Field field = ImageView.class.getDeclaredField(name);
            field.setAccessible(true);
            result = field.getInt(imageView);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap decodeBitmapFromPath(String path,int reqWidth, int reqHeight) {
        Bitmap bitmap;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,opts);
        opts.inSampleSize = calculateInSampleSize(opts,reqWidth,reqHeight);
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path,opts);
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options opts, int reqWidth, int reqHeight) {
        int outWidth = opts.outWidth;
        int outHeight = opts.outHeight;
        int inSampleSize = 1;

        if (outHeight > reqHeight || outWidth > reqWidth) {

            final int halfHeight = outHeight / 2;
            final int halfWidth = outWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    class ImageSize{
        int width;
        int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    class ImageBean{
        ImageView imageView;
        String path;
        Bitmap bitmap;

        public ImageBean(ImageView imageView, String path, Bitmap bitmap) {
            this.imageView = imageView;
            this.path = path;
            this.bitmap = bitmap;
        }
    }
}

