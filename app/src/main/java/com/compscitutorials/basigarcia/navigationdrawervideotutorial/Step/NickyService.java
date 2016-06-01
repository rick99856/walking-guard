package com.compscitutorials.basigarcia.navigationdrawervideotutorial.Step;


import android.app.AlertDialog;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase.PkgDAO;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.R;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

//繼承android.app.Service
public class NickyService extends Service implements SensorEventListener {

    private Handler handler = new Handler();
    Context context = this;

    PkgDAO pkgDAO;


    String getTopName="";

    String tempTopName="";
    String tempTopName2 = "";
    String tempTopName3="";
    int mStep=0;

    private SensorManager sensorManager;
    private SharedPreferences sharedPreferences;
    int value, count=5;

//    final AlertDialog alertDialog = getAlertDialog("這是一個對話框","請選擇......");
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.postDelayed(com,2000);
//        handler.postDelayed(showTime, 2000);
        pkgDAO = new PkgDAO(getApplicationContext());
        sharedPreferences = this.getSharedPreferences("StepValue", Context.MODE_PRIVATE);
        value = sharedPreferences.getInt("StepValue", 0);
        switch (value){
            case 0:
                count=5;
                break;
            case 1:
                count=10;
                break;
            case 2:
                count=15;
                break;
            case 3:
                count=20;
                break;
        }
        startCountStep();
        super.onStart(intent, startId);

    }



    @Override
    public void onDestroy() {
//        handler.removeCallbacks(showTime);
        handler.removeCallbacks(com);
        handler.removeCallbacks(com2);
        super.onDestroy();
    }

    //Time
    private Runnable showTime = new Runnable() {
        public void run() {
            //log目前時間
            Log.i("time:", new Date().toString());

            handler.postDelayed(this, 2000);
        }
    };

    //Runnable 1
    private  Runnable com = new Runnable() {
        @Override
        public void run() {
            getTopName = getTopAppName();
            tempTopName3 = getTopName;
            Boolean flag;
            String[] banPkg = pkgDAO.getBanPkgName();
            int size = banPkg.length;
            if (getTopAppName().equals("")){
            }
            Log.e("Service_comTAG", "Now: " + getTopName);
            Log.e("Service_comTAG", "Size: " + size);

            flag = Search(getTopName, size, banPkg);
            if (flag){
                Log.e("Service_comTAG", "Lock");
                tempTopName = getTopName;
                tempTopName2 = getTopName;
                handler.postDelayed(com2, 2000);
                mStep=0;
            } else{
                Log.e("Service_comTAG", "no Lock");
                handler.postDelayed(com, 2000);
            }

        }
    };
    //void Search
    boolean Search(String topName, int length, String[] banPkg){
        Log.e("Search", "hi");
        Boolean flag = false;
        for (int i=0; i<length; i++){
            if (banPkg[i].equals(topName)){
                flag=true;
                break;
            }
            else {
                flag=false;
            }
        }
        return flag;
    }

    //Runnable 2
    private  Runnable com2 = new Runnable() {
        @Override
        public void run() {
            getTopName = getTopAppName();
            tempTopName3 = getTopName;
            Log.e("Service_com2TAG", "Now: " + getTopName + "Temp: " + tempTopName2);

            if (!getTopName.equals(tempTopName2)){
                mStep=0;
                Log.e("Service_com2TAG", "Bye");
                handler.removeCallbacks(com2);
                handler.postDelayed(com, 2000);
            }
            else {
                Log.e("Service_com2TAG", "Keep");
                handler.postDelayed(com2, 2000);
            }
        }
    };

    //void get Top APP
    public String  getTopAppName(){
        String topPackageName="";
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP){
            UsageStatsManager mUsagesStatsManager = (UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats  = mUsagesStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, (time-1000*10), time);
            if (stats != null){
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for(UsageStats usageStats: stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if(!mySortedMap.isEmpty()){
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            } else{
                Log.e("Service_getTopAppTAG", "Can't get PackageName");
            }
        }

        if (topPackageName.equals("")){
            topPackageName = tempTopName3;
        }
        return topPackageName;
    }


    //Sensor
    @Override
    public void onSensorChanged(SensorEvent event) {
        mStep += event.values.length;
        Log.e("Service_SensorTAG", mStep+"");
        if ((mStep>=count)){
            String n = getTopAppName();
            Log.e("Service_SensorTAG", "Now: " + n);
            Log.e("Service_SensorTAG", "temp: " + tempTopName);
            if (n.equals(tempTopName)){
                handler.removeCallbacks(com2);
                Log.e("Service_SensorTAG", "Bye Bye Screen");
                showDialog();
            }
            else{
                mStep=0;
                handler.postDelayed(com, 2000);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    //void Open Sensor
    public void startCountStep(){
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null){
            Log.e("Service_SensorTAG", "Open Step Count");
            sensorManager.registerListener(this, countSensor, sensorManager.SENSOR_DELAY_UI);
        }
        else {
            Log.e("Service_SensorTAG", "Failed open Step Count");
        }
    }

    //void Lock Screen
    void showDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setCancelable(false);
        final AlertDialog alert;
        alert = builder.create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                //停兩秒
                if (keyCode == KeyEvent.KEYCODE_HOME) {
                    alert.dismiss();
                }

                return true;
            }
        });
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.postDelayed(com, 2000);
            }
        });
        if (alert.isShowing()){
            alert.dismiss();
        }
        alert.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(5000);
                    alert.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


    }


}




