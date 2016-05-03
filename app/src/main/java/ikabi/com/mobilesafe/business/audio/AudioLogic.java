package ikabi.com.mobilesafe.business.audio;

import android.content.Context;

import com.huangjiang.business.BaseLogic;
import com.huangjiang.business.event.FindResEvent;
import com.huangjiang.business.model.TFileInfo;
import com.huangjiang.core.ThreadPoolManager;

import java.util.List;

/**
 *
 */
public class AudioLogic extends BaseLogic {


    private AudioInterface audioInterface;

    public AudioLogic(Context context) {
        audioInterface = new AudioInterface(context);
    }

    public void searchAudio() {
        ThreadPoolManager.getInstance(AudioLogic.class.getName()).startTaskThread(new Runnable() {
            @Override
            public void run() {
                List<TFileInfo> audioList = audioInterface.searchAudio();
                triggerEvent(FindResEvent.MimeType.AUDIO, audioList);
            }
        });
    }

}
