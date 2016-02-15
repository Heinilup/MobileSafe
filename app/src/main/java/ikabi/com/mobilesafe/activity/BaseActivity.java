package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import ikabi.com.mobilesafe.R;

public abstract class BaseActivity extends Activity {
	private Button btn_left;
	private TextView tv_title;
	private Button btn_right;
	private FrameLayout top_titlebar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE); 载app_theme里面注释了 <item name="android:windowNoTitle">true</item>
		setContentView(R.layout.activity_base);
		initView();
		setOnclickListener();
	}

	private void setOnclickListener() {
		btn_left.setOnClickListener(mOnClickListener);
		btn_right.setOnClickListener(mOnClickListener);
		
	}
	OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_left:
				leftButtonClick();
				
				break;
            case R.id.btn_right:
            	rightButtonClick();
				break;

			default:
				break;
			}
			
		}

	};

	private void initView() {
		btn_left = (Button) findViewById(R.id.btn_left);
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_right = (Button) findViewById(R.id.btn_right);
		top_titlebar = (FrameLayout) findViewById(R.id.top_titlebar);
		LinearLayout ll_child_content = (LinearLayout) findViewById(R.id.ll_child_content);
		//添加子类布局文件
		View child = setContentView();
	    if (child != null) {
	    	//导入父类包文件
			LayoutParams params = new LayoutParams(-1, -1);
			ll_child_content.addView(child, params);
		}
		
	}
	/**
	 * 这个方法由孩子实现
	 * @return
	 */
	 public abstract View setContentView();
	/**
	 * 设置左边按钮状态
	 */
	public void setLeftButton(int visibility){
		btn_left.setVisibility(visibility);
	}
	/**
	 * 设置标题栏状态
	 */
	public void setTitleBar(int visibility){
		top_titlebar.setVisibility(visibility);
	}
	/**
	 * 设置标题
	 * @param title 要设置的标题内容
	 */
	public void setTitle(String title) {
		tv_title.setText(title);
	}
	
	/**
	 * 设置右边按钮状态
	 */
	public void setRightButton(int visibility){
		btn_right.setVisibility(visibility);
	}
	public void rightButtonClick() {
		// TODO Auto-generated method stub
		
	}

	public void leftButtonClick() {
		// TODO Auto-generated method stub
		
	}

}
