/*
package ikabi.com.mobilesafe.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import ikabi.com.mobilesafe.R;

*/
/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/27
 *//*

public class FileUtils {


    private static final int ERROR = -1;

    //External Storage Total space
    public static long getTotalExternalSize(){
        if(externalSDAvailable()){


        StatFs stat = new StatFs("/storage/sdcard1");
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks =stat.getBlockCountLong();
        return blockSize*totalBlocks;
        } else {
            return ERROR;
        }
    }
    //Free External Storage space
    public static long getFreeExternalSize(){
        if(externalSDAvailable()){


            StatFs stat = new StatFs("/storage/sdcard1");
            long blockSize = stat.getBlockSizeLong();
            long freeBlocks =stat.getAvailableBlocksLong();
            return blockSize*freeBlocks;
        } else {
            return ERROR;
        }
    }

    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");

    public static String formatFileSize(long size, boolean isInteger) {
        DecimalFormat df = isInteger ? fileIntegerFormat : fileDecimalFormat;
        String fileSizeString = "0M";
        if (size < 1024 && size > 0) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1024 * 1024) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) size / (1024 * 1024 * 1024)) + "G";
        }
        return fileSizeString;
    }

    public static boolean externalSDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    */
/*
     * 是否存在SD存储卡
     *//*

    public static boolean ExistSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    */
/*
     * 后缀检查
     *//*

    public static boolean checkEndsWithInStringArray(String checkItsEnd, String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.endsWith(aEnd))
                return true;
        }
        return false;
    }

    */
/*
     * 获取存储卡路径
     *//*

    public static String getStorageCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    */
/*
     * 时间转换
     *//*

    @SuppressLint("SimpleDateFormat")
    public static String parseTimeToYMD(long time) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(new Date(time * 1000L));
    }

    */
/**
     * 根据输入的参数大小，进行单位换算，size单位为bit.视情况转换为KB,MB,GB
     *//*

    public static String parseSize(long size) {
        String sizeStr;
        float kb = 1024;
        float mb = kb * 1024;
        float gb = mb * 1024;
        DecimalFormat fnum = new DecimalFormat("##0.00");
        if (size < kb) {
            sizeStr = size + "bit";
        } else if (size < mb) {
            float showSize = (float) size / kb;
            fnum.format(showSize);
            sizeStr = fnum.format(showSize) + "KB";
        } else if (size < gb) {
            float showSize = (float) size / mb;
            fnum.format(showSize);
            sizeStr = fnum.format(showSize) + "MB";
        } else {
            float showSize = (float) size / gb;
            fnum.format(showSize);
            sizeStr = fnum.format(showSize) + "GB";
        }
        return sizeStr;
    }

    */
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
     *//*

    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        System.out.println("w" + bitmap.getWidth());
        System.out.println("h" + bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    */
/*
     * 获取外置存储卡路径
     *//*

    public static List<String> getSdCardPaths() {

        List<String> paths = new ArrayList<>();
        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        if (extFileStatus.endsWith(Environment.MEDIA_UNMOUNTED) && extFile.exists() && extFile.isDirectory() && extFile.canWrite()) {
            paths.add(extFile.getAbsolutePath());
        }
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            int mountPathIndex = 1;
            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line.contains("storage")) || line.contains("secure") || line.contains("asec") || line.contains("firmware") || line.contains("shell") || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }
                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }
                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains("/") || mountPath.contains("data") || mountPath.contains("Data") || mountPath.contains("usbotg") || mountPath.contains("uicc")) {
                    continue;
                }
                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory() || !mountRoot.canWrite()) {
                    continue;
                }
                boolean equalsToPrimarySD = mountPath.equals(extFile.getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }
                paths.add(mountPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paths;
    }


    */
/**
     * 获取视频时长
     *//*

    public static String getDuration(int durationSeconds) {
        int hours = durationSeconds / (60 * 60);
        int leftSeconds = durationSeconds % (60 * 60);
        int minutes = leftSeconds / 60;
        int seconds = leftSeconds % 60;
        String duration;
        duration = addZeroPrefix(hours);
        duration += ":";
        duration += addZeroPrefix(minutes);
        duration += ":";
        duration += addZeroPrefix(seconds);
        return duration;
    }

    static String addZeroPrefix(int number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return "" + number;
        }
    }

    */
/**
     * 获取版本号
     *//*

    public static String getVersion(Context context) {
        String version = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    */
/**
     * 获取屏幕宽度
     *//*

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        // 获取屏幕分辨率宽度
        return dm.widthPixels;
    }

    */
/**
     * 获取文件MD5值
     *//*

    public static String getMD5(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    */
/**
     * 读取设备号
     *//*

    public static String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) XFileApplication.context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    */
/**
     * 根据后缀生成保存路径
     *//*

    public static String getSavePathByExtension(String extension) {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append("XFile");
        sb.append(File.separator);
        switch (extension) {
            case "doc":
                sb.append("doc");
                sb.append(File.separator);
                break;
            case "mp3":
                sb.append("music");
                sb.append(File.separator);
                break;
            default:
                sb.append("other");
                sb.append(File.separator);
                break;
        }
        return sb.toString();
    }

    public static String buildTaskId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static FileType getFileType(Context context, String extension) {
        FileType fileType;
        if (XFileUtils.checkEndsWithInStringArray(extension, context.getResources().getStringArray(R.array.fileEndingImage))) {
            fileType = FileType.Image;
        } else if (XFileUtils.checkEndsWithInStringArray(extension, context.getResources().getStringArray(R.array.fileEndingAudio))) {
            fileType = FileType.Audio;
        } else if (XFileUtils.checkEndsWithInStringArray(extension, context.getResources().getStringArray(R.array.fileEndingVideo))) {
            fileType = FileType.Video;
        } else {
            fileType = FileType.Normal;
        }
        return fileType;
    }

    public static XFileProtocol.File buildSFile(TFileInfo fileInfo) {
        XFileProtocol.File.Builder sendFile = XFileProtocol.File.newBuilder();
        sendFile.setName(fileInfo.getName());
        sendFile.setPosition(fileInfo.getPosition());
        sendFile.setLength(fileInfo.getLength());
        sendFile.setPath(fileInfo.getPath());
        sendFile.setExtension(fileInfo.getExtension());
        sendFile.setFullName(fileInfo.getFullName());
        sendFile.setTaskId(fileInfo.getTaskId());
        sendFile.setIsSend(fileInfo.isSend());
        return sendFile.build();
    }

    public static TFileInfo buildTFile(XFileProtocol.File fileInfo) {
        TFileInfo tFile = new TFileInfo();
        tFile.setName(fileInfo.getName());
        tFile.setPosition(fileInfo.getPosition());
        tFile.setLength(fileInfo.getLength());
        tFile.setPath(fileInfo.getPath());
        tFile.setExtension(fileInfo.getExtension());
        tFile.setFullName(fileInfo.getFullName());
        tFile.setTaskId(fileInfo.getTaskId());
        tFile.setFrom(fileInfo.getFrom());
        tFile.setIsSend(fileInfo.getIsSend());
        return tFile;
    }

    public static DFile buildDFile(TFileInfo tFileInfo) {
        DFile dFile = new DFile();
        dFile.setName(tFileInfo.getName());
        dFile.setTaskId(tFileInfo.getTaskId());
        dFile.setLength(tFileInfo.getLength());
        dFile.setPosition(tFileInfo.getPosition());
        dFile.setPath(tFileInfo.getPath());
        dFile.setIsSend(tFileInfo.isSend());
        dFile.setExtension(tFileInfo.getExtension());
        dFile.setFullName(tFileInfo.getFullName());
        dFile.setFrom(tFileInfo.getFrom());
        dFile.setPercent(tFileInfo.getPercent());
        switch (tFileInfo.getFileEvent()) {
            case CREATE_FILE_SUCCESS:
            case SET_FILE:
                // 正在传送
                dFile.setStatus(0);
                break;
            case SET_FILE_SUCCESS:
                // 传输完成
                dFile.setStatus(1);
                break;
        }
        return dFile;
    }

}*/
