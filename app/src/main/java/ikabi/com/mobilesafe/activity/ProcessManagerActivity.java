package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.bean.ProcessInfo;
import ikabi.com.mobilesafe.provider.ProcessProvider;
import ikabi.com.mobilesafe.service.AutoCleanService;
import ikabi.com.mobilesafe.utils.ServiceStateUtils;
import ikabi.com.mobilesafe.view.ProgressDesView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/21
 */
public class ProcessManagerActivity extends Activity {
    @Bind(R.id.pm_pdv_process)
    ProgressDesView mPdvProcess;
    @Bind(R.id.pm_Pdv_memory)
    ProgressDesView mPdvMemory;
    @Bind(R.id.pm_list_view)
    ListView mListView;
    private List<ProcessInfo> mDates;
    private ProcessAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);
        ButterKnife.bind(this);

        //progress
        int runningProcessCount = ProcessProvider.getRunningProcessCount(this);
        int totalProcessCount = ProcessProvider.getTotalProcessCount(this);
        mPdvProcess.setDesTitle("进程数:");
        mPdvProcess.setDesLeftTitle("正在运行" + runningProcessCount + "个");
        mPdvProcess.setDesRightTitle("可有进程" + totalProcessCount + "个");
        mPdvProcess.setDesProgress((int) (runningProcessCount * 100f / totalProcessCount + 0.5f));

        //Memory
        long freeMemory = ProcessProvider.getFreeMemory(this);
        long totalMemory = ProcessProvider.getTotalMemory(this);
        long usedMemory = totalMemory - freeMemory;
        mPdvMemory.setDesTitle("内存:");
        mPdvMemory.setDesLeftTitle("占用内存:" + Formatter.formatFileSize(this, usedMemory));
        mPdvMemory.setDesRightTitle("可用内存:" + Formatter.formatFileSize(this, freeMemory));
        mPdvMemory.setDesProgress((int) (usedMemory * 100f / totalMemory + 0.5f));

        // 设置listView的item点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ProcessInfo info = mDates.get(position);
                if (info.packageName.equals(getPackageName())) {
                    return;
                }

                // 如果选中了，就取消
                info.checked = !info.checked;

                // UI更新
                mAdapter.notifyDataSetChanged();
            }
        });
        //数据加载

        mDates = ProcessProvider.getAllRunningProcesses(this);

        //给ListView加载数据
        mListView.setAdapter(new ProcessAdapter());


    }
    private class ProcessAdapter extends BaseAdapter implements StickyListHeadersAdapter{

        @Override
        public int getCount() {
            if (mDates != null){
                return mDates.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            if (mDates != null){
                return mDates.get(i);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, android.view.View view, ViewGroup viewGroup) {
            ViewHolder holder = null;

            if (view == null || !(view instanceof TextView)){
                view = View.inflate(getApplicationContext(),R.layout.item_process,null);
                holder = new ViewHolder();
                view.setTag(holder);
                holder.imageView = (ImageView) view.findViewById(R.id.item_process_iv);
                holder.tvName = (TextView) view.findViewById(R.id.item_process_tv_name);
                holder.tvMemory = (TextView) view.findViewById(R.id.item_process_tv_memory_size);
                holder.checkBox = (CheckBox) view.findViewById(R.id.item_process_checkbox);

            } else {
                holder = (ViewHolder) view.getTag();
            }
            ProcessInfo info = mDates.get(i);
            holder.imageView.setImageDrawable(info.icon);
            holder.tvName.setText(info.name);
            holder.tvMemory.setText("占用内存" + Formatter.formatFileSize(getApplicationContext(), info.memory));

            return view;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            ProcessInfo info = mDates.get(position);
            TextView textView = null;
            if (convertView == null || !(convertView instanceof TextView)){
                convertView = new TextView(getApplicationContext());
                textView = (TextView) convertView;
                textView.setPadding(8,8,8,8);
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextSize(15);
                textView.setTextColor(Color.BLACK);
            } else {
                textView = (TextView) convertView;
            }
            boolean isSystem = info.isSystem;

            textView.setText(isSystem ? "系统进程":"用户进程");
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            ProcessInfo info = mDates.get(position);
            return info.isSystem?1:0;
        }
    }

    private class ViewHolder{
        ImageView imageView;
        TextView tvName;
        TextView tvMemory;
        CheckBox checkBox;

    }
    @Override
    protected void onStart() {
        super.onStart();

        //init sevices
        boolean runing = ServiceStateUtils.isRunging(getApplicationContext(), AutoCleanService.class);
    }
}
