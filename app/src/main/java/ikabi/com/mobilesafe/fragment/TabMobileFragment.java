package ikabi.com.mobilesafe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.adapter.MobilePagerAdapter;
import ikabi.com.mobilesafe.view.TabBar;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:2016/5/3
 */
public class TabMobileFragment extends Fragment implements OnPageChangeListener, TabBar.OnTabListener, View.OnClickListener {

    ViewPager viewPager;
    TabBar tabBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mobile, null);
        tabBar = (TabBar) view.findViewById(R.id.tab_mobile);
        tabBar.setListener(this);
        tabBar.setMenu(R.mipmap.common_tab_refresh_white, R.string.mobile_all, R.string.picture, R.string.music, R.string.video, R.string.application);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new MobilePagerAdapter(getChildFragmentManager()));
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(1);
        return view;
    }

    @Override
    public void onTabSelect(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int index) {
        tabBar.setCurrentTab(index);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
