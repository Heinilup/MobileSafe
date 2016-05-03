package ikabi.com.mobilesafe.business.image;

import android.content.Context;

import java.util.List;

import ikabi.com.mobilesafe.business.BaseLogic;
import ikabi.com.mobilesafe.business.event.FindResEvent;
import ikabi.com.mobilesafe.business.model.TFileInfo;

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
