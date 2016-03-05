/*
package ikabi.com.mobilesafe.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

*/
/**
 * 当slideMenu打开的时候，拦截并消费掉触摸事件
 * 
 * @author Administrator
 * 
 *//*

public class MyLinearLayout extends LinearLayout {
	public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLinearLayout(Context context) {
		super(context);
	}
	private SlideMenu slideMenu;
	public void setSlideMenu(SlideMenu slideMenu){
		this.slideMenu = slideMenu;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(slideMenu!=null && slideMenu.getCurrentState()==DragState.Open){
			//如果slideMenu打开则应该拦截并消费掉事件
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(slideMenu!=null && slideMenu.getCurrentState()==DragState.Open){
			if(event.getAction()==MotionEvent.ACTION_UP){
				//抬起则应该关闭slideMenu
				slideMenu.close();
			}
			
			//如果slideMenu打开则应该拦截并消费掉事件
			return true;
		}
		return super.onTouchEvent(event);
	}
}
*/
