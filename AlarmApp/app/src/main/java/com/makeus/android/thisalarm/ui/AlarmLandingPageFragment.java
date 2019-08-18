package com.makeus.android.thisalarm.ui;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import com.makeus.android.thisalarm.R;
import com.makeus.android.thisalarm.data.DatabaseHelper;
import com.makeus.android.thisalarm.model.Alarm;
import com.makeus.android.thisalarm.service.AlarmReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public final class AlarmLandingPageFragment extends Fragment implements View.OnClickListener {
    private Vibrator vibrator;
    private MediaPlayer mMediaPlayer;
    private AudioManager audioManager;
    private int mIntId;
    private  Button mBtnRecall;


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

        long[] pattern = {100, 1000, 100, 500, 100, 500, 100, 1000};
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);
        //vibrator.vibrate(5* 1000);

        dayList = new ArrayList<>();
        for(int i=0;i<7;i++){
            dayList.add(false);
        }

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null) {
            if (extras.containsKey("mission")) {
                Log.i("vdvds",String.valueOf(mission));
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

        return v;
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
        //SECOND설정

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

        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.SECOND, second);
        //SECOND설정

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

    private Alarm getAlarm() {
        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA);
    }

    @SuppressLint("ServiceCast")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.load_main_activity_btn:
//                if(mission == 0){
//
//                }else if(mission ==1){
//                }
//                else if(mission ==2){
//                    startActivity(new Intent(getContext(), SpeechActivity.class));
//                }
//                else if(mission ==3){
//                    startActivity(new Intent(getContext(), FaceTrackerActivity.class));
//                }
//                // 5분 후 RECALL
//                AlarmLandingPageActivity.isAlarmFinish = false;
//                nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                nm.cancel(0);
//                mMediaPlayer.stop();
//                getActivity().finish();
//                vibrator.cancel();

//                break;
            case R.id.dismiss_btn:
                AlarmLandingPageActivity.isAlarmFinish = false;
                nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(0);
                if(sound) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                }
                getActivity().finish();
                vibrator.cancel();

                setAlarm();

                if(mission == 0){

                }else if(mission ==1){
                }
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
                break;
            case R.id.recall_btn:
                AlarmLandingPageActivity.isAlarmFinish = false;
                nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(0);
                if(sound) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                }
                getActivity().finish();
                vibrator.cancel();

                reCallAlarm();
                break;
        }
    }

    private void startRingtone() throws IOException {
        //play ringtone
        Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        mMediaPlayer = MediaPlayer.create(getContext(), ringtone);
//        mMediaPlayer.setLooping(true);
        //mMediaPlayer.createVolumeShaper();

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

    public void MuteAudio(){
        audioManager = (AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
            audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
            audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
        } else {
            audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            audioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            audioManager.setStreamMute(AudioManager.STREAM_RING, true);
            audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
    }

    public void UnMuteAudio(){
        audioManager = (AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
            audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE,0);
            audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
            audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
        } else {
            audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            audioManager.setStreamMute(AudioManager.STREAM_RING, false);
            audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
    }


    public void presentDay() {
        Calendar cal = Calendar.getInstance();

        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(nWeek ==1){
            dayList.set(0,true);
        }
        else if (nWeek == 2){
            dayList.set(1,true);
        }
        else if (nWeek == 3){
            dayList.set(2,true);
        }
        else if (nWeek == 4){
            dayList.set(3,true);
        }
        else if (nWeek == 5){
            dayList.set(4,true);
        }
        else if (nWeek == 6){
            dayList.set(5,true);
        }
        else if (nWeek == 7){
            dayList.set(6,true);
        }
    }
}
