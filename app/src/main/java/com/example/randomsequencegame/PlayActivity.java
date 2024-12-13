package com.example.randomsequencegame;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ArrayList<Integer> sequence;
    private int currentStep = 0;
    private int score = 0;
    private boolean inputEnabled = false;
    private boolean movementDetected = false;
    private static final float THRESHOLD = 6.0f;
    private static final float DOMINANCE_FACTOR = 1.5f;
    private TextView goTextView, scoreTextView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sequence = getIntent().getIntegerArrayListExtra("sequence");
        score = getIntent().getIntExtra("score", 0);

        goTextView = findViewById(R.id.goTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        handler = new Handler();
        updateScore();
        Toast.makeText(this, "Tilt the phone to match the sequence", Toast.LENGTH_SHORT).show();
        showGoForNextColor();
    }

    private void showGoForNextColor() {
        if (currentStep < sequence.size()) {
            goTextView.setText("GO!");
            inputEnabled = false;

            handler.postDelayed(() -> {
                goTextView.setText("");
                inputEnabled = true;
                movementDetected = false;
            }, 1000);
        }
    }

    private void updateScore() {
        scoreTextView.setText("Score: " + score);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!inputEnabled || movementDetected) return;

        float x = event.values[0];
        float y = event.values[1];

        int direction = determineDirection(x, y);

        if (direction != -1 && direction == sequence.get(currentStep)) {
            movementDetected = true;
            currentStep++;
            if (currentStep < sequence.size()) {
                showGoForNextColor();
            } else {
                score += 4;
                updateScore();

                Toast.makeText(this, "Round Complete!", Toast.LENGTH_SHORT).show();


                handler.postDelayed(() -> {
                    Intent intent = new Intent(this, SequenceActivity.class);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                }, 1000);
            }
        } else if (direction != -1) {
            Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
            inputEnabled = false;
            handler.postDelayed(() -> {
                Intent intent = new Intent(this, GameOverActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
                finish();
            }, 1000);
        }
    }

    private int determineDirection(float x, float y) {
        if (Math.abs(x) > THRESHOLD && Math.abs(x) > Math.abs(y) * DOMINANCE_FACTOR) {
            if (x > 0) return 3;
            else return 2;
        }

        if (Math.abs(y) > THRESHOLD && Math.abs(y) > Math.abs(x) * DOMINANCE_FACTOR) {
            if (y > 0) return 0;
            else return 1;
        }

        return -1;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
