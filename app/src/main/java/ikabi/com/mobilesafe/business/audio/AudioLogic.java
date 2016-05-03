package ikabi.com.mobilesafe.business.audio;

import android.content.Context;

import java.util.List;

import ikabi.com.mobilesafe.business.BaseLogic;
import ikabi.com.mobilesafe.business.event.FindResEvent;
import ikabi.com.mobilesafe.business.model.TFileInfo;
import ikabi.com.mobilesafe.utils.ThreadPoolManager;

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
