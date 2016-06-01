package com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user2 on 2016/5/15.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String TAG = "DBHelper";

    public static final String TABLE_PKG = "package";
    public static final String PKG_NAME = "pkgname";
    public static final String PKG_APPNAME = "appname";
    public static final String PKG_STATS = "pkgstats";

    public static final String  TABLE_RECORD = "record";
    public static final String  RECORD_DATE = "date";
    public static final String  RECORD_STEP = "step";
    public static final String  RECORD_USE_PHONE_TIME = "use_phone_time";


    private static final String DATABASE_NAME = "stepstop.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_PKG = "CREATE TABLE " + TABLE_PKG + "("
            + PKG_NAME + " TEXT PRIMARY KEY,"
            + PKG_APPNAME + " TEXT,"
            + PKG_STATS + " INTEGER DEFAULT 1" + ");";

    private static final String SQL_CREATE_TABLE_RECORD = "CREATE TABLE " + TABLE_RECORD + "("
            + RECORD_DATE + " TEXT PRIMARY KEY,"
            + RECORD_STEP + " INTEGER DEFAULT 0 ,"
            + RECORD_USE_PHONE_TIME + " INTEGER DEFAULT 0" + ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PKG);
        db.execSQL(SQL_CREATE_TABLE_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PKG);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_RECORD);
        onCreate(db);
    }
}
