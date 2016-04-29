/*
package ikabi.com.mobilesafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangjiang.business.model.Catalog;
import com.huangjiang.filetransfer.R;

import java.util.ArrayList;
import java.util.List;

public class CatalogAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Catalog> list;

    public CatalogAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public List<Catalog> getCatalogs() {
        return list;
    }

    public void addCatalogs(List<Catalog> catalogs) {
        this.list.addAll(catalogs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_explorer_catalog, null);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Catalog vo = list.get(position);
        holder.image.setImageResource(vo.getImage());
        holder.name.setText(vo.getName());
        return convertView;
    }


    final class ViewHolder {
        ImageView image;
        TextView name;
    }

}
*/
