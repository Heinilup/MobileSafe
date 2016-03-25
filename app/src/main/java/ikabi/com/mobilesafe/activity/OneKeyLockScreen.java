package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import ikabi.com.mobilesafe.receiver.LockScreenReceiver;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/28
 */
public class OneKeyLockScreen extends Activity {

    /**
     * DevicePolicyManager 顾名思义，这个类的作用是管理设备。通过这个类，我们可以实现屏幕锁定、亮度调节甚至是恢复出厂设置等功能。
     */
    private DevicePolicyManager policyManager;
    private ComponentName componentName;
    private static final int MY_REQUEST_CODE = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        // 获取设备管理服务
        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        // ComponentName这个我们在用intent跳转的时候用到过。
        // 自己的AdminReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(this, LockScreenReceiver.class);
		/*
		 * 假如先判断是否有权限，如果没有则调用activeManage()，然后立即锁屏，最后再finish()。
		 * 这样做是有问题的，因为activeManage()可能还在等待另一个Activity的结果，那么此时依然没有权限却
		 * 执行了lockNow()，这样就出错了。 处理方法有2个：
		 * 1、是重写OnActivityResult()函数，在里面判断是否获取权限成功，是则锁屏并finish()
		 * 否则继续调用activeManage()获取权限（这样激活后立即锁屏，效果很好）
		 * 2、不重写OnActivityResult()函数，第一次获取权限后不锁屏而立即finish()，这样从理论上说也可能
		 * 失败，可能权限还没获取好就finish了（这样激活后就回到桌面，还得再按一次锁屏才能锁） 综上推荐第一种方法。
		 */

        // 判断是否有锁屏权限，若有则立即锁屏并结束自己，若没有则获取权限
        if (policyManager.isAdminActive(componentName)) {
            policyManager.lockNow();// 锁屏
            finish();
        } else {
            activeManage(); //获取权限
        }
        //setContentView(R.layout.activity_main); // 把这句放在最后，这样锁屏的时候就不会跳出来（闪一下）
    }
    /**
     * 获取权限
     */
    private void activeManage() {
        // 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        // 权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);

        // 描述(additional explanation) 在申请权限时出现的提示语句
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "激活后就能一键锁屏了");

        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取权限成功，立即锁屏并finish自己，否则继续获取权限
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            policyManager.lockNow();
            finish();
        } else {
            //activeManage();
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}