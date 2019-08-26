package com.makeus.android.thisalarm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.makeus.android.thisalarm.R;
import com.makeus.android.thisalarm.ui.AddEditAlarmFragment;

public class MissionDialog implements View.OnClickListener{
    private Context mContext;
    private TextView mTvNone, mTvRead, mTvEmotion;
    public Dialog mDialog;

    public MissionDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_mission);
        mDialog.show();
        //Set dialog information

        initViews();
    }

    public void initViews(){
        mTvNone = mDialog.findViewById(R.id.mission_dialog_none_tv);
        mTvRead = mDialog.findViewById(R.id.mission_dialog_word_tv);
        mTvEmotion = mDialog.findViewById(R.id.mission_dialog_emotion_tv);

        mTvNone.setOnClickListener(this);
        mTvRead.setOnClickListener(this);
        mTvEmotion.setOnClickListener(this);

        if(AddEditAlarmFragment.mission ==0){
            mTvNone.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
        }
        else if(AddEditAlarmFragment.mission == 2){
            mTvRead.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
        }
        else if(AddEditAlarmFragment.mission ==3){
            mTvEmotion.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
        }
    }
    //Set init

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mission_dialog_none_tv:
                AddEditAlarmFragment.mission=0;
                mTvNone.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
                mDialog.dismiss();
                break;
            case R.id.mission_dialog_word_tv:
                AddEditAlarmFragment.mission =2;
                mTvRead.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
                mDialog.dismiss();
                break;
            case R.id.mission_dialog_emotion_tv:
                AddEditAlarmFragment.mission=3;
                mTvEmotion.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
                mDialog.dismiss();
                break;
        }
    }
    //Set mission click event
}