package com.makeus.android.thisalarm.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import com.makeus.android.thisalarm.R;
import com.makeus.android.thisalarm.model.Alarm;
import com.makeus.android.thisalarm.ui.AlarmLandingPageActivity;
import com.makeus.android.thisalarm.util.AlarmUtils;
import java.util.Calendar;
import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;
import static com.makeus.android.thisalarm.ui.AlarmLandingPageActivity.launchIntent;

public final class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();
    private static final String CHANNEL_ID = "alarm_channel";

    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String ALARM_KEY = "alarm_key";

    public static Intent mIntent;
    public static PendingIntent pIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        final Alarm alarm = intent.getBundleExtra(BUNDLE_EXTRA).getParcelable(ALARM_KEY);
        if(alarm == null) {
            Log.e(TAG, "Alarm is null", new NullPointerException());
            return;
        }

        final int id = alarm.notificationId();

        long [] newVibration = new long[1000];
        for(int i=0;i<newVibration.length;i++){
            newVibration[i]=2000;
        }

        final NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(context);

        mIntent = new Intent(context, AlarmLandingPageActivity.class);
        mIntent.putExtra("mission", alarm.getMission());
        mIntent.putExtra("sound",alarm.getSound());
        mIntent.putExtra("enable",alarm.isEnabled());
        mIntent.putExtra("label",alarm.getLabel());
        mIntent.putExtra("time",alarm.getTime());

        mIntent.putExtra("mon",alarm.getDay(Alarm.MON));
        mIntent.putExtra("tue",alarm.getDay(Alarm.TUES));
        mIntent.putExtra("wen",alarm.getDay(Alarm.WED));
        mIntent.putExtra("thu",alarm.getDay(Alarm.THURS));
        mIntent.putExtra("fri",alarm.getDay(Alarm.FRI));
        mIntent.putExtra("sat",alarm.getDay(Alarm.SAT));
        mIntent.putExtra("sun",alarm.getDay(Alarm.SUN));


        pIntent = PendingIntent.getActivity(context, 0,mIntent, FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_alarm_white_24dp);
        builder.setColor(ContextCompat.getColor(context, R.color.accent));
        builder.setContentText(alarm.getLabel());
        builder.setTicker(alarm.getLabel());
        builder.setVibrate(newVibration);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setContentIntent(AlarmReceiver.pIntent);
        builder.setOngoing(true);
        builder.setPriority(Notification.PRIORITY_HIGH);

        manager.notify(0, builder.build());

        //Reset Alarm manually
        setReminderAlarm(context, alarm);

        try {
            AlarmReceiver.pIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    //Convenience method for setting a notification
    public static void setReminderAlarm(Context context, Alarm alarm) {

        //Check whether the alarm is set to run on any days
        if(!AlarmUtils.isAlarmActive(alarm)) {
            //If alarm not set to run on any days, cancel any existing notifications for this alarm
            cancelReminderAlarm(context, alarm);
            return;
        }

        final Calendar nextAlarmTime = getTimeForNextAlarm(alarm);
        alarm.setTime(nextAlarmTime.getTimeInMillis());
        final Intent intent = new Intent(context, AlarmReceiver.class);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(ALARM_KEY, alarm);
        intent.putExtra(BUNDLE_EXTRA, bundle);

        final PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                alarm.notificationId(),
                intent,
                FLAG_UPDATE_CURRENT
        );

        ScheduleAlarm.with(context).schedule(alarm, pIntent);
    }

    private static Calendar getTimeForNextAlarm(Alarm alarm) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarm.getTime());

        final long currentTime = System.currentTimeMillis();
        final int startIndex = getStartIndexFromTime(calendar);

        int count = 0;
        boolean isAlarmSetForDay;

        final SparseBooleanArray daysArray = alarm.getDays();

        do {
            final int index = (startIndex + count) % 7;
            isAlarmSetForDay =
                    daysArray.valueAt(index) && (calendar.getTimeInMillis() > currentTime);
            if(!isAlarmSetForDay) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                count++;
            }
        } while(!isAlarmSetForDay && count < 7);

        return calendar;

    }

    public static void cancelReminderAlarm(Context context, Alarm alarm) {

        final Intent intent = new Intent(context, AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                alarm.notificationId(),
                intent,
                FLAG_UPDATE_CURRENT
        );

        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pIntent);
    }

    private static int getStartIndexFromTime(Calendar c) {

        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        int startIndex = 0;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                startIndex = 0;
                break;
            case Calendar.TUESDAY:
                startIndex = 1;
                break;
            case Calendar.WEDNESDAY:
                startIndex = 2;
                break;
            case Calendar.THURSDAY:
                startIndex = 3;
                break;
            case Calendar.FRIDAY:
                startIndex = 4;
                break;
            case Calendar.SATURDAY:
                startIndex = 5;
                break;
            case Calendar.SUNDAY:
                startIndex = 6;
                break;
        }

        return startIndex;

    }

    private static void createNotificationChannel(Context ctx) {

        if(SDK_INT < O) return;

        final NotificationManager mgr = ctx.getSystemService(NotificationManager.class);
        if(mgr == null) return;

        long [] newVibration = new long[1000];
        for(int i=0;i<newVibration.length;i++){
            newVibration[i]=2000;
        }

        final String name = ctx.getString(R.string.channel_name);
        if(mgr.getNotificationChannel(name) == null) {
            final NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(newVibration);
            channel.setBypassDnd(true);
            mgr.createNotificationChannel(channel);
        }
    }

    private static PendingIntent launchAlarmLandingPage(Context ctx, Alarm alarm) {
        return PendingIntent.getActivity(
                ctx, alarm.notificationId(), launchIntent(ctx), FLAG_UPDATE_CURRENT
        );
    }

    private static class ScheduleAlarm {

        @NonNull private final Context ctx;
        @NonNull private final AlarmManager am;

        private ScheduleAlarm(@NonNull AlarmManager am, @NonNull Context ctx) {
            this.am = am;
            this.ctx = ctx;
        }

        static ScheduleAlarm with(Context context) {
            final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if(am == null) {
                throw new IllegalStateException("AlarmManager is null");
            }
            return new ScheduleAlarm(am, context);
        }

        void schedule(Alarm alarm, PendingIntent pi) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //API 19 이상 API 23미만
                    am.setExact(AlarmManager.RTC_WAKEUP, alarm.getTime(), pi) ;
                } else {
                    //API 19미만
                    am.set(AlarmManager.RTC_WAKEUP, alarm.getTime(), pi);
                }
            } else {
                //API 23 이상
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.getTime(), pi);
            }
        }
    }
}
