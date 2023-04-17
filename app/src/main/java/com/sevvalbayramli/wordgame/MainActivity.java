package com.sevvalbayramli.wordgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.widget.TextView;


import android.os.Handler;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    List<Integer> letters = new ArrayList<>();
    List<Letter> letterList = new ArrayList<>();
    Handler handler = new Handler();

    private Set<String> wordSet;
    int[] colCount = {6, 6, 6, 6, 6, 6, 6, 6};


    String text = "";
    int userPoint = 0;
    String point = "0";
    boolean flag = true;
    int falseWord = 0;
    int iceCount = 0;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        TextView pointText = findViewById(R.id.pointText);
        ImageButton cancel_button = findViewById(R.id.cancel_button);
        ImageButton submit_button = findViewById(R.id.submit_button);
        loadWords();
        getSupportActionBar().hide();

        createFirstLetters();
        clickControl();
        createNewItemPeriodically(5000);


        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText(textView);

                //Burada tüm harflerin rengini sıfırlıyorum
                for (Letter letter : letterList) {
                    letter.getImage().setColorFilter(null);
                    letter.setClick(false);
                }


            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wordSet.contains(text.toLowerCase())) {
                    toastMessage("Başarılı");
                    editText(textView);
                    point = String.valueOf(userPoint);
                    pointText.setText(point);
                    deleteLetters();
                } else {
                    falseWord++;

                    if (falseWord % 3 == 0 && falseWord != 0) {
                        createLettersForAllColumns();
                    }else{
                        toastMessage(falseWord%3+". yanlış kelime girişi");
                    }


                }


            }
        });


    }

    public void toastMessage(String message){
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        Snackbar snackbar = Snackbar.make(gridLayout, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbarView.setLayoutParams(params);
        snackbar.show();
    }


    public List<Letter> createLettersForAllColumns() {
        List<Letter> letters = new ArrayList<>();
        for (int col = 0; col < 8; col++) {
            Letter letter = createLetters();
            GridLayout gridLayout = findViewById(R.id.gridLayout);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            if (colCount[col] >= 0) {
                params.width = 135;
                params.height = 135;
                params.rowSpec = GridLayout.spec(colCount[col]);
                System.out.println("colCount:" + colCount[col] + " col: " + col);
                params.columnSpec = GridLayout.spec(col);
                params.setGravity(Gravity.CENTER);
                letter.getImage().setScaleType(ImageView.ScaleType.CENTER_CROP);
                letter.setRow(colCount[col]);
                letter.setColumn(col);
                int index = colCount[col] * gridLayout.getColumnCount() + col;
                letter.getImage().setId(index);
                gridLayout.addView(letter.getImage(), params);
                colCount[col]--;
                createAnimation(letter);
                letters.add(letter);
            } else if(colCount[col] <= -1) {
                    System.out.println("oyun bitti " + colCount[col]);
                    flag = false;
                    int userScore =Integer.parseInt(point);
                    saveHighScoreToFile(userScore);


                    Intent intent = new Intent(MainActivity.this, InfoPage.class);
                    intent.putExtra("score", String.valueOf(userScore));
                    startActivity(intent);
            }
        }
        return letters;
    }

    public void pointControl() {
        if (userPoint > 100 && userPoint <= 200) {
            stopItem();
            createNewItemPeriodically(4000);
        } else if (userPoint > 200 && userPoint <= 300) {
            stopItem();
            createNewItemPeriodically(3000);
        } else if (userPoint > 300 && userPoint <= 400) {
            stopItem();
            createNewItemPeriodically(2000);
        } else if (userPoint > 300 && userPoint <= 400) {
            stopItem();
            createNewItemPeriodically(1000);
        }
    }

    public void stopItem() {
        handler.removeCallbacksAndMessages(null);
    }

    public void createNewItemPeriodically(int delay) {

        handler.postDelayed(new Runnable() {
            public void run() {
                if (flag) {
                    createOneLetter();
                    handler.postDelayed(this, delay);
                } else {
                    stopItem();
                }
            }
        }, delay);

    }

    private void clickControl() {
        TextView textView = findViewById(R.id.textView);
        for (Letter letter : letterList) {
            letter.getImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (letter.isClick()) {
                        text = text.replaceFirst(String.valueOf(letter.getLetter()), "");
                        textView.setText(text);
                        letter.getImage().setColorFilter(null);
                        userPoint = userPoint - letter.getPoint();
                        letter.setClick(false);
                    } else {
                        text = text + letter.getLetter();
                        textView.setText(text);
                        addFilter(letter.getImage());
                        userPoint = userPoint + letter.getPoint();
                        letter.setClick(true);
                    }
                    System.out.println("point: " + userPoint);
                }
            });
        }
    }

    private void loadWords() {
        wordSet = new HashSet<>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("kelimeler.txt"), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                wordSet.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void iceLetterControl(Letter letter) {
        if (letter == null) {
            return;
        }

        if (iceCount % 15 == 0 && iceCount != 0) {
            letter.setIce(true);
            addBlackFilter(letter.getImage());
            Letter newLetter = findLetter(+1, letter); // altındakini bulur
            if (newLetter != null) {
                newLetter.setIce(true);
                addBlackFilter(newLetter.getImage());
            }
        } else {
            Letter newLetter = findLetter(+1, letter); // altındakini bulur
            if (newLetter != null && newLetter.isIce()) {
                letter.setIce(true);
                addBlackFilter(letter.getImage());
            }
        }
    }

    public void addBlackFilter(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Bitmap blackAndWhiteBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                int pixel = bitmap.getPixel(i, j);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int gray = (int) (red * 0.3 + green * 0.59 + blue * 0.11);

                int newPixel = Color.argb(Color.alpha(pixel), gray, gray, gray);
                blackAndWhiteBitmap.setPixel(i, j, newPixel);
            }
        }

        imageView.setImageBitmap(blackAndWhiteBitmap);
    }

    public Letter createOneLetter() {
        Letter letter = createLetters();

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        Random rand = new Random();
        int col = rand.nextInt(8);
        params.width = 135;
        params.height = 135;
        params.rowSpec = GridLayout.spec(colCount[col]);

        params.columnSpec = GridLayout.spec(col);
        params.setGravity(Gravity.CENTER);
        letter.getImage().setScaleType(ImageView.ScaleType.CENTER_CROP);
        letter.setRow(colCount[col]);
        letter.setColumn(col);
        int index = colCount[col] * gridLayout.getColumnCount() + col;
        letter.getImage().setId(index);
        colCount[col]--;

        clickControl();
        if (colCount[col] <= -1) {
            System.out.println("oyun bitti " + colCount[col]);
            flag = false;
            int userScore =Integer.parseInt(point);
            saveHighScoreToFile(userScore);

// InfoPage sınıfına geçiş yap ve skoru aktar.
            Intent intent = new Intent(MainActivity.this, InfoPage.class);
            intent.putExtra("score", String.valueOf(userScore));
            startActivity(intent);


            return null;
        } else {
            iceCount++;
            iceLetterControl(letter);
            gridLayout.addView(letter.getImage(), params);
            createAnimation(letter);
            pointControl();
            return letter;
        }


    }

    public void createAnimation(Letter letter) {
        //bir saniye aralıklarla belirtilen konumda yeni bir harf oluşturuyor
        final Handler handler = new Handler();
        final int delay = 1000; // 1 saniyelik gecikme
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_down);
        letter.getImage().startAnimation(animation);
        animation.setDuration(2000); // animasyonun 2 saniye sürmesi için
        letter.getImage().startAnimation(animation);

    }

    public Letter findLetter(int a, Letter letter) {
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        int index = (letter.getRow() + a) * gridLayout.getColumnCount() + letter.getColumn();
        ImageView imageView = findViewById(index);
        if (imageView == null) {
            return null;
        }
        Letter newLetter = null;
        for (Letter findLetter : letterList) {
            if (findLetter.getImage() == imageView) {
                newLetter = findLetter;//Bu imageviewın sahibi olan letter ı bulkuk
            }
        }

        return newLetter;
    }

    public void updateLetters(Letter letter) {//üsttekini bulup siliyor

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        Letter newLetter = findLetter(-1, letter);//üsttekini bulur

        if (newLetter == null) {
        } else {
            System.out.println("girdi: " + newLetter.getLetter());
            gridLayout.removeView(newLetter.getImage());

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 135;
            params.height = 135;
            params.rowSpec = GridLayout.spec(letter.getRow());
            params.columnSpec = GridLayout.spec(letter.getColumn());
            //colCount[letter.getColumn()]++;
            params.setGravity(Gravity.CENTER);
            newLetter.getImage().setScaleType(ImageView.ScaleType.CENTER_CROP);

            gridLayout.addView(newLetter.getImage(), params);
            int newIndex = (letter.getRow()) * gridLayout.getColumnCount() + letter.getColumn();
            newLetter.getImage().setId(newIndex);
            updateLetters(newLetter);
            newLetter.setRow(letter.getRow());
            newLetter.setColumn(letter.getColumn());
        }

    }

    public void deleteLetters() {
        for (Letter letter : letterList) {
            if (letter.isClick() && letter.isIce() != true) {
                GridLayout gridLayout = findViewById(R.id.gridLayout);

                gridLayout.removeView(letter.getImage());
                letter.setClick(false);
                //letterList.remove(letter); //hata verdi
                colCount[letter.getColumn()]++;
                updateLetters(letter);
            } else if (letter.isClick() && letter.isIce() == true) {
                letter.setIce(false);
            }
        }
    }

    public void editText(TextView textView) {
        text = "";
        textView.setText(text);

    }

    public void addFilter(ImageView imageView) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0); // Renk doygunluğunu kaldırarak siyah-beyaz görüntü sağlar
        colorMatrix.setScale(1f, 1f, 1f, 1f); // Sadece mavi rengini korur, diğer renkleri kaldırır
        colorMatrix.postConcat(new ColorMatrix(new float[]{
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
        for (int i = 0; i < 80; i++) {
            GridLayout gridLayout = findViewById(R.id.gridLayout);
            ImageView image = new ImageView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 135;
            params.height = 135;
            params.setGravity(Gravity.CENTER);
            gridLayout.addView(image, params);
        }

        for (int i = 9; i > 6; i--) {
            for (int j = 0; j < 8; j++) {

                Letter letter = createLetters();

                GridLayout gridLayout = findViewById(R.id.gridLayout);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 125;
                params.height = 125;
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);
                letter.setRow(i);
                letter.setColumn(j);
                int index = i * gridLayout.getColumnCount() + j;
                letter.getImage().setId(index);
                gridLayout.addView(letter.getImage(), params);

            }
        }

    }

    public static int karakterIndexBul(char[] dizi, char karakter) {
        for (int i = 0; i < dizi.length; i++) {
            if (dizi[i] == karakter) {
                return i;
            }
        }
        return -1;
    }

    public Letter createLetters() {
        char[] harfler = {'a', 'b', 'c', 'ç', 'd', 'e', 'f', 'g', 'ğ', 'h', 'i', 'ı', 'j', 'k', 'l', 'm', 'n', 'o', 'ö', 'p', 'r', 's', 'ş', 't', 'u', 'ü', 'v', 'y', 'z'};
        char[] vowel = {'a', 'e', 'ı', 'i', 'o', 'ö', 'u', 'ü'};
        char[] consonant = {'b', 'c', 'ç', 'd', 'f', 'g', 'ğ', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'r', 's', 'ş', 't', 'v', 'y', 'z'};

        int[] point = {1, 3, 4, 4, 3, 1, 7, 5, 8, 5, 2, 1, 10, 1, 1, 2, 1, 2, 7, 5, 1, 2, 4, 1, 2, 3, 7, 3, 4};


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
        int randomNumber;
        char randomLetter;

        double randomDouble = rand.nextDouble();
        if (randomDouble < 0.3) {
            randomNumber = rand.nextInt(vowel.length);
            randomLetter = vowel[randomNumber];


        } else {
            randomNumber = rand.nextInt(consonant.length);
            randomLetter = consonant[randomNumber];
        }


        Letter letter = new Letter(randomLetter);

        ImageView imageView = new ImageView(this);
        letter.setImage(imageView);
        letter.getImage().setImageResource(letters.get(karakterIndexBul(harfler, randomLetter)));

        letter.setPoint(point[karakterIndexBul(harfler, randomLetter)]);

        letterList.add(letter);
        return letter;
    }

    //skor işlemleri
    private void saveHighScoreToFile(int newScore) {
        String fileName = "high_scores.txt";
        List<Integer> highScores = readHighScoresFromFile();

        // Yeni skoru mevcut skorlar listesine ekle
        highScores.add(newScore);

        // Skorları azalan sırayla sırala
        Collections.sort(highScores, Collections.reverseOrder());

        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            for (Integer score : highScores) {
                osw.write(score + "\n");
            }

            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> readHighScoresFromFile() {
        List<Integer> highScores = new ArrayList<>();
        String fileName = "high_scores.txt";

        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                int score = Integer.parseInt(line);
                highScores.add(score);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highScores;
    }


}