package ikabi.com.mobilesafe.view;

import android.content.Context;
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
public class DragLayout extends FrameLayout {
    private View redView;
    private View blackView;
    private ViewDragHelper mViewDragHelper;

    public DragLayout(Context context) {
        super(context);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, mCallback);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        redView = getChildAt(0);
        blackView = getChildAt(1);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int size = getResources().getDimensionPixelSize(R.dimen.width_drag);
//        int measureSpec = MeasureSpec.makeMeasureSpec(redView.getLayoutParams().width, MeasureSpec.EXACTLY);
//        redView.measure(measureSpec, measureSpec);
//        blackView.measure(measureSpec, measureSpec);
//
//        measureChild(redView, widthMeasureSpec, heightMeasureSpec);
//        measureChild(blackView, widthMeasureSpec, heightMeasureSpec);
//    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        //int left = getPaddingLeft();获取到左侧数值
        int left = getPaddingLeft() + getMeasuredWidth() / 2 - redView.getMeasuredWidth() / 2;
        int top = getPaddingTop();
        redView.layout(left, top, left + redView.getMeasuredWidth(), top + redView.getMeasuredHeight());
        blackView.layout(left, redView.getBottom(), left + blackView.getMeasuredWidth(), redView.getBottom() + blackView.getMeasuredHeight());

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //rang让ViewDragHelper帮我们判断是否应该拦截
        boolean result = mViewDragHelper.shouldInterceptTouchEvent(ev);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将触摸事件交给mViewDragHelper处理
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        //用于判断是否捕捉child的触摸事件
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == blackView || child == redView;
        }

        /**
         * @param child 获取view水平方向推拖拽范围,但是目前不能限制边界,返回值目前用在手指抬起的时候view缓慢移动的动画时间计算上
         * @return 最好不要返回0
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return super.getViewVerticalDragRange(child);
        }

        /**
         * @param child 控制在水平方向的移动
         * @param left 表示ViewDragHelper认为你想让当前child的left改变的值,left=child.getLeft()+ dx
         * @param dx 本次child水平方向移动的距离
         * @return 表示你想让child的left变成的值
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left < 0) {
                //限制左边
                left = 0;
            } else if (left > (getMeasuredWidth() - child.getMeasuredWidth())) {
                //限制右边界
                left = getMeasuredWidth() - child.getMeasuredWidth();
            }
            return left;
        }

        /**
         * @param child 控制在垂直方向的移动
         * @param top 表示ViewDragHelper认为你想让当前child的left改变的值,left=child.getLeft()+ dx
         * @param dy 本次child垂直方向移动的距离
         * @return 表示你真正想让child的top变成的值
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top < 0) {
                top = 0;
            } else if (top > getMeasuredHeight() - child.getMeasuredHeight()) {
                top = getMeasuredHeight() - child.getMeasuredHeight();


            }
            return top;
        }

        //当view被开始捕获和解析的回调
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
//            LogUtils.d("","onViewCaptured");
        }

        /**
         * @param changedView 位置改变的child
         * @param left child最新的left
         * @param top child最新的top
         * @param dx 本次水平移动的距离
         * @param dy 本次垂直移动的距离
         */
        //修正
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //LogUtils.d("ZSJ", "left" + left + "top" + top + "dx" + dx + "dy" + dy);
            if (changedView == blackView){
                //blackView移动的时候让redView跟随移动
                redView.layout(redView.getLeft() + dx, redView.getTop() + dy , redView.getRight() + dx , redView.getBottom() + dy);
            } else if (changedView == redView){
                //redView移动的时候让blackView跟随移动
                blackView.layout(blackView.getLeft() + dx, blackView.getTop() + dy , blackView.getRight() + dx , blackView.getBottom() + dy);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }
    };

}
