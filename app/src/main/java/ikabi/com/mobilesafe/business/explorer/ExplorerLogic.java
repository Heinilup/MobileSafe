package ikabi.com.mobilesafe.business.explorer;

import android.content.Context;

import com.huangjiang.business.model.FileType;
import com.huangjiang.business.model.TFileInfo;
import com.huangjiang.filetransfer.R;
import com.huangjiang.utils.XFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件查找
 */
public class ExplorerLogic {

    private Context mContext;

    public ExplorerLogic(Context context) {
        mContext = context;
    }

    public List<TFileInfo> getCatalogFiles(String dirPath) {

        if (mContext == null) {
            throw new NullPointerException("Context Null");
        }
        File dirFile = new File(dirPath);
        if (dirFile.isDirectory()) {
            try {
                dirFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File[] dirFiles = dirFile.listFiles();
        List<TFileInfo> tFileInfoList = new ArrayList<>();
        if (dirFiles == null) {
            return tFileInfoList;
        }
        for (File file : dirFiles) {
            TFileInfo tFileInfo = new TFileInfo();
            tFileInfo.setName(file.getName());
            tFileInfo.setPath(file.getPath());
            tFileInfo.setDirectory(file.isDirectory());
            String fileName = file.getName();
            if (file.isDirectory()) {
                tFileInfo.setFileType(FileType.Folder);
            } else {
                tFileInfo.setTaskId(XFileUtils.buildTaskId());
                tFileInfo.setLength(file.length());
                tFileInfo.setCreateTime(XFileUtils.parseTimeToYMD(file.lastModified()));
                if (XFileUtils.checkEndsWithInStringArray(fileName, mContext.getResources().getStringArray(R.array.fileEndingImage))) {
                    tFileInfo.setFileType(FileType.Image);
                } else if (XFileUtils.checkEndsWithInStringArray(fileName, mContext.getResources().getStringArray(R.array.fileEndingAudio))) {
                    tFileInfo.setFileType(FileType.Audio);
                } else if (XFileUtils.checkEndsWithInStringArray(fileName, mContext.getResources().getStringArray(R.array.fileEndingVideo))) {
                    tFileInfo.setFileType(FileType.Video);
                } else if (XFileUtils.checkEndsWithInStringArray(fileName, mContext.getResources().getStringArray(R.array.fileEndingApk))) {
                    tFileInfo.setFileType(FileType.Apk);
                } else {
                    tFileInfo.setFileType(FileType.Normal);
                }
            }
            tFileInfoList.add(tFileInfo);

        }
        return tFileInfoList;
    }
}
