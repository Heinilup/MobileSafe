package ikabi.com.mobilesafe.business.video;

import android.content.Context;

import com.huangjiang.business.BaseLogic;
import com.huangjiang.business.event.FindResEvent;
import com.huangjiang.business.model.TFileInfo;
import com.huangjiang.core.ThreadPoolManager;

import java.util.List;

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
