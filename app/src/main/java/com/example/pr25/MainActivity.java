package com.example.pr25;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView xTextView, yTextView, zTextView;
    private int taps = 0;
    private long lastTime = 0;
    private float lastZAcc;
    private static final int SHAKE_THRESHOLD = 800;
    private static final float TIME_THRESHOLD = 5.0f;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialitzem els TextViews
        xTextView = findViewById(R.id.xTextView);
        yTextView = findViewById(R.id.yTextView);
        zTextView = findViewById(R.id.zTextView);

        SensorEventListener sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // Valors de l'acceleròmetre en m/s^2
                float xAcc = sensorEvent.values[0];
                float yAcc = sensorEvent.values[1];
                float zAcc = sensorEvent.values[2];

                // Actualitzem els TextViews amb els valors de l'acceleròmetre
                xTextView.setText(String.valueOf(xAcc));
                yTextView.setText(String.valueOf(yAcc));
                zTextView.setText(String.valueOf(zAcc));

                // Detectem el double tap
                long curTime = System.currentTimeMillis();
                long timeDiff = curTime - lastTime;

                if (timeDiff > TIME_THRESHOLD) {
                    lastTime = curTime;
                    float speed = Math.abs(zAcc - lastZAcc) / timeDiff * 10000;

                    if (speed > SHAKE_THRESHOLD) {
                        taps++;
                        Log.i("INFO", "Taps: " + taps);
                        if (taps == 2) {
                            Toast.makeText(MainActivity.this, "You've done a Double Tap!", Toast.LENGTH_SHORT).show();
                            taps = 0;
                        }
                    }
                    lastZAcc = zAcc;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Es pot ignorar aquesta CB de moment
            }
        };

        // Seleccionem el tipus de sensor
        SensorManager sensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // Registrem el Listener per capturar els events del sensor
        if (sensor != null) {
            sensorMgr.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
