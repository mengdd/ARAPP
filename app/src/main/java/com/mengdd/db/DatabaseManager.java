package com.mengdd.db;

import java.io.File;

import com.mengdd.utils.AppConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseManager {
    private static DatabaseManager mInstance = null;

    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase = null;

    private static final String DATABASE_NAME = "TestDB.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseManager(Context context) {
        Log.d(AppConstants.LOG_TAG, "DBManager --> Constructor");

        // CursorFactory设置为null,使用系统默认的工厂类
        mHelper = new DatabaseHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        mDatabase = mHelper.getWritableDatabase();
    }

    public static synchronized void initInstance(Context context) {
        File databaseDir = new File("/data/data/com.mengdd.arapp/databases");
        if (!databaseDir.exists()) {
            databaseDir.mkdirs();
        }

        File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (null == mInstance) {
            mInstance = new DatabaseManager(context);
        }
    }

    public static DatabaseManager getInstance() {
        return mInstance;

    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    public void close() {
        // NativeDBManager.getInstance().closeDb();
        if (mDatabase != null && mDatabase.isOpen()) {
            try {
                mDatabase.close();
            }
            catch (Exception e) {
            }
        }
    }
}
