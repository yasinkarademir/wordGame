package com.sevvalbayramli.wordgame;

import android.widget.ImageButton;
import android.widget.ImageView;

public class Letter {
    private char letter;
    private ImageView image;
    private int row;
    private int column;
    private boolean click;

    public Letter(char letter) {
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }
}