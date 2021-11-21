package de.badgersburrow.derailer.objects;

import de.badgersburrow.derailer.Keys;
import de.badgersburrow.derailer.MainActivity;

import java.io.Serializable;

/**
 * Created by reim on 18.02.17.
 */

public class PlayerSelection implements Serializable{

    private int color;
    String selection;

    public PlayerSelection(int color){
        this.color = color;
        this.selection = Keys.unselected;
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
        return this.selection.equals(Keys.unselected);
    }

    public boolean isPlayer(){
        return this.selection.equals(Keys.player);
    }

    public boolean isEasyAI(){
        return this.selection.equals(Keys.aiEasy);
    }

    public boolean isNormalAI(){
        return this.selection.equals(Keys.aiNormal);
    }

    public boolean isHardAI(){
        return this.selection.equals(Keys.aiHard);
    }
}
