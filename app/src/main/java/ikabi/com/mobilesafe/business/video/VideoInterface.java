package ikabi.com.mobilesafe.business.video;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ikabi.com.mobilesafe.business.model.TFileInfo;
import ikabi.com.mobilesafe.utils.FileUtils;

/**
 * 视频查询接口
 */
public class VideoInterface {

    private Logger logger = Logger.getLogger(VideoInterface.class);

    private Context mContext;

    public VideoInterface(Context context) {
        this.mContext = context;
    }

    /**
     * 查找所有视频
     */
    public List<TFileInfo> searchVideo() {

        List<TFileInfo> list = null;
        try {
            Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = mContext.getContentResolver();
            Cursor cursor = mContentResolver.query(videoUri, null, null, null, null);
            list = readVideoCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
            logger.e(e.getMessage());
        }
        return list;

    }

    /**
     * 根据关键字查找音频
     */
    public List<TFileInfo> searchVideo(final String searchKey) {
        List<TFileInfo> list = null;
        try {
            Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = mContext.getContentResolver();
            Cursor cursor = mContentResolver.query(videoUri, null, MediaStore.Video.Media.DISPLAY_NAME + " like '%" + searchKey + "%'", null, null);
            list = readVideoCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
            logger.e(e.getMessage());
        }
        return list;
    }

    /**
     * 读取音频cursor内容
     */
    List<TFileInfo> readVideoCursor(Cursor cursor) {
        List<TFileInfo> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {

                String display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));// 文件名
                String file_path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));// 路径
                long create_time = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));// 创建时间
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));// 文件大小
                int play_time = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));// 播放时长

                TFileInfo video_file = new TFileInfo();
                video_file.setTaskId(FileUtils.buildTaskId());
                video_file.setName(display_name);
                video_file.setPath(file_path);
                video_file.setCreateTime(FileUtils.parseTimeToYMD(create_time));
                video_file.setLength(size);
                video_file.setPlayTime(play_time);
                video_file.setFullName(display_name);
                if (!StringUtils.isEmpty(display_name) && display_name.lastIndexOf(".") != -1) {
                    video_file.setExtension(display_name.substring(display_name.lastIndexOf(".")));
                } else {
                    video_file.setExtension("");
                }
                video_file.setFileType(FileType.Video);
                list.add(video_file);
            }
            cursor.close();
        }
        return list;
    }

}
