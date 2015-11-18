package com.mengdd.poi.data;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mengdd.arapp.R;
import com.mengdd.poi.ui.GoogleMarker;
import com.mengdd.poi.ui.BasicMarker;
import com.mengdd.utils.AppConstants;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

/**
 * This class extends DataSource to fetch data from Wikipedia.
 * 
 * The source of the codes: 1."android-augment-reality-framework" project link:
 * http://code.google.com/p/android-augment-reality-framework/
 * 
 * 2.The book: "Pro Android Augmented Reality"
 * http://www.apress.com/9781430239451 Official repository for Pro Android
 * Augmented Reality: https://github.com/RaghavSood/ProAndroidAugmentedReality
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 */
public class WikipediaDataSource extends NetworkDataSource {

    private static final String BASE_URL = "http://ws.geonames.org/findNearbyWikipediaJSON";

    private static Bitmap icon = null;

    public WikipediaDataSource(Resources res) {
        if (null == res) {
            throw new IllegalArgumentException("Resource object res is null!");
        }

        createIcon(res);
    }

    protected void createIcon(Resources res) {
        icon = BitmapFactory.decodeResource(res, R.drawable.wikipedia);
    }

    @Override
    public String createRequestURL(double lat, double lon, double alt,
            float radius, String locale) {
        Log.i(AppConstants.LOG_TAG, BASE_URL + "?lat=" + lat + "&lng=" + lon
                + "&radius=" + radius + "&maxRows=40" + "&lang=" + locale);
        return BASE_URL + "?lat=" + lat + "&lng=" + lon + "&radius=" + radius
                + "&maxRows=40" + "&lang=" + locale;

    }

    @Override
    public List<BasicMarker> parse(JSONObject root) {
        if (root == null)
            return null;

        JSONObject jo = null;
        JSONArray dataArray = null;
        List<BasicMarker> markers = new ArrayList<BasicMarker>();

        try {
            if (root.has("geonames")) {
                dataArray = root.getJSONArray("geonames");
            }
            if (null == dataArray) {
                return markers;
            }
            int top = Math.min(MAX, dataArray.length());
            for (int i = 0; i < top; i++) {
                jo = dataArray.getJSONObject(i);
                BasicMarker ma = processJSONObject(jo);
                if (null != ma) {
                    markers.add(ma);
                }
            }
        }
        catch (JSONException e) {
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
    private BasicMarker processJSONObject(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        BasicMarker ma = null;
        Log.i(AppConstants.LOG_TAG, "Wiki processJSONObject jsonObject: "
                + jsonObject.toString());
        if (jsonObject.has("title") && jsonObject.has("lat")
                && jsonObject.has("lng") && jsonObject.has("elevation")) {
            try {
                ma = new GoogleMarker(jsonObject.getString("title"),
                        Color.WHITE, icon, jsonObject.getDouble("lat"),
                        jsonObject.getDouble("lng"),
                        jsonObject.getDouble("elevation"));

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ma;
    }
}
