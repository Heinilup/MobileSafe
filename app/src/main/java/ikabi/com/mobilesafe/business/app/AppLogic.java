package ikabi.com.mobilesafe.business.app;

import android.content.Context;

import com.huangjiang.business.BaseLogic;
import com.huangjiang.business.event.FindResEvent;
import com.huangjiang.business.model.TFileInfo;
import com.huangjiang.core.ThreadPoolManager;

import java.util.List;

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


