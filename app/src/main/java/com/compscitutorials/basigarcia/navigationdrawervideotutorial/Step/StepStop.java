package com.compscitutorials.basigarcia.navigationdrawervideotutorial.Step;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase.RecordDAO;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepStop extends Fragment{

    private Switch mSwitch;
    private SharedPreferences sharedPreferences;
    private String onoff;

    private SensorManager sensorManager;
    private TextView count;

    private ImageView img_ban;

    RecordDAO recordDAO;

    NotificationCompat.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stepstop, container, false);

        count = (TextView)rootView.findViewById(R.id.txv_stepcount);
        img_ban = (ImageView) rootView.findViewById(R.id.img_ban);
//        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        builder = new NotificationCompat.Builder(getActivity());
        recordDAO = new RecordDAO(getActivity());

        sharedPreferences = getActivity().getSharedPreferences("SWITCH", Context.MODE_PRIVATE);
        onoff = sharedPreferences.getString("OnOff", "");

        mSwitch = (Switch) rootView.findViewById(R.id.switchButton);
        if (onoff.equals("open")){
            mSwitch.setChecked(true);
            img_ban.setVisibility(View.VISIBLE);
        }
        else if (onoff.equals("close")) {
            mSwitch.setChecked(false);
            img_ban.setVisibility(View.INVISIBLE);
        }
        else {
            mSwitch.setChecked(true);
            img_ban.setVisibility(View.VISIBLE);
        }

        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };

        timer.schedule(task, 1000, 1000);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                if (isChecked == true) {
                    edit.putString("OnOff", "open");
                    Log.e("StepStopTAG", "Start Service");
                    Intent intent = new Intent(getActivity(), NickyService.class);
                    getActivity().startService(intent);
                    isChecked = false;
                    img_ban.setVisibility(View.VISIBLE);
                } else {
                    edit.putString("OnOff", "close");
                    Log.e("StepStopTAG", "Stop Service");
                    Intent intent = new Intent(getActivity(), NickyService.class);
                    getActivity().stopService(intent);
                    isChecked = true;
                    img_ban.setVisibility(View.INVISIBLE);
                }
                edit.commit();

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    int mYear, mMonth, mDay;
    String mDate;
    public String getDateTime(){
        String DateTime;
        final Calendar c = Calendar.getInstance();
        mYear = c.get((Calendar.YEAR));
        mMonth = c.get((Calendar.MONTH));
        mDay = c.get((Calendar.DAY_OF_MONTH));

        DateTime = mYear+"-"+mMonth+"-"+mDay;
        return DateTime;
    }

    private final Timer timer = new Timer();
    private TimerTask task;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            String date = getDateTime();
            int todayStep = recordDAO.getTodayStep(date);
            count.setText(todayStep+"");
            super.handleMessage(msg);
        }
    };


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onStop() {
        super.onStop();
    }



}
