package com.sevvalbayramli.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import android.widget.TextView;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {

    List<Integer> letters = new ArrayList<Integer>();
    List<Letter> letterList = new ArrayList<Letter>();
    int[] colCount = {7,7,7,7,7,7,7};


    String text="";



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView=findViewById(R.id.textView);
        ImageButton cancel_button=findViewById(R.id.cancel_button);
        ImageButton submit_button=findViewById(R.id.submit_button);
        getSupportActionBar().hide();
        createFirstLetters();
        dropLetter();






        for (Letter letter : letterList) {
            letter.getImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(letter.isClick()){
                        text = text.replaceFirst(String.valueOf(letter.getLetter()), "");
                        textView.setText(text);
                        letter.getImage().setColorFilter(null);
                        letter.setClick(false);
                    }else{
                        text=text+letter.getLetter();
                        textView.setText(text);
                        addFilter(letter.getImage());
                        letter.setClick(true);
                    }
                }
            });
        }



        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editText(textView);
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText(textView);
                deleteLetters();


            }
        });



}

    public void dropLetter(){//itemler 0dan başlayıp düşüyor
        //ama başladığında hızlı düştüğü için düşüş anı yok

        Letter letter=createOneLetter();
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for(int i=0;i<colCount[letter.getColumn()];i++){

                gridLayout.removeView(letter.getImage());

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 135;
                params.height = 135;
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(letter.getColumn());
                params.setGravity(Gravity.CENTER);
                letter.getImage().setScaleType(ImageView.ScaleType.CENTER_CROP);

                gridLayout.addView(letter.getImage(),params);
                int newIndex=(i)*gridLayout.getColumnCount() +letter.getColumn();

                letter.getImage().setId(newIndex);
                updateLetters(letter);
                letter.setRow(i);
                letter.setColumn(letter.getColumn());

        }
        colCount[letter.getColumn()]=colCount[letter.getColumn()]+1;


    }

    public Letter createOneLetter(){
        Letter letter=createLetters();

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        Random rand = new Random();
        int col = rand.nextInt(8);
        //int row = rand.nextInt(7);
        params.width = 135;
        params.height = 135;
        params.rowSpec = GridLayout.spec(0);
        params.columnSpec = GridLayout.spec(col);
        params.setGravity(Gravity.CENTER);
        letter.getImage().setScaleType(ImageView.ScaleType.CENTER_CROP);
        letter.setRow(0);
        letter.setColumn(col);
        int index=0 * gridLayout.getColumnCount() + col;
        letter.getImage().setId(index);
        gridLayout.addView(letter.getImage(),params);
        return letter;
    }

    public void updateLetters(Letter letter){//üsttekini bulup siliyor

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        int index=(letter.getRow()-1)*gridLayout.getColumnCount() +letter.getColumn();
        ImageView imageView = findViewById(index);
        Letter newLetter = null;
        for(Letter findLetter:letterList){
            if(findLetter.getImage()==imageView){
                newLetter=findLetter;//Bu imageviewın sahibi olan letter ı bulkuk
            }
        }
        if(imageView==null){

        }else{
            System.out.println("girdi: "+newLetter.getLetter());
            gridLayout.removeView(imageView);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 135;
            params.height = 135;
            params.rowSpec = GridLayout.spec(letter.getRow());
            params.columnSpec = GridLayout.spec(letter.getColumn());
            params.setGravity(Gravity.CENTER);
            newLetter.getImage().setScaleType(ImageView.ScaleType.CENTER_CROP);

            gridLayout.addView(newLetter.getImage(),params);
            int newIndex=(letter.getRow())*gridLayout.getColumnCount() +letter.getColumn();
            //newLetter.setRow(letter.getRow());
            //newLetter.setColumn(letter.getColumn());
            newLetter.getImage().setId(newIndex);

            updateLetters(newLetter);
            newLetter.setRow(letter.getRow());
            newLetter.setColumn(letter.getColumn());
        }

    }

    public void deleteLetters(){
        for (Letter letter:letterList) {
            if(letter.isClick()){
                GridLayout gridLayout = findViewById(R.id.gridLayout);

                gridLayout.removeView(letter.getImage());
                letter.setClick(false);
                //letterList.remove(letter); //hata verdi
                updateLetters(letter);
            }
        }
    }

    public void editText(TextView textView){
        text="";
        textView.setText(text);

    }

    public void addFilter(ImageView imageView){
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0); // Renk doygunluğunu kaldırarak siyah-beyaz görüntü sağlar
        colorMatrix.setScale(1f, 1f, 1f, 1f); // Sadece mavi rengini korur, diğer renkleri kaldırır
        colorMatrix.postConcat(new ColorMatrix(new float[] {
                0f, 0f, 1f, 0f, 0f, // Red
                0f, 0f, 1f, 0f, 0f, // Green
                0f, 0f, 1f, 0f, 0f, // Blue
                0f, 0f, 0f, 1f, 0f  // Alpha
        }));
        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

// ImageView'a mavi filtre ekleme
        imageView.setColorFilter(colorFilter);

    }

    public void createFirstLetters() {
        for (int i = 0; i <80 ; i++) {
            GridLayout gridLayout = findViewById(R.id.gridLayout);
            ImageView image=new ImageView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 135;
            params.height = 135;
            params.setGravity(Gravity.CENTER);
            gridLayout.addView(image,params);
        }

        for (int i = 9; i > 6; i--) {
            for (int j = 0; j < 8; j++) {

                Letter letter=createLetters();

                GridLayout gridLayout = findViewById(R.id.gridLayout);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 135;
                params.height = 135;
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);
                System.out.println("row:"+i+" column:"+j);
                params.setGravity(Gravity.CENTER);
                letter.getImage().setScaleType(ImageView.ScaleType.CENTER_CROP);
                //letter.getImage().setLayoutParams(params);
                letter.setRow(i);
                letter.setColumn(j);
                int index=i * gridLayout.getColumnCount() + j;
                letter.getImage().setId(index);
                gridLayout.addView(letter.getImage(),params);

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
        ImageView imageView = new ImageView(this);
        letter.setImage(imageView);
        letter.getImage().setImageResource(letters.get(randomNumber));
        letterList.add(letter);
        return letter;
    }



}