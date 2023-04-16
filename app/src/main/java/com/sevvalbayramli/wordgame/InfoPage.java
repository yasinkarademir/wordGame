package com.sevvalbayramli.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InfoPage extends AppCompatActivity {

    private static final int READ_BLOCK_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView score = findViewById(R.id.Score);
        Button playAgain = findViewById(R.id.PlayAgain);
        Button clearScores = findViewById(R.id.ClearScores);
        Button exitGame = findViewById(R.id.ExitGame);
        ListView highScoreListView = findViewById(R.id.highScoreListView);

        Intent intent = getIntent();
        String point = intent.getStringExtra("score");
        System.out.println("score:" + point);
        score.setText("Your Score: " + point);

        List<String> highScores = readHighScoresFromFile();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, highScores);
        highScoreListView.setAdapter(adapter);

        Intent intent1 = new Intent(this, MainActivity.class);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent1);
            }
        });

        clearScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearHighScoresFile();
                highScores.clear();
                adapter.notifyDataSetChanged();
            }
        });

        exitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }

    private List<String> readHighScoresFromFile() {
        List<String> highScores = new ArrayList<>();
        try {
            FileInputStream fileIn = openFileInput("high_scores.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn, StandardCharsets.UTF_8);
            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            int charRead;
            StringBuilder stringBuilder = new StringBuilder();

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                stringBuilder.append(readString);
            }
            InputRead.close();

            String[] highScoresArray = stringBuilder.toString().split("\n");
            for (String s : highScoresArray) {
                if (!s.isEmpty()) {
                    highScores.add(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return highScores;
    }

    private void clearHighScoresFile() {
        try {
            FileOutputStream fileOut = openFileOutput("high_scores.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut, StandardCharsets.UTF_8);
            outputWriter.write("");
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}