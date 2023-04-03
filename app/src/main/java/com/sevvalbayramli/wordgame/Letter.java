package com.sevvalbayramli.wordgame;

import android.widget.ImageButton;

public class Letter {
    private char letter;
    private ImageButton image;
    private int row;
    private int column;

    public Letter(char letter) {
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public ImageButton getImage() {
        return image;
    }

    public void setImage(ImageButton image) {
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
}
