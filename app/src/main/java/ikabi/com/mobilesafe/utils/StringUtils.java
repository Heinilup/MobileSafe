package ikabi.com.mobilesafe.utils;

/**
 * 字符串工具类
 */
public class StringUtils {

    /**
     *
     * @Title: isStringEmpty
     * @Description: TODO(判断字符串是否为空)
     * @param: @param value
     * @param: @return
     * @return: boolean
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equals(value.trim()) && !"null".equals(value.trim())) {
            return false;
        } else {
            return true;
        }
    }
}
