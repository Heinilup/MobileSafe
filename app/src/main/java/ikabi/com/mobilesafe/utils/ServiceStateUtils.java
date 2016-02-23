package ikabi.com.mobilesafe.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:2016/2/23
 */
public class ServiceStateUtils {

    /**
     * 判断服务是否运行
     *
     * @param context
     * @param clazz
     * @return
     */
    public static boolean isRunging(Context context,
                                    Class<? extends Service> clazz) {

        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningServiceInfo> list = am
                .getRunningServices(Integer.MAX_VALUE);

        for (RunningServiceInfo info : list) {

            ComponentName service = info.service;
            String className = service.getClassName();
            if (className.equals(clazz.getName())) {
                return true;
            }
        }

        return false;
    }
}