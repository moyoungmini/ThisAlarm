package com.example.alarmapp.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alarmapp.R;
import com.example.alarmapp.data.DatabaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SpeechActivity extends AppCompatActivity {
    Intent intent;
    SpeechRecognizer mRecognizer;
    Button sttBtn,passBtn;
    ImageView dismissBtn,TextToSpeechBtn;
    TextView textRest,textEngWord,textEngMean;
    final int PERMISSION = 1;
    final int MAX = 3;

    private TextToSpeech mTTS;
    String excelload;
    String[][] engWord;
    int sw,resultcheck =0;

    ArrayAdapter<String> arrayAdapter;
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

        textEngWord= (TextView)findViewById(R.id.speech_word_tv);
        textEngMean=(TextView)findViewById(R.id.speech_mean_tv);
        textRest=(TextView)findViewById(R.id.speech_rest_tv);
        textRest.setText(sw+"/"+MAX);
        //textView = (TextView)findViewById(R.id.sttResult);

        sttBtn = (Button) findViewById(R.id.speech_speack_btn);
        passBtn = (Button) findViewById(R.id.speech_pass_btn);
        dismissBtn = (ImageView) findViewById(R.id.speech_dismiss_iv);
        TextToSpeechBtn = (ImageView) findViewById(R.id.speech_texttospeech_iv);
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
            StringBuilder myName = new StringBuilder(result);
            myName.setCharAt(0, (char)(result.charAt(0)-32));//앞글자 대분자로 바꾸기
            Log.i("Testdsafsdv",excelload);
            Log.i("Testdsafsdv", String.valueOf(myName));
            Log.i("Testdsafsdv", String.valueOf(sw));
            resultcheck=0;
            for(int i = 0; i < matches.size() ; i++) {
                Log.i("Testdsafsdv",matches.get(i));
                if (matches.get(i).equals(excelload) || matches.get(i).equals(String.valueOf(myName))) {
                    resultcheck++;
                    sw++;
                    if(sw == MAX){
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
        dialog.setContentView(R.layout.dialog_speech);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog_1);
        RelativeLayout relativeLayout = (RelativeLayout) dialog.findViewById(R.id.relative_rayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
            textEngMean.setText(excelload_mean);
            Log.i("가져온 단어", String.valueOf(excelload));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }

    }

}