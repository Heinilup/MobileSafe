package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ikabi.com.mobilesafe.R;

/**
 * @ Author: Shuangjun Zou(Rob)
 * @ Email: seolop@gmail.com
 * @ 2016-04-09 0009
 */
public class KeyBoardPanelActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_panel);
    }

    public void onClickResolved(final View view) {
        startActivity(new Intent(this, KBPresolvedActivity.class));
    }

    public void onClickUnResolved(final View view) {
        // 使用差别只是未使用CustomContentRootLayout与PanelRotLayout 并且在切换的时候未使用PanelRootLayout#setIsHide
        startActivity(new Intent(this, KBPUnresolvedActivity.class));
    }

}
