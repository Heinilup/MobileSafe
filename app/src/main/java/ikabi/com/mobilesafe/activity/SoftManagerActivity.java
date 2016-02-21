package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
    private TextView mHeaderText;
    private PopupWindow mWindow;
    private AppAdapter mAdapter;
    private BroadcastReceiver mPackageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String dataString = intent.getDataString();
            String packageName = dataString.replace("package", "");

            // UI update
            ListIterator<AppInfo> iterator = mUserDatas.listIterator();
            while (iterator.hasNext()){
                AppInfo next = iterator.next();
                if (next.packageName.equals(packageName)){
                    iterator.remove();
                    break;
                }
            }

            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmanager);

        //init view
        mPdvRom = (ProgressDesView) findViewById(R.id.am_pdv_rom);
        mPdvSD = (ProgressDesView) findViewById(R.id.am_pdv_sd);
        mListView = (ListView) findViewById(R.id.am_pdv_list);
        mLoading = (LinearLayout) findViewById(R.id.public_loading);
        mHeaderText = (TextView) findViewById(R.id.am_tv_header);

        // register package install and uninstall
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(mPackageReceiver, filter);


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
        mHeaderText.setVisibility(View.GONE);
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
                        mHeaderText.setVisibility(View.VISIBLE);

                        //set data for mHeaderText

                        mHeaderText.setText("用户程序(" + mUserDatas.size() + "个)");
                        // 4. set listview data

                        mAdapter = new AppAdapter();
                        mListView.setAdapter(mAdapter);
                    }
                });

            }
        }).start();


        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                if (mUserDatas == null || mSystemAppInfo == null) {
                    return;
                }
                int userSize = mUserDatas.size();
                if (i >= 0 && i <= userSize) {
                    mHeaderText.setText("用户程序(" + mUserDatas.size() + "个)");

                } else if (i >= userSize + 1) {
                    mHeaderText.setText("系统程序(" + mSystemAppInfo.size() + ")个");
                }

            }
        });
        // listview onitemclick listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private View mContentView;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //
                if (position == 0) {
                    return;

                }
                int userSize = mUserDatas.size();
                if (position == userSize + 1) {
                    return;
                }
                AppInfo info = null;
                if (position > 0 && position < userSize + 1) {
                    //user app
                    info = mUserDatas.get(position - 1);
                } else {
                    // system app
                    info = mSystemAppInfo.get(position - userSize - 2);
                }

                // show popwindow
                /*TextView contentView = new TextView(getApplicationContext());
                contentView.setText("弹出的层");
                contentView.setPadding(8, 8, 8, 8);
                contentView.setBackgroundColor(Color.RED);*/
                mContentView = View.inflate(getApplicationContext(),R.layout.popwindow_app, null);

                final AppInfo app = info;
                mContentView.findViewById(R.id.pop_ll_uninstall).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //uninstall

                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.DELETE");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setData(Uri.parse("package:" + app.packageName));
                        startActivity(intent);

                        // dismiss popwindow
                        mWindow.dismiss();
                    }
                });
                mContentView.findViewById(R.id.pop_ll_open).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PackageManager pm = getPackageManager();
                        Intent intent = pm.getLaunchIntentForPackage(app.packageName);
                        if (intent != null){
                            startActivity(intent);
                        }
                        mWindow.dismiss();
                    }
                });
                mContentView.findViewById(R.id.pop_ll_share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setType("vnd.android-dir/mms-sms");
                        intent.putExtra("sms_body", "share");
                        startActivity(intent);

                        // dismiss popwindow
                        mWindow.dismiss();

                    }
                });
                mContentView.findViewById(R.id.pop_ll_info).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Info
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setData(Uri.parse("package:" + app.packageName));
                        startActivity(intent);

                        // dismiss popwindow
                        mWindow.dismiss();
                    }
                });
                int width = ViewGroup.LayoutParams.WRAP_CONTENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mWindow = new PopupWindow(mContentView, width, height);

                //focus
                mWindow.setFocusable(true);

                //click slide can touch
                mWindow.setOutsideTouchable(true);
                mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //show

                mWindow.setAnimationStyle(R.style.PopAnimation);
                //window.showAsDropDown(view);
                mWindow.showAsDropDown(view , 60, -view.getHeight());
            }
        });
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
                tv.setBackgroundColor(Color.parseColor("#ffcccccc"));
                tv.setTextColor(Color.BLACK);
                tv.setText("用户程序(" + userSize + ")个");
                return tv;
            }

            int systemSize = mSystemAppInfo.size();
            if (position == userSize + 1) {
                TextView tv = new TextView(getApplicationContext());
                tv.setPadding(4, 4, 4, 4);
                tv.setBackgroundColor(Color.parseColor("#ffcccccc"));
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
