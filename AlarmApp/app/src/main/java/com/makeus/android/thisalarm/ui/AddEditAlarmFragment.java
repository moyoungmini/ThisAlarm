package com.makeus.android.thisalarm.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import com.makeus.android.thisalarm.R;
import com.makeus.android.thisalarm.data.DatabaseHelper;
import com.makeus.android.thisalarm.dialog.MissionDialog;
import com.makeus.android.thisalarm.model.Alarm;
import com.makeus.android.thisalarm.service.AlarmReceiver;
import com.makeus.android.thisalarm.util.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;

public final class AddEditAlarmFragment extends Fragment implements View.OnClickListener {

    private TimePicker mTimePicker;
    private Button mBtnSaved;
    private TextView mTvMon, mTvTue, mTvWen, mTvThu, mTvFri, mTvSat, mTvSun, mTvMission;
    private EditText mLabel;
    private Switch mSwitch;

    private ArrayList<Boolean> dayList;

    public static int mission = 0;

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

        mTimePicker = v.findViewById(R.id.alarm_edit_fragment_timepicker);
        ViewUtils.setTimePickerTime(mTimePicker, alarm.getTime());

        mBtnSaved = v.findViewById(R.id.alarm_edit_fragment_saved_btn);
        mTvMon = v.findViewById(R.id.alarm_edit_fragment_mon_tv);
        mTvTue = v.findViewById(R.id.alarm_edit_fragment_tue_tv);
        mTvWen = v.findViewById(R.id.alarm_edit_fragment_wen_tv);
        mTvThu = v.findViewById(R.id.alarm_edit_fragment_thu_tv);
        mTvFri = v.findViewById(R.id.alarm_edit_fragment_fri_tv);
        mTvSat = v.findViewById(R.id.alarm_edit_fragment_sat_tv);
        mTvSun = v.findViewById(R.id.alarm_edit_fragment_sun_tv);
        mLabel = v.findViewById(R.id.alarm_set_label_et);
        mTvMission = v.findViewById(R.id.alarm_set_mission_tv);
        mSwitch = v.findViewById(R.id.alarm_edit_fragment_switch);

        mBtnSaved.setOnClickListener(this);
        mTvMission.setOnClickListener(this);
        mTvMon.setOnClickListener(this);
        mTvTue.setOnClickListener(this);
        mTvWen.setOnClickListener(this);
        mTvThu.setOnClickListener(this);
        mTvFri.setOnClickListener(this);
        mTvSat.setOnClickListener(this);
        mTvSun.setOnClickListener(this);

        dayList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dayList.add(false);
        }


        AddEditAlarmFragment.mission = alarm.getMission();
        setDayCheckboxes(alarm);
        mSwitch.setChecked(alarm.getSound());
        mLabel.setText(alarm.getLabel());

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarm_edit_fragment_saved_btn:
                save();
                break;
            case R.id.alarm_set_mission_tv:
                MissionDialog missionDialog = new MissionDialog(getContext());

                //selectMission();
                break;
            case R.id.alarm_edit_fragment_mon_tv:
                if (!dayList.get(0)) {
                    mTvMon.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(0, true);
                } else {
                    mTvMon.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
                    dayList.set(0, false);
                }
                break;
            case R.id.alarm_edit_fragment_tue_tv:
                if (!dayList.get(1)) {
                    mTvTue.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(1, true);
                } else {
                    mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
                    dayList.set(1, false);
                }
                break;
            case R.id.alarm_edit_fragment_wen_tv:
                if (!dayList.get(2)) {
                    mTvWen.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(2, true);
                } else {
                    mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
                    dayList.set(2, false);
                }
                break;
            case R.id.alarm_edit_fragment_thu_tv:
                if (!dayList.get(3)) {
                    mTvThu.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(3, true);
                } else {
                    mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
                    dayList.set(3, false);
                }
                break;
            case R.id.alarm_edit_fragment_fri_tv:
                if (!dayList.get(4)) {
                    mTvFri.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(4, true);
                } else {
                    mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
                    dayList.set(4, false);
                }
                break;
            case R.id.alarm_edit_fragment_sat_tv:
                if (!dayList.get(5)) {
                    mTvSat.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(5, true);
                } else {
                    mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
                    dayList.set(5, false);
                }
                break;
            case R.id.alarm_edit_fragment_sun_tv:
                if (!dayList.get(6)) {
                    mTvSun.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    dayList.set(6, true);
                } else {
                    mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
                    dayList.set(6, false);
                }
                break;
        }
    }

    private Alarm getAlarm() {
        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA);
    }

    private void setDayCheckboxes(Alarm alarm) {

        if (alarm.getDay(Alarm.MON)) {
            mTvMon.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            dayList.set(0, true);
        } else {
            mTvMon.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
            dayList.set(0, false);
        }

        if (alarm.getDay(Alarm.TUES)) {
            mTvTue.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            dayList.set(1, true);
        } else {
            mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
            dayList.set(1, false);
        }

        if (alarm.getDay(Alarm.WED)) {
            mTvWen.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            dayList.set(2, true);
        } else {
            mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
            dayList.set(2, false);
        }

        if (alarm.getDay(Alarm.THURS)) {
            mTvThu.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            dayList.set(3, true);
        } else {
            mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
            dayList.set(3, false);
        }

        if (alarm.getDay(Alarm.FRI)) {
            mTvFri.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            dayList.set(4, true);
        } else {
            mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
            dayList.set(4, false);
        }

        if (alarm.getDay(Alarm.SAT)) {
            mTvSat.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            dayList.set(5, true);
        } else {
            mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
            dayList.set(5, false);
        }

        if (alarm.getDay(Alarm.SUN)) {
            mTvSun.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            dayList.set(6, true);
        } else {
            mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontMissColor));
            dayList.set(6, false);
        }

        boolean select = true;

        for (int i = 0; i < 7; i++) {
            if (dayList.get(i)) {
                select = false;
            }
        }

        if (select) {
            for (int i = 0; i < 7; i++) {
                dayList.set(i, true);
            }

            mTvMon.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            mTvTue.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            mTvWen.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            mTvThu.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            mTvFri.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            mTvSat.setTextColor(this.getResources().getColorStateList(R.color.yellow));
            mTvSun.setTextColor(this.getResources().getColorStateList(R.color.yellow));
        }
    }

    private void save() {
        final Alarm alarm = getAlarm();

        final Calendar time = Calendar.getInstance();
        time.set(Calendar.MINUTE, ViewUtils.getTimePickerMinute(mTimePicker));
        time.set(Calendar.HOUR_OF_DAY, ViewUtils.getTimePickerHour(mTimePicker));
        time.set(Calendar.SECOND, 0);
        //SECOND설정

        alarm.setTime(time.getTimeInMillis());
        alarm.setLabel(mLabel.getText().toString());
        alarm.setDay(Alarm.MON, dayList.get(0));
        alarm.setDay(Alarm.TUES, dayList.get(1));
        alarm.setDay(Alarm.WED, dayList.get(2));
        alarm.setDay(Alarm.THURS, dayList.get(3));
        alarm.setDay(Alarm.FRI, dayList.get(4));
        alarm.setDay(Alarm.SAT, dayList.get(5));
        alarm.setDay(Alarm.SUN, dayList.get(6));
        alarm.setMission(mission);
        alarm.setSound(mSwitch.isChecked());
        alarm.setIsEnabled(true);

        DatabaseHelper.getInstance(getContext()).updateAlarm(alarm);
        AlarmReceiver.setReminderAlarm(getContext(), alarm);
        getActivity().finish();

    }
}
