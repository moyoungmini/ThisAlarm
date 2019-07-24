package com.example.thisalarm.Model;

public class AlarmData {
    private int id;
    private int hour;
    private int minute;
    private String mission;
    private String sound;
    private int vibration;
    private int mon;
    private int tue;
    private int wen;
    private int thu;
    private int fri;
    private int sat;
    private int sun;
    private int state;

    public void setId(int id) {
        this.id = id;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setVibration(int vibration) {
        this.vibration = vibration;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public void setTue(int tue) {
        this.tue = tue;
    }

    public void setWen(int wen) {
        this.wen = wen;
    }

    public void setThu(int thu) {
        this.thu = thu;
    }

    public void setFri(int fri) {
        this.fri = fri;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public void setSun(int sun) {
        this.sun = sun;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getMission() {
        return mission;
    }

    public String getSound() {
        return sound;
    }

    public int getVibration() {
        return vibration;
    }

    public int getMon() {
        return mon;
    }

    public int getTue() {
        return tue;
    }

    public int getWen() {
        return wen;
    }

    public int getThu() {
        return thu;
    }

    public int getFri() {
        return fri;
    }

    public int getSat() {
        return sat;
    }

    public int getSun() {
        return sun;
    }

    public int getState() {
        return state;
    }

    public AlarmData(int id, int hour, int minute, String mission, String sound, int vibration, int mon, int tue, int wen, int thu, int fri, int sat, int sun, int state) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.mission = mission;
        this.sound = sound;
        this.vibration = vibration;
        this.mon = mon;
        this.tue = tue;
        this.wen = wen;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
        this.state = state;
    }

    public AlarmData() {
        id = -100;
        hour =0;
        minute =0;
        mission = "";
        sound = "OFF";
        vibration = 0;
        mon =0;
        tue =0;
        wen =0;
        thu =0;
        fri =0;
        sat =0;
        sun =0;
        state = 0;
    }
}
