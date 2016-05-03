package ikabi.com.mobilesafe.business.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AppScanner {

    Logger logger = Logger.getLogger(AppScanner.class);

    private Context mContext;

    public AppScanner(Context context) {
        this.mContext = context;
    }

    @SuppressLint("HandlerLeak")
    public void scanImages(final ScanAppsCompleteCallBack callback) {
        final Handler mHandler = new Handler() {

            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                callback.scanComplete((ArrayList<AppInfo>) msg.obj);
            }
        };

        new Thread(new Runnable() {

            @Override
            public void run() {

                List<AppInfo> list = new ArrayList<AppInfo>();
                PackageManager pm = mContext.getPackageManager();
                //	获取读取已安装程序列表服务
                List<PackageInfo> packs = pm.getInstalledPackages(0);

                for (PackageInfo pi : packs) {
                    if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        AppInfo appInfo = new AppInfo();
                        appInfo.setAppIcon(pi.applicationInfo.loadIcon(pm));//程序图标
                        appInfo.setAppName(pi.applicationInfo.loadLabel(pm).toString());//程序名称
                        appInfo.setPackageName(pi.applicationInfo.packageName);//包名
                        int appSize = Integer.valueOf((int) new File(pi.applicationInfo.publicSourceDir).length());
                        logger.e("****sourceDir:" + pi.applicationInfo.sourceDir);
                        appInfo.setAppSize(appSize);
                        list.add(appInfo);
                    }

                }
                if (list.size() > 0) {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = list;
                    mHandler.sendMessage(msg);
                }

            }
        }).start();

    }

    public static interface ScanAppsCompleteCallBack {
        public void scanComplete(List<AppInfo> list);
    }
}
