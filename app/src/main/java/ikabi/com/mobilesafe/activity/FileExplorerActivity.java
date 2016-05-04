package ikabi.com.mobilesafe.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.XFileApplication;
import ikabi.com.mobilesafe.fragment.TabMobileFragment;
import ikabi.com.mobilesafe.view.AnimationHelper;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:2016-04-28 0028
 */
public class FileExplorerActivity extends FragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    TabMobileFragment tabMobileFragment;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private int mTabIndex;
    ImageView fileThumb;
    FrameLayout head_layout;

    TextView tvPersonNumber, tvCountNumber, tvFileNumber;

    private TextView device_name, connect_device_name;
    public List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        //EventBus.getDefault().register(this);
        initView();
        initData();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
        //stopService(new Intent(HomeActivity.this, IMService.class));
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        tabMobileFragment = new TabMobileFragment();
        fragments.add(tabMobileFragment);

    }
    void initData() {
        tvPersonNumber.setText(String.format(getString(R.string.person_number), "0"));
        tvCountNumber.setText(String.format(getString(R.string.count_number), "0"));
        tvFileNumber.setText(String.format(getString(R.string.file_total_b), "0.00"));
        device_name.setText(android.os.Build.MODEL);
        //startService(new Intent(this, IMService.class));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 切换标签
        for (int i = 0; i < group.getChildCount(); i++) {
            if (group.getChildAt(i).getId() == checkedId) {
                Fragment fragment = fragments.get(i);
                transaction = fragmentManager.beginTransaction();
                getCurrentFragment().onResume();
                if (fragment.isAdded()) {
                    fragment.onResume(); // 启动目标tab的onResume()
                } else {
                    transaction.add(R.id.content, fragment);
                }
                showTab(i, transaction);
                transaction.commit();
                mTabIndex = i;
            }

        }

    }
    private void showTab(int idx, FragmentTransaction ft) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
        }
        mTabIndex = idx; // 更新目标tab为当前tab
    }
    public Fragment getCurrentFragment() {
        return fragments.get(mTabIndex);
    }

    @Override
    public void onClick(View v) {

    }
    public void initFileThumbView(Drawable drawable, int width, int height, int locationX, int locationY) {
        // 修改坐标位置,图标大小
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fileThumb.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.setMargins(locationX, locationY - height / 2, 0, 0);
        fileThumb.setLayoutParams(layoutParams);
        fileThumb.setImageDrawable(drawable);
        fileThumb.setVisibility(View.VISIBLE);
        // 更新图标大小
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        fileThumb.measure(w, h);
        // 读取头像坐标
        int[] endLocation = new int[2];
        head_layout.getLocationOnScreen(endLocation);

        AnimationHelper.startSendFileAnimation(fileThumb, head_layout, locationX, locationY, endLocation[0], endLocation[1]);
        if (XFileApplication.connect_type == 1) {

        } else {

        }
    }
}
