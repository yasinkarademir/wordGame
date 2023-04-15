package com.sevvalbayramli.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView score= findViewById(R.id.Score);
        Button playAgain=findViewById(R.id.PlayAgain);

        Intent intent = getIntent();

        String point = intent.getStringExtra("score");
        System.out.println("score:"+point);

        score.setText("Your Score: "+point);
        Intent intent1=new Intent(this,MainActivity.class);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent1);

            }
        });
    }
}