package com.makeus.android.thisalarm.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeus.android.thisalarm.R;
import com.makeus.android.thisalarm.data.DatabaseHelper;
import com.makeus.android.thisalarm.model.Alarm;
import com.makeus.android.thisalarm.service.AlarmReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SpeechActivity extends AppCompatActivity {
    private Intent intent;
    private SpeechRecognizer mRecognizer;
    private Button sttBtn,passBtn;
    private ImageView dismissBtn,TextToSpeechBtn;
    private TextView textRest,textEngWord,textEngMean;
    private ProgressBar mProgressBar;
    final int PERMISSION = 1;
    final int MAX = 3;
    private TextToSpeech mTTS;
    private String excelload;
    private String[][] engWord;
    private int sw,resultcheck =0;
    private ArrayAdapter<String> arrayAdapter;
    private RelativeLayout mLayout;

    private ArrayList<Boolean> dayList;
    private int mission;
    private boolean sound;
    private  long time;
    private String label;
    private boolean enable;
    public static int value = 100;
    private boolean select;
    private int valueTime;

    Handler handler;
    @Override
    protected void
    onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        textEngWord= (TextView)findViewById(R.id.speech_word_tv);
        textEngMean=(TextView)findViewById(R.id.speech_mean_tv);
        textRest=(TextView)findViewById(R.id.speech_rest_tv);
        mProgressBar = findViewById(R.id.speech_pb);
        textRest.setText(sw+"/"+MAX);
        //textView = (TextView)findViewById(R.id.sttResult);

        sttBtn = (Button) findViewById(R.id.speech_speack_btn);
        passBtn = (Button) findViewById(R.id.speech_pass_btn);
        dismissBtn = (ImageView) findViewById(R.id.speech_dismiss_iv);
        TextToSpeechBtn = (ImageView) findViewById(R.id.speech_texttospeech_iv);
        mLayout = findViewById(R.id.speech_relative_layout);

        select = true;
        value =100;

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SettingActivity.sIntEngTime = pref.getInt("engTime", 50);
        SettingActivity.sIntAppBackground = pref.getInt("primaryColor",0);
        sttBtn.setBackgroundResource(R.drawable.btn_save);
        sttBtn.setTextColor(getResources().getColorStateList(R.drawable.speak_primary_text_set));

        if(SettingActivity.sIntAppBackground ==0){
            mLayout.setBackgroundResource(R.drawable.gradation_primary_non_radiuscolor);
            passBtn.setBackgroundResource(R.drawable.btn_save);
            passBtn.setTextColor(getResources().getColorStateList(R.drawable.save_text_set));
            TextToSpeechBtn.setBackgroundResource(R.drawable.btn_primary_sound);
        }
        else if(SettingActivity.sIntAppBackground ==1){
            mLayout.setBackgroundResource(R.drawable.gradation_pink_non_radiuscolor);
            passBtn.setBackgroundResource(R.drawable.btn_pink);
            passBtn.setTextColor(getResources().getColorStateList(R.drawable.pink_text_color));
            TextToSpeechBtn.setBackgroundResource(R.drawable.btn_pink_sound);
        }
        else if(SettingActivity.sIntAppBackground ==2){
            mLayout.setBackgroundResource(R.drawable.gradation_blue_non_radiuscolor);
            passBtn.setBackgroundResource(R.drawable.btn_blue);
            passBtn.setTextColor(getResources().getColorStateList(R.drawable.blue_text_color));
            TextToSpeechBtn.setBackgroundResource(R.drawable.btn_blue_sound);
        }

        valueTime = SettingActivity.sIntEngTime *10;
        dayList = new ArrayList<>();
        for(int i=0;i<7;i++){
            dayList.add(false);
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (extras.containsKey("mission")) {
                mission = extras.getInt("mission");
            }

            if (extras.containsKey("sound")) {
                sound = extras.getBoolean("sound");
            }

            if (extras.containsKey("enable")) {
                enable = extras.getBoolean("enable");
            }

            if (extras.containsKey("label")) {
                label = extras.getString("label");
            }

            if (extras.containsKey("time")) {
                time = extras.getLong("time");
            }

            if (extras.containsKey("mon")) {
                dayList.set(0,extras.getBoolean("mon"));
            }

            if (extras.containsKey("tue")) {
                dayList.set(1,extras.getBoolean("tue"));
            }

            if (extras.containsKey("wen")) {
                dayList.set(2,extras.getBoolean("wen"));
            }

            if (extras.containsKey("thu")) {
                dayList.set(3,extras.getBoolean("thu"));
            }

            if (extras.containsKey("fri")) {
                dayList.set(4,extras.getBoolean("fri"));
            }

            if (extras.containsKey("sat")) {
                dayList.set(5,extras.getBoolean("sat"));
            }

            if (extras.containsKey("sun")) {
                dayList.set(6,extras.getBoolean("sun"));
            }
        }

        if ( Build.VERSION.SDK_INT >= 23 ){
        // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.
                            RECORD_AUDIO},PERMISSION);
        }

        ExcelRead(); // 영어단어 데이터 가져오기


        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int result = mTTS.setLanguage(Locale.US);
                    if(result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.i("TTS","Language not supported");
                    }else{
                        TextToSpeechBtn.setEnabled(true);
                    }
                }else{
                    Log.i("TTS","Initialization failed");
                }
            }
        });


        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
        sttBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(SpeechActivity.this);
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(intent);
            }
        });

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = false;
                finish();
            }
        });

        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExcelRead();
            }
        });
        TextToSpeechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { 
                textToSpeak();
            }
        });

        handler = new Handler();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() { // Thread 로 작업할 내용을 구현
                while(select) {
                    if(value<=0){
                        select = false;
                        Log.i("vadsfvds","vdsvds");
                        reCallAlarm();
                        finish();
                        break;
                    }
                    value = value - 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() { // 화면에 변경하는 작업을 구현
                            mProgressBar.setProgress(value);
                            Log.i("vadsfvbfbfds","vdsvdbfbfs");
                        }
                    });

                    try {
                        Thread.sleep(valueTime); // 시간지연
                    } catch (InterruptedException e) {    }
                } // end of while
            }
        });
        t.start(); // 쓰레드 시작
    }

    @Override
    protected void onDestroy() {
        if(mTTS != null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    private void textToSpeak() {
        String text =  excelload;
        mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    private RecognitionListener listener = new RecognitionListener() {

        @Override
        public void
        onReadyForSpeech(Bundle params) {
            TextToSpeechBtn.setEnabled(false);
            Toast.
                    makeText(getApplicationContext(),"음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void
        onBeginningOfSpeech() {}

        @Override
        public void
        onRmsChanged(float rmsdB) {}

        @Override
        public void
        onBufferReceived(byte[] buffer) {}

        @Override
        public void
        onEndOfSpeech() {}

        @Override
        public void
        onError(int error) {
            String message;

            switch (error) {

                case SpeechRecognizer.ERROR_AUDIO:
                    message ="오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message ="클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message ="퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message ="네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message ="네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message ="RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message ="서버가 이상함";
                    break;

                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message ="말하는 시간초과";
                    break;
                default:
                    message ="알 수 없는 오류임";
                    break;
            }
            TextToSpeechBtn.setEnabled(true);
            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void
        onResults(Bundle results) {
            TextToSpeechBtn.setEnabled(true);
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.
                            RESULTS_RECOGNITION);
            String result = excelload;
            StringBuilder result_first_UPPER = new StringBuilder(result);
            StringBuilder result_first_ROW = new StringBuilder(result);
            StringBuilder result_all_ROW = new StringBuilder(result);
            result_first_UPPER.setCharAt(0, (char)(result.charAt(0)-32));//앞글자 대문자로 바꾸기
            result_first_ROW.setCharAt(0, (char)(result.charAt(0)+32));//앞글자 대문자를 소문자로 바꾸기
            for(int i = 0; i<result.length();i++){//모든 대문자를 소문자로
                result_all_ROW.setCharAt(i, (char)(result.charAt(i)-32));
            }
            Log.i("Testdsafsdv",excelload);
            Log.i("Testdsafsdv", String.valueOf(result_first_UPPER));
            Log.i("Testdsafsdv", String.valueOf(sw));
            resultcheck=0;
            for(int i = 0; i < matches.size() ; i++) {
                Log.i("Testdsafsdv",matches.get(i));
                if (matches.get(i).equals(excelload) || matches.get(i).equals(String.valueOf(result_first_UPPER))
                        ||matches.get(i).equals(String.valueOf(result_first_ROW))||matches.get(i).equals(String.valueOf(result_all_ROW))) {
                    resultcheck++;
                    sw++;
                    if(sw == MAX){
                        select = false;
                        finish();
                    }else{
                        ExcelRead();
                        textRest.setText(sw+"/"+MAX);
                    }
                    break;
                } else {
                }
            }
            if(resultcheck>0){

            }else{
                showResultDialog();
            }
        }

        @Override
        public void
        onPartialResults(Bundle partialResults) {}

        @Override
        public void
        onEvent(int eventType, Bundle params) {}
    };

    private void showResultDialog() {
        showDialogThird(SpeechActivity.this,"Third Custom Dialog");
    }
    public void showDialogThird(SpeechActivity activity, String msg){

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        dialog.show();
    }

    public void ExcelRead(){
        int randomnumber = (int) (Math.random()*800);
        Log.i("randomnumber 랜덤생성수 ", String.valueOf(randomnumber));
        Workbook workbook = null;
        Sheet sheet = null;
        try {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("Word_TB.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);
            int MaxColumn = 2, RowStart = 0, RowEnd = sheet.getColumn(MaxColumn - 1).length -1, ColumnStart = 0, ColumnEnd = sheet.getRow(2).length - 1;
            excelload = sheet.getCell(1, randomnumber).getContents();
            String excelload_mean = sheet.getCell(2, randomnumber).getContents();

            textEngWord.setText(excelload);
            textEngMean.setText(": "+excelload_mean);
            Log.i("가져온 단어", String.valueOf(excelload));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }

    }

    public void reCallAlarm() {
        final Alarm alarm = new Alarm();
        final Calendar c = Calendar.getInstance();
        time = System.currentTimeMillis();
        c.setTimeInMillis(time);

        int minutes = c.get(Calendar.MINUTE);
        final int hours = c.get(Calendar.HOUR_OF_DAY);
        final int second = c.get(Calendar.SECOND);

        if(minutes<55) {
            minutes +=5;
        }
        else {
            minutes = minutes - 55;
        }

        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.SECOND, second);
        //SECOND설정

        alarm.setTime(c.getTimeInMillis());
        alarm.setLabel(label);
        alarm.setDay(Alarm.MON, dayList.get(0));
        alarm.setDay(Alarm.TUES, dayList.get(1));
        alarm.setDay(Alarm.WED, dayList.get(2));
        alarm.setDay(Alarm.THURS, dayList.get(3));
        alarm.setDay(Alarm.FRI, dayList.get(4));
        alarm.setDay(Alarm.SAT, dayList.get(5));
        alarm.setDay(Alarm.SUN, dayList.get(6));
        alarm.setMission(mission);
        alarm.setSound(sound);
        alarm.setIsEnabled(enable);

        DatabaseHelper.getInstance(this).updateAlarm(alarm);
        AlarmReceiver.setReminderAlarm(this, alarm);
    }

}
