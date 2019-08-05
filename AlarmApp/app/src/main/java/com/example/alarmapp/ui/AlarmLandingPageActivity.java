package com.example.alarmapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.alarmapp.R;

public final class AlarmLandingPageActivity extends AppCompatActivity {
    public static Activity AlarmLandingPageActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AlarmLandingPageActivity = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_landing_page);

    }

    public static Intent launchIntent(Context context) {
        final Intent i = new Intent(context, AlarmLandingPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK :
// 여기에 뒤로가기 버튼을 눌렀을 때 행동 입력
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN :
// 여기에 볼륨 ↓ 버튼을 눌렀을 때 행동 입력
                break;
            case KeyEvent.KEYCODE_VOLUME_UP :
// 여기에 볼륨 ↑ 버튼을 눌렀을 때 행동 입력
                break;
            case KeyEvent.KEYCODE_HOME :
// 여기에 홈 버튼을 눌렀을 때 행동 입력
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
