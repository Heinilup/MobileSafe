package ikabi.com.mobilesafe.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huangjiang.business.app.AppScanner;
import com.huangjiang.business.app.AppScanner.ScanAppsCompleteCallBack;
import com.huangjiang.business.image.LocalImageView;
import com.huangjiang.business.image.LocalImageView.OnMeasureListener;
import com.huangjiang.business.model.AppInfo;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ikabi.com.mobilesafe.R;

public class AppBrowserControl extends StickyGridHeadersGridView{

	AppScanner mAppScanner;

	AppsAdapter appsAdapter;
	List<AppInfo> mList = new ArrayList<AppInfo>();
	private static int section = 1;
	private Map<String, Integer> sectionMap = new HashMap<String, Integer>();
	
	public AppBrowserControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initializeApps(context);
	}

	public AppBrowserControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initializeApps(context);
	}
	
	void initializeApps(Context context) {
		mAppScanner = new AppScanner(context);
		appsAdapter = new AppsAdapter(context);
	}
	
	
	public void loadApps() {
		mAppScanner.scanImages(new ScanAppsCompleteCallBack() {
			{
				// 获取应用程序列表
			}

			@Override
			public void scanComplete(List<AppInfo> list) {
				mList = list;
				setAdapter(appsAdapter);

			}
		});
	}
	
	
	
	class AppsAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

		private LayoutInflater inflater;
		private Point mPoint = new Point(0, 0);

		public AppsAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		final class HeaderViewHolder {
			TextView name;
			TextView sel;
		}

		final class GroupItemViewHolder {
			LocalImageView image;
			TextView appName;
			TextView appSize;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Item布局
			GroupItemViewHolder holder = null;
			if (convertView == null) {
				holder = new GroupItemViewHolder();
				convertView = inflater.inflate(R.layout.group_gridview_apps_item, null);
				holder.image = (LocalImageView) convertView.findViewById(R.id.group_item);
				holder.appName=(TextView)convertView.findViewById(R.id.appName);
				holder.appSize=(TextView)convertView.findViewById(R.id.txtSize);
				
				convertView.setTag(holder);
				holder.image.setOnMeasureListener(new OnMeasureListener() {
					@Override
					public void onMeasureSize(int width, int height) {
						mPoint.set(width, height);
					}
				});

			} else {
				holder = (GroupItemViewHolder) convertView.getTag();
			}
			AppInfo vo = mList.get(position);
			if (vo != null) {
				holder.image.setImageDrawable(vo.getAppIcon());
				holder.appName.setText(vo.getAppName());
				holder.appSize.setText(vo.getAppSizeStr());
			}
			return convertView;

		}

		@Override
		public View getHeaderView(int position, View convertView, ViewGroup parent) {
			// 头布局
			HeaderViewHolder holder = null;
			if (convertView == null) {
				holder = new HeaderViewHolder();
				convertView = inflater.inflate(R.layout.group_gridview_apps_header, null);
				holder.name = (TextView) convertView.findViewById(R.id.headerName);
				holder.sel = (TextView) convertView.findViewById(R.id.headerSelect);
				convertView.setTag(holder);
			} else {
				holder = (HeaderViewHolder) convertView.getTag();
			}
			String ym = "本地应用(" + mList.size() + ")";
			holder.name.setText(ym);
			return convertView;
		}

		@Override
		public long getHeaderId(int position) {
			// TODO Auto-generated method stub
			return -1;
		}

	}
	
	
	
}
