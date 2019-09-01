package com.makeus.android.thisalarm.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.makeus.android.thisalarm.R;

public class SettingActivity extends AppCompatActivity {

    public static int engMissionLevel;
    public static int engMissionNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        engMissionLevel =1;
        engMissionNum =3;

        

    }

}
