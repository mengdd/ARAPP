package com.mengdd.poi.data;

import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.mengdd.poi.ui.BasicMarker;
import com.mengdd.utils.FileUtils;
import com.mengdd.utils.HttpUtils;

/**
 * This abstract class should be extended for new data sources. It has many
 * methods to get and parse data from numerous web sources.
 * 
 * The source of the codes are adapted from:
 * 1."android-augment-reality-framework" project link:
 * http://code.google.com/p/android-augment-reality-framework/
 * 
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
public abstract class NetworkDataSource extends DataSource {

    protected static final int MAX = 5;
    protected static final int READ_TIMEOUT = 10000;
    protected static final int CONNECT_TIMEOUT = 10000;

    protected List<BasicMarker> markersCache = null;

    // the two methods have to be abstract, let the subclass to implement them
    // with details info.
    public abstract String createRequestURL(double lat, double lon, double alt,
            float radius, String locale);

    public abstract List<BasicMarker> parse(JSONObject root);

    /**
     * This method get the Markers if they have already been downloaded once.
     * 
     * @return List of Marker objects or NULL if not downloaded yet.
     */
    public List<BasicMarker> getMarkers() {
        return markersCache;
    }

    /**
     * Parse the given URL for JSON Objects.
     * 
     * @param url
     *            URL to parse.
     * @return List of Marker's from the URL.
     */
    public List<BasicMarker> parse(String url) {
        if (null == url) {
            throw new IllegalArgumentException("url == null !");
        }

        InputStream stream = null;
        stream = HttpUtils.getInputStream(url);
        if (null == stream) {
            throw new NullPointerException(
                    "Get InputScream from HttpUtils.getInputStream(url) but get null! ");
        }

        String string = null;
        string = FileUtils.getInputScreamString(stream);
        if (null == string) {
            throw new NullPointerException(
                    "FileUtils.getInputScreamString return null!");
        }

        JSONObject json = null;
        try {
            json = new JSONObject(string);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if (null == json) {
            throw new NullPointerException();
        }

        return parse(json);
    }
}
