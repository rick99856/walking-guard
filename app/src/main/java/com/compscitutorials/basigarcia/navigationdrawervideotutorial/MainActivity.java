package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.compscitutorials.basigarcia.navigationdrawervideotutorial.AppList.AppList;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase.PkgDAO;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase.RecordDAO;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.StatsRecord.StatsRecord;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.StatsRecord.StatsService;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.Step.StepStop;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.Step.TempPkg;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    PkgDAO pkgDAO;
    RecordDAO recordDAO;
    Cursor cursor;
    TempPkg temp;

    ProgressDialog pDialog;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordDAO = new RecordDAO(MainActivity.this);
        recordDAO.updateStep(getDateTime(), 0);
        Boolean FirstFlag = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isfirstrun", true);
        if (FirstFlag){
            Log.e("MainTAG", "First run");
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit().putBoolean("isfirstrun", false).commit();
            Intent intent = new Intent(MainActivity.this, StatsService.class);
            startService(intent);
        }
//        else{
//            pkgDAO = new PkgDAO(MainActivity.this);
//            cursor = pkgDAO.get();
//            temp = new TempPkg();
//            temp.set(cursor);
//            Log.e("AboutTAG", "Temp Size: " + temp.get().length);
//        }


        //Set the fragment initially
        StepStop fragment = new StepStop();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //How to change elements in the header programatically
        View headerView = navigationView.getHeaderView(0);
//        TextView emailText = (TextView) headerView.findViewById(R.id.email);
//        emailText.setText("newemail@email.com");

        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//         if (id == R.id.menu_20step) {
//            //Set the fragment initially
//            StepStop fragment = new StepStop();
//            android.support.v4.app.FragmentTransaction fragmentTransaction =
//                    getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, fragment);
//            fragmentTransaction.commit();
//
//        } else
         if (id == R.id.menu_appList) {
            AppList fragment = new AppList();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
             fragmentTransaction.addToBackStack("AppList");
            fragmentTransaction.commit();

        }
        else if (id == R.id.menu_function1) {
            StatsRecord fragment = new StatsRecord();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
             fragmentTransaction.addToBackStack("Funcion1");
            fragmentTransaction.commit();
        }
        else if (id == R.id.menu_about){
             About fragment = new About();
             android.support.v4.app.FragmentTransaction fragmentTransaction =
                     getSupportFragmentManager().beginTransaction();
             fragmentTransaction.replace(R.id.fragment_container, fragment);
             fragmentTransaction.addToBackStack("About");
             fragmentTransaction.commit();
         }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
