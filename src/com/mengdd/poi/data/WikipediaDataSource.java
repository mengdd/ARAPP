package com.mengdd.poi.data;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mengdd.ar.ui.IconMarker;
import com.mengdd.ar.ui.Marker;
import com.mengdd.arapp.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

/**
 * This class extends DataSource to fetch data from Wikipedia.
 * 
 * The source of the codes:
 * 1."android-augment-reality-framework"
 * project link: http://code.google.com/p/android-augment-reality-framework/
 * 
 * 2.The book: "Pro Android Augmented Reality"
 * http://www.apress.com/9781430239451
 * Official repository for Pro Android Augmented Reality:
 * https://github.com/RaghavSood/ProAndroidAugmentedReality
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 */
public class WikipediaDataSource extends NetworkDataSource
{

	private static final String BASE_URL = "http://ws.geonames.org/findNearbyWikipediaJSON";

	private static Bitmap icon = null;

	public WikipediaDataSource(Resources res)
	{
		if (null == res)
		{
			throw new IllegalArgumentException("Resource object res is null!");
		}

		createIcon(res);
	}

	protected void createIcon(Resources res)
	{
		icon = BitmapFactory.decodeResource(res, R.drawable.wikipedia);
	}

	@Override
	public String createRequestURL(double lat, double lon, double alt,
			float radius, String locale)
	{
		return BASE_URL + "?lat=" + lat + "&lng=" + lon + "&radius=" + radius
				+ "&maxRows=40" + "&lang=" + locale;

	}

	@Override
	public List<Marker> parse(JSONObject root)
	{
		if (root == null)
			return null;

		JSONObject jo = null;
		JSONArray dataArray = null;
		List<Marker> markers = new ArrayList<Marker>();

		try
		{
			if (root.has("geonames"))
			{
				dataArray = root.getJSONArray("geonames");
			}
			if (null == dataArray)
			{
				return markers;
			}
			int top = Math.min(MAX, dataArray.length());
			for (int i = 0; i < top; i++)
			{
				jo = dataArray.getJSONObject(i);
				Marker ma = processJSONObject(jo);
				if (null != ma)
				{
					markers.add(ma);
				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return markers;
	}

	/**
	 * Process a single JSONObject and use its information to create a marker.
	 * 
	 * @param jsonObject
	 * @return Marker
	 */
	private Marker processJSONObject(JSONObject jsonObject)
	{
		if (null == jsonObject)
		{
			return null;
		}

		Marker ma = null;
		if (jsonObject.has("title") && jsonObject.has("lat")
				&& jsonObject.has("lng") && jsonObject.has("elevation"))
		{
			try
			{
				ma = new IconMarker(jsonObject.getString("title"),
						jsonObject.getDouble("lat"),
						jsonObject.getDouble("lng"),
						jsonObject.getDouble("elevation"), Color.WHITE, icon);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return ma;
	}
}
