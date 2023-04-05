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


public class MainActivity extends AppCompatActivity {

    List<Integer> letterImages = new ArrayList<Integer>();
    List<Letter> letterList = new ArrayList<Letter>();
    List<ImageButton> imageButtonList = new ArrayList<ImageButton>();


    String text = "";


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        ImageButton cancel_button = findViewById(R.id.cancel_button);
        ImageButton submit_button = findViewById(R.id.submit_button);
        getSupportActionBar().hide();

        createFirstLetters();
        setLetterOnClickListeners(textView);
        setCancelAndSubmitButtonListeners(cancel_button, submit_button, textView);
    }


    private void addCharacter(StringBuilder input, char character) {
        input.append(character);
    }

    private void removeCharacter(StringBuilder input, char character) {
        int index = input.indexOf(Character.toString(character));
        if (index != -1) {
            input.deleteCharAt(index);
        }
    }


    private void setLetterOnClickListeners(TextView textView) {
        StringBuilder stringBuilder = new StringBuilder(text);

        for (Letter letter : letterList) {
            letter.getImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println(letter.getLetter());
                    System.out.println(letter.isClick());

                    if (letter.isClick() == true) {
                        removeCharacter(stringBuilder, letter.getLetter());
                        text = stringBuilder.toString();
                        textView.setText(text);
                        letter.getImage().setColorFilter(null);
                        letter.setClick(false);
                    } else {
                        addCharacter(stringBuilder, letter.getLetter());
                        text = stringBuilder.toString();
                        textView.setText(text);
                        addFilter(letter.getImage());
                        letter.setClick(true);
                    }
                }
            });
        }
    }


    /*
    private void setLetterOnClickListeners(TextView textView) {
        for (Letter letter : letterList) {

            letter.getImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println(letter.getLetter());
                    System.out.println(letter.isClick());

                    if (letter.isClick() == true) {
                        text = text.replaceFirst(String.valueOf(letter.getLetter()), "");
                        textView.setText(text);
                        letter.getImage().setColorFilter(null);
                        letter.setClick(false);
                    } else {
                        text = text + letter.getLetter();
                        textView.setText(text);
                        addFilter(letter.getImage());
                        letter.setClick(true);
                    }


                }
            });
        }
    }


     */


    private void setCancelAndSubmitButtonListeners(ImageButton cancel_button, ImageButton submit_button, TextView textView) {
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
            }
        });
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
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < 80; i++) {
            Letter letter = createLetter(i / 8);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 135;
            params.height = 135;
            params.rowSpec = GridLayout.spec(i / 8);
            params.columnSpec = GridLayout.spec(i % 8);
            params.setGravity(Gravity.CENTER);

            letter.getImage().setScaleType(ImageView.ScaleType.CENTER_CROP);
            gridLayout.addView(letter.getImage(), params);

            // Only add the last 3 rows to the letterList
            if (i >= 56) {
                letterList.add(letter);
            }
        }
    }




    /*
    public void createFirstLetters() {
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < 80; i++) {
            Letter letter = createLetter();

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 135;
            params.height = 135;
            params.rowSpec = GridLayout.spec(i / 8);
            params.columnSpec = GridLayout.spec(i % 8);
            params.setGravity(Gravity.CENTER);

            letter.getImage().setScaleType(ImageView.ScaleType.CENTER_CROP);
            gridLayout.addView(letter.getImage(), params);

            // İlk 3 satırı doldurun
            if (i >= 56) {
                letterList.add(letter);
            }
        }
    }
    */


    public static int karakterIndexBul(char[] dizi, char karakter) {
        for (int i = 0; i < dizi.length; i++) {
            if (dizi[i] == karakter) {
                return i;
            }
        }
        return -1;
    }


    public Letter createLetter(int rowNumber) {


        if (rowNumber < 3) {
            System.out.println("rowNumber: " + rowNumber);
            ImageView emptyImageView = new ImageView(this);
            return new Letter(emptyImageView);
        }

        char[] vowel = {'a', 'e', 'ı', 'i', 'o', 'ö', 'u', 'ü'};
        char[] consonant = {'b', 'c', 'ç', 'd', 'f', 'g', 'ğ', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'r', 's', 'ş', 't', 'v', 'y', 'z'};
        char[] harfler = {'a', 'b', 'c', 'ç', 'd', 'e', 'f', 'g', 'ğ', 'h', 'i', 'ı', 'j', 'k', 'l', 'm', 'n', 'o', 'ö', 'p', 'r', 's', 'ş', 't', 'u', 'ü', 'v', 'y', 'z'};


        Field[] fields = R.drawable.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().length() < 3) {
                try {
                    int resourceId = fields[i].getInt(null);
                    letterImages.add(resourceId);
                } catch (Exception e) {
                }
            }
        }
        Random rand = new Random();
        int randomNumber;
        char randomLetter;

        // Sesli ve sessiz harfleri doğru oranlarda seçmek için:
        double randomDouble = rand.nextDouble();
        if (randomDouble < 0.3) { // %30 olasılıkla sesli harf seç
            randomNumber = rand.nextInt(vowel.length);
            randomLetter = vowel[randomNumber];
        } else { // %70 olasılıkla sessiz harf seç
            randomNumber = rand.nextInt(consonant.length);
            randomLetter = consonant[randomNumber];
        }


        Letter letter = new Letter(randomLetter);

        ImageView imageView = new ImageView(this);
        letter.setImage(imageView);
        letter.getImage().setImageResource(letterImages.get(karakterIndexBul(harfler, randomLetter)));


        letterList.add(letter);
        return letter;
    }


}


//************************************************************************


/*
public class MainActivity extends AppCompatActivity {

    List<Integer> letterImages = new ArrayList<Integer>();
    List<Letter> letterList = new ArrayList<Letter>();
    List<ImageButton> imageButtonList = new ArrayList<ImageButton>();


    String text = "";


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        ImageButton cancel_button = findViewById(R.id.cancel_button);
        ImageButton submit_button = findViewById(R.id.submit_button);
        getSupportActionBar().hide();
        createFirstLetters();
        for (Letter letter : letterList) {//Burası LetterList içerindeki imageler dönücek şekilde düzeltilicek
            letter.getImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println(letter.getLetter());
                    System.out.println(letter.isClick());

                    if (letter.isClick() == true) {
                        text = text.replaceFirst(String.valueOf(letter.getLetter()), "");
                        textView.setText(text);
                        letter.getImage().setColorFilter(null);
                        letter.setClick(false);
                    } else {
                        text = text + letter.getLetter();
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

            }
        });

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

                Letter letter = createLetter();

                GridLayout gridLayout = findViewById(R.id.gridLayout);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 135;
                params.height = 135;
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);
                //System.out.println("row:" + i + " column:" + j);
                params.setGravity(Gravity.CENTER);
                letter.getImage().setScaleType(ImageView.ScaleType.CENTER_CROP);
                //letter.getImage().setLayoutParams(params);
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


    public Letter createLetter() {
        char[] vowel = {'a', 'e', 'ı', 'i', 'o', 'ö', 'u', 'ü'};
        char[] consonant = {'b', 'c', 'ç', 'd', 'f', 'g', 'ğ', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'r', 's', 'ş', 't', 'v', 'y', 'z'};
        char[] harfler = {'a', 'b', 'c', 'ç', 'd', 'e', 'f', 'g', 'ğ', 'h', 'i', 'ı', 'j', 'k', 'l', 'm', 'n', 'o', 'ö', 'p', 'r', 's', 'ş', 't', 'u', 'ü', 'v', 'y', 'z'};


        Field[] fields = R.drawable.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().length() < 3) {
                try {
                    int resourceId = fields[i].getInt(null);
                    letterImages.add(resourceId);
                } catch (Exception e) {
                }
            }
        }
        Random rand = new Random();
        int randomNumber;
        char randomLetter;

        // Sesli ve sessiz harfleri doğru oranlarda seçmek için:
        double randomDouble = rand.nextDouble();
        if (randomDouble < 0.3) { // %30 olasılıkla sesli harf seç
            randomNumber = rand.nextInt(vowel.length);
            randomLetter = vowel[randomNumber];
        } else { // %70 olasılıkla sessiz harf seç
            randomNumber = rand.nextInt(consonant.length);
            randomLetter = consonant[randomNumber];
        }


        Letter letter = new Letter(randomLetter);

        ImageView imageView = new ImageView(this);
        letter.setImage(imageView);
        letter.getImage().setImageResource(letterImages.get(karakterIndexBul(harfler, randomLetter)));


        letterList.add(letter);
        return letter;
    }


}
*/