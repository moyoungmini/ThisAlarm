package com.makeus.android.thisalarm.ui;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import com.makeus.android.thisalarm.R;
import net.cachapa.expandablelayout.ExpandableLayout;
import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    public static int sIntAppBackground; // 0:primary 1:pink 2:blue
    public static int sIntEngMissionLevel;
    public static int sIntEngMissionNum;
    public static int sIntEngTime;
    public static int sIntEmotionTime;
    public static boolean sIntEarphnoe;
    private ImageView mIvPrimary, mIvBlue, mIvPink, mIvEngBottom, mIvEmotionBottom;
    private LinearLayout mLayoutEngExpanding, mLayoutEmotionExpanding, mLayoutEntire;
    private Switch mSwitchEarphone;
    private Spinner mSpinnerEngLevel, mSpinnerEngNum, mSpinnerEngTime, mSpinnerEmotionTime;
    private Animation slideDownEng, slideUpEng, slideUpEmotion, slideDownEmotion;
    private boolean mIsEngExpanding, mIsEmotionExpanding;
    private ArrayList mListEngLevel;
    private ArrayList mListEngTime;
    private  ArrayList mListEngNum;
    private  ArrayList mListEmotionTime;
    private ArrayAdapter<String> mAdapterEngLevel, mAdapterEngTime, mAdapterEngNum, mAdapterEmotionTime;
    private int[][] states;
    private int[] intSwitchPrimaryOval, intSiwtchPrimaryBackground, intSwitchPinkOval, intSiwtchPinkBackground, intSwitchBlueOval, intSiwtchBlueBackground;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ExpandableLayout mEngExpandableLayout, mFaceExpandableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        //Float screen

        initviews();

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();
        SettingActivity.sIntAppBackground = pref.getInt("primaryColor",0);

        mIsEmotionExpanding = false;
        mIsEngExpanding = false;
        mLayoutEmotionExpanding.setVisibility(View.GONE);
        mLayoutEngExpanding.setVisibility(View.GONE);
        //Init value

        slideDownEng = AnimationUtils.loadAnimation(this, R.anim.anim_silide_down);
        slideDownEmotion = AnimationUtils.loadAnimation(this, R.anim.anim_silide_down);
        slideUpEng = AnimationUtils.loadAnimation(this, R.anim.anim_silide_up);
        slideUpEmotion = AnimationUtils.loadAnimation(this, R.anim.anim_silide_up);

        slideUpEng.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // additional functionality
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // additional functionality
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutEngExpanding.setVisibility(View.GONE);
                // additional functionality
            }
        });
        slideUpEmotion.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // additional functionality
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // additional functionality
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutEmotionExpanding.setVisibility(View.GONE);
                // additional functionality
            }
        });
        //Set animation

        mListEngLevel = new ArrayList<>();
        mListEngLevel.add("Lv.1");
        mListEngLevel.add("Lv.2");
        mListEngLevel.add("Lv.3");

        mListEngNum = new ArrayList<>();
        mListEngNum.add("3개");
        mListEngNum.add("4개");
        mListEngNum.add("5개");

        mListEngTime = new ArrayList<>();
        mListEngTime.add("50초");
        mListEngTime.add("60초");
        mListEngTime.add("70초");

        mListEmotionTime = new ArrayList<>();
        mListEmotionTime.add("50초");
        mListEmotionTime.add("60초");
        mListEmotionTime.add("70초");

        mAdapterEngLevel = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mListEngLevel);
        mSpinnerEngLevel.setAdapter(mAdapterEngLevel);

        mAdapterEngTime = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mListEngTime);
        mSpinnerEngTime.setAdapter(mAdapterEngTime);

        mAdapterEngNum = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mListEngNum);
        mSpinnerEngNum.setAdapter(mAdapterEngNum);

        mAdapterEmotionTime = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mListEmotionTime);
        mSpinnerEmotionTime.setAdapter(mAdapterEmotionTime);

        mSpinnerEngLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sIntEngMissionLevel=1;
                }
                else if(i==1){
                    sIntEngMissionLevel=2;
                }
                else if(i==2){
                    sIntEngMissionLevel=3;
                }

                editor.putInt("engLevel", sIntEngMissionLevel);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpinnerEngNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sIntEngMissionNum=3;
                }
                else if(i==1){
                    sIntEngMissionNum=4;
                }
                else if(i==2){
                    sIntEngMissionNum=5;
                }

                editor.putInt("engNum", sIntEngMissionNum);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpinnerEngTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sIntEngTime=50;
                }
                else if(i==1){
                    sIntEngTime=60;
                }
                else if(i==2){
                    sIntEngTime=70;
                }
                editor.putInt("engTime",sIntEngTime);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpinnerEmotionTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sIntEmotionTime=50;
                }
                else if(i==1){
                    sIntEmotionTime=60;
                }
                else if(i==2){
                    sIntEmotionTime=70;
                }
                editor.putInt("emotionTime",sIntEmotionTime);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // Set spinner

        states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked},
        };
        intSwitchPrimaryOval = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#6b85e3")
        };
        intSiwtchPrimaryBackground = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#6b85e3")
        };
        intSwitchPinkOval = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#eda2a2")
        };
        intSiwtchPinkBackground = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#eda2a2")
        };
        intSwitchBlueOval = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#66b4d3")
        };
        intSiwtchBlueBackground = new int[] {
                Color.parseColor("#cfcfcf"),
                Color.parseColor("#66b4d3")
        };
        // Set switch

        primaryColorChange();
    }

    public void primaryColorChange() {
        if(SettingActivity.sIntAppBackground ==0){
            mLayoutEntire.setBackgroundResource(R.drawable.gradation_primary_non_radiuscolor);
            removeItemBackground();
            mIvPrimary.setBackgroundResource(R.drawable.oval_primar_press);

            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitchEarphone.getThumbDrawable()), new ColorStateList(states, intSwitchPrimaryOval));
            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitchEarphone.getTrackDrawable()), new ColorStateList(states, intSiwtchPrimaryBackground));

            mIvEmotionBottom.setBackgroundResource(R.drawable.setting_bottom_img);
            mIvEngBottom.setBackgroundResource(R.drawable.setting_bottom_img);
        }
        else if(SettingActivity.sIntAppBackground ==1){
            mLayoutEntire.setBackgroundResource(R.drawable.gradation_pink_non_radiuscolor);
            removeItemBackground();
            mIvPink.setBackgroundResource(R.drawable.oval_pink_press);

            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitchEarphone.getThumbDrawable()), new ColorStateList(states, intSwitchPinkOval));
            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitchEarphone.getTrackDrawable()), new ColorStateList(states, intSiwtchPinkBackground));

            mIvEmotionBottom.setBackgroundResource(R.drawable.setting_pink_bottom_img);
            mIvEngBottom.setBackgroundResource(R.drawable.setting_pink_bottom_img);
        }
        else if(SettingActivity.sIntAppBackground ==2){
            mLayoutEntire.setBackgroundResource(R.drawable.gradation_blue_non_radiuscolor);
            removeItemBackground();
            mIvBlue.setBackgroundResource(R.drawable.oval_blue_press);

            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitchEarphone.getThumbDrawable()), new ColorStateList(states, intSwitchBlueOval));
            DrawableCompat.setTintList(DrawableCompat.wrap(mSwitchEarphone.getTrackDrawable()), new ColorStateList(states, intSiwtchBlueBackground));

            mIvEmotionBottom.setBackgroundResource(R.drawable.setting_blue_bottom_img);
            mIvEngBottom.setBackgroundResource(R.drawable.setting_blue_bottom_img);
        }
    }
    // Set app color according to color in setting acitivty

    public void initviews() {
        mIvPrimary = findViewById(R.id.setting_primary_iv);
        mIvBlue = findViewById(R.id.setting_blue_iv);
        mIvPink = findViewById(R.id.setting_pink_iv);
        mLayoutEngExpanding = findViewById(R.id.setting_eng_read_expanding_layout);
        mLayoutEmotionExpanding = findViewById(R.id.setting_emotion_expanding_layout);
        mLayoutEntire = findViewById(R.id.setting_entire_layout);
        mSwitchEarphone = findViewById(R.id.setting_ear_sound_switch);
        mSpinnerEngLevel = findViewById(R.id.setting_eng_level_spinner);
        mSpinnerEngNum = findViewById(R.id.setting_eng_num_spinner);
        mSpinnerEngTime = findViewById(R.id.setting_eng_time_spinner);
        mSpinnerEmotionTime = findViewById(R.id.setting_emotion_time_spinner);
        mIvEngBottom = findViewById(R.id.setting_eng_read_bottom_iv);
        mIvEmotionBottom = findViewById(R.id.setting_emotion_bottom_iv);
        mEngExpandableLayout = findViewById(R.id.setting_eng_expandable_layout);
        mFaceExpandableLayout = findViewById(R.id.setting_face_expandable_layout);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.setting_exit_iv:
                finish();
                break;
            case R.id.setting_primary_iv:
                sIntAppBackground = 0;
                primaryColorChange();
                editor.putInt("primaryColor",0);
                editor.commit();
                //setting_bottom_img
                break;
            case R.id.setting_pink_iv:
                sIntAppBackground = 1;
                primaryColorChange();
                editor.putInt("primaryColor",1);
                editor.commit();
                break;
            case R.id.setting_blue_iv:
                sIntAppBackground =2;
                primaryColorChange();
                editor.putInt("primaryColor",2);
                editor.commit();
                break;
            case R.id.setting_eng_read_overlap_layout:
                if(!mIsEngExpanding) {
                    mEngExpandableLayout.expand();
                    mLayoutEngExpanding.setVisibility(View.VISIBLE);
                }
                else {
                    mEngExpandableLayout.collapse();
                }
                mIsEngExpanding = !mIsEngExpanding;
                break;
            case R.id.setting_emotion_overlap_layout:
                if(!mIsEmotionExpanding) {
                    mFaceExpandableLayout.expand();
                    mLayoutEmotionExpanding.setVisibility(View.VISIBLE);
                }
                else {
                    mFaceExpandableLayout.collapse();
                }
                mIsEmotionExpanding = !mIsEmotionExpanding;
                break;
        }
    }

    public void removeItemBackground() {
        mIvPrimary.setBackgroundResource(R.drawable.oval_primary);
        mIvBlue.setBackgroundResource(R.drawable.oval_blue_img);
        mIvPink.setBackgroundResource(R.drawable.oval_pink_img);
    }
}