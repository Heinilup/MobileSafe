package ikabi.com.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ikabi.com.mobilesafe.bean.HomeItem;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/4
 */
public class HomeActivity extends Activity implements AdapterView.OnItemClickListener{
    private static final String TAG = "HomeActivity";

    private final static String[] TITLES = new String[] { "手机防盗", "骚扰拦截",
            "软件管家", "进程管理", "流量统计", "手机杀毒", "缓存清理", "常用工具" };
    private final static String[] DESCS = new String[] { "远程定位手机", "全面拦截骚扰",
            "管理您的软件", "管理运行进程", "流量一目了然", "病毒无处藏身", "系统快如火箭", "工具大全" };

    private final static int[] ICONS = new int[] { R.drawable.sjfd,
            R.drawable.srlj, R.drawable.rjgj, R.drawable.jcgl, R.drawable.lltj,
            R.drawable.sjsd, R.drawable.hcql, R.drawable.cygj };
    private GridView mGridView;
    private List<HomeItem> mDates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mGridView = (GridView) findViewById(R.id.home_grid_view);

        //初始化List数据

        mDates = new ArrayList<HomeItem>();
        for (int i = 0; i < ICONS.length; i++){
            HomeItem item = new HomeItem();
            item.iconID = ICONS[i];
            item.title = TITLES[i];
            item.desc = DESCS[i];
            //添加到mDates
            mDates.add(item);

        }

        //设置gridview的adapter
        mGridView.setAdapter(new HomeAdatper());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private class HomeAdatper extends BaseAdapter {
        @Override
        public int getCount() {
            if(mDates != null){
                return mDates.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(mDates != null){
                return mDates.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(HomeActivity.this, R.layout.item_home, null);

            ImageView ivIcon = (ImageView) view.findViewById(R.id.item_home_iv_icon);
            TextView tvTitle = (TextView) view.findViewById(R.id.item_home_tv_title);
            TextView tvDesc = (TextView) view.findViewById(R.id.item_home_tv_desc);

            HomeItem item = mDates.get(position);
            ivIcon.setImageResource(item.iconID);
            tvTitle.setText(item.title);
            tvDesc.setText(item.desc);

            return view1;
        }
    }
}
