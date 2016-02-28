package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.socks.library.KLog;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import ikabi.com.mobilesafe.R;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/28
 */
public class SmartHomeActivity extends Activity {
    private final String IP = "192.168.1.100";
    private final int PORT = 6722;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smarthome);
    }

    public void open(View view) {
        sendMsg("11");

    }

    public void close(View view) {
        sendMsg("21");

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
}
