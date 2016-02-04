package ikabi.com.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/5
 */
public class PreferenceUtils {
    private static SharedPreferences sp;
    private static SharedPreferences getPreferences (Context context){
        if (sp == null ){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp;
    }
}
