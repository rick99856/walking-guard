package com.compscitutorials.basigarcia.navigationdrawervideotutorial.Step;

import android.database.Cursor;

import com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase.DBHelper;

import java.util.ArrayList;

/**
 * Created by user2 on 2016/5/27.
 */
public class TempPkg {
    static String [] s;
    public TempPkg(){}

    public void set(Cursor cursorPkgName){
        int size = cursorPkgName.getCount();
        s = new String[size];
        int i=0;
        cursorPkgName.moveToFirst();
        while (!cursorPkgName.isAfterLast()){
            String name = cursorPkgName.getString(cursorPkgName.getColumnIndex(DBHelper.PKG_NAME));
            s[i] = name;
            i++;
            cursorPkgName.moveToNext();
        }
    }

    public String[] get(){
        return s;
    }
}
