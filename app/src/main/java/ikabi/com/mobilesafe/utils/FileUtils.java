package ikabi.com.mobilesafe.utils;

import android.os.Environment;
import android.os.StatFs;

import java.text.DecimalFormat;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/27
 */
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
}
