package com.mengdd.db;

import java.util.ArrayList;
import java.util.Collection;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mengdd.custommarker.MarkerItem;
import com.mengdd.utils.AppConstants;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class CustomMarkerTable implements BaseColumns {

    private static final String TABLE_NAME = "custom_markers";

    private static final String MARKER_NAME = "marker_name";
    private static final String MARKER_DESCRIPTION = "marker_description";

    private static final String COORDINATE_TYPE = "coordinate_type";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private static final String MARKER_TYPE = "marker_type";
    private static final String RESOURCE1 = "resource1";
    private static final String RESOURCE2 = "resource2";
    private static final String RESOURCE3 = "resource3";

    private static final String CHECK_TIMES = "check_times";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_DATE = "last_date";

    private static SQLiteDatabase getDatabase() {
        // 获取数据库对象
        SQLiteDatabase db = DatabaseManager.getInstance().getDatabase();
        return db;
    }

    private static String getCreateTableString() {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer
                .append("CREATE TABLE IF NOT EXISTS [" + TABLE_NAME + "] (");
        stringBuffer.append("[" + _ID
                + "] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        stringBuffer.append("[" + MARKER_NAME + "] TEXT, ");
        stringBuffer.append("[" + MARKER_DESCRIPTION + "] TEXT, ");

        stringBuffer.append("[" + COORDINATE_TYPE + "] TEXT, ");
        stringBuffer.append("[" + LATITUDE + "] [DECIMAL(6,6)], ");
        stringBuffer.append("[" + LONGITUDE + "] [DECIMAL(6,6)], ");

        stringBuffer.append("[" + MARKER_TYPE + "] TEXT, ");
        stringBuffer.append("[" + RESOURCE1 + "] INTEGER(20), ");
        stringBuffer.append("[" + RESOURCE2 + "] INTEGER(20), ");
        stringBuffer.append("[" + RESOURCE3 + "] INTEGER(20), ");

        stringBuffer.append("[" + CHECK_TIMES + "] INTEGER, ");
        stringBuffer.append("[" + CREATE_DATE + "] TEXT, ");
        stringBuffer.append("[" + LAST_DATE + "] TEXT)");

        Log.i(AppConstants.LOG_TAG, stringBuffer.toString());
        return stringBuffer.toString();

    }

    public static void createTable() {
        SQLiteDatabase db = getDatabase();

        if (!db.isOpen()) {
            return;
        }

        String sql = CustomMarkerTable.getCreateTableString();
        try {
            db.execSQL(sql);

        }
        catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static boolean isTableExist() {
        boolean isExist = false;

        SQLiteDatabase db = getDatabase();
        if (!db.isOpen()) {
            return false;
        }

        String where = _ID + " = ?";
        String[] whereValue = { Integer.toString(1) };
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, null, where, whereValue, null, null,
                    null);
            if (cursor != null && cursor.moveToNext()) {
                isExist = true;

            }
        }
        catch (SQLException e) {

            if (e.getMessage().contains("no such table")) {
                isExist = false;
            }
        }
        finally {
            if (cursor != null) {
                try {
                    cursor.close();
                }
                catch (Exception e) {
                }
            }
        }

        return isExist;
    }

    public static ContentValues getContentValues(MarkerItem item) {
        if (null == item) {
            throw new IllegalArgumentException("MarkerItem item is null!");
        }

        ContentValues cv = new ContentValues();

        cv.put(MARKER_NAME, item.getName());
        cv.put(MARKER_DESCRIPTION, item.getDescription());
        cv.put(LATITUDE, item.getPosition().getLatitudeE6() / 1E6);
        cv.put(LONGITUDE, item.getPosition().getLongitudeE6() / 1E6);

        cv.put(COORDINATE_TYPE, item.getCoordinateType());
        cv.put(MARKER_TYPE, item.getMarkerType());

        cv.put(RESOURCE1, item.getResourceId1());
        cv.put(RESOURCE2, item.getResourceId2());

        cv.put(CHECK_TIMES, item.getCheckTimes());
        cv.put(CREATE_DATE, item.getCreateDate());
        cv.put(LAST_DATE, item.getLastVisitDate());

        return cv;

    }

    public static MarkerItem getMarkerItemFromCursor(Cursor cursor) {
        if (null == cursor) {
            throw new IllegalArgumentException("cursor is null!");
        }

        int id = cursor.getInt(cursor.getColumnIndex(_ID));
        String name = cursor.getString(cursor.getColumnIndex(MARKER_NAME));
        String description = cursor.getString(cursor
                .getColumnIndex(MARKER_DESCRIPTION));
        int resourceId1 = cursor.getInt(cursor.getColumnIndex(RESOURCE1));
        int resourceId2 = cursor.getInt(cursor.getColumnIndex(RESOURCE2));
        double latitude = cursor.getDouble(cursor.getColumnIndex(LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndex(LONGITUDE));
        GeoPoint geoPoint = new GeoPoint((int) (latitude * 1E6),
                (int) (longitude * 1E6));

        MarkerItem item = new MarkerItem(geoPoint, name, description,
                resourceId1, resourceId2);

        item.setId(id);

        String coordinateType = cursor.getString(cursor
                .getColumnIndex(COORDINATE_TYPE));
        item.setCoordinateType(coordinateType);

        String markerType = cursor
                .getString(cursor.getColumnIndex(MARKER_TYPE));
        item.setMarkerType(markerType);

        int checkTimes = cursor.getInt(cursor.getColumnIndex(CHECK_TIMES));
        item.setCheckTimes(checkTimes);

        String createDate = cursor
                .getString(cursor.getColumnIndex(CREATE_DATE));
        item.setCreateDate(createDate);

        String lastVisitDate = cursor.getString(cursor
                .getColumnIndex(LAST_DATE));
        item.setLastVisitDate(lastVisitDate);

        return item;
    }

    public static long insert(MarkerItem item) {
        long ret = -1L;

        if (null == item || item.getId() > 0)// id>0, means the item is in table
                                             // already.
        {
            return ret;

        }
        SQLiteDatabase db = getDatabase();

        if (!db.isOpen()) {
            return ret;
        }

        db.beginTransaction();
        try {
            ret = db.insert(TABLE_NAME, null, getContentValues(item));
            db.setTransactionSuccessful();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }

        if (ret > 0) {
            // insert successfully

            item.setId((int) ret);

        }

        return ret;

    }

    public static int insert(Collection<MarkerItem> markerItems) {
        int count = 0;

        if (null == markerItems || 0 == markerItems.size()) {
            return count;
        }
        SQLiteDatabase db = getDatabase();

        if (!db.isOpen()) {
            return count;
        }

        db.beginTransaction();
        try {

            for (MarkerItem item : markerItems) {
                if (null == item || item.getId() > 0) {
                    continue;
                }
                long ret = db.insert(TABLE_NAME, null, getContentValues(item));

                // 放入数据库中之后设置Id
                if (ret > 0) {
                    item.setId((int) ret);
                    ++count;
                }
            }
            db.setTransactionSuccessful();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }

        return count;

    }

    public static boolean delete(int markerItemId) {
        boolean ret = false;

        if (markerItemId <= 0) {
            return ret;
        }

        SQLiteDatabase db = getDatabase();
        if (!db.isOpen()) {
            return false;
        }

        String where = _ID + " = ?";
        String[] whereValue = { Integer.toString(markerItemId) };

        db.beginTransaction();
        try {
            int rn = db.delete(TABLE_NAME, where, whereValue);
            ret = rn == 1;
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally {
            db.endTransaction();
        }

        return ret;
    }

    public static int deleteItems(final Collection<MarkerItem> items) {
        int count = 0;

        if (null == items || 0 == items.size()) {
            return count;
        }

        SQLiteDatabase db = getDatabase();
        if (!db.isOpen()) {
            return count;
        }

        String where = _ID + " = ?";
        String[] whereValue = new String[1];

        db.beginTransaction();
        try {
            for (MarkerItem item : items) {
                whereValue[0] = Integer.toString(item.getId());
                int rn = db.delete(TABLE_NAME, where, whereValue);
                count += rn;
            }
            db.setTransactionSuccessful();

        }
        catch (Exception e) {

        }
        finally {
            db.endTransaction();
        }
        return count;

    }

    public static boolean clearAll() {
        boolean ret = false;

        SQLiteDatabase db = getDatabase();

        if (!db.isOpen()) {
            return ret;
        }

        String sql = "Delete FROM [" + TABLE_NAME + "]";

        db.beginTransaction();
        try {
            db.execSQL(sql);
            ret = true;
            db.setTransactionSuccessful();

        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        finally {
            db.endTransaction();
        }

        return ret;

    }

    public static boolean update(final MarkerItem markerItem) {
        boolean ret = false;

        if (null == markerItem || markerItem.getId() <= 0) {
            return ret;
        }

        SQLiteDatabase db = getDatabase();
        if (!db.isOpen()) {
            return ret;
        }

        String where = _ID + " = ?";
        String[] whereValue = { Integer.toString(markerItem.getId()) };
        db.beginTransaction();
        try {
            int rn = db.update(TABLE_NAME, getContentValues(markerItem), where,
                    whereValue);
            ret = rn == 1;
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally {
            db.endTransaction();
        }
        return ret;

    }

    public static int update(final Collection<MarkerItem> items) {
        int count = 0;

        if (null == items || 0 == items.size()) {
            return count;
        }

        SQLiteDatabase db = getDatabase();
        if (!db.isOpen()) {
            return count;
        }

        String where = _ID + " = ?";
        String[] whereValue = new String[1];

        db.beginTransaction();
        try {
            for (MarkerItem item : items) {
                whereValue[0] = Integer.toString(item.getId());
                int rn = db.update(TABLE_NAME, getContentValues(item), where,
                        whereValue);
                count += rn;
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e) {

        }
        finally {
            db.endTransaction();
        }
        return count;

    }

    public static MarkerItem query(int markerId) {
        MarkerItem item = null;

        if (markerId <= 0) {
            return item;
        }

        SQLiteDatabase db = getDatabase();
        if (!db.isOpen()) {
            return item;
        }
        String where = _ID + " = ?";
        String[] whereValue = { Integer.toString(markerId) };
        Cursor cursor = null;

        try {

            cursor = db.query(TABLE_NAME, null, where, whereValue, null, null,
                    null);

            if (null != cursor && cursor.moveToNext()) {

                item = getMarkerItemFromCursor(cursor);
            }

        }
        catch (Exception e) {

            e.printStackTrace();

        }
        finally {
            if (null != cursor) {
                try {

                    cursor.close();

                }
                catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
        return item;
    }

    public static Collection<MarkerItem> queryAllCustomMarkerItems() {
        Collection<MarkerItem> items = null;

        SQLiteDatabase db = getDatabase();
        if (!db.isOpen()) {
            return items;
        }

        Cursor cursor = null;
        String orderBy = _ID + " ASC";

        try {
            cursor = db
                    .query(TABLE_NAME, null, null, null, null, null, orderBy);

            if (null != cursor && cursor.getCount() > 0) {
                items = new ArrayList<MarkerItem>();
                while (cursor.moveToNext()) {
                    MarkerItem item = getMarkerItemFromCursor(cursor);
                    if (item != null) {
                        items.add(item);
                    }
                }
            }
        }
        finally {

            if (cursor != null) {
                try {
                    cursor.close();
                }
                catch (Exception e) {
                }
            }
        }

        return items;
    }
}
