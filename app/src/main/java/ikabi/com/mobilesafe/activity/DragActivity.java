package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.utils.Constant;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/3/5
 */
public class DragActivity extends Activity {
    private ListView menu_Listview;
    private ListView main_Listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_menu);
        menu_Listview = (ListView) findViewById(R.id.menu_listview);
        menu_Listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Constant.sCheeseStrings) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        });
        main_Listview = (ListView) findViewById(R.id.main_listview);
        main_Listview.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_drag_list, Constant.NAMES) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView == null ? super.getView(position, convertView, parent):convertView;
                ViewHelper.setScaleX(view, 0.5f);
                ViewHelper.setScaleY(view, 0.5f);
                ViewPropertyAnimator.animate(view).scaleX(1).setDuration(350).start();
                ViewPropertyAnimator.animate(view).scaleY(1).setDuration(350).start();
                return view;
            }
        });

    }
}
