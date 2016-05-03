package ikabi.com.mobilesafe.business.search;

import android.content.Context;

import com.huangjiang.business.BaseLogic;
import com.huangjiang.business.app.AppInterface;
import com.huangjiang.business.audio.AudioInterface;
import com.huangjiang.business.event.FindResEvent;
import com.huangjiang.business.image.ImageInterface;
import com.huangjiang.business.model.TFileInfo;
import com.huangjiang.business.video.VideoInterface;
import com.huangjiang.core.ThreadPoolManager;
import com.huangjiang.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 在图片,音频,视频三种资源里面查找
 */
public class SearchLogic extends BaseLogic {

    Logger logger = Logger.getLogger(SearchLogic.class);


    private AudioInterface audioInterface;
    private ImageInterface imageInterface;
    private VideoInterface videoInterface;
    private AppInterface appInterface;

    public SearchLogic(Context context) {
        audioInterface = new AudioInterface(context);
        imageInterface = new ImageInterface(context);
        videoInterface = new VideoInterface(context);
        appInterface = new AppInterface(context);
    }

    public void searchResource(final String searchKey) {
        ThreadPoolManager.getInstance(SearchLogic.class.getName()).startTaskThread(new Runnable() {
            @Override
            public void run() {
                List<TFileInfo> searchList = new ArrayList<>();
                List<TFileInfo> imageList = imageInterface.searchImage(searchKey);
                List<TFileInfo> audioList = audioInterface.searchAudio(searchKey);
                List<TFileInfo> videoList = videoInterface.searchVideo(searchKey);
                List<TFileInfo> appList = appInterface.searchApp(searchKey);
                searchList.addAll(imageList);
                searchList.addAll(audioList);
                searchList.addAll(videoList);
                searchList.addAll(appList);
                triggerEvent(FindResEvent.MimeType.SEARCH, searchList);
            }
        });
    }


}
