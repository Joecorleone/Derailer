package com.example.cetty.derailer.objects;

import com.example.cetty.derailer.MainActivity;

import java.io.Serializable;

/**
 * Created by reim on 18.02.17.
 */

public class PlayerSelection implements Serializable{

    private int color;
    String selection;

    public PlayerSelection(int color){
        this.color = color;
        this.selection = MainActivity.keyUnselected;
    }

    public int getColor(){
        return color;
    }

    public void setSelection(String key){
        this.selection = key;
    }

    public String getSelection(){
        return selection;
    }

    public boolean isUnselected(){
        return this.selection.equals(MainActivity.keyUnselected);
    }

    public boolean isPlayer(){
        return this.selection.equals(MainActivity.keyPlayer);
    }

    public boolean isEasyAI(){
        return this.selection.equals(MainActivity.keyKIEasy);
    }

    public boolean isNormalAI(){
        return this.selection.equals(MainActivity.keyKINormal);
    }

    public boolean isHardAI(){
        return this.selection.equals(MainActivity.keyKIHard);
    }
}
