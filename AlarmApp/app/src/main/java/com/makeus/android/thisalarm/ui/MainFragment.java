package com.makeus.android.thisalarm.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.makeus.android.thisalarm.R;
import com.makeus.android.thisalarm.adapter.AlarmsAdapter;
import com.makeus.android.thisalarm.listener.ItemTouchHelperCallback;
import com.makeus.android.thisalarm.model.Alarm;
import com.makeus.android.thisalarm.service.LoadAlarmsReceiver;
import com.makeus.android.thisalarm.service.LoadAlarmsService;
import com.makeus.android.thisalarm.util.AlarmUtils;
import com.makeus.android.thisalarm.view.EmptyRecyclerView;

import java.util.ArrayList;

public final class MainFragment extends Fragment
        implements LoadAlarmsReceiver.OnAlarmsLoadedListener {

    private LoadAlarmsReceiver mReceiver;
    private AlarmsAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new LoadAlarmsReceiver(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_main, container, false);

        final EmptyRecyclerView rv = (EmptyRecyclerView) v.findViewById(R.id.main_fragment_rv);
        final Button btnUser =  v.findViewById(R.id.main_fragment_user_iv);
        mAdapter = new AlarmsAdapter();
        rv.setEmptyView(v.findViewById(R.id.empty_view));
        rv.setAdapter(mAdapter);
        rv.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        //Recyclerview set

        final ImageView btnPlus = (ImageView) v.findViewById(R.id.main_fragment_plus_iv);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlarmUtils.checkAlarmPermissions(getActivity());
                final Intent i =
                        AddEditAlarmActivity.buildAddEditAlarmActivityIntent(
                                getContext(), AddEditAlarmActivity.ADD_ALARM
                        );
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.amin_slide_in_right, R.anim.amin_slide_out_left);
            }
        });
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent (Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSeG0wDmvFG5qX9WEeviMiEzKJeFPd9VAvOc-f2JDvcrqoydLg/viewform"));
                startActivity(intent);
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter(LoadAlarmsService.ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
        LoadAlarmsService.launchLoadAlarmsService(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onAlarmsLoaded(ArrayList<Alarm> alarms) {
        mAdapter.setAlarms(alarms);
    }

}
