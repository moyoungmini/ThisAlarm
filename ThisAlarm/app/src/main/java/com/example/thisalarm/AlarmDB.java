package com.example.thisalarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thisalarm.Main.AlarmData;
import com.example.thisalarm.Main.MainActivity;

import java.util.ArrayList;

public class AlarmDB extends SQLiteOpenHelper {

    public AlarmDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ALARM (ID INTEGER PRIMARY KEY, HOUR INTEGER, MINUTE INTEGER, MISSION TEXT, SOUND TEXT, " +
                "VIBRATION INTEGER, MON INTEGER, TUE INTEGER, WEN INTEGER, THU INTEGER, FRI INTEGER, SAT INTEGER, SUN INTEGER, STATE INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insert(int hour, int minute, String mission, String sound, int vibration, int mon, int tue, int wen, int thu, int fri, int sat, int sun, int state) {
        int id = getRecentID();
        id++;
        //get max id + 1

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO ALARM VALUES( "+id +", " + hour + ", " + minute + ", '" + mission +"', '"+sound+"', "+vibration+", "+mon +", "+tue +", "+wen +", "+thu +", "+fri +", "+sat+", "+sun +", "+state +");");
        db.close();
    }

    public void update(int id, int hour, int minute, String mission, String sound, int vibration, int mon, int tue, int wen, int thu, int fri, int sat, int sun, int state) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE ALARM SET hour="+ hour + ", MINUTE = " + minute + ", MISSION = '" + mission +"', SOUND = '"+sound+"', VIBRATION = "+vibration+", MON = "+mon +", TUE = "+tue +", WEN = "+wen +", THU = "+thu +", FRI = "+fri +", SAT = "+sat+", SUN = "+sun +", STATE = "+state + " WHERE ID=" + id + ";");
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM ALARM WHERE ID=" + id + ";");
        db.close();
    }

    public void readAlarmList() {
        SQLiteDatabase db = getReadableDatabase();
        MainActivity.mData = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM ALARM", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int hour = cursor.getInt(1);
            int minute = cursor.getInt(2);
            String mission = cursor.getString(3);
            String sound = cursor.getString(4);
            int vibration = cursor.getInt(5);
            int mon = cursor.getInt(6);
            int tue = cursor.getInt(7);
            int wen = cursor.getInt(8);
            int thu = cursor.getInt(9);
            int fri = cursor.getInt(10);
            int sat = cursor.getInt(11);
            int sun = cursor.getInt(12);
            int state = cursor.getInt(13);
            MainActivity.mData.add(new AlarmData(id,hour,minute,mission,sound,vibration, mon, tue, wen, thu, fri, sat, sun, state));
        }
    }
    // Read Alarm DB

    public int getRecentID() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(ID) FROM ALARM", null);

        cursor.moveToNext();
        if(cursor.getCount() ==0) {
            return -1;
        }
        // empty -> return -1
        return cursor.getInt(0);
    }

    public void updateState(int id, int state) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE ALARM SET  STATE = "+state + " WHERE ID=" + id + ";");
        db.close();
    }
    //Read MAX ID of Alarm Table

}

