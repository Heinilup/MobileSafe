package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import com.socks.library.KLog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ikabi.com.mobilesafe.R;

/**
 * @ Author: Shuangjun Zou(Rob)
 * @ Email: seolop@gmail.com
 * @ 2016-04-15 0015
 */
public class MmsBackupActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mms_backup);
    }

    public void mmsBackup(View view) {
        try {
            ContentResolver resolver = getContentResolver();
            Uri uri = Uri.parse("content://sms/");
            Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            xmlSerializer.setOutput(fileOutputStream, "utf-8");

            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "root");

            while (cursor.moveToNext()) {
                xmlSerializer.startTag(null, "sms");
                xmlSerializer.startTag(null, "address");
                String address = cursor.getString(0);
                xmlSerializer.text(address);
                xmlSerializer.endTag(null, "address");
                xmlSerializer.startTag(null, "date");
                String date = cursor.getString(1);
                xmlSerializer.text(date);
                xmlSerializer.endTag(null, "date");
                xmlSerializer.startTag(null, "type");
                String type = cursor.getString(2);
                xmlSerializer.text(type);
                xmlSerializer.endTag(null, "type");
                xmlSerializer.startTag(null, "body");
                String body = cursor.getString(3);
                xmlSerializer.text(body);
                xmlSerializer.endTag(null, "body");
                xmlSerializer.endTag(null, "sms");
                Log.d("ZSJ", "address" + address + "date" + date + "type" + type);
                KLog.d(date, body, type, address);
            }
            xmlSerializer.endTag(null, "root");
            xmlSerializer.endDocument();
            cursor.close();
            Toast.makeText(this, "BackUp MMS succeed", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "BackUp MMS error", Toast.LENGTH_LONG).show();
        }
    }

    public void mmsRestore(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
        // file.lastModified(); 获取文件上一次备份的时间。
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提醒");
        builder.setMessage("是否清楚旧的短信");
        builder.setPositiveButton("确定清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("content://sms/");
                getContentResolver().delete(uri, null, null);
                    restore();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    restore();

            }
        });
        builder.show();
    }

    private void restore(){
        try {
        File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
        FileInputStream fileInputStream = new FileInputStream(file);
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(fileInputStream,"utf-8");
        int type = xmlPullParser.getEventType();
            String address = null;
            String mmstype= null;
            String date= null;
            String body= null;

            while (type!= XmlPullParser.END_DOCUMENT){
                switch (type){
                    case XmlPullParser.START_TAG:
                        if("type".equals(xmlPullParser.getName())){
                            mmstype = xmlPullParser.nextText();
                        } else if("body".equals(xmlPullParser.getName())){
                            body = xmlPullParser.nextText();
                        } else if("date".equals(xmlPullParser.getName())){
                            date = xmlPullParser.nextText();
                        } else if("address".equals(xmlPullParser.getName())){
                            address = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if("sms".equals(xmlPullParser.getName())){
                            ContentResolver contentResolver = getContentResolver();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("address",address);
                            contentValues.put("date",date);
                            contentValues.put("type",mmstype);
                            contentValues.put("body",body);
                            contentResolver.insert(Uri.parse("content://sms/"), contentValues);
                        }
                        break;
                    default:
                        break;
                }
                xmlPullParser.next();
            }

            Toast.makeText(this,"还原短信成功",Toast.LENGTH_LONG).show();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Toast.makeText(this,"还原短信失败",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
