package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.bean.ProcessInfo;
import ikabi.com.mobilesafe.provider.ProcessProvider;
import ikabi.com.mobilesafe.service.AutoCleanService;
import ikabi.com.mobilesafe.utils.ServiceStateUtils;
import ikabi.com.mobilesafe.view.ProgressDesView;

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

        //给ListView加载数据
        mListView.setAdapter(null);


    }
    private class ProcessAdapter extends BaseAdapter{

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
            return null;
        }
    }

    private class ViewHolder{
        
    }
    @Override
    protected void onStart() {
        super.onStart();

        //init sevices
        boolean runing = ServiceStateUtils.isRunging(getApplicationContext(), AutoCleanService.class);
    }
}
