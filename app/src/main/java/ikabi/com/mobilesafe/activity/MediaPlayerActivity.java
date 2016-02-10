package ikabi.com.mobilesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.utils.VideoItem;

/**
 * Created by Administrator on 2016/2/9 0009.
 */
public class MediaPlayerActivity extends Activity {

    private ArrayList<VideoItem> videoItems;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (videoItems != null && videoItems.size() > 0) {
                lv_novideo.setVisibility(View.GONE);
                lv_videolist.setAdapter(new VideoListAdapter());

            } else {
                lv_novideo.setVisibility(View.VISIBLE);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplayer);
        getAllVideo();
    }

    private class VideoListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return videoItems.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(MediaPlayerActivity.this, R.layout.activity_welcome, null);
                holder = new ViewHolder();
                holder.vi_name = (TextView) view.findViewById(R.id.vi_name);
                holder.vi_duration = (TextView) view.findViewById(R.id.vi_duration);
                holder.vi_size = (TextView) view.findViewById(R.id.vi_size);
                //对象关系
                view.setTag(holder);

            }
            //根据位置，得到具体某一个视频的信息
            VideoItem videoItem = videoItems.get(position);
            holder.vi_name.setText(videoItem.getTitle());
            holder.vi_duration.setText(utils.stringForTime(Integer.valueOf(videoItem.getDuration())));
            holder.vi_size.setText(Formatter.formatFileSize(MediaPlayerActivity.this, videoItem.getSize()));

            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


    }

    static class ViewHolder {
        TextView vi_name;
        TextView vi_duration;
        TextView vi_size;
    }

    /**
     * 在子线程加载视频
     */

    private void getAllVideo() {
        new Thread() {
            public void run() {
                //把子线程视频读取出来
                videoItems = new ArrayList<VideoItem>();
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {
                        MediaStore.Video.Media.TITLE,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DATA,
                };
                Cursor cursor = resolver.query(uri, projection, null, null, null);
                while (cursor.moveToNext()) {
                    long size = cursor.getLong(2);
                    //具体视频信息
                    if (size > 3 * 1024 * 1024) {
                        VideoItem item = new VideoItem();

                        String title = cursor.getString(0);
                        item.setTitle(title);

                        String duration = cursor.getString(1);
                        item.setDuration(duration);


                        item.setSize(size);

                        String data = cursor.getString(3);
                        item.setData(data);

                        videoItems.add(item);
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }.start();

    }
}
