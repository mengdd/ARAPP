package com.mengdd.custommarker;

import java.util.List;

import com.mengdd.arapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MarkerItemAdapter extends BaseAdapter
{
	private Context mContext = null;
	private List<MarkerItem> mMarkerData = null;
	private LayoutInflater mInflater = null;

	public MarkerItemAdapter(Context context, List<MarkerItem> markerItems)
	{
		mContext = context;
		mMarkerData = markerItems;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setMarkerData(List<MarkerItem> markerItems)
	{
		mMarkerData = markerItems;
	}

	@Override
	public int getCount()
	{
		int count = 0;
		if (null != mMarkerData)
		{
			count = mMarkerData.size();
		}
		return count;
	}

	@Override
	public MarkerItem getItem(int position)
	{
		MarkerItem item = null;

		if (null != mMarkerData)
		{
			item = mMarkerData.get(position);
		}

		return item;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (null == convertView)
		{
			viewHolder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.item_marker_item, null);

			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.description = (TextView) convertView
					.findViewById(R.id.description);
			viewHolder.createTime = (TextView) convertView
					.findViewById(R.id.createTime);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// set item values to the viewHolder:

		MarkerItem markerItem = getItem(position);
		if (null != markerItem)
		{
			viewHolder.name.setText(markerItem.getName());
			viewHolder.description.setText(markerItem.getDescription());
			viewHolder.createTime.setText(markerItem.getCreateDate());
		}

		return convertView;
	}

	private static class ViewHolder
	{
		TextView name;
		TextView description;
		TextView createTime;
	}

}
