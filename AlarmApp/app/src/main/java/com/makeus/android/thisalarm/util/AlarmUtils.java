package com.makeus.android.thisalarm.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.SparseBooleanArray;
import com.makeus.android.thisalarm.data.DatabaseHelper;
import com.makeus.android.thisalarm.model.Alarm;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public final class AlarmUtils {

    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("hh:mm", Locale.getDefault());
    private static final SimpleDateFormat AM_PM_FORMAT =
            new SimpleDateFormat("a", Locale.getDefault());
    private static final int REQUEST_ALARM = 1;
    private static final String[] PERMISSIONS_ALARM = {
            Manifest.permission.VIBRATE
    };

    private AlarmUtils() { throw new AssertionError(); }

    public static void checkAlarmPermissions(Activity activity) {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        final int permission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.VIBRATE
        );

        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_ALARM,
                    REQUEST_ALARM
            );
        }
    }
    //Check permission

    public static ContentValues toContentValues(Alarm alarm) {
        final ContentValues cv = new ContentValues(10);

        cv.put(DatabaseHelper.COL_TIME, alarm.getTime());
        cv.put(DatabaseHelper.COL_LABEL, alarm.getLabel());
        cv.put(DatabaseHelper.COL_MISSION,alarm.getMission());

        final SparseBooleanArray days = alarm.getDays();
        cv.put(DatabaseHelper.COL_MON, days.get(Alarm.MON) ? 1 : 0);
        cv.put(DatabaseHelper.COL_TUES, days.get(Alarm.TUES) ? 1 : 0);
        cv.put(DatabaseHelper.COL_WED, days.get(Alarm.WED) ? 1 : 0);
        cv.put(DatabaseHelper.COL_THURS, days.get(Alarm.THURS) ? 1 : 0);
        cv.put(DatabaseHelper.COL_FRI, days.get(Alarm.FRI) ? 1 : 0);
        cv.put(DatabaseHelper.COL_SAT, days.get(Alarm.SAT) ? 1 : 0);
        cv.put(DatabaseHelper.COL_SUN, days.get(Alarm.SUN) ? 1 : 0);

        cv.put(DatabaseHelper.COL_IS_ENABLED, alarm.isEnabled());
        cv.put(DatabaseHelper.COL_SOUND, alarm.getSound());

        return cv;
    }
    //Set content values

    public static ArrayList<Alarm> buildAlarmList(Cursor c) {

        if (c == null) return new ArrayList<>();

        final int size = c.getCount();
        final ArrayList<Alarm> alarms = new ArrayList<>(size);

        if (c.moveToFirst()){
            do {

                final long id = c.getLong(c.getColumnIndex(DatabaseHelper._ID));
                final long time = c.getLong(c.getColumnIndex(DatabaseHelper.COL_TIME));
                final String label = c.getString(c.getColumnIndex(DatabaseHelper.COL_LABEL));
                final boolean mon = c.getInt(c.getColumnIndex(DatabaseHelper.COL_MON)) == 1;
                final boolean tues = c.getInt(c.getColumnIndex(DatabaseHelper.COL_TUES)) == 1;
                final boolean wed = c.getInt(c.getColumnIndex(DatabaseHelper.COL_WED)) == 1;
                final boolean thurs = c.getInt(c.getColumnIndex(DatabaseHelper.COL_THURS)) == 1;
                final boolean fri = c.getInt(c.getColumnIndex(DatabaseHelper.COL_FRI)) == 1;
                final boolean sat = c.getInt(c.getColumnIndex(DatabaseHelper.COL_SAT)) == 1;
                final boolean sun = c.getInt(c.getColumnIndex(DatabaseHelper.COL_SUN)) == 1;
                final boolean isEnabled = c.getInt(c.getColumnIndex(DatabaseHelper.COL_IS_ENABLED)) == 1;
                final boolean sound = c.getInt(c.getColumnIndex(DatabaseHelper.COL_SOUND)) ==1;
                final int mission = c.getInt(c.getColumnIndex(DatabaseHelper.COL_MISSION));

                final Alarm alarm = new Alarm(id, time, label);
                alarm.setDay(Alarm.MON, mon);
                alarm.setDay(Alarm.TUES, tues);
                alarm.setDay(Alarm.WED, wed);
                alarm.setDay(Alarm.THURS, thurs);
                alarm.setDay(Alarm.FRI, fri);
                alarm.setDay(Alarm.SAT, sat);
                alarm.setDay(Alarm.SUN, sun);
                alarm.setSound(sound);
                alarm.setIsEnabled(isEnabled);
                alarm.setMission(mission);

                alarms.add(alarm);

            } while (c.moveToNext());
        }
        // read alarm datas from database

        final Calendar calendar = Calendar.getInstance();
        Collections.sort(alarms, new Comparator<Alarm>() {
           @Override
           public int compare(Alarm left, Alarm right) {
               calendar.setTimeInMillis(left.getTime());
               long leftHour = calendar.get(Calendar.HOUR_OF_DAY);
               long leftMinute = calendar.get(Calendar.MINUTE) + (60 * leftHour);

               calendar.setTimeInMillis(right.getTime());
               long rightHour = calendar.get(Calendar.HOUR_OF_DAY);
               long rightMinute = calendar.get(Calendar.MINUTE) + (60 * rightHour);

               return (int) (leftMinute - rightMinute);
           }
       });
        // Sort Alarm lists to compare alarm data time
        return alarms;
    }
    // Get alarm lists from databases

    public static String getReadableTime(long time) {
        return TIME_FORMAT.format(time);
    }

    public static String getAmPm(long time) {
        return AM_PM_FORMAT.format(time);
    }

    public static boolean isAlarmActive(Alarm alarm) {

        final SparseBooleanArray days = alarm.getDays();

        boolean isActive = false;
        int count = 0;

        while (count < days.size() && !isActive) {
            isActive = days.valueAt(count);
            count++;
        }

        return isActive;

    }

    public static String getActiveDaysAsString(Alarm alarm) {
        StringBuilder builder = new StringBuilder("Active Days: ");
        if(alarm.getDay(Alarm.MON)) builder.append("Monday, ");
        if(alarm.getDay(Alarm.TUES)) builder.append("Tuesday, ");
        if(alarm.getDay(Alarm.WED)) builder.append("Wednesday, ");
        if(alarm.getDay(Alarm.THURS)) builder.append("Thursday, ");
        if(alarm.getDay(Alarm.FRI)) builder.append("Friday, ");
        if(alarm.getDay(Alarm.SAT)) builder.append("Saturday, ");
        if(alarm.getDay(Alarm.SUN)) builder.append("Sunday.");
        if(builder.substring(builder.length()-2).equals(", ")) {
            builder.replace(builder.length()-2,builder.length(),".");
        }
        return builder.toString();
    }
    // This app does not use this code but this code exists from github code
}
