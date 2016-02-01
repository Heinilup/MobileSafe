package ikabi.com.mobilesafe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/2
 */
public class PackageUtils {

    public static String getVersionName(Context context){
        PackageManager pm = context.getPackageManager();

        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getVersionCode(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
