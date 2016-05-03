package ikabi.com.mobilesafe.business.app;

import android.content.Context;

import java.util.List;

import ikabi.com.mobilesafe.business.BaseLogic;
import ikabi.com.mobilesafe.business.event.FindResEvent;
import ikabi.com.mobilesafe.business.model.TFileInfo;

/**
 * 查找本地安装程序
 */
public class AppLogic extends BaseLogic {

    private AppInterface appInterface;

    public AppLogic(Context context) {
        appInterface = new AppInterface(context);
    }

    public void searchApp() {
        ThreadPoolManager.getInstance(AppLogic.class.getName()).startTaskThread(new Runnable() {
            @Override
            public void run() {
                List<TFileInfo> appList = appInterface.searchApp();
                triggerEvent(FindResEvent.MimeType.APK, appList);
            }
        });
    }
}


