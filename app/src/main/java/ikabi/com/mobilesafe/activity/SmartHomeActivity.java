package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.socks.library.KLog;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.view.SwitchToggleView;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/28
 */
public class SmartHomeActivity extends Activity {
    private final String IP = "blog.ikabi.com";
    private final String Local_IP = "192.168.1.100";
    private final int PORT = 50435;
    private final int Local_PORT = 6722;
    private SwitchToggleView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smarthome);
        mView = (SwitchToggleView) findViewById(R.id.stv);

        mView.setSwitchBackground(R.drawable.switch_background);
        mView.setSwitchSlide(R.drawable.slide_button_background);

        mView.setOnSwitchListener(new SwitchToggleView.OnSwitchListenr() {

            @Override
            public void onSwitchChanged(boolean isOpened) {

                /*Toast.makeText(getApplicationContext(), isOpened ? "打开" : "关闭",
                        Toast.LENGTH_SHORT).show();*/
                if (isOpened){
                    sendMsg("21");
                }else {
                    sendMsg("11");
                }
            }
        });
    }

    public void open(View view) {
        sendMsg("11");

    }

    public void close(View view) {
        sendMsg("21");

    }
    public void open1(View view) {
        sendMsg("12");

    }

    public void close1(View view) {
        sendMsg("22");

    }

    public void sendMsg(final String msg) {
        new Thread() {
            public void run() {
                try {
                    Socket socket = null;
                    OutputStream out = null;
                    InputStream in = null;
                    socket = new Socket(IP, PORT);
                    out = socket.getOutputStream();
                    in = socket.getInputStream();
                    out.write(msg.getBytes());
                    out.flush();
                    byte[] cbuf = new byte[6];
                    in.read(cbuf);
                    KLog.d();
                    out.close();
                    in.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void open_local_1(View view) {
        sendLocalMsg("11");

    }

    public void close_local_1(View view) {
        sendLocalMsg("21");

    }
    public void open_local_2(View view) {
        sendLocalMsg("12");

    }

    public void close_local_2(View view) {
        sendLocalMsg("22");

    }
    public void sendLocalMsg(final String msg) {
        new Thread() {
            public void run() {
                try {
                    Socket socket = null;
                    OutputStream out = null;
                    InputStream in = null;
                    socket = new Socket(Local_IP, Local_PORT);
                    out = socket.getOutputStream();
                    in = socket.getInputStream();
                    out.write(msg.getBytes());
                    out.flush();
                    byte[] cbuf = new byte[6];
                    in.read(cbuf);
                    KLog.d();
                    out.close();
                    in.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
