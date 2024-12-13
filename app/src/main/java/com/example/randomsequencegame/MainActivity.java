package com.example.randomsequencegame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SequenceActivity.class);
            startActivity(intent);
        });

        Button leaderboardBtn = findViewById(R.id.leaderboardButton);
        leaderboardBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
            startActivity(intent);
        });
    }
}
