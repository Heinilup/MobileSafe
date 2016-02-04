package ikabi.com.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import ikabi.com.mobilesafe.utils.PackageUtils;

public class WelcomeActivity extends Activity {

    private static final int SHOW_ERROR = 0;
    private TextView mNameVersion;
    private String mDesc;


    private Handler mHandler = new Handler() {
      public void handleMessage(Message msg) {
          int what = msg.what;
          switch (what){
              case SHOW_ERROR:
                  break;
              default:
                  break;
          }
      }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //init view
        mNameVersion = (TextView) findViewById(R.id.welcome_version);

        //show version code
        mNameVersion.setText(PackageUtils.getVersionName(this));

        checkVersionUpdate();

        //enter to homepage

        enterhomepage();



    }

    private void enterhomepage() {


        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkVersionUpdate() {
        new Thread(new CheckVersionTask()).start();
    }
    private class CheckVersionTask implements Runnable{

        @Override
        public void run() {
            //implements for checkversion
            String uri = "http://869.8866.org/update.txt";

            HttpClient client = AndroidHttpClient.newInstance("ikabi",getApplicationContext());
            HttpGet get = new HttpGet(uri);
            try {
                HttpResponse response = client.execute(get);

                //获得response返回值
                int statusCode = response.getStatusLine().getStatusCode();
                // 200返回成功0
                if (statusCode == 200){

                    String result = EntityUtils.toString(response.getEntity(), "utf-8");

                    int localCode = PackageUtils.getVersionCode(getApplicationContext());

                    JSONObject jsonObject = new JSONObject();
                    int netCode = jsonObject.getInt("versionCode");

                    if (netCode > localCode){
                        mDesc = jsonObject.getString("desc");



                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
