package ikabi.com.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.kpswitch.utils.KPSwitchConflictUtil;
import ikabi.com.mobilesafe.kpswitch.utils.KeyboardUtil;
import ikabi.com.mobilesafe.kpswitch.widget.KPSwitchPanelLinearLayout;

/**
 * @ Author: Shuangjun Zou(Rob)
 * @ Email: seolop@gmail.com
 * @ 2016-04-13 0013
 */
public class KBPresolvedActivity extends AppCompatActivity {

    private static final String TAG = "ResolvedActivity";
    private RecyclerView mContentRyv;
    private EditText mSendEdt;
    private KPSwitchPanelLinearLayout mPanelRoot;
    private TextView mSendImgTv;
    private ImageView mPlusIv;

    private void assignViews() {
        mContentRyv = (RecyclerView) findViewById(R.id.content_ryv);
        mSendEdt = (EditText) findViewById(R.id.send_edt);
        mPanelRoot = (KPSwitchPanelLinearLayout) findViewById(R.id.panel_root);
        mSendImgTv = (TextView) findViewById(R.id.send_img_tv);
        mPlusIv = (ImageView) findViewById(R.id.plus_iv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kbp_resolved);

        assignViews();
        KeyboardUtil.attach(this, mPanelRoot,
                // Add keyboard showing state callback, do like this when you want to listen in the
                // keyboard's show/hide change.
                new KeyboardUtil.OnKeyboardShowingListener() {
                    @Override
                    public void onKeyboardShowing(boolean isShowing) {
                        Log.d(TAG, String.format("Keyboard is %s", isShowing ? "showing" : "hiding"));
                    }
                });

        KPSwitchConflictUtil.attach(mPanelRoot, mPlusIv, mSendEdt);

        mSendImgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mock start the translucent full screen activity.
                startActivity(new Intent(KBPresolvedActivity.this, TranslucentActivity.class));
            }
        });

        mContentRyv.setLayoutManager(new LinearLayoutManager(this));

        mContentRyv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
                }

                return false;
            }
        });
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mPanelRoot.getVisibility() == View.VISIBLE) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

}