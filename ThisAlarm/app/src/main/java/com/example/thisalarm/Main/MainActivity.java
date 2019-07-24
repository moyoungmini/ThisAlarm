package com.example.thisalarm.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.example.thisalarm.AlarmDB;
import com.example.thisalarm.AlarmSet.AlarmSetActivity;
import com.example.thisalarm.R;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static RecyclerView mRecyclerView;
    public static LinearLayoutManager linearLayoutManager;
    public static ArrayList<AlarmData> mData = new ArrayList<>();
    public static MainAdapter mRecyclerViewAdapter;
    public static Activity activity;
    public static AlarmDB db;
    private long time=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity=this;

        db = new AlarmDB(this, "ThisAlarm.db", null, 1);
        //SQLite DB Create

        initViews();

        db.readAlarmList();
        //Read Database alarm table

        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerViewAdapter = new MainAdapter(this, mData);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        //Recyclerview set


        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        //Recycler view Divider Line
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.main_plus_btn) {
            Intent mIntent = new Intent(this, AlarmSetActivity.class);
            this.startActivity(mIntent);
            this.overridePendingTransition(R.anim.amin_slide_in_right, R.anim.amin_slide_out_left);
        }
    }

    public void initViews(){
        mRecyclerView = findViewById(R.id.main_activity_rv);
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
            else {
                ActivityCompat.finishAffinity(this);
                System.runFinalization();
                System.exit(0);
            }
        }
    }
}
