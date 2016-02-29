package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;

import ikabi.com.mobilesafe.R;
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
    private ProgressDesView mPdvProcess;
    private ProgressDesView mPdvMemory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);


        //init view
        mPdvProcess = (ProgressDesView) findViewById(R.id.pm_pdv_process);
        mPdvMemory = (ProgressDesView) findViewById(R.id.pm_Pdv_memory);

        //progress
        int runningProcessCount = ProcessProvider.getRunningProcessCount(this);
        int totalProcessCount = ProcessProvider.getTotalProcessCount(this);
        mPdvProcess.setDesTitle("进程数:");
        mPdvProcess.setDesLeftTitle("正在运行" + runningProcessCount + "个");
        mPdvProcess.setDesRightTitle("可有进程" + totalProcessCount + "个");
        mPdvProcess.setDesProgress((int) (runningProcessCount * 100f / totalProcessCount +0.5f));

        //Memory
        long freeMemory = ProcessProvider.getFreeMemory(this);
        long totalMemory = ProcessProvider.getTotalMemory(this);
        long usedMemory = totalMemory - freeMemory;
        mPdvMemory.setDesTitle("内存:");
        mPdvMemory.setDesLeftTitle("占用内存:" + Formatter.formatFileSize(this, usedMemory));
        mPdvMemory.setDesRightTitle("可用内存:" + Formatter.formatFileSize(this, freeMemory));
        mPdvMemory.setDesProgress((int) (usedMemory * 100f / totalMemory +0.5f));


    }

    @Override
    protected void onStart() {
        super.onStart();

        //init sevices
        boolean runing = ServiceStateUtils.isRunging(getApplicationContext(), AutoCleanService.class);
    }
}
