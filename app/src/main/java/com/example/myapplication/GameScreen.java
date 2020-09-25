package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GameScreen extends AppCompatActivity {

    public TextView textView;

    SensorManager sensorMgr;
    Sensor accelerometer;

    float x2,y2,z2;

    SensorEventListener accelListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            x2=0;
            y2=0;
            Log.i("TAG", "" + event.values[0] +
                    ", " + event.values[1] +
                    ", " + event.values[2]);

            x2=event.values[0];
            y2=event.values[1];
            z2=event.values[2];

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ConstraintLayout rootView = (ConstraintLayout) findViewById(R.id.rootView);

        GraphicsView myView = new GraphicsView(this);
        rootView.addView(myView);

        textView = findViewById(R.id.showText);

        sensorMgr = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    public class GraphicsView extends View{

        private float x;
        private float y;
        private int radius;
        private Paint paint;

        private float x1;
        private float y1;
        private int radius1;
        private Paint paint1;

        private int width;
        private int height;
        private int death;
        private double distance;


        public GraphicsView(Context context) {
            super(context);
            width = getWidth();
            height = getHeight();
            paint=new Paint();
            paint.setColor(getColor(R.color.green));
            paint1 = new Paint();
            paint1.setColor(getColor(R.color.yellow));

            death = 0;
            x=50;
            y=50;
            radius=50;

            x1=1000;
            y1=1850;
            radius1=50;
        }

        public int getDeath(){return death;}

        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);

            canvas.drawCircle(x,y,radius,paint);
            canvas.drawCircle(x1,y1,radius1,paint1);

            if(x1>=1000){
                x1=1000;
            }

            if(y1>=1850){
                y1=1850;
            }

            if(x2==0.00 && z2<=0){
                y1 += 10;
            }

            if(x2==0.00&&z2>0){
                y1 -=10;
            }

            if(x2<0 && z2<=0){
                x1 +=10;
                y1 +=10;
            }

            if(x2<0 && z2>0){
                x1 -=10;
                y1 -=10;
            }

            if(x2>0 && z2<=0){
                x1 -=10;
                y1 +=10;
            }

            if(x2>0 && z2>0){
                x1 +=10;
                y1 -=10;
            }

            if(x1<0){
                x1=1000;
                y1=1850;
                death++;
            }

            if(y1<0){
                x1=1000;
                y1=1850;
                death++;
            }

            textView.setText("DEATH:" + death);

            distance = Math.sqrt((x1-x)*(x1-x)+(y1-y)*(y1-y));

            if(distance <= 100){
                Intent intent1 = new Intent();
                intent1.setClass(GameScreen.this, EndScreen.class);
                startActivity(intent1);
            }


            if(death == 3) {
                death = 0;
                Intent intent1 = new Intent();
                intent1.setClass(GameScreen.this, GameOver.class);
                startActivity(intent1);
            }
            else{
                invalidate();
            }

        }



    }



    protected void onResume() {
        super.onResume();
        sensorMgr.registerListener(accelListener, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMgr.unregisterListener(accelListener, accelerometer);
    }
}
