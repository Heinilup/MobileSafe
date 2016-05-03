package ikabi.com.mobilesafe.business.history;


import java.util.List;

import ikabi.com.mobilesafe.business.model.TFileInfo;

/**
 * 历史消息
 */
public class HistoryEvent {

    private List<TFileInfo> historyList;

    public List<TFileInfo> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<TFileInfo> historyList) {
        this.historyList = historyList;
    }
}
