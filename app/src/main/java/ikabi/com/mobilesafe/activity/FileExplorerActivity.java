package ikabi.com.mobilesafe.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.fragment.TabMobileFragment;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:2016-04-28 0028
 */
public class FileExplorerActivity extends FragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    TabMobileFragment tabMobileFragment;
    public List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        initView();
    }

    private void initView() {
        tabMobileFragment = new TabMobileFragment();
        fragments.add(tabMobileFragment);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public void onClick(View v) {

    }
}
