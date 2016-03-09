package ikabi.com.mobilesafe.refresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.concurrent.Executors;

/**
 * Created by Hitomis on 2016/3/2.
 */
public class HitBlockRefreshView extends LinearLayout implements View.OnTouchListener {

    private static final String tag = "HitBlockRefreshView";

    /**
     * 下拉状态
     */
    public static final int STATUS_PULL_TO_REFRESH = 0;

    /**
     * 释放准备刷新状态
     */
    public static final int STATUS_RELEASE_TO_REFRESH = 1;

    /**
     * 正在刷新状态
     */
    public static final int STATUS_REFRESHING = 2;

    /**
     * 释放后，又按住玩游戏状态
     */
    public static final int STATUS_AGAIN_DOWN = 3;

    /**
     * 刷新完成状态
     */
    public static final int STATUS_REFRESH_FINISHED = 4;

    /**
     * 下拉拖动的黏性比率
     */
    private static final float STICK_RATIO = .65f;

    /**
     * 下拉刷新的回调接口
     */
    private HitBlockRefreshListener mListener;

    /**
     * 下拉头的View
     */
    private HitBlockHeader header;

    /**
     * 需要去下拉刷新的ListView
     */
    private ListView listView;

    /**
     * 下拉控件布局参数
     */
    private MarginLayoutParams headerLayoutParams;

    /**
     * 下拉控件高度
     */
    private int hideHeaderHeight;

    /**
     * 当前状态
     */
    private int currentStatus = STATUS_REFRESH_FINISHED;;

    /**
     * 手指按下时屏幕纵坐标
     */
    private float preDownY;

    /**
     * 在被判定为滚动之前用户手指可以移动的最大值
     */
    private int touchSlop;

    /**
     * 用于控制onLayout中的初始化只需加载一次
     */
    private boolean once;

    /**
     * 当前是否可以下拉，只有ListView滚动到头的时候才允许下拉
     */
    private boolean ableToPull;

    private int tempHeaderTopMargin;

    public HitBlockRefreshView(Context context) {
        this(context, null);
    }

    public HitBlockRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HitBlockRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        header = new HitBlockHeader(context, attrs);
        addView(header, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !once) {
            hideHeaderHeight = -header.getHeight();
            headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
            headerLayoutParams.topMargin = hideHeaderHeight;
            listView = (ListView) getChildAt(1);
            listView.setOnTouchListener(this);
            once = true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        checkAblePull(event);
        if (!ableToPull) return false;
        if (currentStatus == STATUS_AGAIN_DOWN) {
            return handleAgainDownAction(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preDownY = event.getRawY();
                if (currentStatus == STATUS_REFRESHING) { //表示释放后处于刷新状态时候，又按住了
                    currentStatus = STATUS_AGAIN_DOWN;
                    setHeaderTopMarign(0);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float currY = event.getRawY();
                float distance = currY - preDownY;
                float offsetY = distance * STICK_RATIO;
                if (distance <= 0 && headerLayoutParams.topMargin <= hideHeaderHeight) {
                    return false;
                }
                if (distance < touchSlop) {
                    return false;
                }

                if (headerLayoutParams.topMargin > 0 ) { // 头部全部被下拉出来的时候状态转换为释放刷新
                    currentStatus = STATUS_RELEASE_TO_REFRESH;
                }

                if (headerLayoutParams.topMargin > 0) {
                    currentStatus = STATUS_RELEASE_TO_REFRESH;
                } else {
                    currentStatus = STATUS_PULL_TO_REFRESH;
                }

                // 通过偏移下拉头的topMargin值，来实现下拉效果
                setHeaderTopMarign((int) (offsetY + hideHeaderHeight));

                break;
            case MotionEvent.ACTION_UP:
                if (currentStatus == STATUS_PULL_TO_REFRESH) {
                    rollbackHeader();
                }
                if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                    refreshingRollBack2Header();
                }
                break;
        }
        if (currentStatus == STATUS_PULL_TO_REFRESH || currentStatus == STATUS_RELEASE_TO_REFRESH) {
            //让ListView失去焦点, 不可被点击
            disableListView();
            return true;
        }
        return false;
    }

    /**
     * 给header设置topMargin参数
     * @param margin
     */
    private void setHeaderTopMarign(int margin) {
        headerLayoutParams.topMargin = margin;
        header.setLayoutParams(headerLayoutParams);
    }

    /**
     * 禁用ListView，让其失去焦点不可接受点击
     */
    private void disableListView() {
        listView.setPressed(false);
        listView.setFocusable(false);
        listView.setFocusableInTouchMode(false);
    }

    /**
     * 处理手指第二次按住屏幕玩游戏的事件
     * @param event
     * @return
     */
    private boolean handleAgainDownAction(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float currY = event.getRawY();
                float distance = currY - preDownY;
                float offsetY = distance * STICK_RATIO;
                header.moveRacket(offsetY);
                setHeaderTopMarign((int) (offsetY));
                break;
            case MotionEvent.ACTION_UP:
                rollbackHeader();
                break;
        }
        disableListView();
        return true;
    }

    /**
     * 根据当前ListView的滚动状态来设定 {@link #ableToPull}
     * 的值，每次都需要在onTouch中第一个执行，这样可以判断出当前应该是滚动ListView，还是应该进行下拉。
     *
     * @param event
     */
    private void checkAblePull(MotionEvent event) {
        View firstChild = listView.getChildAt(0);
        if (firstChild != null) {
            int firstVisiblePos = listView.getFirstVisiblePosition();
            if (firstVisiblePos == 0 && firstChild.getTop() == 0) {
                // 如果首个元素的上边缘，距离父布局值为0，就说明ListView滚动到了最顶部，此时应该允许下拉刷新
                if (!ableToPull) {
                    preDownY = event.getRawY();
                }
                ableToPull = true;
            } else { // 反之
                if (headerLayoutParams.topMargin != hideHeaderHeight) {
                    setHeaderTopMarign(hideHeaderHeight);
                }
                ableToPull = false;
            }
        } else {
            // 如果ListView中没有元素，也应该允许下拉刷新
            ableToPull = true;
        }
    }

    /**
     * 给下拉刷新控件注册一个监听器。
     *
     * @param listener
     *            监听器的实现。
     */
    public void setOnRefreshListener(HitBlockRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 当所有的刷新逻辑完成后，记录调用一下，否则你的ListView将一直处于正在刷新状态。
     */
    public void finishRefreshing() {
        header.postComplete();
        if (currentStatus != STATUS_AGAIN_DOWN) {
            rollbackHeader();
        }
    }

    /**
     * 回滚到头部刷新控件的高度，并触发后台刷新任务
     */
    private void refreshingRollBack2Header() {
        ValueAnimator rbToHeaderAnimator = ValueAnimator.ofInt(headerLayoutParams.topMargin, 0);
        long duration = (long) (headerLayoutParams.topMargin * 1.1f) >=0 ? (long) (headerLayoutParams.topMargin * 1.1f) : 0;
        rbToHeaderAnimator.setDuration(duration);
        rbToHeaderAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rbToHeaderAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int marginValue = Integer.parseInt(animation.getAnimatedValue().toString());
                setHeaderTopMarign(marginValue);
            }
        });
        rbToHeaderAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentStatus = STATUS_REFRESHING;
                header.postStart();
                Executors.newSingleThreadExecutor().submit(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.onRefreshing();
                        }
                    }
                });
            }
        });
        rbToHeaderAnimator.start();
    }

    /**
     * 回滚下拉刷新头部控件
     */
    private void rollbackHeader() {
        tempHeaderTopMargin = headerLayoutParams.topMargin;
        ValueAnimator rbAnimator = ValueAnimator.ofInt(0, header.getHeight() + tempHeaderTopMargin);
        rbAnimator.setDuration(300);
        rbAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rbAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int marginValue = Integer.parseInt(animation.getAnimatedValue().toString());
                setHeaderTopMarign(-marginValue + tempHeaderTopMargin);
            }
        });
        rbAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (currentStatus == STATUS_PULL_TO_REFRESH || currentStatus == STATUS_REFRESH_FINISHED) {
                    currentStatus = STATUS_REFRESH_FINISHED;
                    return ;
                }
                currentStatus = STATUS_REFRESH_FINISHED;
                header.postEnd();
            }
        });
        rbAnimator.start();
    }

    /**
     * 下拉刷新的监听器，使用下拉刷新的地方应该注册此监听器来获取刷新回调。
     */
    public interface HitBlockRefreshListener {
        /**
         * 刷新时回调方法
         */
        void onRefreshing();
    }

}
