package com.makeus.android.thisalarm.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.face.Face;
import com.makeus.android.thisalarm.R;
import com.makeus.android.thisalarm.camera.GraphicOverlay;

public class MissionSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mWeather,mSpeech,mEmotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_select);

        mWeather = findViewById(R.id.text_weather);
        mSpeech = findViewById(R.id.text_speech);
        mEmotion = findViewById(R.id.text_emotion);

        mWeather.setOnClickListener(this);
        mSpeech.setOnClickListener(this);
        mEmotion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_weather:
                if(AddEditAlarmFragment.mission != 1){
                    AddEditAlarmFragment.mission =1;
                    mWeather.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    mSpeech.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    mEmotion.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                }
                else {
                    mWeather.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    AddEditAlarmFragment.mission = 0;
                }
                break;
            case R.id.text_speech:
                if(AddEditAlarmFragment.mission != 2){
                    AddEditAlarmFragment.mission =2;
                    mSpeech.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    mWeather.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    mEmotion.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                }
                else {
                    mSpeech.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    AddEditAlarmFragment.mission = 0;
                }
                break;
            case R.id.text_emotion:
                if(AddEditAlarmFragment.mission != 3){
                    AddEditAlarmFragment.mission =3;
                    mEmotion.setTextColor(this.getResources().getColorStateList(R.color.yellow));
                    mWeather.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    mSpeech.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                }
                else {
                    mEmotion.setTextColor(this.getResources().getColorStateList(R.color.fontColor));
                    AddEditAlarmFragment.mission = 0;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }

    private void save() {
    }

    /**
     * Graphic instance for rendering face position, orientation, and landmarks within an associated
     * graphic overlay view.
     */
    static class FaceGraphic extends GraphicOverlay.Graphic {
        private static final float FACE_POSITION_RADIUS = 10.0f;
        private static final float ID_TEXT_SIZE = 40.0f;
        private static final float ID_Y_OFFSET = 50.0f;
        private static final float ID_X_OFFSET = -50.0f;
        private static final float BOX_STROKE_WIDTH = 5.0f;
        public float happiness = 0;
    
    
        private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
        };
        private static int mCurrentColorIndex = 0;
    
    
        private Paint mFacePositionPaint;
        private Paint mIdPaint;
        private Paint mBoxPaint;
    
        private volatile Face mFace;
        private int mFaceId;
        private float mFaceHappiness;
        public float righteye;
        public float lefteye;
        FaceGraphic(GraphicOverlay overlay) {
            super(overlay);
            mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
            final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];
    
            mFacePositionPaint = new Paint();
            mFacePositionPaint.setColor(selectedColor);
    
            mIdPaint = new Paint();
            mIdPaint.setColor(selectedColor);
            mIdPaint.setTextSize(ID_TEXT_SIZE);
    
            mBoxPaint = new Paint();
            mBoxPaint.setColor(selectedColor);
            mBoxPaint.setStyle(Paint.Style.STROKE);
            mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
        }
    
        void setId(int id) {
            mFaceId = id;
        }
    
    
        /**
         * Updates the face instance from the detection of the most recent frame.  Invalidates the
         * relevant portions of the overlay to trigger a redraw.
         */
        void updateFace(Face face) {
            mFace = face;
            postInvalidate();
        }
    
        /**
         * Draws the face annotations for position on the supplied canvas.
         */
        @Override
        public void draw(Canvas canvas) {
            Face face = mFace;
            if (face == null) {
                return;
            }
    
            // Draws a circle at the position of the detected face, with the face's track id below.
            float x = translateX(face.getPosition().x + face.getWidth() / 2);
            float y = translateY(face.getPosition().y + face.getHeight() / 2);
            canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);
            //canvas.drawText("id: " + mFaceId, x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
            //canvas.drawText("happiness: " + String.format("%.2f", face.getIsSmilingProbability()), x - ID_X_OFFSET, y - ID_Y_OFFSET, mIdPaint);//웃음
           // canvas.drawText("right eye: " + String.format("%.2f", face.getIsLeftEyeOpenProbability()), x - ID_X_OFFSET * 2, y - ID_Y_OFFSET * 2, mIdPaint);
           // canvas.drawText("left eye: " + String.format("%.2f", face.getIsRightEyeOpenProbability()), x + ID_X_OFFSET*8, y - ID_Y_OFFSET*2, mIdPaint);

            // Draws a bounding box around the face.
            float xOffset = scaleX(face.getWidth() / 2.0f);
            float yOffset = scaleY(face.getHeight() / 2.0f);
            float left = x - xOffset;
            float top = y - yOffset;
            float right = x + xOffset;
            float bottom = y + yOffset;
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
            this.happiness = face.getIsSmilingProbability();
            this.lefteye = face.getIsLeftEyeOpenProbability();
            this.righteye = face.getIsRightEyeOpenProbability();
    
        }
    
    
    }
}
