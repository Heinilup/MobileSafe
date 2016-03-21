package ikabi.com.mobilesafe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016-03-22 0022.
 */
public class SwitchToggleView extends View {

    private final static int STATE_NONE = 0;
    private final static int STATE_DOWN = 1;
    private final static int STATE_MOVE = 2;
    private final static int STATE_UP = 3;

    private Bitmap mSwitchBackground;// 背景的图片
    private Bitmap mSwitchSlide;// 滑块的图片

    private boolean isOpened = true;// 用来标记滑块是否打开
    private int mState = STATE_NONE;// 用来标记状态

    private Paint mPaint = new Paint();
    private float mCurrentX;
    private OnSwitchListenr mListener;

    public SwitchToggleView(Context context) {
        this(context, null);
    }

    public SwitchToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置背景的资源
     *
     * @param resId
     */
    public void setSwitchBackground(int resId) {
        mSwitchBackground = BitmapFactory.decodeResource(getResources(), resId);

    }

    /**
     *
     * @param resId
     */
    public void setSwitchSlide(int resId) {
        mSwitchSlide = BitmapFactory.decodeResource(getResources(), resId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mSwitchBackground != null) {
            int width = mSwitchBackground.getWidth();
            int height = mSwitchBackground.getHeight();

            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制背景的显示
        if (mSwitchBackground != null) {

            int left = 0;
            int top = 0;
            canvas.drawBitmap(mSwitchBackground, left, top, mPaint);
        }

        if (mSwitchSlide == null) {
            return;
        }

        int slideWidth = mSwitchSlide.getWidth();// 滑块的宽度
        int switchWidth = mSwitchBackground.getWidth();

        switch (mState) {
            case STATE_DOWN:
            case STATE_MOVE:
                // 当按下的时候
                if (!isOpened) {
                    // 如果滑块是关闭的
                    // 点击滑块的左侧，滑块不动
                    if (mCurrentX < slideWidth / 2f) {
                        // 绘制在左侧
                        canvas.drawBitmap(mSwitchSlide, 0, 0, mPaint);
                    } else {
                        // 点击滑块的右侧，滑块的中线和按下的x坐标对齐
                        float left = mCurrentX - slideWidth / 2f;
                        float maxLeft = switchWidth - slideWidth;
                        if (left > maxLeft) {
                            left = maxLeft;
                        }
                        canvas.drawBitmap(mSwitchSlide, left, 0, mPaint);
                    }
                } else {
                    // 如果滑块是打开的
                    float middle = switchWidth - slideWidth / 2f;
                    if (mCurrentX > middle) {
                        // 绘制为打开
                        canvas.drawBitmap(mSwitchSlide, switchWidth - slideWidth,
                                0, mPaint);
                    } else {
                        float left = mCurrentX - slideWidth / 2f;
                        if (left < 0) {
                            left = 0;
                        }
                        canvas.drawBitmap(mSwitchSlide, left, 0, mPaint);
                    }
                }
                break;
            case STATE_UP:
            case STATE_NONE:
                if (!isOpened) {
                    canvas.drawBitmap(mSwitchSlide, 0, 0, mPaint);
                } else {
                    canvas.drawBitmap(mSwitchSlide, switchWidth - slideWidth, 0,
                            mPaint);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mState = STATE_DOWN;

                mCurrentX = event.getX();

                invalidate();// 触发刷新-->主线程调用
                // postInvalidate();// 触发刷新--->子线程中调用

                break;
            case MotionEvent.ACTION_MOVE:
                mState = STATE_MOVE;

                mCurrentX = event.getX();

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mState = STATE_UP;

                mCurrentX = event.getX();

                // 判断状态改变--> isOpened
                int switchWidth = mSwitchBackground.getWidth();

                if (switchWidth / 2f > mCurrentX && isOpened) {
                    // 关闭状态
                    isOpened = false;

                    if (mListener != null) {
                        mListener.onSwitchChanged(isOpened);
                    }

                } else if (switchWidth / 2f <= mCurrentX && !isOpened) {
                    isOpened = true;

                    if (mListener != null) {
                        mListener.onSwitchChanged(isOpened);
                    }
                }
                invalidate();
                break;
            default:
                break;
        }

        // 消费touch事件
        return true;
    }

    public void setOnSwitchListener(OnSwitchListenr listener) {
        this.mListener = listener;
    }

    public interface OnSwitchListenr {

        // 开关状态改变--->
        void onSwitchChanged(boolean isOpened);
    }
}
