package com.example.randomsequencegame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int playerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);


        dbHelper = new DatabaseHelper(this);
        playerScore = getIntent().getIntExtra("score", 0);

        TextView scoreDisplay = findViewById(R.id.finalScore);
        EditText playerNameInput = findViewById(R.id.inputName);
        Button saveScoreButton = findViewById(R.id.saveButton);
        Button playAgainButton = findViewById(R.id.playAgainButton);

        scoreDisplay.setText("Final Score: " + playerScore);

        saveScoreButton.setOnClickListener(v -> {
            String playerName = playerNameInput.getText().toString().trim();
            if (!playerName.isEmpty()) {
                dbHelper.savePlayerScore(playerName, playerScore);
                Toast.makeText(this, "Your score has been saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GameOverActivity.this, HighScoreActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Please enter a valid name!", Toast.LENGTH_SHORT).show();
            }
        });


        playAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, SequenceActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
