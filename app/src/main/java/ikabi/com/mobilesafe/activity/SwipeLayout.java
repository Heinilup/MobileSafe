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
 * @ Data:16/3/7
 */
public class SwipeLayout extends FrameLayout {

    private View contentView;
    private View deletetView;
    private int deleteHeight;
    private int deleteWidth;
    private int contentWidth;
    private ViewDragHelper mViewDragHelper;

    public SwipeLayout(Context context) {
        super(context);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    enum SwipeState{
        Open,Close
    }

    private SwipeState currentState = SwipeState.Close;//当前默认是关闭状态

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, mCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        deletetView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        deleteHeight = deletetView.getMeasuredHeight();
        deleteWidth = deletetView.getMeasuredWidth();
        contentWidth = contentView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        contentView.layout(0, 0, contentWidth, deleteHeight);
        deletetView.layout(contentView.getRight(), 0, contentView.getRight() + deleteWidth, deleteHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = mViewDragHelper.shouldInterceptTouchEvent(ev);

        //如果当前有打开的，则需要直接拦截，交给onTouch处理
        if(!SwipeLayoutManager.getInstance().isShouldSwipe(this)){
            //先关闭已经打开的layout
            SwipeLayoutManager.getInstance().closeCurrentLayout();

            result = true;
        }

        return result;
    }

    private float downX,downY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果当前有打开的，则下面的逻辑不能执行
        if(!SwipeLayoutManager.getInstance().isShouldSwipe(this)){
            requestDisallowInterceptTouchEvent(true);
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //1.获取x和y方向移动的距离
                float moveX = event.getX();
                float moveY = event.getY();
                float delatX = moveX - downX;//x方向移动的距离
                float delatY = moveY - downY;//y方向移动的距离
                if(Math.abs(delatX)>Math.abs(delatY)){
                    //表示移动是偏向于水平方向，那么应该SwipeLayout应该处理，请求listview不要拦截
                    requestDisallowInterceptTouchEvent(true);
                }
                //更新downX，downY
                downX = moveX;
                downY = moveY;
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == contentView || child == deletetView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return deleteWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == contentView) {
                if (left > 0) left = 0;
                if (left < -deleteWidth) left = -deleteWidth;
            } else if (child == deletetView) {
                if (left > contentWidth) left = contentWidth;
                if (left < contentWidth - deleteWidth) left = contentWidth - deleteWidth;
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == contentView) {
                //手动移动deleteView
                deletetView.layout(deletetView.getLeft() + dx, deletetView.getTop() + dy, deletetView.getRight() + dx, deletetView.getBottom() + dy);
            } else if (deletetView == changedView) {
                //手动移动contentView
                contentView.layout(contentView.getLeft() + dx, contentView.getTop() + dy, contentView.getRight() + dx, contentView.getBottom() + dy);
            }
            //判断开和关闭的逻辑
            if(contentView.getLeft()==0 && currentState!=SwipeState.Close){
                //说明应该将state更改为关闭
                currentState = SwipeState.Close;

                //回调接口关闭的方法
                if(listener!=null){
                    listener.close(getTag());
                }

                //说明当前的SwipeLayout已经关闭，需要让Manager清空一下
                SwipeLayoutManager.getInstance().clearCurrentLayout();
            }else if (contentView.getLeft()==-deleteWidth && currentState!=SwipeState.Open) {
                //说明应该将state更改为开
                currentState = SwipeState.Open;

                //回调接口打开的方法
                if(listener!=null){
                    listener.open(getTag());
                }
                //当前的Swipelayout已经打开，需要让Manager记录一下下
                SwipeLayoutManager.getInstance().setSwipeLayout(SwipeLayout.this);
            }
        }


        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if(contentView.getLeft()<-deleteWidth/2){
                open();
            } else {
                close();
            }
        }
    };

    public void close() {
        mViewDragHelper.smoothSlideViewTo(contentView,0,contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    public void open() {
        mViewDragHelper.smoothSlideViewTo(contentView,-deleteWidth,contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    @Override
    public void computeScroll() {
        if(mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private OnSwipeStateChangeListener listener;
    public void setOnSwipeStateChangeListener(OnSwipeStateChangeListener listener){
        this.listener = listener;
    }
    public interface OnSwipeStateChangeListener{
        void open(Object tag);
        void close(Object tag);
    }
}
