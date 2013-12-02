package com.mengdd.custommarker;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mengdd.arapp.R;
import com.mengdd.db.CustomMarkerTable;
import com.mengdd.utils.AppConstants;
import com.mengdd.utils.DialogUtils;
import com.mengdd.utils.DialogUtils.OnSaveCustomMarkerListener;
import com.mengdd.utils.UIUtils;

public class MyOverlay extends ItemizedOverlay<OverlayItem> {
    private MapView mMapView = null;

    private List<MarkerItem> mItemsList = null;
    private int mCurrentMoveItemId = -1;
    private MarkerItem mCurItem = null;

    private PopupOverlay mPopupOverlay = null;
    private View viewCache = null;
    private View popupInfo = null;
    private View popupLeft = null;
    private View popupRight = null;
    private TextView popupText = null;

    private Activity mActivity = null;

    public MyOverlay(Activity activity, Drawable defaultMarker, MapView mapView) {
        super(defaultMarker, mapView);

        mActivity = activity;
        mMapView = mapView;

        initOverlay();

    }

    private void initOverlay() {
        mMapView.getOverlays().add(this);
        mItemsList = new ArrayList<MarkerItem>();

        /**
         * 创建一个popupoverlay
         */
        mPopupOverlay = new PopupOverlay(mMapView, popListener);

        viewCache = mActivity.getLayoutInflater().inflate(R.layout.pop_view,
                null);
        popupInfo = (View) viewCache.findViewById(R.id.popinfo);
        popupLeft = (View) viewCache.findViewById(R.id.popleft);
        popupRight = (View) viewCache.findViewById(R.id.popright);
        popupText = (TextView) viewCache.findViewById(R.id.textcache);

    }

    PopupClickListener popListener = new PopupClickListener() {
        @Override
        public void onClickedPopup(int index) {

        }
    };

    public void addNewItem(MarkerItem item) {
        mItemsList.add(item);
        mCurrentMoveItemId = mItemsList.size() - 1;

        this.addItem(item.getItem());
        mMapView.refresh();
    }

    public void clearItems() {
        mItemsList.clear();
        mCurrentMoveItemId = -1;
        this.removeAll();
        mMapView.refresh();
    }

    @Override
    public boolean onTap(int index) {
        Log.i(AppConstants.LOG_TAG, "onTap(int index)");
        // OverlayItem item = getItem(index);

        mCurItem = mItemsList.get(index);

        popupText.setText(getItem(index).getTitle());
        Bitmap[] bitMaps = { UIUtils.getBitmapFromView(popupLeft),
                UIUtils.getBitmapFromView(popupInfo),
                UIUtils.getBitmapFromView(popupRight) };
        mPopupOverlay.showPopup(bitMaps, mCurItem.getPosition(), 32);

        return true;
    }

    @Override
    public boolean onTap(GeoPoint pt, MapView mMapView) {
        Log.i(AppConstants.LOG_TAG, "onTap(GeoPoint pt, MapView mMapView): "
                + pt);
        if (mPopupOverlay != null) {
            mPopupOverlay.hidePop();

        }

        if (-1 != mCurrentMoveItemId) {
            MarkerItem moveItem = mItemsList.get(mCurrentMoveItemId);

            moveItem.setPosition(pt);
            updateItem(moveItem.getItem());
            mMapView.refresh();
        }

        return false;
    }

    public boolean saveMarkerToDb(MarkerItem markerItem) {
        boolean success = false;

        long ret = CustomMarkerTable.insert(markerItem);

        if (ret > 0) {
            mCurrentMoveItemId = -1;
            markerItem.setFixed(true);
            updateItem(markerItem.getItem());

            mMapView.refresh();
            success = true;
        }
        return success;

    }

    public MarkerItem getMovingItem() {
        if (mCurrentMoveItemId >= 0 && mCurrentMoveItemId < mItemsList.size()) {
            return mItemsList.get(mCurrentMoveItemId);
        }
        else {
            return null;
        }
    }

}
