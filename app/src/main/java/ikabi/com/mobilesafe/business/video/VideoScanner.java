package ikabi.com.mobilesafe.business.video;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

public class VideoScanner {

	private Context mContext;

	public VideoScanner(Context context) {
		mContext = context;
	}
	
	@SuppressLint("HandlerLeak")
	public void scanVideo(final ScanVideoCompleteCallBack callback){
		final Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				callback.scanVideoComplete((Cursor) msg.obj);
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				Uri mImageUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = mContext.getContentResolver();
				Cursor mCursor = mContentResolver.query(mImageUri, null, null, null, null);
				Message msg = mHandler.obtainMessage();
				msg.obj = mCursor;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	public static interface ScanVideoCompleteCallBack {
		public void scanVideoComplete(Cursor cursor);
	}
}
