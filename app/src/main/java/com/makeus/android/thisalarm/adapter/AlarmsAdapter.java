package com.makeus.android.thisalarm.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.makeus.android.thisalarm.R;
import com.makeus.android.thisalarm.data.DatabaseHelper;
import com.makeus.android.thisalarm.listener.ItemTouchHelperListener;
import com.makeus.android.thisalarm.model.Alarm;
import com.makeus.android.thisalarm.service.AlarmReceiver;
import com.makeus.android.thisalarm.service.LoadAlarmsService;
import com.makeus.android.thisalarm.ui.AddEditAlarmActivity;
import com.makeus.android.thisalarm.ui.MainActivity;
import com.makeus.android.thisalarm.ui.SettingActivity;
import com.makeus.android.thisalarm.util.AlarmUtils;
import java.util.List;

public final class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder> implements ItemTouchHelperListener {
    private List<Alarm> mAlarms;
    private String[] mDays; // Day 0:Mon ~ 6:Sun
    private int mAccentColor = -1;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context c = parent.getContext();
        final View v = LayoutInflater.from(c).inflate(R.layout.alarm_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Context c = holder.itemView.getContext();

        if(mAccentColor == -1) {
            mAccentColor = ContextCompat.getColor(c, R.color.accent);
        }

        if(mDays == null){
            mDays = c.getResources().getStringArray(R.array.days_abbreviated);
        }

        final Alarm alarm = mAlarms.get(position);
        //set basic alarm data

        holder.time.setText(AlarmUtils.getReadableTime(alarm.getTime()));
        holder.amPm.setText(AlarmUtils.getAmPm(alarm.getTime()));
        holder.label.setText(alarm.getLabel());
        holder.days.setText(buildSelectedDays(alarm));
        if(alarm.isEnabled()) {
            if (SettingActivity.sIntAppBackground == 0) {
                if(alarm.getMission() ==0) {
                    holder.ivEnabled.setBackgroundResource(R.drawable.nomal);
                }
                else if(alarm.getMission() == 2){
                    holder.ivEnabled.setBackgroundResource(R.drawable.english_mission_circle);
                }
                else if(alarm.getMission() == 3){
                    holder.ivEnabled.setBackgroundResource(R.drawable.face_mission_circle);
                }
                holder.layout.setBackgroundResource(R.drawable.gradation_primary_color);
            }
            else if(SettingActivity.sIntAppBackground ==1){
                if(alarm.getMission() ==0) {
                    holder.ivEnabled.setBackgroundResource(R.drawable.pink_normal);
                }
                else if(alarm.getMission() == 2){
                    holder.ivEnabled.setBackgroundResource(R.drawable.pink_abc);
                }
                else if(alarm.getMission() == 3){
                    holder.ivEnabled.setBackgroundResource(R.drawable.pink_motion);
                }
                holder.layout.setBackgroundResource(R.drawable.gradation_pink_color);
            }
            else if(SettingActivity.sIntAppBackground ==2){
                if(alarm.getMission() ==0) {
                    holder.ivEnabled.setBackgroundResource(R.drawable.blue_normal);
                }
                else if(alarm.getMission() == 2){
                    holder.ivEnabled.setBackgroundResource(R.drawable.blue_abc);
                }
                else if(alarm.getMission() == 3){
                    holder.ivEnabled.setBackgroundResource(R.drawable.blue_motion);
                }
                holder.layout.setBackgroundResource(R.drawable.gradation_blue_color);
            }
        }
        else {
            if (SettingActivity.sIntAppBackground == 0) {
                if(alarm.getMission() ==0) {
                    holder.ivEnabled.setBackgroundResource(R.drawable.nomal_sleep);
                }
                else if(alarm.getMission() == 2){
                    holder.ivEnabled.setBackgroundResource(R.drawable.abc_sleep);
                }
                else if(alarm.getMission() == 3){
                    holder.ivEnabled.setBackgroundResource(R.drawable.motion_sleep);
                }
                holder.allLayout.setBackgroundResource(R.drawable.gradation_primary_color);
            }
            else if(SettingActivity.sIntAppBackground ==1){
                if(alarm.getMission() ==0) {
                    holder.ivEnabled.setBackgroundResource(R.drawable.pink_normal_sleep);
                }
                else if(alarm.getMission() == 2){
                    holder.ivEnabled.setBackgroundResource(R.drawable.pink_abc_sleep);
                }
                else if(alarm.getMission() == 3){
                    holder.ivEnabled.setBackgroundResource(R.drawable.pink_motion_sleep);
                }
                holder.allLayout.setBackgroundResource(R.drawable.gradation_pink_color);
            }
            else if(SettingActivity.sIntAppBackground ==2){
                if(alarm.getMission() ==0) {
                    holder.ivEnabled.setBackgroundResource(R.drawable.blue_normal_sleep);
                }
                else if(alarm.getMission() == 2){
                    holder.ivEnabled.setBackgroundResource(R.drawable.blue_abc_sleep);
                }
                else if(alarm.getMission() == 3){
                    holder.ivEnabled.setBackgroundResource(R.drawable.blue_motion_sleep);
                }
                holder.allLayout.setBackgroundResource(R.drawable.gradation_blue_color);
            }
            holder.layout.setBackgroundResource(R.color.primaryTransparent);
        }
        //Set holder layout of each position

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context c = view.getContext();
                final Intent launchEditAlarmIntent =
                        AddEditAlarmActivity.buildAddEditAlarmActivityIntent(
                                c, AddEditAlarmActivity.EDIT_ALARM
                        );
                launchEditAlarmIntent.putExtra(AddEditAlarmActivity.ALARM_EXTRA, alarm);
                c.startActivity(launchEditAlarmIntent);
            }
        });
        //Set layout Click event and this event starts addEditAlacmactivity

        holder.ivEnabled.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alarm.isEnabled()) {
                    holder.layout.setBackgroundResource(R.color.primaryTransparent);
                    if(SettingActivity.sIntAppBackground ==0){
                        if(alarm.getMission() ==0){
                            holder.ivEnabled.setBackgroundResource(R.drawable.nomal_sleep);
                        }
                        else if(alarm.getMission() == 2){
                            holder.ivEnabled.setBackgroundResource(R.drawable.abc_sleep);
                        }
                        else if(alarm.getMission() == 3){
                            holder.ivEnabled.setBackgroundResource(R.drawable.motion_sleep);
                        }
                    }
                    else if(SettingActivity.sIntAppBackground ==1){
                        if(alarm.getMission() ==0){
                            holder.ivEnabled.setBackgroundResource(R.drawable.pink_normal_sleep);
                        }
                        else if(alarm.getMission() == 2){
                            holder.ivEnabled.setBackgroundResource(R.drawable.pink_abc_sleep);
                        }
                        else if(alarm.getMission() == 3){
                            holder.ivEnabled.setBackgroundResource(R.drawable.pink_motion_sleep);
                        }
                    }
                    else if(SettingActivity.sIntAppBackground ==2){
                        if(alarm.getMission() ==0){
                            holder.ivEnabled.setBackgroundResource(R.drawable.blue_normal_sleep);
                        }
                        else if(alarm.getMission() == 2){
                            holder.ivEnabled.setBackgroundResource(R.drawable.blue_abc_sleep);
                        }
                        else if(alarm.getMission() == 3){
                            holder.ivEnabled.setBackgroundResource(R.drawable.blue_motion_sleep);
                        }
                    }
                    alarm.setIsEnabled(false);
                    DatabaseHelper.getInstance(c).updateAlarm(alarm);
                    AlarmReceiver.cancelReminderAlarm(c,alarm);
                    //update database and cancel alarm when off alarm
                }
                else {
                    if(SettingActivity.sIntAppBackground ==0){
                        if(alarm.getMission() ==0){
                            holder.ivEnabled.setBackgroundResource(R.drawable.nomal);
                        }
                        else if(alarm.getMission() == 2){
                            holder.ivEnabled.setBackgroundResource(R.drawable.abc);
                        }
                        else if(alarm.getMission() == 3){
                            holder.ivEnabled.setBackgroundResource(R.drawable.motion);
                        }
                        holder.layout.setBackgroundResource(R.drawable.gradation_primary_color);
                    }
                    else if(SettingActivity.sIntAppBackground ==1){
                        if(alarm.getMission() ==0){
                            holder.ivEnabled.setBackgroundResource(R.drawable.pink_normal);
                        }
                        else if(alarm.getMission() == 2){
                            holder.ivEnabled.setBackgroundResource(R.drawable.pink_abc);
                        }
                        else if(alarm.getMission() == 3){
                            holder.ivEnabled.setBackgroundResource(R.drawable.pink_motion);
                        }
                        holder.layout.setBackgroundResource(R.drawable.gradation_pink_color);
                    }
                    else if(SettingActivity.sIntAppBackground ==2){
                        if(alarm.getMission() ==0){
                            holder.ivEnabled.setBackgroundResource(R.drawable.blue_normal);
                        }
                        else if(alarm.getMission() == 2){
                            holder.ivEnabled.setBackgroundResource(R.drawable.blue_abc);
                        }
                        else if(alarm.getMission() == 3){
                            holder.ivEnabled.setBackgroundResource(R.drawable.blue_motion);
                        }
                        holder.layout.setBackgroundResource(R.drawable.gradation_blue_color);
                    }
                    alarm.setIsEnabled(true);
                    DatabaseHelper.getInstance(c).updateAlarm(alarm);
                    AlarmReceiver.setReminderAlarm(c,alarm);
                    //update database and register alarm when off alarm
                }

                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        notifyDataSetChanged();
                    }
                };
                handler.post(r);
                //notify recyclerview for changing data
            }
        }) ;
    }

    @Override
    public int getItemCount() {
        return (mAlarms == null) ? 0 : mAlarms.size();
    }

    private Spannable buildSelectedDays(Alarm alarm) {
        final int numDays = 7;
        final SparseBooleanArray days = alarm.getDays();

        final SpannableStringBuilder builder = new SpannableStringBuilder();
        ForegroundColorSpan span;

        int startIndex, endIndex;
        for (int i = 0; i < numDays; i++) {
            startIndex = builder.length();

            final String dayText = mDays[i];
            builder.append(dayText);
            builder.append(" ");

            endIndex = startIndex + dayText.length();

            final boolean isSelected = days.valueAt(i);
            if(isSelected) {
                span = new ForegroundColorSpan(mAccentColor);
                builder.setSpan(span, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return builder;
    }
    //Set day

    public void setAlarms(List<Alarm> alarms) {
        mAlarms = alarms;
        notifyDataSetChanged();
    }
    //Set alarm data

    @Override
    public void onItemRemove(int position) {
        final Alarm alarm = mAlarms.get(position);
        AlarmReceiver.cancelReminderAlarm(MainActivity.mainActivity, alarm);
        final int rowsDeleted = DatabaseHelper.getInstance(MainActivity.mainActivity).deleteAlarm(alarm);
        if(rowsDeleted == 1) {
            LoadAlarmsService.launchLoadAlarmsService(MainActivity.mainActivity);
        }
    }
    // Delete Alarm


    static final class ViewHolder extends RecyclerView.ViewHolder {

        TextView time, amPm, label, days;
        ImageView ivEnabled;
        LinearLayout allLayout, layout;

        ViewHolder(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.ar_time);
            amPm = itemView.findViewById(R.id.ar_am_pm);
            label = itemView.findViewById(R.id.ar_label);
            days = itemView.findViewById(R.id.ar_days);
            layout = itemView.findViewById(R.id.ar_transparent_layout);
            ivEnabled = itemView.findViewById(R.id.ar_enabled_iv);
            allLayout = itemView.findViewById(R.id.item_all_layout);

            if(SettingActivity.sIntAppBackground ==0){
                allLayout.setBackgroundResource(R.drawable.gradation_primary_color);
            }
            else if(SettingActivity.sIntAppBackground == 1){
                allLayout.setBackgroundResource(R.drawable.gradation_pink_color);
            }
            else if(SettingActivity.sIntAppBackground == 2){
                allLayout.setBackgroundResource(R.drawable.gradation_blue_color);
            }
        }

    }
    // Set recyclerview item view
}
