package ikabi.com.mobilesafe.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.utils.TimeFomatUtils;
import ikabi.com.mobilesafe.utils.VideoItem;
import ikabi.com.mobilesafe.view.VideoView;



/*import android.widget.VideoView;*/

/**
 *
 */
public class VideoPlayActivity extends BaseActivity {
	//更新进度
	private static final int PROGRESS = 1;
	private static final int DELAYED_HIDECONTROL = 2;
	/**
	 * 全屏
	 */
	private static final int FULL_SCREEN = 3;
	/**
	 * 默认屏幕
	 */
	private static final int DEFAULT_SCREEN = 4;
	private VideoView videoView;
	private Uri uri;
	private TextView video_title;
	private ImageView iv_battery;
	private TextView tv_system_time;
	private Button btn_voice;
	private SeekBar seekbar_voice;
	private Button btn_switch;
	private LinearLayout ll_control_play;

	private TextView tv_current_time;
	private SeekBar seekbar_play;
	private TextView tv_duration;
	private Button btn_exit;
	private Button btn_back;
	private Button btn_play_pause;
	private Button btn_next;
	private Button btn_fullscreen;
	private boolean isPlaying = false;
	private boolean isDestoryed = false;
	private TimeFomatUtils utils;
	private OnclickListener mOnclickListener;
	private MyReceiver receiver;
	private int level;
	private ArrayList<VideoItem> videoItems;
	private int position;
	//定义手势识别器
    private GestureDetector mDetector;
	private boolean isShowControl = false;
	private WindowManager wm;
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case PROGRESS:
					//得到播放进度
					int currentPosition = videoView.getCurrentPosition();
					tv_current_time.setText(utils.stringForTime(currentPosition));
					//SeekBar 进度更新
					seekbar_play.setProgress(currentPosition);
					//设置隐藏控制面板
					hideControlPlayer();

					//消息死循环
					if(!isDestoryed){
                    handler.removeMessages(PROGRESS);
						sendDelayedHideControlPlayer();
					}
					break;
				case DELAYED_HIDECONTROL:
					hideControlPlayer();
					break;
				default:
					break;
			}
		}
	};
	/**
	 * 屏幕的宽
	 */
	private int screenWidth;
	/**
	 * 屏幕的高
	 */
	private int screenHeight;

	/**
	 * 音量大小管理
	 */
	private AudioManager am;
	/**
	 * 当前音量
	 */
	private int currentVolume;
	/**
	 * 最大音量
	 */
	private int maxVolume;

	/**
	 * 设置是否静音
	 * true:静音
	 * false:非静音
	 */
	private boolean isMute = false;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDate();
		initView();
		getDate();
		setDate();
		setListener();
	}

	//设置数据
	private void setDate() {
		if(videoItems != null&&videoItems.size()>0){
        //从播放列表来的数据
			VideoItem videoItem = videoItems.get(position);
			videoView.setVideoPath(videoItem.getData());
			video_title.setText(videoItem.getTitle());
		}else if (uri!=null){
		videoView.setVideoURI(uri);
			video_title.setText(uri.toString());
		}
		//seekbar和音量的总大小进行关联
		seekbar_voice.setMax(maxVolume);
		seekbar_voice.setProgress(currentVolume);
	}

	//得到数据--来至第三方软件，文件管理器、浏览器
	private void getDate() {
		//得到播放列表
		videoItems = (ArrayList<VideoItem>) getIntent().getSerializableExtra("videolist");
		position = getIntent().getIntExtra("position",0);
		//得到播放地址 取出Position播放位置
		uri = getIntent().getData();

	}

	private void initDate() {
		utils = new TimeFomatUtils();
		isDestoryed =false;

		//设置当播放视频的时候不锁屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//得到屏幕的高和宽

		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
		//监听电量变化
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		receiver = new MyReceiver();
		registerReceiver(receiver, filter);
		//实例化手势识别器
		mDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
			@Override
			public void onLongPress(MotionEvent e) {
				/*Toast.makeText(getApplicationContext(),"长按屏幕",Toast.LENGTH_SHORT).show();*/
				startOrPause();
				super.onLongPress(e);
			}

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				/*Toast.makeText(getApplicationContext(),"双击屏幕",Toast.LENGTH_SHORT).show();
				return super.onDoubleTap(e);*/
				if(isFullScreen){
					setVideoType(DEFAULT_SCREEN);

				} else {
					setVideoType(FULL_SCREEN);
				}
				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				/*Toast.makeText(getApplicationContext(),"单击屏幕",Toast.LENGTH_SHORT).show();
				return super.onSingleTapConfirmed(e);*/
				if(isShowControl){
					removeDelayedHideControlPlayer();
					hideControlPlayer();
				}else {
					ShowControlPlayer();
					sendDelayedHideControlPlayer();
				}
				return true;
			}
		});
		//得到当前音量和最大音量值
		am = (AudioManager) getSystemService(AUDIO_SERVICE);
		//当前音量
		currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		//最大音量值：音量范围：0~15之间
		maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

	}
	//移除消息
	private void removeDelayedHideControlPlayer() {
		handler.removeMessages(DELAYED_HIDECONTROL);
	}
	//发送延迟消息
	private void sendDelayedHideControlPlayer() {
		handler.sendEmptyMessageDelayed(DELAYED_HIDECONTROL, 5000);
	}

	/**
	 * 手指在屏幕滑动的起始Y轴坐标
	 */
	private float startY;
	/**
	 * 屏幕滑动的一个范围
	 */
	private float audioTouchRang;
	/**
	 * 滑动钱的音量
	 */
	private int mVol;
	//使用手势识别器
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);//执行父类的方法
		mDetector.onTouchEvent(event);
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN://手指按下屏幕
				removeDelayedHideControlPlayer();
				//1.记录初始Y
				startY = event.getY();
				audioTouchRang = Math.min(screenHeight,screenWidth);
				am.getStreamVolume(AudioManager.STREAM_MUSIC);
				break;
			case MotionEvent.ACTION_MOVE://手指在屏幕上移动
				//2.记录endY
				float endY = event.getY();
				//3.计算偏移量
				float distanceY = startY - endY;
				//4.计算屏幕滑动比例
				float datel = distanceY/audioTouchRang;
				//5.计算改变的音量值 ：改变的音量 = 滑动的距离/总距离*总音量
				float volume = distanceY/audioTouchRang * maxVolume;
				//6.屏幕非法值
				//7.找出要设置的音量值
				float volumeS = Math.min(Math.max(volume+mVol, 0), maxVolume);
				if(datel!=0){
					updateVolume((int) volumeS);
				}
				break;
			case MotionEvent.ACTION_UP://手指在屏幕上离开
				sendDelayedHideControlPlayer();
				break;

			default:
				break;

		}
		return true;//对事件进行处理
	}

	private class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//得到电量的值0~100
			level = intent.getIntExtra("level",0);

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isDestoryed = true;
		//取消注册电量监听
		unregisterReceiver(receiver);
		receiver = null;
	}
	private void setBattery() {
		if(level <= 0){
			iv_battery.setImageResource(R.drawable.ic_battery_0);
		}else if(level <= 10)
			iv_battery.setImageResource(R.drawable.ic_battery_10);
		else if(level <= 20)
			iv_battery.setImageResource(R.drawable.ic_battery_20);
		else if(level <= 40)
			iv_battery.setImageResource(R.drawable.ic_battery_40);
		else if(level <= 60)
			iv_battery.setImageResource(R.drawable.ic_battery_60);
		else if(level <= 80)
			iv_battery.setImageResource(R.drawable.ic_battery_80);
		else if(level <= 100)
			iv_battery.setImageResource(R.drawable.ic_battery_100);
	}
	private void setListener() {

		//设置按钮的监听
		View.OnClickListener mOnclickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				removeDelayedHideControlPlayer();
				sendDelayedHideControlPlayer();
				switch (v.getId()) {
					case R.id.btn_play_pause:
						startOrPause();
						break;
					case R.id.btn_next://下一步
						playNextVideo();
						break;

					case R.id.btn_back://上一步
						playbackVideo();
						break;
					case R.id.btn_fullscreen:
						if(isFullScreen){
							setVideoType(DEFAULT_SCREEN);

						} else {
							setVideoType(FULL_SCREEN);
						}
						break;
					case R.id.btn_voice://设置静音和非静音
						isMute = !isMute;
						updateVolume(currentVolume);
						break;

					default:
						break;
				}
			}
		};

		btn_play_pause.setOnClickListener(mOnclickListener);
		btn_next.setOnClickListener(mOnclickListener);
		//设置上一曲点击事件监听
		btn_back.setOnClickListener(mOnclickListener);
		//全屏按钮点击事件监听
		btn_fullscreen.setOnClickListener(mOnclickListener);
		//监听静音按钮
		btn_voice.setOnClickListener(mOnclickListener);



		//设置SeekBar拖动监听
		seekbar_play.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					//拖动到具体视频位置
					videoView.seekTo(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				removeDelayedHideControlPlayer();

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				sendDelayedHideControlPlayer();
			}
		});

		//设置SeekBar音量改变状态的监听
		seekbar_voice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(fromUser){
					updateVolume(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				removeDelayedHideControlPlayer();

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				sendDelayedHideControlPlayer();

			}
		});
		//监听是否准备好了-开始播放
		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				//开始播放视频
				videoView.start();
				isPlaying = true;

				//设置屏幕为默认大小
				setVideoType(DEFAULT_SCREEN);

				//得到视频长度
				int duration = videoView.getDuration();
				tv_duration.setText(utils.stringForTime(duration));
				//视频的总时长关联SeekBar
				seekbar_play.setMax(duration);

				//设置电量的显示
				setBattery();

				//设置当前手机时间
				tv_system_time.setText(utils.getSystemTime());

				//开始更新播放进度
				handler.sendEmptyMessage(PROGRESS);
			}
		});

		//设置监听播放完成
		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				playNextVideo();

			}
		});
		//使用 VideoView 中的MediaController来控制媒体播放
		/*videoView.setMediaController(new MediaController(this));*/
	}

	/**
	 * 调节音量的方法flasg :0不显示  1显示默认音量调节
	 * @param volume:要调节成的音量值
	 */
	private void updateVolume(int volume) {
		if (isMute) {
			//静音
			am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			seekbar_voice.setProgress(0);
		} else {
			//非静音
			am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
			seekbar_voice.setProgress(volume);
		}
		currentVolume = volume;
	}
	//视频的播放和暂停
	private void startOrPause() {
		if (isPlaying) {
            //暂停
            videoView.pause();
            btn_play_pause.setBackgroundResource(R.drawable.play_selector);
        } else {
            //播放
            videoView.start();
            btn_play_pause.setBackgroundResource(R.drawable.pause_selector);
        }
		isPlaying = !isPlaying;
	}

	//下一步
	private void playNextVideo() {
		//如果没有下一个视频，退出播放器
		//如果有下一个视频播放下一个
		if(videoItems != null && videoItems.size()>0){
            position++;//下一个视频
            if(position < videoItems.size()){
                VideoItem videoItem = videoItems.get(position);
                videoView.setVideoPath(videoItem.getData());
                //设置标题
                video_title.setText(videoItem.getTitle());
                setPlayOrPauseStatus();


            }else {
                //最后一个位置
                position = videoItems.size()-1;
                //最后一个视频
                Toast.makeText(getApplicationContext(), "最后一个视频", Toast.LENGTH_SHORT).show();
                finish();//退出播放器
            }
        }
	}
	//上一步
	private void playbackVideo() {

		if(videoItems != null && videoItems.size()>0){
			position--;//上一个视频
			if(position >= 0){
				VideoItem videoItem = videoItems.get(position);
				videoView.setVideoPath(videoItem.getData());
				//设置标题
				video_title.setText(videoItem.getTitle());
				setPlayOrPauseStatus();


			}else {
				//最后一个位置
				position = 0;
				//最后一个视频
				Toast.makeText(getApplicationContext(), "第一个视频", Toast.LENGTH_SHORT).show();

			}
		}
	}

	private void setPlayOrPauseStatus() {
		//如果是最后一个视频,下一步按钮就应该不可以点击，并且按钮变灰
		if(position == 0){
            //第一个位置的视频
            btn_back.setBackgroundResource(R.drawable.btn_back_huise);
            btn_back.setEnabled(false);
        }else if(position == videoItems.size()-1){
            //最后一个位置的视频
            btn_next.setBackgroundResource(R.drawable.btn_next_huise);
            btn_next.setEnabled(false);
        }else {
            btn_back.setBackgroundResource(R.drawable.back_selector);
            btn_back.setEnabled(true);
            btn_next.setBackgroundResource(R.drawable.next_selector);
            btn_next.setEnabled(true);
        }
	}


	/*
    * 初始化View
    */
	private void initView() {
		setTitleBar(View.GONE);
		videoView = (VideoView) findViewById(R.id.videoview);
		video_title = (TextView) findViewById(R.id.video_title);
		iv_battery = (ImageView) findViewById(R.id.iv_battery);
		tv_system_time = (TextView) findViewById(R.id.tv_system_time);
		btn_voice = (Button) findViewById(R.id.btn_voice);
		seekbar_voice = (SeekBar) findViewById(R.id.seekbar_voice);
		btn_switch = (Button) findViewById(R.id.btn_switch);
		tv_current_time = (TextView) findViewById(R.id.tv_current_time);
		seekbar_play = (SeekBar) findViewById(R.id.seekbar_play);
		tv_duration = (TextView) findViewById(R.id.tv_duration);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_play_pause = (Button) findViewById(R.id.btn_play_pause);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_fullscreen = (Button) findViewById(R.id.btn_fullscreen);
		ll_control_play = (LinearLayout) findViewById(R.id.ll_control_play);
	}

	@Override
	public View setContentView() {
    return View.inflate(this, R.layout.activity_videoplay, null);
	}
	@Override
	public void rightButtonClick(){
		Toast.makeText(this, "右边点击按键成功", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void leftButtonClick(){
		finish();
	}

	private class OnclickListener {
	}
	//是否显示控制面板

	private void hideControlPlayer(){
		ll_control_play.setVisibility(View.GONE);
		isShowControl = false;
	}
	private void ShowControlPlayer (){
		ll_control_play.setVisibility(View.VISIBLE);
		isShowControl = true;
	}

	/**
	 * 是否是全屏
	 * true:全屏
	 * false:默认
	 */
	private boolean isFullScreen = false;

	/**
	 * 设置视频的类型：全屏和默认
	 * @param type
	 */
	private void setVideoType(int type){
		switch (type) {
			case FULL_SCREEN:
				videoView.setVideoSize(screenWidth, screenHeight);
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
				isFullScreen = true;
				btn_fullscreen.setBackgroundResource(R.drawable.original_size_selector);
				break;
			case DEFAULT_SCREEN://默认
				int mVideoWidth = videoView.getVideoWidth();
				int mVideoHeight = videoView.getVideoHeight();
				//计算后视频该设置多宽和多高
				int width = screenWidth;
				int height = screenHeight;

				if (mVideoWidth > 0 && mVideoHeight > 0) {
                   if ( mVideoWidth * height  > width * mVideoHeight ) {
							//Log.i("@@@", "image too tall, correcting");
							height = width * mVideoHeight / mVideoWidth;
					} else if ( mVideoWidth * height  < width * mVideoHeight ) {
					 width = height * mVideoWidth / mVideoHeight;
					 } else {
					   //Log.i

					}
		}

				videoView.setVideoSize(width,height);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
				isFullScreen = false;
				btn_fullscreen.setBackgroundResource(R.drawable.fullscreen_selector);
				break;


		}

	}
}

