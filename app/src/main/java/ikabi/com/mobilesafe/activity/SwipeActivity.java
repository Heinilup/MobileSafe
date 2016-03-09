package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.activity.SwipeLayout.OnSwipeStateChangeListener;
import ikabi.com.mobilesafe.refresh.HitBlockRefreshView;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/3/8
 */
public class SwipeActivity extends Activity {
    private HitBlockRefreshView refreshView;

    private ListView listView;
    private ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipelayout);
        listView = (ListView) findViewById(R.id.swipe_listview);
        refreshView = (HitBlockRefreshView) findViewById(R.id.refresh_hit_block);
        for (int i = 0; i < 30; i++) {
            list.add("name - " + i);
        }
        listView.setAdapter(new swipeAdapter());
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scroolstate) {
                if(scroolstate== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    SwipeLayoutManager.getInstance().closeCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        refreshView.setOnRefreshListener(new HitBlockRefreshView.HitBlockRefreshListener() {
            @Override
            public void onRefreshing() {
                try {
                    // 模拟网络请求耗时动作
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
        });
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            refreshView.finishRefreshing();
            Toast.makeText(SwipeActivity.this, "Refresh complete!", Toast.LENGTH_SHORT).show();
        }
    };

    class swipeAdapter extends BaseAdapter implements OnSwipeStateChangeListener {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(SwipeActivity.this, R.layout.activity_drag_list, null);

            }
            ViewHolder holder = ViewHolder.getHolder(view);
            holder.tv_name.setText(list.get(i));
            holder.mSwipeLayout.setTag(i);
            holder.mSwipeLayout.setOnSwipeStateChangeListener(this);

            return view;
        }

        @Override
        public void open(Object tag) {
            Toast.makeText(SwipeActivity.this, "第" + tag + "个打开", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void close(Object tag) {
            Toast.makeText(SwipeActivity.this, "第" + tag + "个关闭", Toast.LENGTH_SHORT).show();

        }
    }
    public void Rename(View view){
        Toast.makeText(this,"Click Rename",Toast.LENGTH_LONG).show();
    }
    public void Delete(View view){
        Toast.makeText(this,"Click Delete",Toast.LENGTH_LONG).show();
    }

    static class ViewHolder {
        TextView tv_name, tv_delete;
        SwipeLayout mSwipeLayout;

        public ViewHolder(View convertView) {
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            mSwipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipelayout);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
