package com.example.randomsequencegame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;

public class SequenceActivity extends AppCompatActivity {
    private ArrayList<Integer> sequence;
    private Random random;
    private TextView sequenceText;
    private Handler handler;
    private int score;
    private int round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);

        sequenceText = findViewById(R.id.sequenceText);
        handler = new Handler();

        random = new Random();
        sequence = new ArrayList<>();


        score = getIntent().getIntExtra("score", 0);
        round = getIntent().getIntExtra("round", 1);

        generateSequence();
        showSequence();
    }

    private void generateSequence() {
        sequence.clear();
        int sequenceLength = 4 + (round - 1) * 2;
        for (int i = 0; i < sequenceLength; i++) {
            sequence.add(random.nextInt(4));
        }
    }

    private void showSequence() {
        sequenceText.setText("Sequence colours");
        handler.postDelayed(() -> displaySequenceStep(0), 1000);
    }

    private void displaySequenceStep(int step) {
        if (step < sequence.size()) {
            sequenceText.setText("Color: " + getColorName(sequence.get(step)));


            handler.postDelayed(() -> displaySequenceStep(step + 1), 1000);
        } else {
            startPlayActivity();
        }
    }

    private String getColorName(int colorIndex) {
        switch (colorIndex) {
            case 0: return "Red";
            case 1: return "Blue";
            case 2: return "Green";
            case 3: return "Yellow";
            default: return "Unknown";
        }
    }

    private void startPlayActivity() {
        handler.postDelayed(() -> {
            Intent intent = new Intent(SequenceActivity.this, PlayActivity.class);
            intent.putIntegerArrayListExtra("sequence", sequence);
            intent.putExtra("score", score);
            intent.putExtra("round", round);
            startActivity(intent);
            finish();
        }, 1000);
    }
}
