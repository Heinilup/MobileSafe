package ikabi.com.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import java.util.ArrayList;
import java.util.List;

import ikabi.com.mobilesafe.activity.BoomMenuActivity;
import ikabi.com.mobilesafe.activity.DragActivity;
import ikabi.com.mobilesafe.activity.FileManagerActivity;
import ikabi.com.mobilesafe.activity.MediaPlayerActivity;
import ikabi.com.mobilesafe.activity.OneKeyLockScreen;
import ikabi.com.mobilesafe.activity.ProcessManagerActivity;
import ikabi.com.mobilesafe.activity.SettingActivity;
import ikabi.com.mobilesafe.activity.SlidingMenu;
import ikabi.com.mobilesafe.activity.SmartHomeActivity;
import ikabi.com.mobilesafe.activity.SoftManagerActivity;
import ikabi.com.mobilesafe.activity.SwipeActivity;
import ikabi.com.mobilesafe.bean.HomeItem;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/4
 */
public class HomeActivity extends Activity implements AdapterView.OnItemClickListener {
    private static final String TAG = "HomeActivity";

    private final static String[] TITLES = new String[]{"手机防盗", "骚扰拦截",
            "软件管家", "进程管理", "流量统计", "SwipeLayout", "缓存清理", "常用工具", "文件管理", "媒体播放", "智能家居", "应用市场", "一键锁屏", "BoomMenu"};
    private final static String[] DESCS = new String[]{"远程定位手机", "全面拦截骚扰",
            "管理您的软件", "管理运行进程", "流量一目了然", "滑动删除", "系统快如火箭", "工具大全", "文件管理器", "媒体播放器", "物联网", "应用下载安装", "快捷一键锁屏", "爆炸抽屉效果"};

    private final static int[] ICONS = new int[]{R.drawable.btn_mobile_light,
            R.drawable.btn_mobile_open, R.drawable.btn_mobile_power_none_open, R.drawable.btn_mobile_power_sleep_open, R.drawable.btn_mobile_upgrade,
            R.drawable.btn_mobile_more, R.drawable.btn_mobile_optimize, R.drawable.btn_mobile_tools, R.drawable.btn_mobile_fonts, R.drawable.btn_mobile_power, R.drawable.btn_mobile_temperature, R.drawable.btn_mobile_uninstall, R.drawable.btn_mobile_open, R.drawable.btn_mobile_power_none_open};
    private GridView mGridView;
    private List<HomeItem> mDates;
    private boolean init = false;
    private BoomMenuButton mBoomMenuButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mGridView = (GridView) findViewById(R.id.home_grid_view);
        mBoomMenuButton = (BoomMenuButton) findViewById(R.id.boom);

        //初始化List数据

        mDates = new ArrayList<HomeItem>();
        for (int i = 0; i < ICONS.length; i++) {
            HomeItem item = new HomeItem();
            item.iconID = ICONS[i];
            item.title = TITLES[i];
            item.desc = DESCS[i];
            //添加到mDates
            mDates.add(item);

        }

        //设置gridview的adapter
        mGridView.setAdapter(new HomeAdatper());
        mGridView.setOnItemClickListener(this);
    }

    public void clickSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                //手机防盗
                break;
            case 1:
                //骚扰拦截
                break;
            case 2:
                //软件管理
                performSoftManager();
                break;
            case 3:
                //进程管理
                performProcessManager();
                break;
            case 4:
                //流量统计
                break;
            case 5:
                //SwipeLayout
                performSwipeLayout();
                break;
            case 6:
                //SlidingMenu侧滑栏
                performSlidingMenu();
                break;
            case 7:
                //常用工具
                performNavigation();
                break;
            case 8:
                //文件管理器
                performFileManager();
                break;
            case 9:
                //媒体播放器
                performMediaPlayer();
                break;
            case 10:
                //智能家居
                performSmartHome();
                break;
            case 11:
                //应用市场
                break;
            case 12:
                //一键锁屏
                performLockScreen();
                break;
            case 13:
                //BoomMenu效果
                performBoomMenu();
                break;
            default:
                break;

        }

    }

    private void performBoomMenu() {
        Intent intent = new Intent(this, BoomMenuActivity.class);
        startActivity(intent);
    }

    private void performLockScreen() {
        Intent intent = new Intent(this, OneKeyLockScreen.class);
        startActivity(intent);
    }

    private void performSwipeLayout() {
        Intent intent = new Intent(this, SwipeActivity.class);
        startActivity(intent);
    }

    private void performSlidingMenu() {
        Intent intent = new Intent(this, SlidingMenu.class);
        startActivity(intent);
    }

    private void performNavigation() {
        Intent intent = new Intent(this, DragActivity.class);
        startActivity(intent);
    }

    private void performSmartHome() {
        Intent intent = new Intent(this, SmartHomeActivity.class);
        startActivity(intent);
    }

    private void performProcessManager() {
        Intent intent = new Intent(this, ProcessManagerActivity.class);
        startActivity(intent);
    }

    private void performSoftManager() {
        Intent intent = new Intent(this, SoftManagerActivity.class);
        startActivity(intent);

    }

    private void performFileManager() {
        Intent intent = new Intent(this, FileManagerActivity.class);
        startActivity(intent);
    }

    private void performMediaPlayer() {
        Intent intent = new Intent(this, MediaPlayerActivity.class);
        startActivity(intent);

    }

    private class HomeAdatper extends BaseAdapter {
        @Override
        public int getCount() {
            if (mDates != null) {
                return mDates.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mDates != null) {
                return mDates.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this, R.layout.item_home, null);

            ImageView ivIcon = (ImageView) view.findViewById(R.id.item_home_iv_icon);
            TextView tvTitle = (TextView) view.findViewById(R.id.item_home_tv_title);
            TextView tvDesc = (TextView) view.findViewById(R.id.item_home_tv_desc);

            HomeItem item = mDates.get(position);
            ivIcon.setImageResource(item.iconID);
            tvTitle.setText(item.title);
            tvDesc.setText(item.desc);

            return view;
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(init) return;
        init =true;
        Drawable[] subButtonDrawables = new Drawable[3];
        int[] drawablesResource = new int[]{
                R.drawable.boom,
                R.drawable.java,
                R.drawable.github
        };
        for (int i = 0; i < 3; i++)
            subButtonDrawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);

        String[] subButtonTexts = new String[]{"BoomMenuButton", "View source code", "Follow me"};

        int[][] subButtonColors = new int[3][2];
        for (int i = 0; i < 3; i++) {
            subButtonColors[i][1] = ContextCompat.getColor(this, R.color.material_white);
            subButtonColors[i][0] = Util.getInstance().getPressedColor(subButtonColors[i][1]);
        }

        mBoomMenuButton.init(
                subButtonDrawables, // The drawables of images of sub buttons. Can not be null.
                subButtonTexts,     // The texts of sub buttons, ok to be null.
                subButtonColors,    // The colors of sub buttons, including pressed-state and normal-state.
                ButtonType.HAM,     // The button type.
                BoomType.PARABOLA,  // The boom type.
                PlaceType.HAM_3_1,  // The place type.
                null,               // Ease type to move the sub buttons when showing.
                null,               // Ease type to scale the sub buttons when showing.
                null,               // Ease type to rotate the sub buttons when showing.
                null,               // Ease type to move the sub buttons when dismissing.
                null,               // Ease type to scale the sub buttons when dismissing.
                null,               // Ease type to rotate the sub buttons when dismissing.
                null                // Rotation degree.
        );

        mBoomMenuButton.setTextViewColor(ContextCompat.getColor(this, R.color.black));
    }
}
