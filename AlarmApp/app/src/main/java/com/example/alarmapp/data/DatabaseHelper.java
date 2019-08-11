package com.example.alarmapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.alarmapp.model.Alarm;
import com.example.alarmapp.util.AlarmUtils;

import java.util.ArrayList;

public final class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarms.db";
    private static final int SCHEMA = 1;

    private static final String TABLE_NAME = "alarms";
    private static final String ENGLISH_TABLE_NAME = "englishWord_TB";



    public static final String _ID = "_id";
    public static final String COL_TIME = "time";
    public static final String COL_LABEL = "label";
    public static final String COL_MON = "mon";
    public static final String COL_TUES = "tues";
    public static final String COL_WED = "wed";
    public static final String COL_THURS = "thurs";
    public static final String COL_FRI = "fri";
    public static final String COL_SAT = "sat";
    public static final String COL_SUN = "sun";
    public static final String COL_IS_ENABLED = "is_enabled";
    public static final String COL_MISSION = "mission";
    public static final String COL_SOUND ="sound";

    String[][] engWord;


    private static DatabaseHelper sInstance = null;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.i(getClass().getSimpleName(), "Creating database...");

        final String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TIME + " INTEGER NOT NULL, " +
                COL_LABEL + " TEXT, " +
                COL_MON + " INTEGER NOT NULL, " +
                COL_TUES + " INTEGER NOT NULL, " +
                COL_WED + " INTEGER NOT NULL, " +
                COL_THURS + " INTEGER NOT NULL, " +
                COL_FRI + " INTEGER NOT NULL, " +
                COL_SAT + " INTEGER NOT NULL, " +
                COL_SUN + " INTEGER NOT NULL, " +
                COL_IS_ENABLED + " INTEGER NOT NULL," +
                COL_SOUND + " TEXT, " +
                COL_MISSION + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(CREATE_ALARMS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new UnsupportedOperationException("This shouldn't happen yet!");
    }

    public long addAlarm() {
        return addAlarm(new Alarm());
    }

    long addAlarm(Alarm alarm) {
        return getWritableDatabase().insert(TABLE_NAME, null, AlarmUtils.toContentValues(alarm));
    }

    public int updateAlarm(Alarm alarm) {
        final String where = _ID + "=?";
        final String[] whereArgs = new String[] { Long.toString(alarm.getId()) };
        return getWritableDatabase()
                .update(TABLE_NAME, AlarmUtils.toContentValues(alarm), where, whereArgs);
    }

    public void updateStateAlarm(int value) {
        // switch 변경
    }

    public int deleteAlarm(Alarm alarm) {
        return deleteAlarm(alarm.getId());
    }

    int deleteAlarm(long id) {
        final String where = _ID + "=?";
        final String[] whereArgs = new String[] { Long.toString(id) };
        return getWritableDatabase().delete(TABLE_NAME, where, whereArgs);
    }

    public ArrayList<Alarm> getAlarms() {
        Cursor c = null;
        try{
            c = getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
            return AlarmUtils.buildAlarmList(c);
        } finally {
            if (c != null && !c.isClosed()) c.close();
        }
    }

    public Alarm readAlarm(int id){
        Alarm returnAlarm =new Alarm();
        Cursor c = null;
        String[] args = {String.valueOf(id)};
        try{
            c = getReadableDatabase().query(TABLE_NAME, null, "id = ?", args, null, null, null);
            return AlarmUtils.oneAlarmList(c);
        } finally {
            if (c != null && !c.isClosed()) c.close();
        }
    }

    public String[][] getEngWord() {
        Cursor iCursor = null;
        int engWordnum = 0;
        try{
            iCursor = getReadableDatabase().query(ENGLISH_TABLE_NAME, null, null, null, null, null, null);
            engWord = new String[5][2];
            if(iCursor.moveToFirst()) {
                do {
                    int word_group = iCursor.getInt(iCursor.getColumnIndex("Word_Group"));
                    String word_content = iCursor.getString(iCursor.getColumnIndex("Word_Content"));
                    String word_mean = iCursor.getString(iCursor.getColumnIndex("Word_Mean"));
                    if (word_group==1) {
                        engWord[engWordnum][0] = word_content;
                        engWord[engWordnum][1] = word_mean;
                        engWordnum++;
                    }
                }while (iCursor.moveToNext());
            }
            return engWord;
        } finally {
            if (iCursor != null && !iCursor.isClosed()) iCursor.close();
        }
    }

}
