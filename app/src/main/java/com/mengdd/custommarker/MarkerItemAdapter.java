package com.mengdd.custommarker;

import java.util.List;

import com.mengdd.arapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MarkerItemAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<MarkerItem> mMarkerData = null;
    private LayoutInflater mInflater = null;

    public MarkerItemAdapter(Context context, List<MarkerItem> markerItems) {
        mContext = context;
        mMarkerData = markerItems;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setMarkerData(List<MarkerItem> markerItems) {
        mMarkerData = markerItems;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != mMarkerData) {
            count = mMarkerData.size();
        }
        return count;
    }

    @Override
    public MarkerItem getItem(int position) {
        MarkerItem item = null;

        if (null != mMarkerData) {
            item = mMarkerData.get(position);
        }

        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.item_marker_item, null);

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.description = (TextView) convertView
                    .findViewById(R.id.description);
            viewHolder.createTime = (TextView) convertView
                    .findViewById(R.id.createTime);
            viewHolder.checkBox = (CheckBox) convertView
                    .findViewById(R.id.selected);

            // 每当checked的状态改变，就notifyDataSetChanged，从而重新调用getView，来设置item的selected属性
            viewHolder.checkBox
                    .setOnCheckedChangeListener(mOnCheckedChangeListener);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // set item values to the viewHolder:

        MarkerItem markerItem = getItem(position);
        if (null != markerItem) {
            viewHolder.name.setText(markerItem.getName());
            viewHolder.description.setText(markerItem.getDescription());
            viewHolder.createTime.setText(markerItem.getCreateDate());
            // 将对应的item作为tag设置给checkBox
            viewHolder.checkBox.setTag(markerItem);

            viewHolder.checkBox.setChecked(markerItem.isSelected());

        }

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        TextView description;
        TextView createTime;
        CheckBox checkBox;
    }

    private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {

            MarkerItem item = (MarkerItem) buttonView.getTag();
            if (null != item) {
                item.setSelected(isChecked);
            }
        }
    };

}
