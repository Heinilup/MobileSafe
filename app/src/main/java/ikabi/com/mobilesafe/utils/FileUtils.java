package ikabi.com.mobilesafe.utils;

import android.os.Environment;
import android.os.StatFs;

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

    public static boolean externalSDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
