package ikabi.com.mobilesafe.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.XFileApplication;
import ikabi.com.mobilesafe.business.model.TFileInfo;


/**
 * 图片加载
 */
public class ImageLoader {

    private static ImageLoader inst = null;

    //创建cache
    private LruCache<String, Bitmap> lruCache;

    public static ImageLoader getInstance() {
        if (inst == null) {
            inst = new ImageLoader();
        }
        return inst;
    }

    public ImageLoader() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取最大的运行内存
        int maxSize = maxMemory / 4;//拿到缓存的内存大小 35
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //这个方法会在每次存入缓存的时候调用
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                    return value.getByteCount();
                } else {
                    return value.getRowBytes() * value.getHeight();
                }
            }
        };
    }

    public void displayThumb(ImageView imageView, TFileInfo tFileInfo) {
        Bitmap bitmap = getCache(tFileInfo.getPath());
        if (bitmap == null) {
            imageView.setTag(tFileInfo.getPath());
            new LoadImageAsync(imageView, tFileInfo).execute();
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    private void addCache(String path, Bitmap bitmap) {
        if (getCache(path) == null) {
            //当前地址没有缓存时，就添加
            lruCache.put(path, bitmap);
        }
    }

    private Bitmap getCache(String path) {
        return lruCache.get(path);
    }

    class LoadImageAsync extends AsyncTask<Void, Void, Bitmap> {

        private ImageView imageView;
        private TFileInfo tFileInfo;

        public LoadImageAsync(ImageView imageView, TFileInfo tFileInfo) {
            this.imageView = imageView;
            this.tFileInfo = tFileInfo;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            TFileInfo file = tFileInfo;
            Bitmap bitmap = null;
            String file_path = file.getPath();
            if (getCache(file_path) == null) {
                switch (file.getFileType()) {
                    case Image:
                        bitmap = loadImageThumb(file_path, 90, 90);
                        break;
                    case Video:
                        bitmap = loadVideoThumb(file_path, 90, 90);
                        break;
                    case Apk:
                        bitmap = loadApkThumb(file_path);
                        break;
                }
                if (bitmap != null) {
                    addCache(file_path, bitmap);
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null && tFileInfo.getPath().equals(imageView.getTag().toString())) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.mipmap.data_folder_documents_placeholder);
            }
        }
    }

    Bitmap loadVideoThumb(final String path, int width, int height) {
        return getVideoThumbnail(path, width, height, MediaStore.Images.Thumbnails.MICRO_KIND);
    }

    Bitmap loadImageThumb(final String path, int width, int height) {
        return getImageThumbnail(path, width, height);
    }

    Bitmap loadApkThumb(String apkPath) {
        PackageManager pm = XFileApplication.context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                Drawable drawable = appInfo.loadIcon(pm);
                BitmapDrawable bd = (BitmapDrawable) drawable;
                Bitmap bitmap = bd.getBitmap();
                return bitmap;
            } catch (OutOfMemoryError e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    private Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


}
