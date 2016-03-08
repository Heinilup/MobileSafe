package ikabi.com.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ikabi.com.mobilesafe.activity.DragActivity;
import ikabi.com.mobilesafe.activity.FileManagerActivity;
import ikabi.com.mobilesafe.activity.MediaPlayerActivity;
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
public class HomeActivity extends Activity implements AdapterView.OnItemClickListener{
    private static final String TAG = "HomeActivity";

    private final static String[] TITLES = new String[] { "手机防盗", "骚扰拦截",
            "软件管家", "进程管理", "流量统计", "手机杀毒", "缓存清理", "常用工具", "文件管理", "媒体播放", "智能家居", "应用市场" };
    private final static String[] DESCS = new String[] { "远程定位手机", "全面拦截骚扰",
            "管理您的软件", "管理运行进程", "流量一目了然", "病毒无处藏身", "系统快如火箭", "工具大全", "文件管理器", "媒体播放器", "物联网", "应用下载安装" };

    private final static int[] ICONS = new int[] { R.drawable.sjfd,
            R.drawable.srlj, R.drawable.rjgj, R.drawable.jcgl, R.drawable.lltj,
            R.drawable.sjsd, R.drawable.hcql, R.drawable.cygj, R.drawable.lltj, R.drawable.sjsd, R.drawable.rjgj, R.drawable.jcgl };
    private GridView mGridView;
    private List<HomeItem> mDates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mGridView = (GridView) findViewById(R.id.home_grid_view);

        //初始化List数据

        mDates = new ArrayList<HomeItem>();
        for (int i = 0; i < ICONS.length; i++){
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

    public void clickSetting(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
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
                //手机杀毒
                performSwipeLayout();
                break;
            case 6:
                //常用工具
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
            default:
                break;

        }

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
            if(mDates != null){
                return mDates.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(mDates != null){
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
}
