package ikabi.com.mobilesafe.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.FloatEvaluator;
import com.nineoldandroids.animation.IntEvaluator;
import com.nineoldandroids.view.ViewHelper;

import ikabi.com.mobilesafe.utils.ColorUtil;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/3/5
 */
public class SlidingMenu extends FrameLayout {
    private View menuView;
    private View mainView;
    private ViewDragHelper mViewDragHelper;
    private int mWidth;
    private float dragRange;
    private FloatEvaluator floatEvaluator;//float的计算器
    private IntEvaluator intEvaluator;//int的计算器

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlidingMenu(Context context) {
        super(context);
        init();
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, mCallback);
        floatEvaluator = new FloatEvaluator();
        intEvaluator = new IntEvaluator();
    }

    //定义状态常量
    enum DragState{
        Open,Close;
    }
    private DragState currentState = DragState.Close;//当前SlideMenu的状态默认是关闭的

    /**
     * 获取当前的状态
     * @return
     */
    public DragState getCurrentState(){
        return currentState;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //简单异常处理
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("SlidingMenu only hava 2 child");
        }
        mainView = getChildAt(0);
        menuView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        dragRange = mWidth * 0.6f;

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == menuView || child == mainView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return (int) dragRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mainView) {
                if (left < 0) left = 0;//限制mainview的左边
                if (left > dragRange) left = (int) dragRange; //限制mainview的右边
            } /*else if (child == menuView) {
                left = left - dx;
            }*/
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == menuView) {
                //固定menuView
                menuView.layout(0,0,menuView.getMeasuredWidth(),menuView.getMeasuredHeight());
                //让mainview动起来
                int newLeft = mainView.getLeft() + dx;
                if (newLeft < 0)newLeft = 0;
                if (newLeft > dragRange) newLeft = (int) dragRange;
                mainView.layout(newLeft, mainView.getTop() + dy, newLeft + mainView.getMeasuredWidth(), mainView.getBottom() + dy);
            }

            //计算滑动的百分比
            float fraction = mainView.getLeft()/dragRange;
            executeAnim(fraction);
            //2.执行伴随动画
            executeAnim(fraction);
            //3.更改状态，回调listener的方法
            if(fraction==0 && currentState!=DragState.Close){
                //更改状态为关闭，并回调关闭的方法
                currentState = DragState.Close;
                if(listener!=null)listener.onClose();
            }else if (fraction==1f && currentState!=DragState.Open) {
                //更改状态为打开，并回调打开的方法
                currentState = DragState.Open;
                if(listener!=null)listener.onOpen();
            }
            //将drag的fraction暴漏给外界
            if(listener!=null){
                listener.onDraging(fraction);
            }

        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (mainView.getLeft() < dragRange/2){
                //在左半边
                mViewDragHelper.smoothSlideViewTo(mainView,0,mainView.getTop());
                ViewCompat.postInvalidateOnAnimation(SlidingMenu.this);
            } else {
                //在右半边
                mViewDragHelper.smoothSlideViewTo(mainView, (int) dragRange,mainView.getTop());
                ViewCompat.postInvalidateOnAnimation(SlidingMenu.this);
            }

        }
    };

    public void executeAnim (float fraction){
        //fraction:0-1
        //缩小mainView
//		float scaleValue = 0.8f+0.2f*(1-fraction);//1-0.8f
        ViewHelper.setScaleX(mainView, floatEvaluator.evaluate(fraction,1f,0.8f));
        ViewHelper.setScaleY(mainView, floatEvaluator.evaluate(fraction,1f,0.8f));
        //移动menuView
        ViewHelper.setTranslationX(menuView,intEvaluator.evaluate(fraction,-menuView.getMeasuredWidth()/2,0));
        //放大menuView
        ViewHelper.setScaleX(menuView,floatEvaluator.evaluate(fraction,0.5f,1f));
        ViewHelper.setScaleY(menuView,floatEvaluator.evaluate(fraction,0.5f,1f));
        //改变menuView的透明度
        ViewHelper.setAlpha(menuView,floatEvaluator.evaluate(fraction,0.3f,1f));

        //给SlideMenu的背景添加黑色的遮罩效果
        getBackground().setColorFilter((Integer) ColorUtil.evaluateColor(fraction, Color.BLACK,Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);

    }
    private OnDragStateChangeListener listener;
    public void setOnDragStateChangeListener(OnDragStateChangeListener listener){
        this.listener = listener;
    }
    public interface OnDragStateChangeListener{
        /**
         * 打开的回调
         */
        void onOpen();
        /**
         * 关闭的回调
         */
        void onClose();
        /**
         * 正在拖拽中的回调
         */
        void onDraging(float fraction);
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SlidingMenu.this);
        }
    }
}
