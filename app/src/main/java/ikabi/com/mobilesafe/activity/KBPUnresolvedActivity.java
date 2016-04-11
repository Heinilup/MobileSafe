package ikabi.com.mobilesafe.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import ikabi.com.mobilesafe.R;

/**
 * @ Author: Shuangjun Zou(Rob)
 * @ Email: seolop@gmail.com
 * @ 2016-04-11 0011
 */
public class KBPUnresolvedActivity extends AppCompatActivity {

    @Bind(R.id.content_ryv)
    RecyclerView mContentRyv;
    @Bind(R.id.send_edt)
    EditText mSendEdt;
    @Bind(R.id.plus_iv)
    ImageView mPlusIv;
    @Bind(R.id.panel_root)
    LinearLayout mPanelRoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kbp_unresolved);
        ButterKnife.bind(this);
        mPlusIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPlusIv(view);
            }
        });
        mContentRyv.setLayoutManager(new LinearLayoutManager(this));

        mSendEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mPanelRoot.setVisibility(View.GONE);
                }
            }
        });

        mContentRyv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    hideKeyboard();
                    mPanelRoot.setVisibility(View.GONE);
                }

                return false;
            }
        });
    }

    public void onClickPlusIv(final View view) {
        if (mPanelRoot.getVisibility() == View.VISIBLE) {
            showKeyboard();
        } else {
            hideKeyboard();
            mPanelRoot.setVisibility(View.VISIBLE);
        }
    }

    private void showKeyboard() {
        mSendEdt.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) mSendEdt.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mSendEdt, 0);
    }

    private void hideKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mSendEdt.clearFocus();
        imm.hideSoftInputFromWindow(mSendEdt.getWindowToken(), 0);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mPanelRoot.getVisibility() == View.VISIBLE) {
                mPanelRoot.setVisibility(View.GONE);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
