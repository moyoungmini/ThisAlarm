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
    private TextView mNoneTv, mReadTv, mEmotionTv;
    public Dialog mDialog;

    public MissionDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_mission);
        mDialog.show();

        initViews();
    }

    public void initViews(){
        mNoneTv = mDialog.findViewById(R.id.mission_dialog_none_tv);
        mReadTv = mDialog.findViewById(R.id.mission_dialog_word_tv);
        mEmotionTv = mDialog.findViewById(R.id.mission_dialog_emotion_tv);

        mNoneTv.setOnClickListener(this);
        mReadTv.setOnClickListener(this);
        mEmotionTv.setOnClickListener(this);

        if(AddEditAlarmFragment.mission ==0){
            mNoneTv.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
        }
        else if(AddEditAlarmFragment.mission == 2){
            mReadTv.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
        }
        else if(AddEditAlarmFragment.mission ==3){
            mEmotionTv.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mission_dialog_none_tv:
                AddEditAlarmFragment.mission=0;
                mNoneTv.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
                mDialog.dismiss();
                break;
            case R.id.mission_dialog_word_tv:
                AddEditAlarmFragment.mission =2;
                mReadTv.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
                mDialog.dismiss();
                break;
            case R.id.mission_dialog_emotion_tv:
                AddEditAlarmFragment.mission=3;
                mEmotionTv.setTextColor(mContext.getResources().getColorStateList(R.color.fontBlueColor));
                mDialog.dismiss();
                break;
        }
    }


}