package com.makeus.android.thisalarm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.makeus.android.thisalarm.model.Alarm;
import java.util.ArrayList;

public final class LoadAlarmsReceiver extends BroadcastReceiver {
    private OnAlarmsLoadedListener mListener;

    @SuppressWarnings("unused")
    public LoadAlarmsReceiver(){}

    public LoadAlarmsReceiver(OnAlarmsLoadedListener listener){
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final ArrayList<Alarm> alarms =
                intent.getParcelableArrayListExtra(LoadAlarmsService.ALARMS_EXTRA);
        mListener.onAlarmsLoaded(alarms);

        Log.i("bdbf","bfbf");
    }

    public void setOnAlarmsLoadedListener(OnAlarmsLoadedListener listener) {
        mListener = listener;
    }

    public interface OnAlarmsLoadedListener {
        void onAlarmsLoaded(ArrayList<Alarm> alarms);
    }

}
