package ikabi.com.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.IOException;

import ikabi.com.mobilesafe.utils.Constants;
import ikabi.com.mobilesafe.utils.LogUtils;
import ikabi.com.mobilesafe.utils.PackageUtils;
import ikabi.com.mobilesafe.utils.PreferenceUtils;


public class WelcomeActivity extends Activity {

    private static final String TAG = "WelcomeActivity";

    private static final int SHOW_ERROR = 0;
    private static final int SHOW_UPDATE_DIALOG = 1;
    private TextView mNameVersion;
    private String mDesc;
    private String mUrl;


    private Handler mHandler = new Handler() {
      public void handleMessage(Message msg) {
          int what = msg.what;
          switch (what){
              case SHOW_ERROR:
                  LogUtils.d(TAG,"返回错误进入主页");
                  enterhomepage();
                  break;
              case SHOW_UPDATE_DIALOG:
                  LogUtils.d(TAG, "SHOW_UPDATE_DIALOG");
                  showUpdateDialog();
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

        boolean update = PreferenceUtils.getBoolean(this, Constants.AUTO_UPDATE, true);
        if(update){
            checkVersionUpdate();
        }else {

        //enter to homepage
        enterhomepage();
        }
    }
    private void close (Closeable io){
        if (io != null){
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            io = null;
        }
    }

    private void enterhomepage() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1200);


    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LogUtils.d(TAG, "showUpdateDialog");
        builder.setCancelable(false);
        builder.setTitle("版本更新提醒");

        builder.setMessage(mDesc);
        builder.setPositiveButton("立刻升级", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                showProgressDialog();
                //下载最新版本
            }
        });
        builder.setNegativeButton("稍后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //进入主页
                enterhomepage();
            }
        });
        builder.show();

    }

    private void showProgressDialog() {

    }

    private void checkVersionUpdate() {
        new Thread(new CheckVersionTask()).start();
    }
    private class CheckVersionTask implements Runnable{

        @Override
        public void run() {
            //implements for checkversion
            String uri = "http://blog.ikabi.com/update.txt";

            AndroidHttpClient client = AndroidHttpClient.newInstance("ikabi",getApplicationContext());
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);//设置访问网络超时时间
            HttpConnectionParams.setSoTimeout(params, 5000);//设置读取超时时间
            HttpGet get = new HttpGet(uri);
            try {
                HttpResponse response = client.execute(get);

                //获得response返回值
                int statusCode = response.getStatusLine().getStatusCode();
                // 200返回成功0
                if (statusCode == 200){

                    String result = EntityUtils.toString(response.getEntity(), "utf-8");

                    int localCode = PackageUtils.getVersionCode(getApplicationContext());

                    JSONObject jsonObject = new JSONObject(result);
                    int netCode = jsonObject.getInt("versionCode");

                    LogUtils.d(TAG, "netCode = " + netCode);

                    if (netCode > localCode){
                        mDesc = jsonObject.getString("desc");

                        mUrl = jsonObject.getString("url");

                        Message msg = Message.obtain();
                        msg.what = SHOW_UPDATE_DIALOG;
                        mHandler.sendMessage(msg);
                    } else {
                        LogUtils.d(TAG,"不需要更新");
                        enterhomepage();
                }
                } else {
                    Message msg = Message.obtain();
                    msg.what = SHOW_ERROR;
                    msg.obj = "code:130";
                    mHandler.sendMessage(msg);
                }
            } catch (ClientProtocolException e) {

                Message msg = Message.obtain();
                msg.what = SHOW_ERROR;
                msg.obj = "code:110";
                mHandler.sendMessage(msg);

            } catch (IOException e) {

                Message msg = Message.obtain();
                msg.what = SHOW_ERROR;
                msg.obj = "code:120";
                mHandler.sendMessage(msg);

            } catch (JSONException e) {

                Message msg = Message.obtain();
                msg.what = SHOW_ERROR;
                msg.obj = "code:119";
                mHandler.sendMessage(msg);
            } finally {
                if (client != null){
                    client.close();
                    client = null;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        enterhomepage();
        return super.onTouchEvent(event);
    }
}
