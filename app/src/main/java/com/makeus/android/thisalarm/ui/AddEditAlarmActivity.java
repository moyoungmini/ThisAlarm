package com.makeus.android.thisalarm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import com.makeus.android.thisalarm.R;
import com.makeus.android.thisalarm.data.DatabaseHelper;
import com.makeus.android.thisalarm.model.Alarm;
import com.makeus.android.thisalarm.service.LoadAlarmsService;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class AddEditAlarmActivity extends AppCompatActivity {
    public static final String ALARM_EXTRA = "alarm_extra";
    public static final String MODE_EXTRA = "mode_extra";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({EDIT_ALARM,ADD_ALARM,UNKNOWN})
    @interface Mode{}
    public static final int EDIT_ALARM = 1;
    public static final int ADD_ALARM = 2;
    public static final int UNKNOWN = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        final Alarm alarm = getAlarm();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        // full secreen including status bar and HW key

        if(getSupportFragmentManager().findFragmentById(R.id.edit_alarm_frag_container) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.edit_alarm_frag_container, AddEditAlarmFragment.newInstance(alarm))
                    .commit();
        }
        // Set fragment
    }

    private Alarm getAlarm() {
        switch (getMode()) {
            case EDIT_ALARM:
                return getIntent().getParcelableExtra(ALARM_EXTRA);
            case ADD_ALARM:
                final long id = DatabaseHelper.getInstance(this).addAlarm();
                LoadAlarmsService.launchLoadAlarmsService(this);
                return new Alarm(id);
            case UNKNOWN:
            default:
                throw new IllegalStateException("Mode supplied as intent extra for " +
                        AddEditAlarmActivity.class.getSimpleName() + " must match value in " +
                        Mode.class.getSimpleName());
        }
    }
    // Get alarm data

    private @Mode int getMode() {
        final @Mode int mode = getIntent().getIntExtra(MODE_EXTRA, UNKNOWN);
        return mode;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent buildAddEditAlarmActivityIntent(Context context, @Mode int mode) {
        final Intent i = new Intent(context, AddEditAlarmActivity.class);
        i.putExtra(MODE_EXTRA, mode);
        return i;
    }
}