package com.example.alarmapp.ui;

import android.app.NotificationManager;
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
import android.widget.Toast;

import com.example.alarmapp.FaceTrackerActivity;
import com.example.alarmapp.R;
import com.example.alarmapp.SpeechActivity;

public final class AlarmLandingPageFragment extends Fragment implements View.OnClickListener {
    private Vibrator vibrator;
    private MediaPlayer mMediaPlayer;
    private int mIntId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AlarmLandingPageActivity.AlarmLandingPageActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final View v = inflater.inflate(R.layout.fragment_alarm_landing_page, container, false);
        final Button launchMainActivityBtn = (Button) v.findViewById(R.id.load_main_activity_btn);
        final Button dismiss = (Button) v.findViewById(R.id.dismiss_btn);

        long[] pattern = {100,1000,100,500,100,500,100,1000};
        vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);

        startRingtone();

        launchMainActivityBtn.setOnClickListener(this);
        dismiss.setOnClickListener(this);

        //mIntId = getActivity().getIntent().getIntExtra("id",-1);
        mIntId = 0;

        return v;
    }

    @Override
    public void onClick(View view) {
        NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        switch (view.getId()) {
            case R.id.load_main_activity_btn:
                startActivity(new Intent(getContext(), FaceTrackerActivity.class));
                nm.cancel(0);
                vibrator.cancel();
                mMediaPlayer.stop();
                getActivity().finish();
                break;
            case R.id.dismiss_btn:
                nm.cancel(0);
                mMediaPlayer.stop();
                getActivity().finish();
                vibrator.cancel();
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
}
