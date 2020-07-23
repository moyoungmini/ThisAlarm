package com.makeus.android.thisalarm.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import static android.content.Context.MODE_PRIVATE;

public final class MainFragment extends Fragment implements LoadAlarmsReceiver.OnAlarmsLoadedListener {

    private LoadAlarmsReceiver mReceiver;
    private AlarmsAdapter mAdapter;
    private View v;
    private ImageView btnPlus, ivSetting;
    private EmptyRecyclerView rv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new LoadAlarmsReceiver(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences pref= getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        SettingActivity.sIntAppBackground = pref.getInt("primaryColor",0);
        SettingActivity.sIntEngTime = pref.getInt("engNum",0);
        if(SettingActivity.sIntAppBackground ==0){
            btnPlus.setBackgroundResource(R.drawable.primary_saved_background);
            ivSetting.setBackgroundResource(R.drawable.btn_setting_primary);
        }
        else if(SettingActivity.sIntAppBackground ==1){
            btnPlus.setBackgroundResource(R.drawable.pink_saved_background);
            ivSetting.setBackgroundResource(R.drawable.btn_setting_pink);
        }
        else if(SettingActivity.sIntAppBackground ==2){
            btnPlus.setBackgroundResource(R.drawable.blue_saved_background);
            ivSetting.setBackgroundResource(R.drawable.btn_setting_blue);
        }
        // Set app color according to app primay color
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);

        rv = (EmptyRecyclerView) v.findViewById(R.id.main_fragment_rv);
        mAdapter = new AlarmsAdapter();
        rv.setEmptyView(v.findViewById(R.id.empty_view));
        rv.setAdapter(mAdapter);
        rv.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        //Recyclerview set

        btnPlus = v.findViewById(R.id.main_fragment_plus_iv);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlarmUtils.checkAlarmPermissions(getActivity());
                final Intent i =
                        AddEditAlarmActivity.buildAddEditAlarmActivityIntent(
                                getContext(), AddEditAlarmActivity.ADD_ALARM
                        );
                startActivity(i);
            }
        });

        ivSetting = (ImageView) v.findViewById(R.id.main_fragment_setting_iv);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        //register button listener

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
