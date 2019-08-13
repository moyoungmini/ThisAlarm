package com.example.alarmapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alarmapp.R;
import com.example.alarmapp.data.DatabaseHelper;
import com.example.alarmapp.listener.ItemTouchHelperListener;
import com.example.alarmapp.model.Alarm;
import com.example.alarmapp.service.AlarmReceiver;
import com.example.alarmapp.service.LoadAlarmsService;
import com.example.alarmapp.ui.AddEditAlarmActivity;
import com.example.alarmapp.ui.MainActivity;
import com.example.alarmapp.util.AlarmUtils;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getContext;

public final class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder> implements ItemTouchHelperListener {

    private List<Alarm> mAlarms;

    private String[] mDays;
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

        holder.time.setText(AlarmUtils.getReadableTime(alarm.getTime()));
        holder.amPm.setText(AlarmUtils.getAmPm(alarm.getTime()));
        holder.label.setText(alarm.getLabel());
        holder.days.setText(buildSelectedDays(alarm));

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

        holder.enabledSwitch.setOnCheckedChangeListener(null);
        holder.enabledSwitch.setChecked(alarm.isEnabled());
        if(alarm.isEnabled()) {
            holder.amPm.setTextColor(c.getResources().getColorStateList(R.color.fontColor));
            holder.time.setTextColor(c.getResources().getColorStateList(R.color.fontColor));
            holder.layout.setBackgroundResource(R.color.colorPrimary);
            holder.allLayout.setBackgroundResource(R.drawable.recycler_item_background);
        }
        else {
            holder.amPm.setTextColor(c.getResources().getColorStateList(R.color.fontMissColor));
            holder.time.setTextColor(c.getResources().getColorStateList(R.color.fontMissColor));
            holder.layout.setBackgroundResource(R.color.grayColor);
            holder.allLayout.setBackgroundResource(R.drawable.recycler_item_empty_background);
        }
        holder.enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    holder.amPm.setTextColor(c.getResources().getColorStateList(R.color.fontColor));
                    holder.time.setTextColor(c.getResources().getColorStateList(R.color.fontColor));
                    holder.layout.setBackgroundResource(R.color.colorPrimary);
                    holder.allLayout.setBackgroundResource(R.drawable.recycler_item_background);
                    //->
                    //Alarm 등록
                    //DB변경 enabled가 true로 변경

                    alarm.setIsEnabled(true);
                    DatabaseHelper.getInstance(c).updateAlarm(alarm);
                    AlarmReceiver.setReminderAlarm(c,alarm);
                }
                else {
                    //<-
                    //Alarm cancel
                    // DB 변경 enabled가 false로 변경

                    holder.amPm.setTextColor(c.getResources().getColorStateList(R.color.fontMissColor));
                    holder.time.setTextColor(c.getResources().getColorStateList(R.color.fontMissColor));
                    holder.layout.setBackgroundResource(R.color.grayColor);
                    holder.allLayout.setBackgroundResource(R.drawable.recycler_item_empty_background);
                    alarm.setIsEnabled(false);
                    DatabaseHelper.getInstance(c).updateAlarm(alarm);
                    AlarmReceiver.cancelReminderAlarm(c,alarm);
                }
                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        notifyDataSetChanged();
                    }
                };
                handler.post(r);
            }
        });
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

    public void setAlarms(List<Alarm> alarms) {
        mAlarms = alarms;
        notifyDataSetChanged();
    }

    @Override
    public void onItemRemove(int position) {
        final Alarm alarm = mAlarms.get(position);

        //Cancel any pending notifications for this alarm
        AlarmReceiver.cancelReminderAlarm(MainActivity.mainActivity, alarm);

        final int rowsDeleted = DatabaseHelper.getInstance(MainActivity.mainActivity).deleteAlarm(alarm);
        int messageId;
        if(rowsDeleted == 1) {
            LoadAlarmsService.launchLoadAlarmsService(MainActivity.mainActivity);
        } else {
            messageId = R.string.delete_failed;
            Toast.makeText(MainActivity.mainActivity, messageId, Toast.LENGTH_SHORT).show();
        }
        //Delete
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        TextView time, amPm, label, days;
        Switch enabledSwitch;
        LinearLayout layout, allLayout;

        ViewHolder(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.ar_time);
            amPm = (TextView) itemView.findViewById(R.id.ar_am_pm);
            label = (TextView) itemView.findViewById(R.id.ar_label);
            days = (TextView) itemView.findViewById(R.id.ar_days);
            enabledSwitch = itemView.findViewById(R.id.ar_switch);
            layout = itemView.findViewById(R.id.item_layout);
            allLayout = itemView.findViewById(R.id.item_all_layout);
        }
    }

}
