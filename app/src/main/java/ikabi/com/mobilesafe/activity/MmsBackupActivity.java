package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.socks.library.KLog;

import ikabi.com.mobilesafe.R;

/**
 * @ Author: Shuangjun Zou(Rob)
 * @ Email: seolop@gmail.com
 * @ 2016-04-15 0015
 */
public class MmsBackupActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mms_backup);
    }

    public void mmsBackup(View view){
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://sms/");
       Cursor cursor = resolver.query(uri, new String[]{"address","date","type","body"},null,null,null);
        while (cursor.moveToNext()){
            String address = cursor.getString(0);
            String date = cursor.getString(1);
            String type = cursor.getString(2);
            String body = cursor.getString(3);
            Log.d("ZSJ","address" + address +"date"+date+"type"+type);
            KLog.d(date,body,type,address);
        }
        cursor.close();
    }
}
