package ikabi.com.mobilesafe.business.video;

import android.content.Context;

import java.util.List;

import ikabi.com.mobilesafe.business.BaseLogic;
import ikabi.com.mobilesafe.business.event.FindResEvent;
import ikabi.com.mobilesafe.business.model.TFileInfo;
import ikabi.com.mobilesafe.utils.ThreadPoolManager;

/**
 * 查找本地图片
 */
public class VideoLogic extends BaseLogic {

    private VideoInterface videoInterface;

    public VideoLogic(Context context) {
        videoInterface = new VideoInterface(context);
    }


    public void searchImage() {
        ThreadPoolManager.getInstance(VideoLogic.class.getName()).startTaskThread(new Runnable() {
            @Override
            public void run() {
                List<TFileInfo> videoList = videoInterface.searchVideo();
                triggerEvent(FindResEvent.MimeType.VIDEO, videoList);
            }
        });
    }
}
