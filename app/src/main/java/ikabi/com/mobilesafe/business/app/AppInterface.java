package ikabi.com.mobilesafe.business.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.huangjiang.business.model.FileType;
import com.huangjiang.business.model.TFileInfo;
import com.huangjiang.utils.Logger;
import com.huangjiang.utils.StringUtils;
import com.huangjiang.utils.XFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索本地安装程序
 */
public class AppInterface {

    private Logger logger = Logger.getLogger(AppInterface.class);

    private Context mContext;

    public AppInterface(Context context) {
        this.mContext = context;
    }

    /**
     * 查找所有安装程序
     */
    public List<TFileInfo> searchApp(String... searchKey) {

        List<TFileInfo> list = new ArrayList<>();
        try {
            PackageManager pm = mContext.getPackageManager();
            // 获取读取已安装程序列表服务
            List<PackageInfo> packs = pm.getInstalledPackages(0);
            for (PackageInfo pi : packs) {
                // 判断是否是系统程序
                if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    TFileInfo appFile = new TFileInfo();
                    String display_name = pi.applicationInfo.loadLabel(pm).toString();
                    appFile.setTaskId(XFileUtils.buildTaskId());
                    appFile.setPath(pi.applicationInfo.publicSourceDir);//安装包路径
                    appFile.setName(display_name);//程序名称
                    appFile.setPackageName(pi.applicationInfo.packageName);//包名
                    appFile.setLength(new File(pi.applicationInfo.publicSourceDir).length());//大小
                    appFile.setFullName(display_name + ".apk");
                    if (!StringUtils.isEmpty(display_name)) {
                        appFile.setExtension(".apk");
                    } else {
                        appFile.setExtension("");
                    }
                    appFile.setFileType(FileType.Apk);
                    if (searchKey != null && searchKey.length > 0) {
                        if (display_name.startsWith(searchKey[0])) {
                            list.add(appFile);
                        }
                    } else {
                        list.add(appFile);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.e(e.getMessage());
        }
        return list;

    }
}
