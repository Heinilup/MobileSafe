package ikabi.com.mobilesafe.business.image;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import ikabi.com.mobilesafe.business.model.FileType;
import ikabi.com.mobilesafe.business.model.TFileInfo;
import ikabi.com.mobilesafe.utils.FileUtils;
import ikabi.com.mobilesafe.utils.Logger;
import ikabi.com.mobilesafe.utils.StringUtils;

/**
 * 读取本地图片
 */
public class ImageInterface {

    private Logger logger = Logger.getLogger(ImageInterface.class);


    private Context mContext;

    public ImageInterface(Context context) {
        this.mContext = context;
    }

    /**
     * 查找所有图片
     */
    public List<TFileInfo> searchImage() {

        List<TFileInfo> list = null;
        try {
            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = mContext.getContentResolver();
            Cursor cursor = mContentResolver.query(imageUri, null, null, null, null);
            list = readImageCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
            logger.e(e.getMessage());
        }
        return list;

    }

    /**
     * 根据关键字查找图片
     */
    public List<TFileInfo> searchImage(final String searchKey) {
        List<TFileInfo> list = null;
        try {
            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = mContext.getContentResolver();
            Cursor cursor = mContentResolver.query(imageUri, null, MediaStore.Images.Media.DISPLAY_NAME + " like '%" + searchKey + "%'", null, null);
            list = readImageCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
            logger.e(e.getMessage());
        }
        return list;
    }

    /**
     * 读取图片cursor内容
     */
    List<TFileInfo> readImageCursor(Cursor cursor) {
        List<TFileInfo> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));// 文件名
                String file_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));// 路径
                long create_time = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));// 创建时间
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));// 文件大小

                TFileInfo image_file = new TFileInfo();
                image_file.setTaskId(FileUtils.buildTaskId());
                image_file.setName(display_name);
                image_file.setPath(file_path);
                image_file.setCreateTime(FileUtils.parseTimeToYMD(create_time));
                image_file.setLength(size);
                image_file.setFullName(display_name);
                if (!StringUtils.isEmpty(display_name) && display_name.lastIndexOf(".") != -1) {
                    image_file.setExtension(display_name.substring(display_name.lastIndexOf(".")));
                } else {
                    image_file.setExtension("");
                }
                image_file.setFileType(FileType.Image);
                list.add(image_file);
            }
            cursor.close();
        }
        return list;
    }
}
