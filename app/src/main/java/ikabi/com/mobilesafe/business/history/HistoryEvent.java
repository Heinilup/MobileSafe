package ikabi.com.mobilesafe.business.history;

import com.huangjiang.business.model.TFileInfo;

import java.util.List;

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
