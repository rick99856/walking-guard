package com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by user2 on 2016/6/1.
 */
public class RecordDAO {

    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mStepColumns = {DBHelper.RECORD_STEP};
    private String[] mUsingColumns = {DBHelper.RECORD_USE_PHONE_TIME};

    public RecordDAO(Context context) {
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

    public void updateStep(String date, int step){
        ContentValues values = new ContentValues();
        values.put(DBHelper.RECORD_DATE, date);
        values.put(DBHelper.RECORD_STEP, step);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_RECORD,
                mStepColumns,
                DBHelper.RECORD_DATE+"='"+date+"'",
                null, null, null, null);
        int size = cursor.getCount();
        if (size>0){
            mDatabase.update(DBHelper.TABLE_RECORD, values, null, null);
        }
        else {
            mDatabase.insert(DBHelper.TABLE_RECORD, null, values);
        }

    }

    public int getTotalStep(){
        int step = 0;
        int total = 0;
       Cursor cursor = mDatabase.query(DBHelper.TABLE_RECORD,
               mStepColumns,
               null, null, null, null, null);
        int size = cursor.getCount();
        cursor.moveToFirst();
        if (size==1){
            step = cursor.getInt(cursor.getColumnIndex(DBHelper.RECORD_STEP));
            total = step;
        }
        else if (size>1){
            for (int i=0; i<size; i++){
                step = cursor.getInt(cursor.getColumnIndex(DBHelper.RECORD_STEP));
                total +=step;
                cursor.moveToNext();
            }
        }
        return total;
    }

    public int getTodayStep(String date){
        int step=0;
        Cursor cursor = mDatabase.query(DBHelper.TABLE_RECORD,
                mStepColumns,
                DBHelper.RECORD_DATE + "='" + date + "'",
                null, null, null, null);
        int size = cursor.getCount();
        cursor.moveToFirst();
        if (size>0){
            step = cursor.getInt(cursor.getColumnIndex(DBHelper.RECORD_STEP));
        }
        else{
            step=0;
        }


        return step;
    }

    public void updateUse(String date, int time){
        ContentValues values = new ContentValues();
        values.put(DBHelper.RECORD_DATE, date);
        values.put(DBHelper.RECORD_USE_PHONE_TIME, time);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_RECORD,
                mStepColumns,
                DBHelper.RECORD_DATE+"='"+date+"'",
                null, null, null, null);
        int size = cursor.getCount();
        if (size>0){
            mDatabase.update(DBHelper.TABLE_RECORD, values, null, null);
        }
        else {
            mDatabase.insert(DBHelper.TABLE_RECORD, null, values);
        }
    }

    public int getUseingTime(String date){
        int useingTime =0;
        Cursor cursor = mDatabase.query(DBHelper.TABLE_RECORD,
                mUsingColumns,
                DBHelper.RECORD_DATE + "='" + date + "'",
                null, null, null, null);
        int size = cursor.getCount();
        cursor.moveToFirst();
        if (size>0){
            useingTime = cursor.getInt(cursor.getColumnIndex(DBHelper.RECORD_USE_PHONE_TIME));
        }
        else{
            useingTime=0;
        }


        return useingTime;
    }


}
