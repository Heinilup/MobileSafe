package ikabi.com.mobilesafe.business;


import java.util.List;

import eventbus.EventBus;
import ikabi.com.mobilesafe.business.event.FindResEvent;
import ikabi.com.mobilesafe.business.event.OpFileEvent;
import ikabi.com.mobilesafe.business.model.TFileInfo;


public class BaseLogic {

    public void triggerEvent(FindResEvent.MimeType mimeType, List<TFileInfo> fileInfoList) {
        FindResEvent searchTFileInfoEvent = new FindResEvent(mimeType, fileInfoList);
        EventBus.getDefault().post(searchTFileInfoEvent);
    }

    public void triggerEvent(OpFileEvent event) {
        EventBus.getDefault().post(event);
    }

}
