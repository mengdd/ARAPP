package com.mengdd.poi.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.mengdd.arapp.GlobalARData;
import android.content.Context;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class RadarZoomController {
    private SeekBar mSeekBar = null;
    private static final int MAX_ZOOM = 100;// in KM
    private static final int INIT_PROGRESS = 50;

    private static final float ONE_PERCENT = MAX_ZOOM / 100f;
    private static final float TEN_PERCENT = 10f * ONE_PERCENT;
    private static final float TWENTY_PERCENT = 2f * TEN_PERCENT;
    private static final float EIGHTY_PERCENTY = 4f * TWENTY_PERCENT;

    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    public RadarZoomController(Context context) {
        mSeekBar = new SeekBar(context);
        mSeekBar.setMax(MAX_ZOOM);
        mSeekBar.setProgress(INIT_PROGRESS);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

    }

    public SeekBar getSeekBar() {
        return mSeekBar;
    }

    private OnSeekBarChangeListener mSeekBarChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                boolean fromUser) {

            updateDataOnZoom();
        }
    };

    public interface OnRadarZoomChangedListener {
        public void onZoomChanged();
    }

    private List<OnRadarZoomChangedListener> mZoomChangedListeners = null;

    public void addOnZoomChangedListener(OnRadarZoomChangedListener listener) {

        if (null == listener) {
            throw new IllegalArgumentException("listener is null!");
        }

        if (null == mZoomChangedListeners) {
            mZoomChangedListeners = new ArrayList<RadarZoomController.OnRadarZoomChangedListener>();
        }

        mZoomChangedListeners.add(listener);

    }

    public boolean removeOnZoomChangedListener(
            OnRadarZoomChangedListener listener) {
        boolean result = false;
        if (null == listener) {
            throw new IllegalArgumentException("listener is null!");
        }

        if (null != mZoomChangedListeners) {
            result = mZoomChangedListeners.remove(listener);
        }

        return result;
    }

    private void updateDataOnZoom() {

        float zoomLevel = calcZoomLevel();
        GlobalARData.setRadius(zoomLevel);
        GlobalARData.setZoomLevel(FORMAT.format(zoomLevel));
        GlobalARData.setZoomProgress(mSeekBar.getProgress());

        // outsider listener
        if (null != mZoomChangedListeners) {
            for (OnRadarZoomChangedListener listener : mZoomChangedListeners) {
                listener.onZoomChanged();
            }
        }
    }

    private float calcZoomLevel() {
        int myZoomLevel = mSeekBar.getProgress();
        float myout = 0;

        float percent = 0;
        if (myZoomLevel <= 25) {
            percent = myZoomLevel / 25f;
            myout = ONE_PERCENT * percent;
        }
        else if (myZoomLevel > 25 && myZoomLevel <= 50) {
            percent = (myZoomLevel - 25f) / 25f;
            myout = ONE_PERCENT + (TEN_PERCENT * percent);
        }
        else if (myZoomLevel > 50 && myZoomLevel <= 75) {
            percent = (myZoomLevel - 50f) / 25f;
            myout = TEN_PERCENT + (TWENTY_PERCENT * percent);
        }
        else {
            percent = (myZoomLevel - 75f) / 25f;
            myout = TWENTY_PERCENT + (EIGHTY_PERCENTY * percent);
        }

        return myout;
    }

}
