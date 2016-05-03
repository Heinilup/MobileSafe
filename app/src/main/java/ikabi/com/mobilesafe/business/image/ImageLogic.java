package ikabi.com.mobilesafe.business.image;

import android.content.Context;

import com.huangjiang.business.BaseLogic;
import com.huangjiang.business.event.FindResEvent;
import com.huangjiang.business.model.TFileInfo;
import com.huangjiang.core.ThreadPoolManager;

import java.util.List;

/**
 * 查找本地图片
 */
public class ImageLogic extends BaseLogic {

    private ImageInterface imageInterface;

    public ImageLogic(Context context) {
        imageInterface = new ImageInterface(context);
    }


    public void searchImage() {
        ThreadPoolManager.getInstance(ImageLogic.class.getName()).startTaskThread(new Runnable() {
            @Override
            public void run() {
                List<TFileInfo> imageList = imageInterface.searchImage();
                triggerEvent(FindResEvent.MimeType.IMAGE, imageList);
            }
        });
    }
}
