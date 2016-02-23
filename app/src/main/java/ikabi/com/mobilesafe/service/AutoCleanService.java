package ikabi.com.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;

import ikabi.com.mobilesafe.bean.ProcessInfo;
import ikabi.com.mobilesafe.provider.ProcessProvider;
import ikabi.com.mobilesafe.utils.LogUtils;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:2016/2/23
 */
public class AutoCleanService extends Service {

    private static final String TAG = "AutoCleanService";

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 锁屏操作
            // 清理进程

            List<ProcessInfo> processes = ProcessProvider
                    .getAllRunningProcesses(context);

            for (ProcessInfo info : processes) {
                ProcessProvider.killProcess(context, info.packageName);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.d(TAG, "开启锁屏清理的服务");

        // 注册锁屏的广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtils.d(TAG, "关闭锁屏清理的服务");
        // 注销
        unregisterReceiver(mReceiver);
    }

}
