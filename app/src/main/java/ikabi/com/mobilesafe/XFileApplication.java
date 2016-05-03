package ikabi.com.mobilesafe;

import android.app.Application;
import android.content.Context;

import ikabi.com.mobilesafe.utils.FileUtils;
import ikabi.com.mobilesafe.utils.NetStateUtils;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:2016/5/3
 */
public class XFileApplication extends Application {

    public static Context context;

    public static String device_id;

    public static int connect_type = 0;//0 未连接,1 客户端连接,2 服务端连接


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        device_id = FileUtils.getDeviceId();
        String ip= NetStateUtils.getIPv4(context);
        System.out.println("ip:"+ip);


    }

}
