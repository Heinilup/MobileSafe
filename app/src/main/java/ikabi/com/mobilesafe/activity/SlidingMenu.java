package ikabi.com.mobilesafe.activity;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

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

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SlidingMenu.this);
        }
    }
}
