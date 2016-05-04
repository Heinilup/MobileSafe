package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * 界面管理器
 */
public class XFileActivityManager {

    private static Stack<Activity> activityStack = new Stack<Activity>();

    private XFileActivityManager() {
    }

    private static class ManagerHolder {
        private static final XFileActivityManager instance = new XFileActivityManager();
    }

    public static XFileActivityManager create() {
        return ManagerHolder.instance;
    }

    /**
     * 获取当前栈里面activity的数量
     *
     * @return
     */
    public int getCount() {
        if (activityStack != null && !activityStack.isEmpty()) {
            return activityStack.size();
        } else {
            return 0;
        }
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
//    	Activity topActivity = topActivity();
        //如果栈顶的Activity和正要加入的activity是同一个的话,则关闭此Activity
//    	if(topActivity!=null&&topActivity.getClass().getName().equals(activity.getClass().getName())){
//    		activity.finish();
//    	} else {
        if (activity != null) {
            activityStack.add(activity);
        }

//    	}
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public Activity topActivity() {
        if (activityStack == null) {
            throw new NullPointerException(
                    "Activity stack is Null,your Activity must extend BaseActivity");
        }
        if (activityStack.isEmpty()) {
            return null;
        }
        return activityStack.lastElement();
    }

    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public Activity findActivity(Class<?> cls) {
        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                return aty;
            }
        }
        return null;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        if (activityStack == null) {
            throw new NullPointerException(
                    "Activity stack is Null,your Activity must extend BaseActivity");
        }
        //如果它的大小为0的话,则会报NoSuchElement异常,已经没有元素了
        if (!activityStack.isEmpty()) {
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        Iterator<Activity> itr = activityStack.iterator();
        while (itr.hasNext()) {
            Activity activity = (Activity) itr.next();
            if (activity.getClass().equals(cls)) {
                itr.remove();
                activity.finish();
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        Iterator<Activity> itr = activityStack.iterator();
        while (itr.hasNext()) {
            Activity activity = (Activity) itr.next();
            if (!activity.getClass().equals(cls)) {
                itr.remove();
                activity.finish();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null) {
            return;
        }
        Iterator<Activity> itr = activityStack.iterator();
        while (itr.hasNext()) {
            Activity activity = (Activity) itr.next();
            if (activity != null)
                activity.finish();
        }
        activityStack.clear();
    }

    /**
     * 清楚指定的Activity上的所有Activity的实例，不包含本Activity
     *
     * @Title: finishCurActivityTopAllActivitys
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param: @param class1
     * @return: void
     */
    public void finishCurActivityTopAllActivitys(Class<?> class1) {
        if (activityStack == null)
            return;
        int curActivityIndex = -1;
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(class1)) {
                curActivityIndex = i;
                break;
            }
        }
        int i = 0;
        Iterator<Activity> itr = activityStack.iterator();
        while (itr.hasNext()) {
            Activity activity = (Activity) itr.next();
            if (i > curActivityIndex) {
                itr.remove();
                activity.finish();
            }
            i++;
        }
    }

    /**
     * 应用程序退出
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
        }
    }

    /**
     * 返回当前的Activity是否处于前台显示状态
     *
     * @param activityClassName
     * @return
     */
    public boolean isTopActivity(Context context, String activityClassName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if (info != null && info.size() > 0) {
            ComponentName component = info.get(0).topActivity;
            if (activityClassName.equals(component.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断app是否打开运行状态
     *
     * @param context
     * @return
     */
    public static boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    /**
     * 判断应用是否在前台运行
     *
     * @param context
     * @return
     */
    public boolean isRunningForeground(Context context) {
        String packageName = getPackageName(context);
        String topActivityClassName = getTopActivityName(context);
        System.out.println("packageName=" + packageName + ",topActivityClassName=" + topActivityClassName);
        if (packageName != null && topActivityClassName != null && topActivityClassName.startsWith(packageName)) {
            System.out.println("---> isRunningForeGround");
            return true;
        } else {
            System.out.println("---> isRunningBackGround");
            return false;
        }
    }

    /**
     * 获取前台运行的Activity
     *
     * @param context
     * @return
     */
    public String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
                (ActivityManager) (context.getSystemService(android.content.Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    public String getPackageName(Context context) {
        String packageName = context.getPackageName();
        return packageName;
    }

}
