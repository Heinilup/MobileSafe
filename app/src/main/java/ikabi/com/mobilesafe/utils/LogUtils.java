package ikabi.com.mobilesafe.utils;

import android.util.Log;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/5
 */
public class LogUtils {

    public static final int LEVEL_V = 0;
    public static final int LEVEL_D = 1;
    private static boolean isEnable = true;
    private static int LOG_LEVEL = LEVEL_V;


    public static void d(String tag, String msg){
        if (!isEnable){
            return;
        }
        if (LOG_LEVEL <= LEVEL_D){
            Log.d(tag, msg);
        }
    }


    public static void v(String tag, String msg){
        if (!isEnable){
            return;
        }
        if (LOG_LEVEL <= LEVEL_V){
            Log.v(tag, msg);
        }
    }
}
