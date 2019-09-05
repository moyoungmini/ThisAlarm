package com.makeus.android.thisalarm.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.makeus.android.thisalarm.R;

public class SplashActivity extends AppCompatActivity {

    private Thread splashThread;
    private ImageView mIvLogo;
    private RelativeLayout mLayout;
    private SharedPreferences pref;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mIvLogo = findViewById(R.id.splash_logo_iv);
        mLayout = findViewById(R.id.splash_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_opacity);
        mIvLogo.startAnimation(animation);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        int select = pref.getInt("primaryColor",0);
        if(select ==0){
            mLayout.setBackgroundResource(R.drawable.gradation_primary_color);
        }
        else if(select ==1){
            mLayout.setBackgroundResource(R.drawable.gradation_pink_color);
        }
        else if(select ==2){
            mLayout.setBackgroundResource(R.drawable.gradation_blue_color);
        }

        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        // Wait given period of time or exit on touch
                        wait(2000);
                    }
                } catch (InterruptedException ex) {
                }
                finish();
                overridePendingTransition(R.anim.amin_slide_in_left, R.anim.amin_slide_out_right);

                // Run next activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        };
        splashThread.start();
    }

    public String getVersionInfo(Context context){
        String version = null;
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;
        } catch(PackageManager.NameNotFoundException e) { }
        return version;
    }
}
