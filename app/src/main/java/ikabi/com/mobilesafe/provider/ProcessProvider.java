package ikabi.com.mobilesafe.provider;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.bean.ProcessInfo;
import ikabi.com.mobilesafe.utils.LogUtils;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:2016/2/23
 */
public class ProcessProvider {

    // 正在运行的进程数，总的可有进程数

    private static final String TAG = "ProcessProvider";

    /**
     * 正在运行的进程数
     *
     * @param context
     * @return
     */
    public static int getRunningProcessCount(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        if (processes != null) {
            return processes.size();
        }
        return 0;
    }

    /**
     * 获得可有的所有的进程数
     *
     * @param context
     * @return
     */
    public static int getTotalProcessCount(Context context) {
        PackageManager pm = context.getPackageManager();

        List<PackageInfo> packages = pm.getInstalledPackages(0);
        int count = 0;
        for (PackageInfo pack : packages) {
            // 去重
            HashSet<String> set = new HashSet<String>();
            set.add(pack.applicationInfo.processName);

            // activity
            ActivityInfo[] activities = pack.activities;
            if (activities != null) {
                for (ActivityInfo activity : activities) {
                    String processName = activity.processName;
                    set.add(processName);
                }
            }

            // services
            ServiceInfo[] services = pack.services;
            if (services != null) {
                for (ServiceInfo service : services) {
                    String processName = service.processName;
                    set.add(processName);
                }
            }

            // receiver
            ActivityInfo[] receivers = pack.receivers;
            if (receivers != null) {
                for (ActivityInfo receiver : receivers) {
                    String processName = receiver.processName;
                    set.add(processName);
                }
            }

            ProviderInfo[] providers = pack.providers;
            if (providers != null) {
                for (ProviderInfo provi : providers) {
                    String processName = provi.processName;
                    set.add(processName);
                }
            }

            count += set.size();
        }

        return count;
    }

    /**
     * 获得可用的内存信息
     *
     * @param context
     * @return
     */
    public static long getFreeMemory(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);

        return outInfo.availMem;
    }

    /**
     * 获得所有的内存
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    public static long getTotalMemory(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);

        // 代码适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LogUtils.d(TAG, "高版本总内存");
            return outInfo.totalMem;
        } else {
            LogUtils.d(TAG, "低版本总内存");
            return getLowTotalMemory();
        }
    }

    private static long getLowTotalMemory() {
        File file = new File("/proc/meminfo");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            // MemTotal: 513492 kB

            String line = reader.readLine();
            line = line.replace("MemTotal:", "");
            line = line.replace("kB", "");
            line = line.trim();

            return Long.valueOf(line) * 1024;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<ProcessInfo> getAllRunningProcesses(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();

        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();

        List<ProcessInfo> list = new ArrayList<ProcessInfo>();
        if (processes != null) {
            for (int i = 0; i < processes.size(); i++) {
                ActivityManager.RunningAppProcessInfo process = processes.get(i);

                // process.pid;//进程id
                String packageName = process.processName;// 进程id包名

                Drawable icon = null;// 图标
                String name = null;// 应用的名称
                long memory = 0;
                boolean isSystem = false;
                try {
                    ApplicationInfo applicationInfo = pm.getApplicationInfo(
                            packageName, 0);
                    // 应用的图标
                    icon = applicationInfo.loadIcon(pm);
                    name = applicationInfo.loadLabel(pm).toString();

                    int flags = applicationInfo.flags;

                    if ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                        isSystem = true;
                    } else {
                        isSystem = false;
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    // e.printStackTrace();

                    icon = context.getResources().getDrawable(
                            R.drawable.ic_launcher);
                    name = packageName;
                    isSystem = true;
                }

                // 内存信息
                android.os.Debug.MemoryInfo memoryInfo = am
                        .getProcessMemoryInfo(new int[] { process.pid })[0];
                memory = memoryInfo.getTotalPss() * 1024;

                ProcessInfo info = new ProcessInfo();
                info.icon = icon;
                info.name = name;
                info.memory = memory;// 占用的memory
                info.isSystem = isSystem;
                info.packageName = packageName;
                info.isForeground = process.importance == 100
                        || process.importance == 50
                        || process.importance == 130
                        || (info.isSystem && process.importance == 400);

                LogUtils.d(TAG, info.packageName + " === " + process.importance);
                list.add(info);
            }

        }
        return list;
    }

    public static void killProcess(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(packageName);
    }
}
