package com.example.thisalarm.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.thisalarm.R;

public class MissionSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_set);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);
    }
}