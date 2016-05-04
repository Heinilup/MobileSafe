package ikabi.com.mobilesafe.view;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stickygridheaders.StickyGridHeadersGridView;
import com.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.business.image.ImageScanner;
import ikabi.com.mobilesafe.business.image.LocalImageView;
import ikabi.com.mobilesafe.business.image.NativeImageLoader;
import ikabi.com.mobilesafe.business.model.PictureInfo;
import ikabi.com.mobilesafe.utils.FileUtils;


public class PictureBrowserControl extends StickyGridHeadersGridView {

    private ImageScanner mScanner;
    PictureAdapter pictureAdapter;
    List<PictureInfo> mList = new ArrayList<PictureInfo>();
    private static int section = 1;
    private Map<String, Integer> sectionMap = new HashMap<String, Integer>();

    public PictureBrowserControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializePicture(context);
    }

    public PictureBrowserControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializePicture(context);
    }

    void initializePicture(Context context) {
        mScanner = new ImageScanner(context);
        pictureAdapter = new PictureAdapter(context);

    }

    public void loadPicture() {
        mScanner.scanImages(new ImageScanner.ScanCompleteCallBack() {
            {
                Log.i("XFile", "开始扫描图片");
                //rogressDialog dialog=ProgressDialog.show(getContext(), "正在扫描图片", "AAAAAAA");
            }

            @Override
            public void scanComplete(Cursor cursor) {

                while (cursor.moveToNext()) {

                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    long times = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));

                    PictureInfo pictureVO = new PictureInfo(path, FileUtils.parseTimeToYMD(times));
                    mList.add(pictureVO);

                }
                cursor.close();
                Collections.sort(mList);

                for (ListIterator<PictureInfo> it = mList.listIterator(); it.hasNext();) {
                    PictureInfo mGridItem = it.next();
                    String ym = mGridItem.getCreateTime();
                    if (!sectionMap.containsKey(ym)) {
                        mGridItem.setSection(section);
                        sectionMap.put(ym, section);
                        section++;
                    } else {
                        mGridItem.setSection(sectionMap.get(ym));
                    }
                }
                setAdapter(pictureAdapter);
                Log.i("XFile", "mList.size():" + mList.size());

            }
        });
    }

    class PictureAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

        private LayoutInflater inflater;
        private Point mPoint = new Point(0, 0);

        public PictureAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        final class HeaderViewHolder {
            TextView name;
            TextView sel;
        }

        final class GroupItemViewHolder {
            LocalImageView image;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Item布局
            GroupItemViewHolder holder = null;
            if (convertView == null) {
                holder = new GroupItemViewHolder();
                convertView = inflater.inflate(R.layout.group_gridview_picture_item, null);
                holder.image = (LocalImageView) convertView.findViewById(R.id.group_item);
                convertView.setTag(holder);
                holder.image.setOnMeasureListener(new LocalImageView.OnMeasureListener() {
                    @Override
                    public void onMeasureSize(int width, int height) {
                        mPoint.set(width, height);
                    }
                });

            } else {
                holder = (GroupItemViewHolder) convertView.getTag();
            }
            PictureInfo vo = mList.get(position);
            if (vo != null) {
                holder.image.setTag(vo.getFilePath());
                String filePath = vo.getFilePath();
                Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(filePath, mPoint, new NativeImageLoader.NativeImageCallBack() {

                    @Override
                    public void onImageLoader(Bitmap bitmap, String path) {
                        ImageView mImageView = (ImageView) PictureBrowserControl.this.findViewWithTag(path);
                        if (bitmap != null && mImageView != null) {
                            mImageView.setImageBitmap(bitmap);
                        }
                    }
                });

                if (bitmap != null) {
                    holder.image.setImageBitmap(bitmap);
                }
            }
            return convertView;

        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            // 头布局
            HeaderViewHolder holder = null;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.group_gridview_picture_header, null);
                holder.name = (TextView) convertView.findViewById(R.id.headerName);
                holder.sel = (TextView) convertView.findViewById(R.id.headerSelect);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            holder.name.setText(mList.get(position).getCreateTime());
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            // TODO Auto-generated method stub
            return mList.get(position).getSection();
        }

    }

}
