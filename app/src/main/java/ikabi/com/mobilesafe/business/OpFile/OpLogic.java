package ikabi.com.mobilesafe.business.OpFile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

import ikabi.com.mobilesafe.business.BaseLogic;
import ikabi.com.mobilesafe.business.event.OpFileEvent;
import ikabi.com.mobilesafe.business.model.TFileInfo;
import ikabi.com.mobilesafe.utils.Logger;
import ikabi.com.mobilesafe.utils.StringUtils;
import ikabi.com.mobilesafe.utils.ThreadPoolManager;

/**
 * 文件操作
 */
public class OpLogic extends BaseLogic {

    Logger logger = Logger.getLogger(OpLogic.class);

    private Context context;

    public OpLogic(Context context) {
        this.context = context;
    }

    public void renameFile(final TFileInfo tFileInfo, final String newName) {
        ThreadPoolManager.getInstance(OpLogic.class.getName()).startTaskThread(new Runnable() {
            @Override
            public void run() {
                OpFileEvent event = new OpFileEvent(OpFileEvent.OpType.RENAME, tFileInfo);
                File file = new File(tFileInfo.getPath());
                String prePath = file.getParent();
                File saveFile = new File(prePath + File.separator + newName);
                if (!saveFile.exists()) {
                    file.renameTo(saveFile);
                    tFileInfo.setName(saveFile.getName());
                    tFileInfo.setFullName(saveFile.getName());
                    tFileInfo.setPath(saveFile.getAbsolutePath());
                    event.setSuccess(true);
                } else {
                    event.setSuccess(false);
                }
                triggerEvent(event);
            }
        });


    }

    public void deleteFile(final TFileInfo tFileInfo) {
        ThreadPoolManager.getInstance(OpLogic.class.getName()).startTaskThread(new Runnable() {
            @Override
            public void run() {
                OpFileEvent event = new OpFileEvent(OpFileEvent.OpType.DELETE, tFileInfo);
                try {
                    File file = new File(tFileInfo.getPath());
                    if (file.exists()) {
                        file.delete();
                        event.setSuccess(true);
                    } else {
                        event.setSuccess(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.e(e.getMessage());
                    event.setSuccess(false);
                }
                triggerEvent(event);
            }
        });
    }

    public void unInstall(TFileInfo tFileInfo) {
        if (tFileInfo != null && !StringUtils.isEmpty(tFileInfo.getPackageName())) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + tFileInfo.getPackageName()));
            context.startActivity(intent);
        }
    }

    public void backUpApk(final TFileInfo tFileInfo) {
        ThreadPoolManager.getInstance(OpLogic.class.getName()).startTaskThread(new Runnable() {
            @Override
            public void run() {
                OpFileEvent event = new OpFileEvent(OpFileEvent.OpType.BACKUP, tFileInfo);
                try {
                    File fromFile = new File(tFileInfo.getPath());
                    if (!fromFile.exists() || !fromFile.isFile()) {
                        return;
                    }
                    String prePath = "";
                    prePath += Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
                    prePath += "XFile" + File.separator;
                    prePath += "backup" + File.separator;
                    String tempSavePath = prePath + tFileInfo.getFullName();
                    File toFile = new File(tempSavePath);
                    if (!toFile.getParentFile().exists()) {
                        toFile.getParentFile().mkdirs();
                    }
                    int i = 0;
                    while (toFile.exists()) {
                        i++;
                        toFile = new File(tempSavePath + "-" + i);
                    }

                    java.io.FileInputStream fosfrom = new java.io.FileInputStream(fromFile);
                    java.io.FileOutputStream fosto = new FileOutputStream(toFile);
                    byte bt[] = new byte[1024];
                    int c;
                    while ((c = fosfrom.read(bt)) > 0) {
                        fosto.write(bt, 0, c); //将内容写到新文件当中
                    }
                    fosfrom.close();
                    fosto.close();
                    event.setSuccess(true);

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.e(e.getMessage());
                    event.setSuccess(false);
                }
                triggerEvent(event);

            }
        });
    }
}
