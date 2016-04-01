package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.socks.library.KLog;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @Bind(R.id.btn_open1)
    Button btnOpen1;
    @Bind(R.id.btn_close1)
    Button btnClose1;
    @Bind(R.id.btn_open)
    Button btnOpen;
    @Bind(R.id.btn_close)
    Button btnClose;
    @Bind(R.id.btn_open2)
    Button btnOpen2;
    @Bind(R.id.btn_close2)
    Button btnClose2;
    @Bind(R.id.btn_open_local_1)
    Button btnOpenLocal1;
    @Bind(R.id.btn_close_local_1)
    Button btnCloseLocal1;
    @Bind(R.id.stv)
    SwitchToggleView stv;
    @Bind(R.id.btn_open_local_2)
    Button btnOpenLocal2;
    @Bind(R.id.btn_close_local_2)
    Button btnCloseLocal2;
    @Bind(R.id.btn_open_local_3)
    Button btnOpenLocal3;
    @Bind(R.id.btn_close_local_3)
    Button btnCloseLocal3;
    private SwitchToggleView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smarthome);
        ButterKnife.bind(this);
        mView = (SwitchToggleView) findViewById(R.id.stv);

        mView.setSwitchBackground(R.drawable.switch_background);
        mView.setSwitchSlide(R.drawable.slide_button_background);

        mView.setOnSwitchListener(new SwitchToggleView.OnSwitchListenr() {

            @Override
            public void onSwitchChanged(boolean isOpened) {

                /*Toast.makeText(getApplicationContext(), isOpened ? "打开" : "关闭",
                        Toast.LENGTH_SHORT).show();*/
                if (isOpened) {
                    sendMsg("21");
                } else {
                    sendMsg("11");
                }
            }
        });
    }

    /*public void open(View view) {
        sendMsg("11");

    }

    public void close(View view) {
        sendMsg("21");

    }*/

    public void open1(View view) {
        sendMsg("12");

    }

    public void close1(View view) {
        sendMsg("22");

    }

    public void open2(View view) {
        sendMsg("13");

    }

    public void close2(View view) {
        sendMsg("23");

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

    public void open_local_3(View view) {
        sendLocalMsg("13");

    }

    public void close_local_3(View view) {
        sendLocalMsg("23");

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

    @OnClick({R.id.btn_open1, R.id.btn_close1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open1:
                sendMsg("12");
                break;
            case R.id.btn_close1:
                sendMsg("22");
                break;
        }
    }
}
