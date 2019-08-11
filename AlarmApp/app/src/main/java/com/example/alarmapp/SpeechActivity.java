package com.example.alarmapp;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alarmapp.data.DatabaseHelper;
import com.example.alarmapp.model.Alarm;

import java.util.ArrayList;

public class SpeechActivity extends AppCompatActivity {
    Intent intent;
    SpeechRecognizer mRecognizer;
    Button sttBtn;
    TextView textView,textMatch,textEngWord,textEngMean;
    final int PERMISSION = 1;

    String[][] engWord;
    int sw =0;
    @Override
    protected void
    onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        if ( Build.VERSION.SDK_INT >= 23 ){
// 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.
                            RECORD_AUDIO},PERMISSION);
        }

        textEngWord= (TextView)findViewById(R.id.sttEngWord);
        textEngMean=(TextView)findViewById(R.id.sttEngMean);
        textView = (TextView)findViewById(R.id.sttResult);
        textMatch = (TextView)findViewById(R.id.maching);

        engWord = new String[5][2];
        engWord =  DatabaseHelper.getInstance(this).getEngWord().clone();
        textEngWord.setText(engWord[sw][0]);
        textEngMean.setText(engWord[sw][1]);
        sttBtn = (Button) findViewById(R.id.sttStart);

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
    }

    private RecognitionListener listener = new RecognitionListener() {

        @Override
        public void
        onReadyForSpeech(Bundle params) {
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
            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void
        onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.
                            RESULTS_RECOGNITION);

            for(int i = 0; i < matches.size() ; i++){
                textView.setText(matches.get(i));

            }


            String result = engWord[sw][0];
            StringBuilder myName = new StringBuilder(result);
            myName.setCharAt(0, (char)(result.charAt(0)-32));
            Log.i("Testdsafsdv",engWord[sw][0]);
            Log.i("Testdsafsdv", String.valueOf(myName));

            for(int i = 0; i < matches.size() ; i++) {
                Log.i("Testdsafsdv",matches.get(i));

                if (matches.get(i).equals(engWord[sw][0]) || matches.get(i).equals(String.valueOf(myName))) {

                    textMatch.setText("성공");
                    sw++;
                    if(sw == 5){
                        finish();
                    }
                    textEngWord.setText(engWord[sw][0]);
                    textEngMean.setText(engWord[sw][1]);

                    break;

                } else {
                    textMatch.setText("실패! 다시 시도해 보세요!");

                }
            }
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    //여기에 딜레이 후 시작할 작업들을 입력
                }
            }, 2000);// 2초 정도 딜레이를 준 후 시작
            if(sw==5){
                /*Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
                finish();
            }else{

            }

        }

        @Override
        public void
        onPartialResults(Bundle partialResults) {}

        @Override
        public void
        onEvent(int eventType, Bundle params) {}
    };
}
