package com.example.alarmapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alarmapp.R;
import com.example.alarmapp.data.DatabaseHelper;
import com.example.alarmapp.model.Alarm;
import com.example.alarmapp.service.AlarmReceiver;
import com.example.alarmapp.service.LoadAlarmsService;
import com.example.alarmapp.util.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;

public final class AddEditAlarmFragment extends Fragment implements View.OnClickListener {

    private TimePicker mTimePicker;
    //private EditText mLabel;
    private CheckBox mMon, mTues, mWed, mThurs, mFri, mSat, mSun;
    private Button mBtnSaved;
    private TextView mTvMon, mTvTue, mTvWen, mTvThu, mTvFri, mTvSat, mTvSun,mTvMission;

    private ArrayList<Boolean> dayList;

    public static AddEditAlarmFragment newInstance(Alarm alarm) {

        Bundle args = new Bundle();
        args.putParcelable(AddEditAlarmActivity.ALARM_EXTRA, alarm);

        AddEditAlarmFragment fragment = new AddEditAlarmFragment();
        fragment.setArguments(args);


        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_add_edit_alarm, container, false);

        setHasOptionsMenu(true);

        final Alarm alarm = getAlarm();

        mTimePicker = (TimePicker) v.findViewById(R.id.alarm_edit_fragment_timepicker);
        ViewUtils.setTimePickerTime(mTimePicker, alarm.getTime());

        mBtnSaved = v.findViewById(R.id.alarm_edit_fragment_saved_btn);
        mTvMission = v.findViewById(R.id.alarm_set_mission_tv);

        mTvMon = v.findViewById(R.id.alarm_edit_fragment_mon_tv);
        mTvTue = v.findViewById(R.id.alarm_edit_fragment_tue_tv);
        mTvWen = v.findViewById(R.id.alarm_edit_fragment_wen_tv);
        mTvThu = v.findViewById(R.id.alarm_edit_fragment_thu_tv);
        mTvFri = v.findViewById(R.id.alarm_edit_fragment_fri_tv);
        mTvSat = v.findViewById(R.id.alarm_edit_fragment_sat_tv);
        mTvSun = v.findViewById(R.id.alarm_edit_fragment_sun_tv);


        mBtnSaved.setOnClickListener(this);
        mTvMission.setOnClickListener(this);
        mTvMon.setOnClickListener(this);
        mTvTue.setOnClickListener(this);
        mTvWen.setOnClickListener(this);
        mTvThu.setOnClickListener(this);
        mTvFri.setOnClickListener(this);
        mTvSat.setOnClickListener(this);
        mTvSun.setOnClickListener(this);

//        mLabel = (EditText) v.findViewById(R.id.edit_alarm_label);
//        mLabel.setText(alarm.getLabel());
//
//        mMon = (CheckBox) v.findViewById(R.id.edit_alarm_mon);
//        mTues = (CheckBox) v.findViewById(R.id.edit_alarm_tues);
//        mWed = (CheckBox) v.findViewById(R.id.edit_alarm_wed);
//        mThurs = (CheckBox) v.findViewById(R.id.edit_alarm_thurs);
//        mFri = (CheckBox) v.findViewById(R.id.edit_alarm_fri);
//        mSat = (CheckBox) v.findViewById(R.id.edit_alarm_sat);
//        mSun = (CheckBox) v.findViewById(R.id.edit_alarm_sun);
//

        dayList = new ArrayList<>();
        for(int i=0;i<7;i++){
            dayList.add(false);
        }
        setDayCheckboxes(alarm);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_alarm_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarm_edit_fragment_saved_btn:
                save();
                break;
            case R.id.alarm_set_mission_tv:
                selectMission();
                break;
            case R.id.alarm_edit_fragment_mon_tv:
                if(!dayList.get(0)){
                    mTvMon.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(0,true);
                }
                else {
                    mTvMon.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    dayList.set(0,false);
                }
                break;
            case R.id.alarm_edit_fragment_tue_tv:
                if(!dayList.get(1)){
                    mTvTue.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(1,true);
                }
                else {
                    mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    dayList.set(1,false);
                }
                break;
            case R.id.alarm_edit_fragment_wen_tv:
                if(!dayList.get(2)){
                    mTvWen.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(2,true);
                }
                else {
                    mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    dayList.set(2,false);
                }
                break;
            case R.id.alarm_edit_fragment_thu_tv:
                if(!dayList.get(3)){
                    mTvThu.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(3,true);
                }
                else {
                    mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    dayList.set(3,false);
                }
                break;
            case R.id.alarm_edit_fragment_fri_tv:
                if(!dayList.get(4)){
                    mTvFri.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(4,true);
                }
                else {
                    mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    dayList.set(4,false);
                }
                break;
            case R.id.alarm_edit_fragment_sat_tv:
                if(!dayList.get(5)){
                    mTvSat.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(5,true);
                }
                else {
                    mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    dayList.set(5,false);
                }
                break;
            case R.id.alarm_edit_fragment_sun_tv:
                if(!dayList.get(6)){
                    mTvSun.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(6,true);
                }
                else {
                    mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    dayList.set(6,false);
                }
                break;
            //Day Listener
//            case R.id.action_delete:
//                delete();
//                break;
        }
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.alarm_edit_fragment_saved_btn:
//                save();
//                break;
//            case R.id.action_delete:
//                delete();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private Alarm getAlarm() {
        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA);
    }

    private void setDayCheckboxes(Alarm alarm) {
        mTvMon.setTextColor(this.getResources().getColorStateList(R.color.yellow));
        mTvTue.setTextColor(this.getResources().getColorStateList(R.color.yellow));
        mTvWen.setTextColor(this.getResources().getColorStateList(R.color.yellow));
        mTvThu.setTextColor(this.getResources().getColorStateList(R.color.yellow));
        mTvFri.setTextColor(this.getResources().getColorStateList(R.color.yellow));
        mTvSat.setTextColor(this.getResources().getColorStateList(R.color.yellow));
        mTvSun.setTextColor(this.getResources().getColorStateList(R.color.yellow));

        for(int i=0;i<7;i++){
            dayList.set(i, true);
        }

//        mMon.setChecked(alarm.getDay(Alarm.MON));
//        mTues.setChecked(alarm.getDay(Alarm.TUES));
//        mWed.setChecked(alarm.getDay(Alarm.WED));
//        mThurs.setChecked(alarm.getDay(Alarm.THURS));
//        mFri.setChecked(alarm.getDay(Alarm.FRI));
//        mSat.setChecked(alarm.getDay(Alarm.SAT));
//        mSun.setChecked(alarm.getDay(Alarm.SUN));
    }

    private void save() {

        final Alarm alarm = getAlarm();

        final Calendar time = Calendar.getInstance();
        time.set(Calendar.MINUTE, ViewUtils.getTimePickerMinute(mTimePicker));
        time.set(Calendar.HOUR_OF_DAY, ViewUtils.getTimePickerHour(mTimePicker));
        alarm.setTime(time.getTimeInMillis());

//        alarm.setLabel(mLabel.getText().toString());

        alarm.setDay(Alarm.MON, dayList.get(0));
        alarm.setDay(Alarm.TUES, dayList.get(1));
        alarm.setDay(Alarm.WED, dayList.get(2));
        alarm.setDay(Alarm.THURS, dayList.get(3));
        alarm.setDay(Alarm.FRI, dayList.get(4));
        alarm.setDay(Alarm.SAT, dayList.get(5));
        alarm.setDay(Alarm.SUN, dayList.get(6));

        final int rowsUpdated = DatabaseHelper.getInstance(getContext()).updateAlarm(alarm);
        final int messageId = (rowsUpdated == 1) ? R.string.update_complete : R.string.update_failed;

        Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();

        AlarmReceiver.setReminderAlarm(getContext(), alarm);

        getActivity().finish();

    }
    private void selectMission() {
        final Intent i = new Intent(getActivity(),MissionSelectActivity.class);
        startActivity(i);
    }
    private void delete() {

        final Alarm alarm = getAlarm();

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext(), R.style.DeleteAlarmDialogTheme);
        builder.setTitle(R.string.delete_dialog_title);
        builder.setMessage(R.string.delete_dialog_content);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Cancel any pending notifications for this alarm
                AlarmReceiver.cancelReminderAlarm(getContext(), alarm);

                final int rowsDeleted = DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm);
                int messageId;
                if(rowsDeleted == 1) {
                    messageId = R.string.delete_complete;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                    LoadAlarmsService.launchLoadAlarmsService(getContext());
                    getActivity().finish();
                } else {
                    messageId = R.string.delete_failed;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();
    }

}
