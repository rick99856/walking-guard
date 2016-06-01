package com.compscitutorials.basigarcia.navigationdrawervideotutorial;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase.DBHelper;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase.PkgDAO;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.Step.NickyService;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.Step.TempPkg;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class About extends Fragment {

    Switch mSwitch;
    SeekBar mSeekBar;

    private SharedPreferences sharedPreferences;
    private int onoff;
    private int value;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        mSwitch = (Switch) rootView.findViewById(R.id.switch_about);
        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar_about);

        sharedPreferences = getActivity().getSharedPreferences("notify", Context.MODE_PRIVATE);
        onoff = sharedPreferences.getInt("notifySwitch", 0);
        if (onoff==1){
            mSwitch.setChecked(true);
        }
        else if (onoff==0) {
            mSwitch.setChecked(false);
        } else {
            mSwitch.setChecked(true);
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                if (isChecked == true) {
                    edit.putInt("notifySwitch", 1);
                    edit.commit();
                    isChecked = false;

                } else {
                    edit.putInt("notifySwitch", 0);
                    edit.commit();
                    isChecked = true;
                }


            }
        });

        sharedPreferences = getActivity().getSharedPreferences("StepValue", Context.MODE_PRIVATE);
        value = sharedPreferences.getInt("StepValue", 0);
        switch (value){
            case 0:
                mSeekBar.setProgress(0);
                break;
            case 1:
                mSeekBar.setProgress(1);
                break;
            case 2:
                mSeekBar.setProgress(2);
                break;
            case 3:
                mSeekBar.setProgress(3);
                break;
        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edit.putInt("StepValue", progress);
                edit.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        return rootView;
    }


}
