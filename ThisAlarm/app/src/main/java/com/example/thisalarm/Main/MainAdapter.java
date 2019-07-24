package com.example.thisalarm.Main;

import android.app.Activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thisalarm.AlarmSet.AlarmSetActivity;
import com.example.thisalarm.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<AlarmData> mData;
    private AlarmData data;

    public MainAdapter(Activity activity, ArrayList<AlarmData> data) {
        this.mActivity = activity;
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mIvEye;
        private TextView mTvClock;
        private Switch mSwitch;
        private LinearLayout mLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            mIvEye = itemView.findViewById(R.id.main_item_list_eye_iv);
            mTvClock = itemView.findViewById(R.id.main_item_list_clock_tv);
            mSwitch = itemView.findViewById(R.id.main_item_list_switch);
            mLayout = itemView.findViewById(R.id.main_item_list_layout);
            //Init views
        }

        @Override
        public void onClick(View v) {
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    //Inflate view


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        data = mData.get(position);
        holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    MainActivity.db.updateState(mData.get(position).getId(), 1);
                }
                else {
                    MainActivity.db.updateState(mData.get(position).getId(), 0);
                }
            }
        });
        //update state switch

        holder.mLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mActivity, AlarmSetActivity.class);
                mIntent.putExtra("id", mData.get(position).getId());
                mIntent.putExtra("hour",mData.get(position).getHour());
                mIntent.putExtra("minute",mData.get(position).getMinute());
                mIntent.putExtra("mission",mData.get(position).getMission());
                mIntent.putExtra("sound",mData.get(position).getSound());
                mIntent.putExtra("vibration",mData.get(position).getVibration());
                mIntent.putExtra("mon",mData.get(position).getMon());
                mIntent.putExtra("tue",mData.get(position).getTue());
                mIntent.putExtra("wen",mData.get(position).getWen());
                mIntent.putExtra("thu",mData.get(position).getThu());
                mIntent.putExtra("fri", mData.get(position).getFri());
                mIntent.putExtra("sat",mData.get(position).getSat());
                mIntent.putExtra("sun",mData.get(position).getSun());
                mIntent.putExtra("state",mData.get(position).getState());
                //Send data to AlarmSetActivity

                mActivity.startActivity(mIntent);
                mActivity.overridePendingTransition(R.anim.amin_slide_in_right, R.anim.amin_slide_out_left);
            }
        });
        // item data listener

        holder.mTvClock.setText(String.valueOf(data.getHour() + " : " + String.valueOf(data.getMinute())));
        boolean tmp = false;
        if(data.getState() == 1){
            tmp = true;
        }
        holder.mSwitch.setChecked(tmp);
        //set item data

        // eye도 tmp 값에 따라 변경

    }
    //Data Binding

    private void removeItemView(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }
    //data remove

}