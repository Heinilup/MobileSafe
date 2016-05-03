package ikabi.com.mobilesafe.business.event;


import java.util.List;

import ikabi.com.mobilesafe.business.model.TFileInfo;

/**
 *
 */
public class FindResEvent {

    public FindResEvent(MimeType mimeType, List<TFileInfo> fileInfoList) {
        this.mimetype = mimeType;
        this.fileInfoList = fileInfoList;
    }

    private MimeType mimetype = MimeType.NONE;

    public void setMimeType(MimeType mimetype) {
        this.mimetype = mimetype;
    }

    public MimeType getMimeType() {
        return mimetype;
    }

    private List<TFileInfo> fileInfoList;

    public List<TFileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<TFileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }

    private int stateCode;

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public int getStateCode() {
        return stateCode;
    }

    public enum MimeType {
        AUDIO,
        APK,
        EXPLORER,
        IMAGE,
        SEARCH,
        VIDEO,
        NONE,
        HISTORY
    }

}
