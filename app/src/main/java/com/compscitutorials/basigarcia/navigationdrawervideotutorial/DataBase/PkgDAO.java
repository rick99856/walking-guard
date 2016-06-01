package com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.Map;

/**
 * Created by user2 on 2016/5/15.
 */
public class PkgDAO {
    public static final String TAG = "PkgDAO";

    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = {DBHelper.PKG_APPNAME, DBHelper.PKG_NAME};
    private String[] mStatsColumns = {DBHelper.PKG_APPNAME};
    private String[] mPkgColumns = {DBHelper.PKG_NAME};

    public PkgDAO(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(context);
        // open the database
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public void addAll(String pkgName, String appName){
        ContentValues values = new ContentValues();
        values.put(DBHelper.PKG_NAME, pkgName);
        values.put(DBHelper.PKG_APPNAME, appName);
//        Log.e("DatabaseTAG", "Insert " + pkgName + " & " + appName);
        Cursor cursor;
        cursor = mDatabase.query(DBHelper.TABLE_PKG,
                new String[]{DBHelper.PKG_NAME},
                DBHelper.PKG_NAME + "='" + pkgName + "'",
                null, null, null, null);
        int size = cursor.getCount();
//        Log.e("DatabaseTAG", "Size: " + size);
        if (size<=0){
            mDatabase.insert(DBHelper.TABLE_PKG, null, values);
            Log.e("DatabaseTAG", "Insert ");
        } else {
//            Log.e("DatabaseTAG", "Ignore ");
        }

    }

    public void add(String pkgName){
        ContentValues values = new ContentValues();
        values.put(DBHelper.PKG_STATS, 1);
        mDatabase.update(DBHelper.TABLE_PKG, values, DBHelper.PKG_NAME + "='" + pkgName + "'", null);
    }

    public void delete(String pkgName){
        ContentValues values = new ContentValues();
        values.put(DBHelper.PKG_STATS, 0);
        mDatabase.update(DBHelper.TABLE_PKG, values, DBHelper.PKG_NAME + "='" + pkgName + "'", null);
    }

    public Cursor get(){
        Cursor cursor = mDatabase.query(DBHelper.TABLE_PKG,
                mPkgColumns,
                null, null, null, null, null);
        return cursor;
    }

    public String[] getBanPkgName(){
        String [] pkg;
        Cursor cursor =
                mDatabase.query(DBHelper.TABLE_PKG,
                        mPkgColumns,
                        DBHelper.PKG_STATS+"=1",
                        null, null, null, null);
        int size = cursor.getCount();
//        Log.e("DataBaseTAG", "Size: " + size);
        if (size>0){
            pkg = new String[size];
            cursor.moveToFirst();
            for (int i=0; i <size; i++){
                pkg[i] = cursor.getString(cursor.getColumnIndex(DBHelper.PKG_NAME));
                cursor.moveToNext();
            }
        }else {
            pkg = new String[1];
        }
        return pkg;

    }

    public boolean getStats(String pkgName){
//        Log.e("DatabaseTAG", pkgName + " Stats");
        Cursor cursor = mDatabase.query(DBHelper.TABLE_PKG,
                new String[]{DBHelper.PKG_STATS},
                DBHelper.PKG_NAME + "='" + pkgName + "'",
                null, null, null, null);
        Boolean stats;
        int size = cursor.getCount();
        if (cursor.getCount()<1){
            Log.e("DatabaseTAG", "No" + size);
            stats = true;
        } else{
//            Log.e("DatabaseTAG", "Yes");
            cursor.moveToFirst();
            String s = cursor.getString(cursor.getColumnIndex(DBHelper.PKG_STATS));

            int i = Integer.valueOf(s);
            if (i==0){
                stats = false;
            } else {
                stats = true;
            }

        }

        return stats;
    }

}
