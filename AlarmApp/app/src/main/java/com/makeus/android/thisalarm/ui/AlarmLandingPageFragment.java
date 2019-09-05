package com.makeus.android.thisalarm.ui;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.makeus.android.thisalarm.R;
import com.makeus.android.thisalarm.data.DatabaseHelper;
import com.makeus.android.thisalarm.model.Alarm;
import com.makeus.android.thisalarm.service.AlarmReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public final class AlarmLandingPageFragment extends Fragment implements View.OnClickListener {
    private Vibrator vibrator;
    private MediaPlayer mMediaPlayer;
    private AudioManager audioManager;
    private int mIntId;
    private  Button mBtnRecall;
    private RelativeLayout mLayout;


    private NotificationManager nm;

    private Intent mIntent;
    private PendingIntent pIntent;
    private @SuppressLint("ServiceCast") AlarmManager am;

    private ArrayList<Boolean> dayList;
    private int mission;
    private boolean sound;
    private  long time;
    private String label;
    private boolean enable;

    @SuppressLint("ServiceCast")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_alarm_landing_page, container, false);
        final ImageView dismiss =  v.findViewById(R.id.dismiss_btn);
        mBtnRecall = v.findViewById(R.id.recall_btn);
        mLayout =v.findViewById(R.id.landing_page_fragment_layout);

        long[] pattern = {100, 1000, 100, 500, 100, 500, 100, 1000};
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);

        dayList = new ArrayList<>();
        for(int i=0;i<7;i++){
            dayList.add(false);
        }



        Bundle extras = getActivity().getIntent().getExtras();
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
        //read HW handling

        if(sound) {
            try {
                startRingtone();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dismiss.setOnClickListener(this);
        mBtnRecall.setOnClickListener(this);

        mIntId = 0;

        SharedPreferences pref = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        SettingActivity.sIntAppBackground = pref.getInt("primaryColor",0);

        if(SettingActivity.sIntAppBackground ==0){

        }
        else if(SettingActivity.sIntAppBackground ==1){

        }
        else if(SettingActivity.sIntAppBackground ==2){

        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setAlarm() {
        final Alarm alarm = new Alarm();
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        final int minutes = c.get(Calendar.MINUTE);
        final int hours = c.get(Calendar.HOUR_OF_DAY);

        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.SECOND, 0);

        alarm.setTime(c.getTimeInMillis());
        alarm.setLabel(label);
        alarm.setDay(Alarm.MON, dayList.get(0));
        alarm.setDay(Alarm.TUES, dayList.get(1));
        alarm.setDay(Alarm.WED, dayList.get(2));
        alarm.setDay(Alarm.THURS, dayList.get(3));
        alarm.setDay(Alarm.FRI, dayList.get(4));
        alarm.setDay(Alarm.SAT, dayList.get(5));
        alarm.setDay(Alarm.SUN, dayList.get(6));
        alarm.setMission(mission);
        alarm.setSound(sound);
        alarm.setIsEnabled(enable);

        DatabaseHelper.getInstance(getContext()).updateAlarm(alarm);
        AlarmReceiver.setReminderAlarm(getContext(), alarm);
    }
    //Reset Alarm

    public void reCallAlarm() {
        final Alarm alarm = new Alarm();
        final Calendar c = Calendar.getInstance();
        time = System.currentTimeMillis();
        c.setTimeInMillis(time);

        int minutes = c.get(Calendar.MINUTE);
        final int hours = c.get(Calendar.HOUR_OF_DAY);
        final int second = c.get(Calendar.SECOND);

        if(minutes<55) {
            minutes +=5;
        }
        else {
            minutes = minutes - 55;
        }
        //Exception handling minutues

        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.SECOND, second+10);

        alarm.setTime(c.getTimeInMillis());
        alarm.setLabel(label);
        alarm.setDay(Alarm.MON, dayList.get(0));
        alarm.setDay(Alarm.TUES, dayList.get(1));
        alarm.setDay(Alarm.WED, dayList.get(2));
        alarm.setDay(Alarm.THURS, dayList.get(3));
        alarm.setDay(Alarm.FRI, dayList.get(4));
        alarm.setDay(Alarm.SAT, dayList.get(5));
        alarm.setDay(Alarm.SUN, dayList.get(6));
        alarm.setMission(mission);
        alarm.setSound(sound);
        alarm.setIsEnabled(enable);

        DatabaseHelper.getInstance(getContext()).updateAlarm(alarm);
        AlarmReceiver.setReminderAlarm(getContext(), alarm);
    }
    //Recall alarm set in 5minutes

    private Alarm getAlarm() {
        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA);
    }

    @SuppressLint("ServiceCast")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dismiss_btn:
                AlarmLandingPageActivity.isAlarmFinish = false;
                nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(0);
                if(sound) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                }
                vibrator.cancel();
                setAlarm();
                getActivity().finish();
                // Set function excute one not repeat so necessary that alarm reset

                if(mission == 0){}//None Mission
                else if(mission ==1){}
                else if(mission ==2){
                    mIntent = new Intent(AlarmLandingPageActivity.AlarmLandingPageActivity, SpeechActivity.class);
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
                    startActivity(mIntent);
                }
                // Sepeech
                else if(mission ==3){
                    mIntent = new Intent(AlarmLandingPageActivity.AlarmLandingPageActivity, FaceTrackerActivity.class);
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
                    startActivity(mIntent);
                }
                //FaceTracker
                break;
            case R.id.recall_btn:
                AlarmLandingPageActivity.isAlarmFinish = false;
                nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(0);
                // Remove Notification id zero(all notification id is zero)
                if(sound) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                }
                getActivity().finish();
                vibrator.cancel();

                reCallAlarm();
                break;
                // Set Recall Alarm
        }
    }

    private void startRingtone() throws IOException {
        Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        mMediaPlayer.setDataSource(getActivity(), ringtone);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }
    // Set Audio Player
}
