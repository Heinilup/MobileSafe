package ikabi.com.mobilesafe.business.history;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ikabi.com.mobilesafe.business.BaseLogic;
import ikabi.com.mobilesafe.business.event.FindResEvent;
import ikabi.com.mobilesafe.business.model.FileType;
import ikabi.com.mobilesafe.business.model.TFileInfo;
import ikabi.com.mobilesafe.dao.DFile;
import ikabi.com.mobilesafe.dao.DFileDao;
import ikabi.com.mobilesafe.dao.DaoMaster;
import ikabi.com.mobilesafe.utils.FileEvent;
import ikabi.com.mobilesafe.utils.FileUtils;
import ikabi.com.mobilesafe.utils.ThreadPoolManager;

/**
 * 历史消息业务逻辑
 */
public class HistoryLogic extends BaseLogic {

    private HistoryInterface historyInterface;
    private Context context;
    private DFileDao fileDao;

    public HistoryLogic(Context context) {
        historyInterface = new HistoryInterface(context);
        this.context = context;
        this.fileDao = DaoMaster.getInstance().newSession().getDFileDao();
    }

    /**
     * 读取历史消息
     */
    public void getHistory() {
        ThreadPoolManager.getInstance(HistoryLogic.class.getName()).startTaskThread(new Runnable() {
            @Override
            public void run() {
                List<DFile> history = historyInterface.getHistory();
                if (history != null) {
                    List<TFileInfo> tFileInfoList = convertDFileToTFile(history);
                    triggerEvent(FindResEvent.MimeType.HISTORY, tFileInfoList);
                }
            }
        });
    }

    public void addTMessage(TFileInfo tFileInfo) {
        DFile dFile = FileUtils.buildDFile(tFileInfo);
        fileDao.insert(dFile);
    }

    public List<TFileInfo> convertDFileToTFile(List<DFile> dList) {
        List<TFileInfo> list = new ArrayList<>();
        for (DFile dFile : dList) {
            TFileInfo tFileInfo = new TFileInfo();
            tFileInfo.setName(dFile.getName());
            tFileInfo.setPosition(dFile.getPosition());
            tFileInfo.setLength(dFile.getLength());
            tFileInfo.setPath(dFile.getPath());
            tFileInfo.setExtension(dFile.getExtension());
            tFileInfo.setFullName(dFile.getFullName());
            tFileInfo.setTaskId(dFile.getTaskId());
            tFileInfo.setIsSend(dFile.getIsSend());
            tFileInfo.setFrom(dFile.getFrom());
//            tFileInfo.setPercent(dFile.getPercent());
            switch (dFile.getStatus()) {
                case 0:
                    tFileInfo.setFileEvent(FileEvent.CREATE_FILE_SUCCESS);
                    break;
                case 1:
                    tFileInfo.setFileEvent(FileEvent.SET_FILE_STOP);
                    break;
                case 2:
                    tFileInfo.setFileEvent(FileEvent.SET_FILE_SUCCESS);
                    break;
            }
            FileType fileType = FileUtils.getFileType(context, dFile.getFullName());
            tFileInfo.setFileType(fileType);
            list.add(tFileInfo);
        }
        return list;
    }
}
