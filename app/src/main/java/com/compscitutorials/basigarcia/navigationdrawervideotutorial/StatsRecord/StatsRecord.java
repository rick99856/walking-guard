package com.compscitutorials.basigarcia.navigationdrawervideotutorial.StatsRecord;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase.RecordDAO;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.R;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatsRecord extends Fragment {

    TextView total, today, distance, time;
    RecordDAO recordDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statsrecord, container, false);
        total = (TextView) rootView.findViewById(R.id.stats_total_step);
        today = (TextView) rootView.findViewById(R.id.stats_today_step);
        distance = (TextView) rootView.findViewById(R.id.stats_distance);
        time = (TextView) rootView.findViewById(R.id.stats_use_phone_time);
        recordDAO = new RecordDAO(getActivity());

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
            int totalStep = recordDAO.getTotalStep();
            total.setText(""+totalStep);

            int todayStep = recordDAO.getTodayStep(getDateTime());
            today.setText(""+todayStep);

            double distanceStep = (todayStep*0.7)/1000;
            DecimalFormat df = new DecimalFormat("#.##");
            String s = df.format(distanceStep);
            distance.setText(s);

            int useingTime =  recordDAO.getUseingTime(getDateTime());
            double dUseingTime = (useingTime/3600);
            String ss = df.format(dUseingTime);
            time.setText(ss);
            super.handleMessage(msg);
        }
    };


}
