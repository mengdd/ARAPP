package com.mengdd.search;

import java.util.List;

import com.baidu.mapapi.search.MKPoiInfo;
import com.mengdd.arapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PoiInfoAdapter extends BaseAdapter {
    private List<MKPoiInfo> mPoiInfoList = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;

    public PoiInfoAdapter(Context context, List<MKPoiInfo> infoList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mPoiInfoList = infoList;
    }

    public void setData(List<MKPoiInfo> infoList) {
        mPoiInfoList = infoList;

    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != mPoiInfoList) {
            count = mPoiInfoList.size();
        }
        return count;
    }

    @Override
    public MKPoiInfo getItem(int position) {
        MKPoiInfo result = null;
        if (null != mPoiInfoList) {
            if (position >= 0 && position < mPoiInfoList.size()) {
                result = mPoiInfoList.get(position);
            }

        }
        return result;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {

            convertView = mInflater.inflate(R.layout.search_poi_item, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.title);
            viewHolder.address = (TextView) convertView
                    .findViewById(R.id.address);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        // set data
        MKPoiInfo item = getItem(position);
        viewHolder.name.setText(item.name);
        viewHolder.address.setText(item.address);

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        TextView address;
    }
}
