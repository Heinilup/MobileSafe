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
    @Bind(R.id.btn_net_open1)
    Button btnNetOpen1;
    @Bind(R.id.btn_net_close1)
    Button btnNetClose1;
    @Bind(R.id.btn_net_open2)
    Button btnNetOpen2;
    @Bind(R.id.btn_net_close2)
    Button btnNetClose2;
    @Bind(R.id.btn_net_open3)
    Button btnNetOpen3;
    @Bind(R.id.btn_net_close3)
    Button btnNetClose3;
    @Bind(R.id.btn_local_open1)
    Button btnLocalOpen1;
    @Bind(R.id.btn_local_close1)
    Button btnLocalClose1;
    @Bind(R.id.btn_local_open2)
    Button btnLocalOpen2;
    @Bind(R.id.btn_local_close2)
    Button btnLocalClose2;
    @Bind(R.id.btn_local_open3)
    Button btnLocalOpen3;
    @Bind(R.id.btn_local_close3)
    Button btnLocalClose3;
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

    @OnClick({R.id.btn_net_open1, R.id.btn_net_close1, R.id.btn_net_open2, R.id.btn_net_close2, R.id.btn_net_open3, R.id.btn_net_close3, R.id.btn_local_open1, R.id.btn_local_close1, R.id.btn_local_open2, R.id.btn_local_close2, R.id.btn_local_open3, R.id.btn_local_close3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_net_open1:
                sendMsg("11");
                break;
            case R.id.btn_net_close1:
                sendMsg("21");
                break;
            case R.id.btn_net_open2:
                sendMsg("12");
                break;
            case R.id.btn_net_close2:
                sendMsg("22");
                break;
            case R.id.btn_net_open3:
                sendMsg("13");
                break;
            case R.id.btn_net_close3:
                sendMsg("23");
                break;
            case R.id.btn_local_open1:
                sendLocalMsg("11");
                break;
            case R.id.btn_local_close1:
                sendLocalMsg("21");
                break;
            case R.id.btn_local_open2:
                sendLocalMsg("12");
                break;
            case R.id.btn_local_close2:
                sendLocalMsg("22");
                break;
            case R.id.btn_local_open3:
                sendLocalMsg("13");
                break;
            case R.id.btn_local_close3:
                sendLocalMsg("23");
                break;
        }
    }
}
