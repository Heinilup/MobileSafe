package ikabi.com.mobilesafe.business.event;


import ikabi.com.mobilesafe.business.model.TFileInfo;

/**
 * 文件操作
 */
public class OpFileEvent {

    private TFileInfo tFileInfo;
    private OpType opType;
    private boolean isSuccess;

    public OpFileEvent(OpType opType, TFileInfo tFileInfo) {
        this.opType = opType;
        this.tFileInfo = tFileInfo;
    }

    public TFileInfo getTFileInfo() {
        return tFileInfo;
    }

    public void setTFileInfo(TFileInfo tFileInfo) {
        this.tFileInfo = tFileInfo;
    }

    public OpType getOpType() {
        return opType;
    }

    public void setOpType(OpType opType) {
        this.opType = opType;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public enum OpType {
        DELETE,
        RENAME,
        BACKUP,
        UNINSTALL
    }
}
