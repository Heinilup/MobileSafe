package ikabi.com.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtils {

    private static SharedPreferences sp;

    private static SharedPreferences getPreferences(Context context) {

        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp;
    }

    /**
     * 获得boolean类型的信息,如果没有返回false
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * 获得boolean类型的信息
     *
     * @param context
     * @param key
     * @param defValue
     *            ： 没有时的默认值
     * @return
     */
    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        SharedPreferences sp = getPreferences(context);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 设置boolean类型的 配置数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getPreferences(context);
        Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * 获得string类型的信息,如果没有返回null
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * 获得String类型的信息
     *
     * @param context
     * @param key
     * @param defValue
     *            ： 没有时的默认值
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = getPreferences(context);
        return sp.getString(key, defValue);
    }

    /**
     * 设置String类型的 配置数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = getPreferences(context);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 获得int类型的信息,如果没有返回-1
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * 获得int类型的信息
     *
     * @param context
     * @param key
     * @param defValue
     *            ： 没有时的默认值
     * @return
     */
    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = getPreferences(context);
        return sp.getInt(key, defValue);
    }

    /**
     * 设置int类型的 配置数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = getPreferences(context);
        Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }
}
