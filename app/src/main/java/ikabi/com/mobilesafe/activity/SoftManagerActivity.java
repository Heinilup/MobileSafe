package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
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

    private List<AppInfo> mDatas;
    private List<AppInfo> mSystemDatas;
    private List<AppInfo> mUserDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmanager);

        //init view
        mPdvRom = (ProgressDesView) findViewById(R.id.am_pdv_rom);
        mPdvSD = (ProgressDesView) findViewById(R.id.am_pdv_sd);
        mListView = (ListView) findViewById(R.id.am_pdv_list);

        //set date
        File dataDirectory = Environment.getDataDirectory();
        long romFreeSpace = dataDirectory.getFreeSpace();
        long romTotalSpace = dataDirectory.getTotalSpace();
        long romUsedSpace = romTotalSpace - romFreeSpace;

        // 1.set rom date

        mPdvRom.setDesTitle("内存");
        mPdvRom.setDesLeftTitle(Formatter.formatFileSize(this, romUsedSpace)+"已用");
        mPdvRom.setDesRightTitle(Formatter.formatFileSize(this, romFreeSpace)+"可用");
        int romProgress = (int) (romUsedSpace * 100f / romTotalSpace + 0.5f);
        mPdvRom.setDesProgress(romProgress);

        // 2. set sd date

        File sdDirctory = Environment.getExternalStorageDirectory();
        long sdFreeSpace = sdDirctory.getFreeSpace();
        long sdTotalSpace = sdDirctory.getTotalSpace();
        long sdUsedSpace = sdTotalSpace - sdFreeSpace;
        int sdProgress = (int) (sdUsedSpace * 100f / sdTotalSpace +0.5f);
        mPdvSD.setDesProgress(sdProgress);

        // 3. loading data
        mDatas = AppInfoProvider.getAllApps(getApplicationContext());

        // 4. set listview data

        mListView.setAdapter(new AppAdapter());

    }

    private class AppAdapter implements ListAdapter {
        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getCount() {
            if (mDatas != null){
                return mDatas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mDatas != null){
                return mDatas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, android.view.View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null){
                view = View.inflate(getApplicationContext(), R.layout.item_app_info, null);

                holder = new ViewHolder();

                view.setTag(holder);
                holder.ivIcon = (ImageView) view.findViewById(R.id.item_appinfo_iv_icon);
                holder.tvName = (TextView) view.findViewById(R.id.item_appinfo_tv_name);
                holder.tvInstallPath = (TextView) view.findViewById(R.id.item_appinfo_tv_install);
                holder.tvSize = (TextView) view.findViewById(R.id.item_appinfo_tv_size);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            AppInfo info = mDatas.get(position);
            //holder.ivIcon.setImageResource();
            holder.tvName.setText(info.name);
            holder.tvSize.setText(Formatter.formatFileSize(getApplicationContext(), info.size));
            holder.tvInstallPath.setTag(info.isInstallSD ? "SD卡安装" : "手机内存");
            return view;
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int i) {
            return false;
        }
    }

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvInstallPath;
        TextView tvSize;

    }
}
