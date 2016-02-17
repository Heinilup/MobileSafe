package ikabi.com.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import ikabi.com.mobilesafe.R;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:16/2/17
 */
public class ProgressDesView extends LinearLayout {

    private TextView mTvTile;
    private TextView mTvLeftTile;
    private TextView mTvRightTile;
    private ProgressBar mProgressBar;

    public ProgressDesView(Context context) {
        this(context,null);
    }
    public ProgressDesView(Context context, AttributeSet attrs) {
        super(context, attrs);



        // xml layout
        View.inflate(context, R.layout.view_progress_des, this);

        mTvTile = (TextView) findViewById(R.id.view_tv_title);
        mTvLeftTile = (TextView) findViewById(R.id.view_tv_left_title);
        mTvRightTile = (TextView) findViewById(R.id.view_tv_right_title);
        mProgressBar = (ProgressBar) findViewById(R.id.view_progressBar);
    }

    public void setDesTitle(String title){
        mTvTile.setText(title);
    }
    public void setDesLeftTitle(String title){
        mTvLeftTile.setText(title);
    }
    public void setDesRightTitle(String title){
        mTvRightTile.setText(title);
    }
    public void setDesProgress(int progress){
        mProgressBar.setProgress(progress);
    }

}
