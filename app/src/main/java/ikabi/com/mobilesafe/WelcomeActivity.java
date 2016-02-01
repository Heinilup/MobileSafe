package ikabi.com.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import ikabi.com.mobilesafe.utils.PackageUtils;

public class WelcomeActivity extends Activity {

    private TextView mNameVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mNameVersion = (TextView) findViewById(R.id.welcome_version);
        mNameVersion.setText(PackageUtils.getVersionName(this));


    }
}
