package com.example.alarmapp.ui;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
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

import com.example.alarmapp.FaceTrackerActivity;
import com.example.alarmapp.R;
import com.example.alarmapp.SpeechActivity;
import com.example.alarmapp.model.Alarm;
import com.example.alarmapp.service.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public final class AlarmLandingPageFragment extends Fragment implements View.OnClickListener {
    private Vibrator vibrator;
    private MediaPlayer mMediaPlayer;
    private int mIntId;

    private boolean isPresentDay;
    private String strPresentDay;
    private ArrayList<Boolean> dayList;

    private NotificationManager nm;

    private Intent mIntent;
    private PendingIntent pIntent;
    private @SuppressLint("ServiceCast") AlarmManager am;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AlarmLandingPageActivity.AlarmLandingPageActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final View v = inflater.inflate(R.layout.fragment_alarm_landing_page, container, false);
        final Button launchMainActivityBtn = (Button) v.findViewById(R.id.load_main_activity_btn);
        final Button dismiss = (Button) v.findViewById(R.id.dismiss_btn);

        long[] pattern = {100, 1000, 100, 500, 100, 500, 100, 1000};
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);
        //vibrator.vibrate(5* 1000);
        startRingtone();

        launchMainActivityBtn.setOnClickListener(this);
        dismiss.setOnClickListener(this);

        //mIntId = getActivity().getIntent().getIntExtra("id",-1);
        mIntId = 0;

        dayList = new ArrayList<>();
        for(int i=0;i<7;i++){
            dayList.add(false);
        }

        return v;
    }

    @SuppressLint("ServiceCast")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load_main_activity_btn:
                // 5분 후 RECALL
                AlarmLandingPageActivity.isAlarmFinish = false;
                nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(0);
                mMediaPlayer.stop();
                getActivity().finish();
                vibrator.cancel();

//                        am = (AlarmManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
//                        mIntent = new Intent(getContext(), AlarmLandingPageActivity.class);
//                        pIntent = PendingIntent.getActivity(getContext(), 1,mIntent, 0);
//                        am.cancel(pIntent);

                long now = System.currentTimeMillis();
                Log.i("CurrentTime", String.valueOf(now));

                Alarm alarm = new Alarm();
                alarm.setTime(now);
                alarm.setDay(Alarm.MON, dayList.get(0));
                alarm.setDay(Alarm.TUES, dayList.get(1));
                alarm.setDay(Alarm.WED, dayList.get(2));
                alarm.setDay(Alarm.THURS, dayList.get(3));
                alarm.setDay(Alarm.FRI, dayList.get(4));
                alarm.setDay(Alarm.SAT, dayList.get(5));
                alarm.setDay(Alarm.SUN, dayList.get(6));
                AlarmReceiver.resetReminderAlarm(getContext(), alarm);
                Log.i("dsfsdf","sdfsd");

                startActivity(new Intent(getContext(), SpeechActivity.class));
                break;
            case R.id.dismiss_btn:
                AlarmLandingPageActivity.isAlarmFinish = false;
                nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(0);
                mMediaPlayer.stop();
                getActivity().finish();
                vibrator.cancel();

                am = (AlarmManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
                mIntent = new Intent(getContext(), AlarmLandingPageActivity.class);
                pIntent = PendingIntent.getActivity(getContext(), 1,mIntent, 0);
                am.cancel(pIntent);
                break;
        }
    }

    private void startRingtone() {
        //play ringtone
        Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mMediaPlayer = MediaPlayer.create(getContext(), ringtone);
        mMediaPlayer.setLooping(true);
        //mMediaPlayer.createVolumeShaper();
        mMediaPlayer.start();
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
