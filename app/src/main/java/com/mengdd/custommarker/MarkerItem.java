package com.mengdd.custommarker;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MarkerItem {
    /*
     * Keys in database MARKER_NAME = "marker_name"; MARKER_DESCRIPTION =
     * "marker_description";
     * 
     * COORDINATE_TYPE = "coordinate_type"; LATITUDE = "latitude"; LONGITUDE =
     * "longitude";
     * 
     * MARKER_TYPE = "marker_type"; RESOURCE = "resource";
     * 
     * CHECK_TIMES = "check_times"; CREATE_DATE = "create_date"; LAST_DATE =
     * "last_date";
     */
    private OverlayItem overlayItem = null;
    private GeoPoint geoPoint = null;

    private String name = null;
    private String description = null;
    private int mId = 0;
    private String markerType = "2d_marker";
    private String coordinateType = "baidu_coordinate";
    private int resourceId1;
    private int resourceId2;
    private int checkTimes = 0;
    private String createDate = null;
    private String lastVisitDate = null;

    private boolean isFixed = false;

    private Drawable moveDrawable = null;
    private Drawable fixedDrawable = null;

    // 为了在List中编辑的时候判断其选中状态
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean isFixed) {
        this.isFixed = isFixed;
        if (isFixed) {
            overlayItem.setMarker(fixedDrawable);
        }
        else {
            overlayItem.setMarker(moveDrawable);
        }
    }

    public MarkerItem() {
        overlayItem = new OverlayItem(new GeoPoint(0, 0), "", "");
        this.geoPoint = new GeoPoint(0, 0);
        name = "";
        description = "";
        setFixed(false);

    }

    public MarkerItem(GeoPoint point, String title, String snippet,
            int moveDrawableId, int fixedDrawableId) {
        this.geoPoint = point;
        overlayItem = new OverlayItem(point, title, snippet);
        name = title;
        description = snippet;

        resourceId1 = moveDrawableId;
        resourceId2 = fixedDrawableId;
    }

    public void getDrawables(Resources resources) {

        this.moveDrawable = resources.getDrawable(resourceId1);
        this.fixedDrawable = resources.getDrawable(resourceId2);

        setFixed(false);
    }

    public MarkerItem(GeoPoint point, String title, String snippet,
            int moveDrawableId, int fixedDrawableId, Resources resources) {

        this.geoPoint = point;
        overlayItem = new OverlayItem(point, title, snippet);
        name = title;
        description = snippet;

        resourceId1 = moveDrawableId;
        resourceId2 = fixedDrawableId;

        this.moveDrawable = resources.getDrawable(moveDrawableId);
        this.fixedDrawable = resources.getDrawable(fixedDrawableId);

        setFixed(false);

    }

    public MarkerItem(GeoPoint point, String title, String snippet,
            Drawable moveDrawable, Drawable fixedDrawable) {
        this.geoPoint = point;
        overlayItem = new OverlayItem(point, title, snippet);
        name = title;
        description = snippet;

        this.moveDrawable = moveDrawable;
        this.fixedDrawable = fixedDrawable;
        setFixed(false);

    }

    public OverlayItem getItem() {
        return overlayItem;

    }

    public void setPosition(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
        overlayItem.setGeoPoint(geoPoint);

    }

    public GeoPoint getPosition() {
        return geoPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        overlayItem.setTitle(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        overlayItem.setSnippet(description);
    }

    public String getMarkerType() {
        return markerType;
    }

    public void setMarkerType(String marker_type) {
        this.markerType = marker_type;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(String coordinate_type) {
        this.coordinateType = coordinate_type;
    }

    public int getCheckTimes() {
        return checkTimes;
    }

    public void setCheckTimes(int checkTimes) {
        this.checkTimes = checkTimes;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(String lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public int getId() {
        return mId;
    }

    public void setId(int Id) {
        this.mId = Id;
    }

    public int getResourceId1() {
        return resourceId1;
    }

    public void setResourceId1(int resourceId1) {
        this.resourceId1 = resourceId1;
    }

    public int getResourceId2() {
        return resourceId2;
    }

    public void setResourceId2(int resourceId2) {
        this.resourceId2 = resourceId2;
    }

}
