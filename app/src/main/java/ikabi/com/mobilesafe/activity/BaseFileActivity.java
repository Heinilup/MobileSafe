package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ikabi.com.mobilesafe.R;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:2016/5/4
 */
public class BaseFileActivity extends Activity implements View.OnClickListener {


        protected FrameLayout frameLayout;

        /**
         * 加载布局的对象
         */
        protected LayoutInflater inflater;

        /**
         * 资源对象
         */
        protected Resources resources;

        /**
         * 上下文环境
         */
        protected Activity context;

        /**
         * 标题栏的布局
         */
        protected RelativeLayout title_layout;

        protected LinearLayout title_right_layout;

        protected ImageView back;

        protected TextView title;

        protected View bodyView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            XFileActivityManager.create().addActivity(this);
            inflater = getLayoutInflater();
            resources = getResources();
            context = this;
            // 背景不设置任何元素
            getWindow().setBackgroundDrawable(null);
            // 没有标题
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            frameLayout = (FrameLayout) findViewById(android.R.id.content);
        }

        protected void initView(int titleResId, int bodyViewId, int... rightResId) {
            initView(getString(titleResId), inflater.inflate(bodyViewId, null),rightResId);
        }

        protected void initView(String title, View bodyView, int... rightResId) {
            inflater.inflate(R.layout.activity_base_file, frameLayout, true);
            title_layout = (RelativeLayout) findViewById(R.id.title_layout);
            title_right_layout = (LinearLayout) findViewById(R.id.title_right_layout);
            this.bodyView = bodyView;
            this.title = (TextView) findViewById(R.id.title);
            this.title.setText(title);
            back = (ImageView) findViewById(R.id.back);
            back.setOnClickListener(this);
            resetTitleRightMenu(rightResId);
            if (this.bodyView != null) {
                FrameLayout.LayoutParams bodyLayoutParams = new FrameLayout.LayoutParams(
                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT, android.widget.FrameLayout.LayoutParams.MATCH_PARENT);
                bodyLayoutParams.setMargins(0, (int) resources.getDimension(R.dimen.dp_48), 0, 0);
                frameLayout.addView(bodyView, bodyLayoutParams);
                frameLayout.setBackgroundColor(resources.getColor(R.color.white));
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    XFileActivityManager.create().finishActivity();
                    break;
            }
        }

        /**
         * @Title: resetTitleRightMenu
         * @Description: TODO(重设标题栏右边的菜单, 包含影藏操作)
         * @param:
         */
        public void resetTitleRightMenu(int... resID) {
            int size = resID == null ? 0 : resID.length;
            boolean isAllStr = true;
            boolean isAllDra = true;
            for (int i = 0; i < size; i++) {
                //判断它是不是图片资源
                if ((resID[i] >= 0x7f020000 && resID[i] <= 0x7f02ffff)||(resID[i] >= 0x7f030000 && resID[i] <= 0x7f03ffff)) {
                    isAllStr = false;
                } else {
                    isAllDra = false;
                }
            }
            if (isAllStr) {
                String[] res = new String[size];
                //全是文字
                for (int i = 0; i < size; i++) {
                    res[i] = getString(resID[i]);
                }
                resetTitleRightMenu(res);
            } else if (isAllDra) {
                //全是图片
                if (title_right_layout != null) {
                    title_right_layout.removeAllViews();
                }
                title_right_layout.setVisibility(View.VISIBLE);
                int dp_10 = (int) resources.getDimension(R.dimen.dp_10);
                for (int i = 0; i < size; i++) {
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    imageView.setPadding(dp_10, 0, dp_10, 0);
                    imageView.setImageResource(resID[i]);
                    title_right_layout.addView(imageView, layoutParams);
                    imageView.setTag(i);
                    imageView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            rightClick((Integer) v.getTag());
                        }
                    });
                }
            } else {
                //又有文字又有图片
                if (title_right_layout != null) {
                    title_right_layout.removeAllViews();
                }
                title_right_layout.setVisibility(View.VISIBLE);
                int dp_10 = (int) resources.getDimension(R.dimen.dp_10);
                for (int i = 0; i < size; i++) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    if (resID[i] >= 0x7f020000 && resID[i] <= 0x7f02ffff) {
                        ImageView imageView = new ImageView(this);
                        imageView.setPadding(dp_10, 0, dp_10, 0);
                        imageView.setImageResource(resID[i]);
                        imageView.setTag(i);
                        imageView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                rightClick((Integer) v.getTag());
                            }
                        });
                        title_right_layout.addView(imageView, layoutParams);
                    } else {
                        TextView textView = new TextView(this);
                        textView.setGravity(Gravity.CENTER);
                        textView.setPadding(dp_10, 0, dp_10, 0);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.sp_14));
                        textView.setText(resID[i]);
                        textView.setTextColor(resources.getColor(R.color.blue));
                        textView.setTag(i);
                        textView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                rightClick((Integer) v.getTag());
                            }
                        });
                        title_right_layout.addView(textView, layoutParams);
                    }
                }
            }
        }


        public void resetTitleRightMenu(String[] res) {
            int size = res == null ? 0 : res.length;
            if (title_right_layout != null) {
                title_right_layout.removeAllViews();
            }
            if (size > 0) {
                title_right_layout.setVisibility(View.VISIBLE);
                int dp_10 = (int) resources.getDimension(R.dimen.dp_10);
                for (int i = 0; i < size; i++) {
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.sp_14));
                    textView.setText(res[i]);
                    textView.setTextColor(resources.getColor(R.color.blue));
                    textView.setPadding(dp_10, 0, dp_10, 0);
                    title_right_layout.addView(textView, layoutParams);
                    textView.setTag(i);
                    textView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            rightClick((Integer) v.getTag());
                        }
                    });
                }
            }
        }

        /**
         *
         * @Title: rightClick
         * @Description: TODO(标题栏右边菜单的点击事件)
         * @param: index(索引,因为右边的菜单可能是一个或则多个所以传一个索引过来)
         */
        protected void rightClick(int index) {}


    }

