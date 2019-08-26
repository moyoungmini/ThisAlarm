package com.makeus.android.thisalarm.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.makeus.android.thisalarm.R;

import java.util.ArrayList;

public final class AlarmLandingPageActivity extends AppCompatActivity {
    public static Activity AlarmLandingPageActivity;
    public static boolean isAlarmFinish = true;

    private ArrayList<Boolean> dayList;
    private int mission;
    private boolean sound;
    private  long time;
    private String label;
    Vibrator vibrator;
    long[] pattern;
    private boolean enable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_landing_page);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }
        // Float this activity in Lock Screen code

        dayList = new ArrayList<>();
        for(int i=0;i<7;i++){
            dayList.add(false);
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (extras.containsKey("mission")) {
                mission = extras.getInt("mission");
            }

            if (extras.containsKey("sound")) {
                sound = extras.getBoolean("sound");
            }

            if (extras.containsKey("enable")) {
                enable = extras.getBoolean("enable");
            }

            if (extras.containsKey("label")) {
                label = extras.getString("label");
            }

            if (extras.containsKey("time")) {
                time = extras.getLong("time");
            }

            if (extras.containsKey("mon")) {
                dayList.set(0,extras.getBoolean("mon"));
            }

            if (extras.containsKey("tue")) {
                dayList.set(1,extras.getBoolean("tue"));
            }

            if (extras.containsKey("wen")) {
                dayList.set(2,extras.getBoolean("wen"));
            }

            if (extras.containsKey("thu")) {
                dayList.set(3,extras.getBoolean("thu"));
            }

            if (extras.containsKey("fri")) {
                dayList.set(4,extras.getBoolean("fri"));
            }

            if (extras.containsKey("sat")) {
                dayList.set(5,extras.getBoolean("sat"));
            }

            if (extras.containsKey("sun")) {
                dayList.set(6,extras.getBoolean("sun"));
            }
        }
        // Set received data from pending intent/

        AlarmLandingPageActivity = this;

        pattern = new long[]{100, 1000, 100, 500, 100, 500, 100, 1000};
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    vibrator.vibrate(pattern, 0);
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
    }

    public static Intent launchIntent(Context context) {
        final Intent i = new Intent(context, AlarmLandingPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    //*************** HW Button set *********************//
    @Override
    public void onBackPressed() {
    }

    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isAlarmFinish) {
            Intent mIntent = new Intent(this, AlarmLandingPageActivity.class);
            mIntent.putExtra("mission", mission);
            mIntent.putExtra("sound",sound);
            mIntent.putExtra("enable",enable);
            mIntent.putExtra("label",label);
            mIntent.putExtra("time",time);
            mIntent.putExtra("mon",dayList.get(0));
            mIntent.putExtra("tue",dayList.get(1));
            mIntent.putExtra("wen",dayList.get(2));
            mIntent.putExtra("thu",dayList.get(3));
            mIntent.putExtra("fri",dayList.get(4));
            mIntent.putExtra("sat",dayList.get(5));
            mIntent.putExtra("sun",dayList.get(6));
            //Save intent value
            mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
        }
    }
    // Start startActivity when click button except dismiss in AlarmLandingPageFragment
    //*************** HW Button set *********************//
}