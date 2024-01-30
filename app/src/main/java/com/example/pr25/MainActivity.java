package com.example.pr25;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorListener;
    private TextView xTextView, yTextView, zTextView;
    private GestureDetector gestureDetector;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialitzem els TextViews
        xTextView = findViewById(R.id.xTextView);
        yTextView = findViewById(R.id.yTextView);
        zTextView = findViewById(R.id.zTextView);

        sensorListener = new SensorEventListener() {
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
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Es pot ignorar aquesta CB de moment
            }
        };

        // Seleccionem el tipus de sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Registrem el Listener per capturar els events del sensor
        if (sensor != null) {
            sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        // Creem una nova instància pel Gesture Detector
        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Passa l'event a la instància del Gesture Detector
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            // Es cridarà quan es detecti un "double tap"
            Toast.makeText(MainActivity.this, "Has fet un doble tap!", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
