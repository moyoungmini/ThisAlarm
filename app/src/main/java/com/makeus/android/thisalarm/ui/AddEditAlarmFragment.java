package com.makeus.android.thisalarm.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import com.makeus.android.thisalarm.R;
import com.makeus.android.thisalarm.data.DatabaseHelper;
import com.makeus.android.thisalarm.model.Alarm;
import com.makeus.android.thisalarm.service.AlarmReceiver;
import com.makeus.android.thisalarm.util.ViewUtils;
import net.cachapa.expandablelayout.ExpandableLayout;
import java.util.ArrayList;
import java.util.Calendar;
import static android.content.Context.MODE_PRIVATE;

public final class AddEditAlarmFragment extends Fragment implements View.OnClickListener {

    private TimePicker mTimePicker;
    private Button mBtnSaved, mBtnMission;
    private TextView mTvMon, mTvTue, mTvWen, mTvThu, mTvFri, mTvSat, mTvSun, mTvSound, mTvName, mTvNone, mTvRead, mTvEmotion;
    private EditText mLabel;
    private Switch mSwitch;
    private ArrayList<Boolean> dayList;
    public static int mission = 0;
    private LinearLayout mMissionLayout;
    private Animation slideDown, slideUp;
    private ScrollView svLayout;
    private int[][] states;
    private int[] intSwitchPrimaryOval, intSiwtchPrimaryBackground, intSwitchPinkOval, intSiwtchPinkBackground, intSwitchBlueOval, intSiwtchBlueBackground;

    private ExpandableLayout mExpandableLayout;

    public static AddEditAlarmFragment newInstance(Alarm alarm) {
        Bundle args = new Bundle();
        args.putParcelable(AddEditAlarmActivity.ALARM_EXTRA, alarm);

        AddEditAlarmFragment fragment = new AddEditAlarmFragment();
        fragment.setArguments(args);

        return fragment;
    }
    // Set fragment

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_add_edit_alarm, container, false);
        setHasOptionsMenu(true);

        final Alarm alarm = getAlarm(); // Get alarm

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
        mMissionLayout = v.findViewById(R.id.edit_alarm_frag_expanding_layout);
        mLabel = v.findViewById(R.id.alarm_set_label_et);
        mBtnMission = v.findViewById(R.id.alarm_set_mission_tv);
        mSwitch = v.findViewById(R.id.alarm_edit_fragment_switch);
        mTvNone = v.findViewById(R.id.edit_alarm_frag_non_mission_tv);
        mTvRead = v.findViewById(R.id.edit_alarm_read_tv);
        mTvEmotion = v.findViewById(R.id.edit_alarm_frag_face_tv);
        svLayout = v.findViewById(R.id.add_edit_fragment_sv);
        mTvName = v.findViewById(R.id.add_edit_fragment_name_tv);
        mTvSound = v.findViewById(R.id.add_edit_fragment_sound_tv);
        mExpandableLayout = v.findViewById(R.id.edit_fragment_expandablelayiout);
        // Init view in fragment

        mBtnSaved.setOnClickListener(this);
        mBtnMission.setOnClickListener(this);
        mTvMon.setOnClickListener(this);
        mTvTue.setOnClickListener(this);
        mTvWen.setOnClickListener(this);
        mTvThu.setOnClickListener(this);
        mTvFri.setOnClickListener(this);
        mTvSat.setOnClickListener(this);
        mTvSun.setOnClickListener(this);
        mMissionLayout.setOnClickListener(this);
        mTvNone.setOnClickListener(this);
        mTvRead.setOnClickListener(this);
        mTvEmotion.setOnClickListener(this);
        //Set listener

        dayList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dayList.add(false);
        }
        // Set day to false

        mission = alarm.getMission();
        mSwitch.setChecked(alarm.getSound());
        mLabel.setText(alarm.getLabel());
        // set view data

        mMissionLayout.bringToFront();
        mMissionLayout.setVisibility(View.INVISIBLE);
        // Set mission layout invisible

        slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.anim_silide_down);
        slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.anim_silide_up);
        // Set animation

        states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked},
        };
        intSwitchPrimaryOval = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#6b85e3")
        };
        intSiwtchPrimaryBackground = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#6b85e3")
        };
        intSwitchPinkOval = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#eda2a2")
        };
        intSiwtchPinkBackground = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#eda2a2")
        };
        intSwitchBlueOval = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#66b4d3")
        };
        intSiwtchBlueBackground = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#66b4d3")
        };
        // Set color of siwtch background and circle

        SharedPreferences pref= getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        SettingActivity.sIntAppBackground = pref.getInt("primaryColor",0);
        setDayCheckboxes(alarm);

        if(SettingActivity.sIntAppBackground ==0){
            svLayout.setBackgroundResource(R.drawable.gradation_primary_non_radiuscolor);
            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitch.getThumbDrawable()), new ColorStateList(states, intSwitchPrimaryOval));
            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitch.getTrackDrawable()), new ColorStateList(states, intSiwtchPrimaryBackground));
            mBtnMission.setBackgroundResource(R.drawable.btn_primary);
            mBtnSaved.setBackgroundResource(R.drawable.btn_save);
            mBtnMission.setTextColor(getContext().getResources().getColorStateList(R.drawable.pirmary_text_color));
            mBtnSaved.setTextColor(getContext().getResources().getColorStateList(R.drawable.save_text_set));
            mMissionLayout.setBackgroundResource(R.drawable.primary_mission_background);
            mTvName.setTextColor(getContext().getResources().getColorStateList(R.color.primaryColor));
            mTvSound.setTextColor(getContext().getResources().getColorStateList(R.color.primaryColor));
            mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.drawable.pirmary_text_color));
            mTvRead.setTextColor(getContext().getResources().getColorStateList(R.drawable.pirmary_text_color));
            mTvNone.setTextColor(getContext().getResources().getColorStateList(R.drawable.pirmary_text_color));
            mTvEmotion.setBackgroundResource(R.drawable.btn_mission_up_background);
            mTvRead.setBackgroundResource(R.drawable.primary_background_test);
            mTvNone.setBackgroundResource(R.drawable.primary_background_test);

            if(mission==0){
                mTvNone.setBackgroundResource(R.color.primaryBackgroundPress);
                mTvNone.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                mBtnMission.setText("미션 없음");
            }else if(mission==2){
                mTvRead.setBackgroundResource(R.color.primaryBackgroundPress);
                mTvRead.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                mBtnMission.setText("영어 단어 읽기");
            }else if(mission==3){
                mTvEmotion.setBackgroundResource(R.drawable.btn_mission_up_press_radius);
                mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                mBtnMission.setText("감정 인식");
            }
        }
        else if(SettingActivity.sIntAppBackground ==1){
            svLayout.setBackgroundResource(R.drawable.gradation_pink_non_radiuscolor);
            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitch.getThumbDrawable()), new ColorStateList(states, intSwitchPinkOval));
            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitch.getTrackDrawable()), new ColorStateList(states, intSiwtchPinkBackground));
            mBtnMission.setBackgroundResource(R.drawable.btn_mission_pink);
            mBtnSaved.setBackgroundResource(R.drawable.btn_pink);
            mBtnMission.setTextColor(getContext().getResources().getColorStateList(R.drawable.pink_mission_text_color));
            mBtnSaved.setTextColor(getContext().getResources().getColorStateList(R.drawable.pink_text_color));
            mMissionLayout.setBackgroundResource(R.drawable.pink_background);
            mTvName.setTextColor(getContext().getResources().getColorStateList(R.color.pinkColor));
            mTvSound.setTextColor(getContext().getResources().getColorStateList(R.color.pinkColor));
            mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.drawable.pink_mission_text_color));
            mTvRead.setTextColor(getContext().getResources().getColorStateList(R.drawable.pink_mission_text_color));
            mTvNone.setTextColor(getContext().getResources().getColorStateList(R.drawable.pink_mission_text_color));
            mTvEmotion.setBackgroundResource(R.drawable.btn_pink_mission_up_background);
            mTvRead.setBackgroundResource(R.drawable.pink_background_test);
            mTvNone.setBackgroundResource(R.drawable.pink_background_test);

            if(mission==0){
                mTvNone.setBackgroundResource(R.color.pinkBackground2);
                mTvNone.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                mBtnMission.setText("미션 없음");
            }else if(mission==2){
                mTvRead.setBackgroundResource(R.color.pinkBackground2);
                mTvRead.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                mBtnMission.setText("영어 단어 읽기");
            }else if(mission==3){
                mTvEmotion.setBackgroundResource(R.drawable.btn_pink_mission_up_press_radius);
                mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                mBtnMission.setText("감정 인식");
            }
        }
        else if(SettingActivity.sIntAppBackground ==2){
            svLayout.setBackgroundResource(R.drawable.gradation_blue_non_radiuscolor);
            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitch.getThumbDrawable()), new ColorStateList(states, intSwitchBlueOval));
            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitch.getTrackDrawable()), new ColorStateList(states, intSiwtchBlueBackground));
            mBtnMission.setBackgroundResource(R.drawable.btn_blue);
            mBtnSaved.setBackgroundResource(R.drawable.btn_blue);
            mBtnMission.setTextColor(getContext().getResources().getColorStateList(R.drawable.blue_text_color));
            mBtnSaved.setTextColor(getContext().getResources().getColorStateList(R.drawable.blue_text_color));
            mMissionLayout.setBackgroundResource(R.drawable.blue_background);
            mTvName.setTextColor(getContext().getResources().getColorStateList(R.color.fontBlue2));
            mTvSound.setTextColor(getContext().getResources().getColorStateList(R.color.fontBlue2));
            mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.drawable.blue_text_color));
            mTvRead.setTextColor(getContext().getResources().getColorStateList(R.drawable.blue_text_color));
            mTvNone.setTextColor(getContext().getResources().getColorStateList(R.drawable.blue_text_color));
            mTvEmotion.setBackgroundResource(R.drawable.btn_blue_mission_up_background);
            mTvRead.setBackgroundResource(R.drawable.blue_background_test);
            mTvNone.setBackgroundResource(R.drawable.blue_background_test);

            if(mission==0){
                mTvNone.setBackgroundResource(R.color.blueBackground);
                mTvNone.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                mBtnMission.setText("미션 없음");
            }else if(mission==2){
                mTvRead.setBackgroundResource(R.color.blueBackground);
                mTvRead.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                mBtnMission.setText("영어 단어 읽기");
            }else if(mission==3){
                mTvEmotion.setBackgroundResource(R.drawable.btn_blue_mission_up_press_radius);
                mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                mBtnMission.setText("감정 인식");
            }
        }
        //Set view color according to app primary color

        return v;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_alarm_frag_non_mission_tv:
                mission=0;
                mExpandableLayout.collapse();
                mBtnMission.setVisibility(View.VISIBLE);
                mBtnMission.bringToFront();

                if(SettingActivity.sIntAppBackground==0){
                    mTvNone.setBackgroundResource(R.color.primaryBackgroundPress);
                    mTvNone.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                    mTvEmotion.setBackgroundResource(R.drawable.btn_mission_up_background);
                    mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.drawable.pirmary_text_color));
                    mTvRead.setBackgroundResource(R.drawable.primary_background_test);
                    mTvRead.setTextColor(getContext().getResources().getColorStateList(R.drawable.pirmary_text_color));
                }
                else if(SettingActivity.sIntAppBackground==1){
                    mTvNone.setBackgroundResource(R.color.pinkBackground2);
                    mTvNone.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                    mTvEmotion.setBackgroundResource(R.drawable.btn_pink_mission_up_background);
                    mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.drawable.pink_mission_text_color));
                    mTvRead.setBackgroundResource(R.drawable.pink_background_test);
                    mTvRead.setTextColor(getContext().getResources().getColorStateList(R.drawable.pink_mission_text_color));
                }
                else if(SettingActivity.sIntAppBackground==2){
                    mTvNone.setBackgroundResource(R.color.blueBackground);
                    mTvNone.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                    mTvEmotion.setBackgroundResource(R.drawable.btn_blue_mission_up_background);
                    mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.drawable.blue_text_color));
                    mTvRead.setBackgroundResource(R.drawable.blue_background_test);
                    mTvRead.setTextColor(getContext().getResources().getColorStateList(R.drawable.blue_text_color));
                }
                mBtnMission.setText("미션 없음");
                break;
            case R.id.edit_alarm_frag_face_tv:
                mission=3;
                mExpandableLayout.collapse();
                mBtnMission.setVisibility(View.VISIBLE);
                mBtnMission.bringToFront();

                if(SettingActivity.sIntAppBackground==0){
                    mTvNone.setBackgroundResource(R.drawable.primary_background_test);
                    mTvNone.setTextColor(getContext().getResources().getColorStateList(R.drawable.pirmary_text_color));
                    mTvEmotion.setBackgroundResource(R.drawable.btn_mission_up_press_radius);
                    mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                    mTvRead.setBackgroundResource(R.drawable.primary_background_test);
                    mTvRead.setTextColor(getContext().getResources().getColorStateList(R.drawable.pirmary_text_color));
                }
                else if(SettingActivity.sIntAppBackground==1){
                    mTvNone.setBackgroundResource(R.drawable.pink_background_test);
                    mTvNone.setTextColor(getContext().getResources().getColorStateList(R.drawable.pink_mission_text_color));
                    mTvEmotion.setBackgroundResource(R.drawable.btn_pink_mission_up_press_radius);
                    mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                    mTvRead.setBackgroundResource(R.drawable.pink_background_test);
                    mTvRead.setTextColor(getContext().getResources().getColorStateList(R.drawable.pink_mission_text_color));
                }
                else if(SettingActivity.sIntAppBackground==2){
                    mTvNone.setBackgroundResource(R.drawable.blue_background_test);
                    mTvNone.setTextColor(getContext().getResources().getColorStateList(R.drawable.blue_text_color));
                    mTvEmotion.setBackgroundResource(R.drawable.btn_blue_mission_up_press_radius);
                    mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                    mTvRead.setBackgroundResource(R.drawable.blue_background_test);
                    mTvRead.setTextColor(getContext().getResources().getColorStateList(R.drawable.blue_text_color));
                }

                mBtnMission.setText("감정 인식");
                break;
            case R.id.edit_alarm_read_tv:
                mission =2;
                mExpandableLayout.collapse();
                mBtnMission.setVisibility(View.VISIBLE);
                mBtnMission.bringToFront();

                if(SettingActivity.sIntAppBackground==0){
                    mTvRead.setBackgroundResource(R.color.primaryBackgroundPress);
                    mTvRead.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                    mTvEmotion.setBackgroundResource(R.drawable.btn_mission_up_background);
                    mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.drawable.pirmary_text_color));
                    mTvNone.setBackgroundResource(R.drawable.primary_background_test);
                    mTvNone.setTextColor(getContext().getResources().getColorStateList(R.drawable.pirmary_text_color));
                }
                else if(SettingActivity.sIntAppBackground==1){
                    mTvRead.setBackgroundResource(R.color.pinkBackground2);
                    mTvRead.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                    mTvEmotion.setBackgroundResource(R.drawable.btn_pink_mission_up_background);
                    mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.drawable.pink_mission_text_color));
                    mTvNone.setBackgroundResource(R.drawable.pink_background_test);
                    mTvNone.setTextColor(getContext().getResources().getColorStateList(R.drawable.pink_mission_text_color));
                }
                else if(SettingActivity.sIntAppBackground==2){
                    mTvRead.setBackgroundResource(R.color.blueBackground);
                    mTvRead.setTextColor(getContext().getResources().getColorStateList(R.color.white));
                    mTvEmotion.setBackgroundResource(R.drawable.btn_blue_mission_up_background);
                    mTvEmotion.setTextColor(getContext().getResources().getColorStateList(R.drawable.blue_text_color));
                    mTvNone.setBackgroundResource(R.drawable.blue_background_test);
                    mTvNone.setTextColor(getContext().getResources().getColorStateList(R.drawable.blue_text_color));
                }
                mBtnMission.setText("영단어 읽기");
                break;
// Change view according to clicking of mission layout
            case R.id.alarm_edit_fragment_saved_btn:
                save();
                break;
            case R.id.alarm_set_mission_tv:
                mExpandableLayout.expand();
                mMissionLayout.setVisibility(View.VISIBLE);
                mBtnMission.setVisibility(View.INVISIBLE);
                mExpandableLayout.bringToFront();
                break;
            case R.id.alarm_edit_fragment_mon_tv:
                if (SettingActivity.sIntAppBackground == 0) {
                    if (!dayList.get(0)) {
                        mTvMon.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                        dayList.set(0, true);
                    } else {
                        mTvMon.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(0, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==1){
                    if (!dayList.get(0)) {
                        mTvMon.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                        dayList.set(0, true);
                    } else {
                        mTvMon.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(0, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==2){
                    if (!dayList.get(0)) {
                        mTvMon.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                        dayList.set(0, true);
                    } else {
                        mTvMon.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(0, false);
                    }
                }
                break;
            case R.id.alarm_edit_fragment_tue_tv:
                if (SettingActivity.sIntAppBackground == 0) {
                    if (!dayList.get(1)) {
                        mTvTue.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                        dayList.set(1, true);
                    } else {
                        mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(1, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==1){
                    if (!dayList.get(1)) {
                        mTvTue.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                        dayList.set(1, true);
                    } else {
                        mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(1, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==2){
                    if (!dayList.get(1)) {
                        mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                        dayList.set(1, true);
                    } else {
                        mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(1, false);
                    }
                }
                break;
            case R.id.alarm_edit_fragment_wen_tv:
                if (SettingActivity.sIntAppBackground == 0) {
                    if (!dayList.get(2)) {
                        mTvWen.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                        dayList.set(2, true);
                    } else {
                        mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(2, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==1){
                    if (!dayList.get(2)) {
                        mTvWen.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                        dayList.set(2, true);
                    } else {
                        mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(2, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==2){
                    if (!dayList.get(2)) {
                        mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                        dayList.set(2, true);
                    } else {
                        mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(2, false);
                    }
                }
                break;
            case R.id.alarm_edit_fragment_thu_tv:
                if (SettingActivity.sIntAppBackground == 0) {
                    if (!dayList.get(3)) {
                        mTvThu.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                        dayList.set(3, true);
                    } else {
                        mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(3, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==1){
                    if (!dayList.get(3)) {
                        mTvThu.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                        dayList.set(3, true);
                    } else {
                        mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(3, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==2){
                    if (!dayList.get(3)) {
                        mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                        dayList.set(3, true);
                    } else {
                        mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(3, false);
                    }
                }
                break;
            case R.id.alarm_edit_fragment_fri_tv:
                if (SettingActivity.sIntAppBackground == 0) {
                    if (!dayList.get(4)) {
                        mTvFri.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                        dayList.set(4, true);
                    } else {
                        mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(4, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==1){
                    if (!dayList.get(4)) {
                        mTvFri.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                        dayList.set(4, true);
                    } else {
                        mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(4, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==2){
                    if (!dayList.get(4)) {
                        mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                        dayList.set(4, true);
                    } else {
                        mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(4, false);
                    }
                }
                break;
            case R.id.alarm_edit_fragment_sat_tv:
                if (SettingActivity.sIntAppBackground == 0) {
                    if (!dayList.get(5)) {
                        mTvSat.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                        dayList.set(5, true);
                    } else {
                        mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(5, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==1){
                    if (!dayList.get(5)) {
                        mTvSat.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                        dayList.set(5, true);
                    } else {
                        mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(5, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==2){
                    if (!dayList.get(5)) {
                        mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                        dayList.set(5, true);
                    } else {
                        mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(5, false);
                    }
                }
                break;
            case R.id.alarm_edit_fragment_sun_tv:
                if (SettingActivity.sIntAppBackground == 0) {
                    if (!dayList.get(6)) {
                        mTvSun.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                        dayList.set(6, true);
                    } else {
                        mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(6, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==1){
                    if (!dayList.get(6)) {
                        mTvSun.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                        dayList.set(6, true);
                    } else {
                        mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(6, false);
                    }
                }
                else if(SettingActivity.sIntAppBackground ==2){
                    if (!dayList.get(6)) {
                        mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                        dayList.set(6, true);
                    } else {
                        mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                        dayList.set(6, false);
                    }
                }
                break;
// Changing day view according to clicking day list also related primary color
        }
    }

    private Alarm getAlarm() {
        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA);
    }

    private void setDayCheckboxes(Alarm alarm) {

        if(SettingActivity.sIntAppBackground ==0){
            if (alarm.getDay(Alarm.MON)) {
                mTvMon.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                dayList.set(0, true);
            } else {
                mTvMon.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(0, false);
            }

            if (alarm.getDay(Alarm.TUES)) {
                mTvTue.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                dayList.set(1, true);
            } else {
                mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(1, false);
            }

            if (alarm.getDay(Alarm.WED)) {
                mTvWen.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                dayList.set(2, true);
            } else {
                mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(2, false);
            }

            if (alarm.getDay(Alarm.THURS)) {
                mTvThu.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                dayList.set(3, true);
            } else {
                mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(3, false);
            }

            if (alarm.getDay(Alarm.FRI)) {
                mTvFri.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                dayList.set(4, true);
            } else {
                mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(4, false);
            }

            if (alarm.getDay(Alarm.SAT)) {
                mTvSat.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                dayList.set(5, true);
            } else {
                mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(5, false);
            }

            if (alarm.getDay(Alarm.SUN)) {
                mTvSun.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                dayList.set(6, true);
            } else {
                mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
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

                mTvMon.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                mTvTue.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                mTvWen.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                mTvThu.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                mTvFri.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                mTvSat.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
                mTvSun.setTextColor(this.getResources().getColorStateList(R.color.primaryColor));
            }
        }
        else if(SettingActivity.sIntAppBackground ==1){
            if (alarm.getDay(Alarm.MON)) {
                mTvMon.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                dayList.set(0, true);
            } else {
                mTvMon.setTextColor(this.getResources().getColorStateList(R.color.pinkColor));
                dayList.set(0, false);
            }

            if (alarm.getDay(Alarm.TUES)) {
                mTvTue.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                dayList.set(1, true);
            } else {
                mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(1, false);
            }

            if (alarm.getDay(Alarm.WED)) {
                mTvWen.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                dayList.set(2, true);
            } else {
                mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(2, false);
            }

            if (alarm.getDay(Alarm.THURS)) {
                mTvThu.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                dayList.set(3, true);
            } else {
                mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(3, false);
            }

            if (alarm.getDay(Alarm.FRI)) {
                mTvFri.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                dayList.set(4, true);
            } else {
                mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(4, false);
            }

            if (alarm.getDay(Alarm.SAT)) {
                mTvSat.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                dayList.set(5, true);
            } else {
                mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(5, false);
            }

            if (alarm.getDay(Alarm.SUN)) {
                mTvSun.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                dayList.set(6, true);
            } else {
                mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
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

                mTvMon.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                mTvTue.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                mTvWen.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                mTvThu.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                mTvFri.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                mTvSat.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
                mTvSun.setTextColor(this.getResources().getColorStateList(R.color.pinkBackgroundPress));
            }
        }
        else if(SettingActivity.sIntAppBackground ==2){
            if (alarm.getDay(Alarm.MON)) {
                mTvMon.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                dayList.set(0, true);
            } else {
                mTvMon.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(0, false);
            }

            if (alarm.getDay(Alarm.TUES)) {
                mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                dayList.set(1, true);
            } else {
                mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(1, false);
            }

            if (alarm.getDay(Alarm.WED)) {
                mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                dayList.set(2, true);
            } else {
                mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(2, false);
            }

            if (alarm.getDay(Alarm.THURS)) {
                mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                dayList.set(3, true);
            } else {
                mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(3, false);
            }

            if (alarm.getDay(Alarm.FRI)) {
                mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                dayList.set(4, true);
            } else {
                mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(4, false);
            }

            if (alarm.getDay(Alarm.SAT)) {
                mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                dayList.set(5, true);
            } else {
                mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
                dayList.set(5, false);
            }

            if (alarm.getDay(Alarm.SUN)) {
                mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                dayList.set(6, true);
            } else {
                mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontMiss));
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

                mTvMon.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                mTvTue.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                mTvWen.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                mTvThu.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                mTvFri.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                mTvSat.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
                mTvSun.setTextColor(this.getResources().getColorStateList(R.color.fontBlue2));
            }
        }
    }
    // Set days color. and  all value(7) is false -> Set daylist all value is yellow and true

    private void save() {
        final Alarm alarm = getAlarm();

        final Calendar time = Calendar.getInstance();
        time.set(Calendar.MINUTE, ViewUtils.getTimePickerMinute(mTimePicker));
        time.set(Calendar.HOUR_OF_DAY, ViewUtils.getTimePickerHour(mTimePicker));
        time.set(Calendar.SECOND, 0);

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
    // Register alarmmanager and update database
}
