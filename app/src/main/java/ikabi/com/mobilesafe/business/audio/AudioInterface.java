package ikabi.com.mobilesafe.business.audio;

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
 * 读取本地音频
 */
public class AudioInterface {

    private Logger logger = Logger.getLogger(AudioInterface.class);

    private Context mContext;

    public AudioInterface(Context context) {
        this.mContext = context;
    }

    /**
     * 查找所有音频
     */
    public List<TFileInfo> searchAudio() {

        List<TFileInfo> list = null;
        try {
            Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = mContext.getContentResolver();
            Cursor cursor = mContentResolver.query(audioUri, null, null, null, null);
            list = readAudioCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
            logger.e(e.getMessage());
        }
        return list;

    }

    /**
     * 根据关键字查找音频
     */
    public List<TFileInfo> searchAudio(final String searchKey) {
        List<TFileInfo> list = null;
        try {
            Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = mContext.getContentResolver();
            Cursor cursor = mContentResolver.query(audioUri, null, MediaStore.Audio.Media.DISPLAY_NAME + " like '%" + searchKey + "%'", null, null);
            list = readAudioCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
            logger.e(e.getMessage());
        }
        return list;
    }

    /**
     * 读取音频cursor内容
     */
    List<TFileInfo> readAudioCursor(Cursor cursor) {
        List<TFileInfo> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {

                String display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));// 文件名
                String file_path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));// 路径
                long create_time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));// 创建时间
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));// 文件大小
                int play_time = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 播放时长

                TFileInfo audio_file = new TFileInfo();
                audio_file.setTaskId(FileUtils.buildTaskId());
                audio_file.setName(display_name);
                audio_file.setPath(file_path);
                audio_file.setCreateTime(XFileUtils.parseTimeToYMD(create_time));
                audio_file.setLength(size);
                audio_file.setPlayTime(play_time);
                audio_file.setFullName(display_name);
                if (!StringUtils.isEmpty(display_name) && display_name.lastIndexOf(".") != -1) {
                    audio_file.setExtension(display_name.substring(display_name.lastIndexOf(".")));
                } else {
                    audio_file.setExtension("");
                }
                audio_file.setFileType(FileType.Audio);
                list.add(audio_file);
            }
            cursor.close();
        }
        return list;
    }


}
