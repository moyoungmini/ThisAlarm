package com.example.alarmapp.ui;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.alarmapp.FaceTrackerActivity;
import com.example.alarmapp.R;
import com.example.alarmapp.SpeechActivity;
import com.example.alarmapp.model.Alarm;
import com.example.alarmapp.service.AlarmReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public final class AlarmLandingPageFragment extends Fragment implements View.OnClickListener {
    private Vibrator vibrator;
    private MediaPlayer mMediaPlayer;
    private AudioManager audioManager;
    private int mIntId;

    private boolean isPresentDay;
    private String strPresentDay;
    private ArrayList<Boolean> dayList;

    private NotificationManager nm;

    private Intent mIntent;
    private PendingIntent pIntent;
    private @SuppressLint("ServiceCast") AlarmManager am;

    private int mission;
    private boolean sound;

    @SuppressLint("ServiceCast")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AlarmLandingPageActivity.AlarmLandingPageActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final View v = inflater.inflate(R.layout.fragment_alarm_landing_page, container, false);
        final Button launchMainActivityBtn = (Button) v.findViewById(R.id.load_main_activity_btn);
        final ImageView dismiss =  v.findViewById(R.id.dismiss_btn);

        long[] pattern = {100, 1000, 100, 500, 100, 500, 100, 1000};
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);
        //vibrator.vibrate(5* 1000);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null) {
            if (extras.containsKey("mission")) {
                mission = extras.getInt("mission");
            }

            if (extras.containsKey("sound")) {
                sound = extras.getBoolean("sound");
            }
        }

        if(sound) {
            try {
                startRingtone();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        launchMainActivityBtn.setOnClickListener(this);
        dismiss.setOnClickListener(this);

        //mIntId = getActivity().getIntent().getIntExtra("id",-1);
        mIntId = 0;

        dayList = new ArrayList<>();
        for(int i=0;i<7;i++){
            dayList.add(false);
        }

        //am = (AlarmManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        //mIntent = new Intent(getContext(), AlarmLandingPageActivity.class);
        //pIntent = PendingIntent.getActivity(getContext(), 1,mIntent, 0);
        //am.cancel(pIntent);

        return v;
    }

    @SuppressLint("ServiceCast")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load_main_activity_btn:
                if(mission == 0){

                }else if(mission ==1){
                }
                else if(mission ==2){
                    startActivity(new Intent(getContext(), SpeechActivity.class));
                }
                else if(mission ==3){
                    startActivity(new Intent(getContext(), FaceTrackerActivity.class));
                }
                // 5분 후 RECALL
                AlarmLandingPageActivity.isAlarmFinish = false;
                nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(0);
                mMediaPlayer.stop();
                getActivity().finish();
                vibrator.cancel();

                break;
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

                //am = (AlarmManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
                //mIntent = new Intent(getContext(), AlarmLandingPageActivity.class);
                //pIntent = PendingIntent.getActivity(getContext(), 0,mIntent, 0);
                //am.cancel(pIntent);
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
