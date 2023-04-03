package com.sevvalbayramli.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    List<Integer> letters = new ArrayList<Integer>();
    List<Letter> letterList = new ArrayList<Letter>();
    List<ImageButton> imageButtonList = new ArrayList<ImageButton>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createFirstLetters();
        //createLetters();
        for (ImageButton image : imageButtonList) {//Burası LetterList içerindeki imageler dönücek şekilde düzeltilicek
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("deneme" + view);
                }
            });
        }
    }


    public void createFirstLetters() {
        for (int i = 9; i > 6; i--) {
            for (int j = 0; j < 8; j++) {
                //imageButton.setImageResource(createLetters());
                Letter letter=createLetters();

                GridLayout gridLayout = findViewById(R.id.gridLayout);
                gridLayout.addView(letter.getImage());
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 135;
                params.height = 135;
                //params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);
                params.setGravity(Gravity.CENTER);
                letter.getImage().setScaleType(ImageView.ScaleType.CENTER_CROP);
                letter.getImage().setLayoutParams(params);
                imageButtonList.add(letter.getImage());

            }
        }
    }


    public Letter createLetters() {
        char[] harfler = {'a', 'b', 'c', 'ç', 'd', 'e', 'f', 'g', 'ğ', 'h', 'i', 'ı', 'j', 'k', 'l', 'm', 'n', 'o', 'ö', 'p', 'r', 's', 'ş', 't', 'u', 'ü', 'v', 'y', 'z'};
        Field[] fields = R.drawable.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().length() < 3) {
                try {
                    int resourceId = fields[i].getInt(null);
                    letters.add(resourceId);
                } catch (Exception e) {
                }
            }
        }
        Random rand = new Random();
        int randomNumber = rand.nextInt(29);
        Letter letter=new Letter(harfler[randomNumber]);
        ImageButton imageButton = new ImageButton(this);
        letter.setImage(imageButton);
        letter.getImage().setImageResource(letters.get(randomNumber));
        letterList.add(letter);
        return letter;
    }

}