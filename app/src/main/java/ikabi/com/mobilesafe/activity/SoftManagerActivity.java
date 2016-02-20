package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.bean.AppInfo;
import ikabi.com.mobilesafe.provider.AppInfoProvider;
import ikabi.com.mobilesafe.view.ProgressDesView;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/16
 */
public class SoftManagerActivity extends Activity {


    private ProgressDesView mPdvRom;
    private ProgressDesView mPdvSD;
    private ListView mListView;

    private List<AppInfo> mAppInfo;
    private List<AppInfo> mSystemAppInfo;
    private List<AppInfo> mUserDatas;
    private LinearLayout mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmanager);

        //init view
        mPdvRom = (ProgressDesView) findViewById(R.id.am_pdv_rom);
        mPdvSD = (ProgressDesView) findViewById(R.id.am_pdv_sd);
        mListView = (ListView) findViewById(R.id.am_pdv_list);
        mLoading = (LinearLayout) findViewById(R.id.public_loading);


        //set date
        File dataDirectory = Environment.getDataDirectory();
        long romFreeSpace = dataDirectory.getFreeSpace();
        long romTotalSpace = dataDirectory.getTotalSpace();
        long romUsedSpace = romTotalSpace - romFreeSpace;

        // 1.set rom date

        mPdvRom.setDesTitle("内存");
        mPdvRom.setDesLeftTitle(Formatter.formatFileSize(this, romUsedSpace) + "已用");
        mPdvRom.setDesRightTitle(Formatter.formatFileSize(this, romFreeSpace) + "可用");
        int romProgress = (int) (romUsedSpace * 100f / romTotalSpace + 0.5f);
        mPdvRom.setDesProgress(romProgress);

        // 2. set sd date

        File sdDirctory = Environment.getExternalStorageDirectory();
        long sdFreeSpace = sdDirctory.getFreeSpace();
        long sdTotalSpace = sdDirctory.getTotalSpace();
        long sdUsedSpace = sdTotalSpace - sdFreeSpace;
        int sdProgress = (int) (sdUsedSpace * 100f / sdTotalSpace + 0.5f);
        mPdvSD.setDesProgress(sdProgress);

        mLoading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 3. loading data
                mAppInfo = AppInfoProvider.getAllApps(getApplicationContext());
                mSystemAppInfo = new ArrayList<AppInfo>();
                mUserDatas = new ArrayList<AppInfo>();

                for (AppInfo info : mAppInfo) {
                    if (info.isSystem) {
                        //system app
                        mSystemAppInfo.add(info);
                    } else {
                        mUserDatas.add(info);
                    }
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoading.setVisibility(View.GONE);
                        // 4. set listview data

                        mListView.setAdapter(new AppAdapter());
                    }
                });

            }
        }).start();

    }

    private class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            /*if (mAppInfo != null){
                return mAppInfo.size();
            }*/
            int systemCount = 0;
            if (mSystemAppInfo != null) {
                systemCount = mSystemAppInfo.size();
                systemCount += 1;
            }
            int userCount = 0;
            if (mUserDatas != null) {
                userCount = mUserDatas.size();
                userCount += 1;
            }
            return systemCount + userCount;
        }

        @Override
        public Object getItem(int position) {
            /*if (mAppInfo != null){
                return mAppInfo.get(position);
            }*/
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, android.view.View view, ViewGroup viewGroup) {
            int userSize = mUserDatas.size();
            if (position == 0) {
                TextView tv = new TextView(getApplicationContext());
                tv.setPadding(4, 4, 4, 4);
                tv.setBackgroundColor(Color.parseColor("#33000000"));
                tv.setTextColor(Color.BLACK);
                tv.setText("用户程序(" + userSize + ")个");
                return tv;
            }

            int systemSize = mSystemAppInfo.size();
            if (position == userSize + 1) {
                TextView tv = new TextView(getApplicationContext());
                tv.setPadding(4, 4, 4, 4);
                tv.setBackgroundColor(Color.parseColor("#33000000"));
                tv.setTextColor(Color.BLACK);
                tv.setText("系统程序(" + systemSize + ")个");
                return tv;
            }
            ViewHolder holder = null;
            if (view == null || (view instanceof TextView)) {

                //1 . init view 
                view = View.inflate(getApplicationContext(), R.layout.item_app_info, null);

                //2. init holder
                holder = new ViewHolder();

                //3 . setTag
                view.setTag(holder);

                //4. init holder view
                holder.ivIcon = (ImageView) view.findViewById(R.id.item_appinfo_iv_icon);
                holder.tvName = (TextView) view.findViewById(R.id.item_appinfo_tv_name);
                holder.tvInstallPath = (TextView) view.findViewById(R.id.item_appinfo_tv_install);
                holder.tvSize = (TextView) view.findViewById(R.id.item_appinfo_tv_size);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            AppInfo info = null;

            if (position < userSize + 1) {
                info = mUserDatas.get(position - 1);

            } else {
                info = mSystemAppInfo.get(position - userSize - 2);
            }
            //holder.ivIcon.setImageResource(info.icon);
            holder.tvName.setText(info.name);
            holder.tvSize.setText(Formatter.formatFileSize(getApplicationContext(), info.size));
            holder.tvInstallPath.setTag(info.isInstallSD ? "SD卡安装" : "手机内存");
            return view;
        }

    }

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvInstallPath;
        TextView tvSize;

    }
}
