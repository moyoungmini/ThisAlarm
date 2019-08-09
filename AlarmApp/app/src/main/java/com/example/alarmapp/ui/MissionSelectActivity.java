package com.example.alarmapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.alarmapp.R;

public class MissionSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mWeather,mSpeech,mEmotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_select);

        mWeather = findViewById(R.id.text_weather);
        mSpeech = findViewById(R.id.text_speech);
        mEmotion = findViewById(R.id.text_emotion);

        mWeather.setOnClickListener(this);
        mSpeech.setOnClickListener(this);
        mEmotion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_weather:
                if(AddEditAlarmFragment.mission != 1){
                    AddEditAlarmFragment.mission =1;
                    mWeather.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    mSpeech.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    mEmotion.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                }
                else {
                    mWeather.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    AddEditAlarmFragment.mission = 0;
                }
                break;
            case R.id.text_speech:
                if(AddEditAlarmFragment.mission != 2){
                    AddEditAlarmFragment.mission =2;
                    mSpeech.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    mWeather.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    mEmotion.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                }
                else {
                    mSpeech.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    AddEditAlarmFragment.mission = 0;
                }
                break;
            case R.id.text_emotion:
                if(AddEditAlarmFragment.mission != 3){
                    AddEditAlarmFragment.mission =3;
                    mEmotion.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    mWeather.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    mSpeech.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                }
                else {
                    mEmotion.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    AddEditAlarmFragment.mission = 0;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }

    private void save() {
    }
}
