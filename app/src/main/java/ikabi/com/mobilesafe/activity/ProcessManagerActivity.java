package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;

import ikabi.com.mobilesafe.R;
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

        mPdvProcess = (ProgressDesView) findViewById(R.id.pm_pdv_process);
        mPdvMemory = (ProgressDesView) findViewById(R.id.pm_Pdv_memory);


    }

    @Override
    protected void onStart() {
        super.onStart();

        //init sevices
        boolean runing = ServiceStateUtils.isRunging(getApplicationContext(), AutoCleanService.class);
    }
}
