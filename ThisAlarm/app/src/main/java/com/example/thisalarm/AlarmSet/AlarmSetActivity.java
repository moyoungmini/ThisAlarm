package com.example.thisalarm.AlarmSet;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import com.example.thisalarm.AlarmDB;
import com.example.thisalarm.Main.AlarmData;
import com.example.thisalarm.Main.MainActivity;
import com.example.thisalarm.Main.MainAdapter;
import com.example.thisalarm.R;

public class AlarmSetActivity extends AppCompatActivity {
    private Button mBtnSaved;
    private TimePicker mTimePicker;
    private Switch mSwitchSound, mSwitchVibration;
    private TextView mTvMon, mTvTue, mTvWen, mTvThu, mTvFri, mTvSat, mTvSun;
    private Intent mIntent;
    private AlarmData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);

        initViews();
        readInformation();

        mSwitchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mData.setSound("ON");
                    Log.i("SOUND CHECK", "df");
                }
                else {
                    mData.setSound("OFF");
                    Log.i("SOUND  NOT CHECK", "df");
                }
            }
        });

        mSwitchVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mData.setVibration(1);
                }
                else {
                    mData.setVibration(0);
                }
            }
        });
        //Switch Listener
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarm_set_mon_tv:
                if(mData.getMon() == 0){
                    mData.setMon(1);
                    mTvMon.setTextColor(R.color.yellow);
                }
                else {
                    mData.setMon(0);
                    mTvMon.setTextColor(R.color.fontColor);
                }
                break;
            case R.id.alarm_set_tue_tv:
                if(mData.getTue() == 0){
                    mData.setTue(1);
                    mTvTue.setTextColor(R.color.yellow);
                }
                else {
                    mData.setTue(0);
                    mTvTue.setTextColor(R.color.fontColor);
                }
                break;
            case R.id.alarm_set_wen_tv:
                if(mData.getWen() == 0){
                    mData.setWen(1);
                    mTvWen.setTextColor(R.color.yellow);
                }
                else {
                    mData.setWen(0);
                    mTvWen.setTextColor(R.color.fontColor);
                }
                break;
            case R.id.alarm_set_thu_tv:
                if(mData.getThu() == 0){
                    mData.setThu(1);
                    mTvThu.setTextColor(R.color.yellow);
                }
                else {
                    mData.setThu(0);
                    mTvThu.setTextColor(R.color.fontColor);
                }
                break;
            case R.id.alarm_set_fri_tv:
                if(mData.getFri() == 0){
                    mData.setFri(1);
                    mTvFri.setTextColor(R.color.yellow);
                }
                else {
                    mData.setFri(0);
                    mTvFri.setTextColor(R.color.fontColor);
                }
                break;
            case R.id.alarm_set_sat_tv:
                if(mData.getSat() == 0){
                    mData.setSat(1);
                    mTvSat.setTextColor(R.color.yellow);
                }
                else {
                    mData.setSat(0);
                    mTvSat.setTextColor(R.color.fontColor);
                }
                break;
            case R.id.alarm_set_sun_tv:
                if(mData.getSun() == 0){
                    mData.setSun(1);
                    mTvSun.setTextColor(R.color.yellow);
                }
                else {
                    mData.setSun(0);
                    mTvSun.setTextColor(R.color.fontColor);
                }
                break;
                //Day Listener
            case R.id.alarm_set_saved_btn:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mData.setHour(mTimePicker.getHour());
                    mData.setMinute(mTimePicker.getMinute());
                }

                if(mData.getId() == -100){
                    MainActivity.db.insert(mData.getHour(), mData.getMinute(), mData.getMission(), mData.getSound(), mData.getVibration(), mData.getMon()
                    ,mData.getTue(), mData.getWen(), mData.getThu(), mData.getFri(), mData.getSat(), mData.getSun(), mData.getState());
                }
                //Insert New Alarm
                else {
                    MainActivity.db.update(mData.getId(), mData.getHour(), mData.getMinute(), mData.getMission(), mData.getSound(), mData.getVibration(), mData.getMon()
                            ,mData.getTue(), mData.getWen(), mData.getThu(), mData.getFri(), mData.getSat(), mData.getSun(), mData.getState());
                }
                //Update Existed Alarm

                MainActivity.db.readAlarmList();

                MainActivity.linearLayoutManager = new LinearLayoutManager(this);
                MainActivity.mRecyclerView.setLayoutManager(MainActivity.linearLayoutManager);
                MainActivity.mRecyclerViewAdapter = new MainAdapter(this, MainActivity.mData);
                MainActivity.mRecyclerView.setAdapter(MainActivity.mRecyclerViewAdapter);

                finish();
                overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);
                break;
        }
    }

    public void initViews() {
        mBtnSaved = findViewById(R.id.alarm_set_saved_btn);
        mTimePicker = findViewById(R.id.alarm_set_timepicker);
        mSwitchSound = findViewById(R.id.alarm_set_sound_switch);
        mSwitchVibration = findViewById(R.id.alarm_set_vibration_switch);
        mTvMon = findViewById(R.id.alarm_set_mon_tv);
        mTvTue = findViewById(R.id.alarm_set_tue_tv);
        mTvWen = findViewById(R.id.alarm_set_wen_tv);
        mTvThu = findViewById(R.id.alarm_set_thu_tv);
        mTvFri = findViewById(R.id.alarm_set_fri_tv);
        mTvSat = findViewById(R.id.alarm_set_sat_tv);
        mTvSun = findViewById(R.id.alarm_set_sun_tv);
    }

    public void readInformation() {
        mIntent = getIntent();

        int id = mIntent.getIntExtra("id",-100);
        // -100 is init value of id


        if(id !=-100) {
            int hour = mIntent.getExtras().getInt("hour");
            int minute = mIntent.getExtras().getInt("minute");
            String mission = mIntent.getExtras().getString("mission");
            String sound = mIntent.getExtras().getString("sound");
            int vibration = mIntent.getExtras().getInt("vibration");
            int mon = mIntent.getExtras().getInt("mon");
            int tue = mIntent.getExtras().getInt("tue");
            int wen = mIntent.getExtras().getInt("wen");
            int thu = mIntent.getExtras().getInt("thu");
            int fri = mIntent.getExtras().getInt("fri");
            int sat = mIntent.getExtras().getInt("sat");
            int sun = mIntent.getExtras().getInt("sun");
            int state = mIntent.getExtras().getInt("state");

            mData = new AlarmData(id,hour,minute,mission,sound,vibration,mon,tue,wen,thu,fri,sat,sun,state);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTimePicker.setHour(hour);
                mTimePicker.setMinute(minute);

                if(sound.equals("OFF")) {
                    mSwitchSound.setChecked(false);
                }
                else if(sound.equals("ON")) {
                    mSwitchSound.setChecked(true);
                }

                if(vibration ==0) {
                    mSwitchVibration.setChecked(false);
                }
                else if(vibration == 1){
                    mSwitchVibration.setChecked(true);
                }

                if(mon ==1){
                    mTvMon.setTextColor(R.color.yellow);
                }
                if(tue ==1){
                    mTvTue.setTextColor(R.color.yellow);
                }
                if(wen ==1){
                    mTvWen.setTextColor(R.color.yellow);
                }
                if(thu ==1){
                    mTvThu.setTextColor(R.color.yellow);
                }
                if(fri ==1){
                    mTvFri.setTextColor(R.color.yellow);
                }
                if(sat ==1){
                    mTvSat.setTextColor(R.color.yellow);
                }
                if(sun ==1){
                    mTvSun.setTextColor(R.color.yellow);
                }
            }
        }
        // Read pre information
        else {
            mData = new AlarmData();
        }
        // Init Data
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);
    }

}
