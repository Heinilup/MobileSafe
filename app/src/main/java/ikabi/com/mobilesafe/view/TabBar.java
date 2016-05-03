package ikabi.com.mobilesafe.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.utils.FileUtils;

/**
 * Tab工具条
 */
public class TabBar extends HorizontalScrollView implements View.OnClickListener {

    private LinearLayout mContainer;
    private Context mContext;
    private List<View> mChildViews;
    private Resources resources;
    private int dp_20, dp_5;
    ColorStateList white_color;
    private int mScreenWidth;
    private int mMarginLeft;
    private int mFirstWidth;
    private OnTabListener mListener;
    private int mCurrentIndex = -1;
    private int tab_select_color;

    public void setListener(OnTabListener mListener) {
        this.mListener = mListener;
    }

    public TabBar(Context context) {
        super(context);

        init(context);
    }

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        this.mContext = context;
        this.resources = context.getResources();
        mContainer = new LinearLayout(mContext);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) resources.getDimension(R.dimen.dp_40)
        );
        mContainer.setLayoutParams(p);
        mContainer.setBackgroundResource(R.color.group_header_select_color);
        tab_select_color = R.color.tab_select;
        this.addView(mContainer);
        mChildViews = new ArrayList<>();
        dp_20 = (int) this.resources.getDimension(R.dimen.dp_25);
        dp_5 = (int) this.resources.getDimension(R.dimen.dp_5);
        white_color = this.resources.getColorStateList(R.color.white);
        this.setHorizontalScrollBarEnabled(false);
        this.mScreenWidth = FileUtils.getScreenWidth(mContext);
    }

    public void setTabBackground(int resid){
        mContainer.setBackgroundResource(resid);
    }

    public void setTabSelectBackground(int resid) {
        tab_select_color = resid;
    }

    public void setMenu(int... resID) {
        int size = resID == null ? 0 : resID.length;
        if (size == 0) {
            throw new NullPointerException();
        }
        for (int i = 0; i < size; i++) {
            FrameLayout childContainer = new FrameLayout(mContext);


            if ((resID[i] >= 0x7f020000 && resID[i] <= 0x7f02ffff) || (resID[i] >= 0x7f030000 && resID[i] <= 0x7f03ffff)) {
                // 图片资源
                ImageView childView = new ImageView(mContext);
                childView.setBackgroundResource(resID[i]);
                FrameLayout.LayoutParams childParams = new FrameLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                childParams.gravity = Gravity.CENTER;
                childParams.setMargins(dp_20, dp_5, dp_20, dp_5);
                childView.setLayoutParams(childParams);
                childContainer.addView(childView);
                mContainer.addView(childContainer);
                mChildViews.add(childContainer);
                childContainer.setTag(i);
                childContainer.setOnClickListener(this);


            } else if ((resID[i] >= 0x7f060000 && resID[i] <= 0x7f06ffff)) {
                // 文字资源
                TextView childView = new TextView(mContext);
                FrameLayout.LayoutParams childParams = new FrameLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                childParams.gravity = Gravity.CENTER;
                childParams.setMargins(dp_20, dp_5, dp_20, dp_5);
                childView.setLayoutParams(childParams);
                childView.setText(resID[i]);
                childView.setTextColor(white_color);
                childContainer.addView(childView);
                mContainer.addView(childContainer);
                mChildViews.add(childContainer);
                childContainer.setTag(i);
                childContainer.setOnClickListener(this);

            }

            LinearLayout.LayoutParams childContainerLayoutParams = new LinearLayout.LayoutParams
                    (LayoutParams.WRAP_CONTENT,
                            LayoutParams.MATCH_PARENT);
            childContainerLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            childContainer.setLayoutParams(childContainerLayoutParams);

            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            childContainer.measure(w, h);
            int width = childContainer.getMeasuredWidth();
            if (i == 0) {
                mMarginLeft = (mScreenWidth - width) / 2;
                childContainerLayoutParams.setMargins((mScreenWidth - width) / 2, 0, 0, 0);
            }
            if (i == size - 1) {
                childContainerLayoutParams.setMargins(0, 0, (mScreenWidth - width) / 2, 0);
            }

        }
    }

    int scrollX = 0;

    public void setCurrentTab(int currentTab) {
        if (currentTab == mCurrentIndex) {
            return;
        }


        scrollX = 0;

        for (int i = 0; i < mChildViews.size(); i++) {
            View view = mChildViews.get(i);
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            int width = view.getMeasuredWidth();

            if (i == currentTab) {
                scrollX += (width / 2);
                break;
            } else {
                scrollX += width;
            }
            if (i == 0) {
                mFirstWidth = width;
            }
        }
        this.post(runnable);

        for (int i = 0; i < mChildViews.size(); i++) {
            View view = mChildViews.get(i);
            if (i == currentTab) {
                view.setBackgroundResource(tab_select_color);
            } else {
                view.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        mCurrentIndex = currentTab;

        if (this.mListener != null) {
            this.mListener.onTabSelect(currentTab);
        }



    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            smoothScrollTo(scrollX - (mFirstWidth / 2), 0);
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getTag() != null && v.getTag() instanceof Integer) {
            int index = (int) v.getTag();
            setCurrentTab(index);
        }
    }

    public interface OnTabListener {
        void onTabSelect(int index);
    }
}
