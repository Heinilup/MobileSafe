package ikabi.com.mobilesafe.business;

import com.huangjiang.business.event.FindResEvent;
import com.huangjiang.business.event.OpFileEvent;
import com.huangjiang.business.model.TFileInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class BaseLogic {

    public void triggerEvent(FindResEvent.MimeType mimeType, List<TFileInfo> fileInfoList) {
        FindResEvent searchTFileInfoEvent = new FindResEvent(mimeType, fileInfoList);
        EventBus.getDefault().post(searchTFileInfoEvent);
    }

    public void triggerEvent(OpFileEvent event) {
        EventBus.getDefault().post(event);
    }

}
