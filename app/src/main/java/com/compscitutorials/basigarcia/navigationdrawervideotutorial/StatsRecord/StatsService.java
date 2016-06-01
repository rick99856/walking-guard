package com.compscitutorials.basigarcia.navigationdrawervideotutorial.StatsRecord;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase.RecordDAO;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by user2 on 2016/6/1.
 */
public class StatsService extends Service implements SensorEventListener {

    Context context = this;
    private Handler handler = new Handler();


    RecordDAO recordDAO;
    SensorManager sensorManager;
    int mStep=0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("StatsServiceTAG", "Hello");
        startCountStep();
        handler.postDelayed(showTime, 1000);
        recordDAO = new RecordDAO(this);
    }
        int i = 0;
        private Runnable showTime = new Runnable() {
        public void run() {
            //log目前時間
//            Log.i("time:", new Date().toString());
            i++;
            if (i==10){
                i=0;
                String date = getDateTime();
                recordDAO.updateUse(date, 15);
            }
            handler.postDelayed(this, 1000);
        }
    };


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
    int mYear, mMonth, mDay;
    String mDate;
    public String getDateTime(){
        String DateTime;
        final Calendar c = Calendar.getInstance();
        mYear = c.get((Calendar.YEAR));
        mMonth = c.get((Calendar.MONTH));
        mDay = c.get((Calendar.DAY_OF_MONTH));

        String date = mYear+"-"+mMonth+"-"+mDay;
        return date;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        mStep += event.values.length;
        mDate = getDateTime();

        recordDAO.updateStep(mDate, mStep);
        Log.e("StatsServiceTAG", mStep + "&" + mDate);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
