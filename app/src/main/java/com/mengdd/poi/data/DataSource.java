package com.mengdd.poi.data;

import com.mengdd.poi.ui.BasicMarker;

import java.util.List;

/**
 * This abstract class should be extended for new data sources.
 * <p/>
 * The source of the codes: 1."android-augment-reality-framework" project link:
 * http://code.google.com/p/android-augment-reality-framework/
 * <p/>
 * <p/>
 * 2.The book: "Pro Android Augmented Reality"
 * http://www.apress.com/9781430239451 Official repository for Pro Android
 * Augmented Reality: https://github.com/RaghavSood/ProAndroidAugmentedReality
 *
 * @author Justin Wetherell <phishman3579@gmail.com>
 * @author Dandan Meng <mengdandanno1@163.com>
 * @version 1.0
 * @since 2013-07-01
 */
public abstract class DataSource {

    public abstract List<BasicMarker> getMarkers();
}
