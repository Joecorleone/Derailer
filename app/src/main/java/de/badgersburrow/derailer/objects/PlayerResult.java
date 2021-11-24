package de.badgersburrow.derailer.objects;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;

import java.io.Serializable;

import de.badgersburrow.derailer.R;
import de.badgersburrow.derailer.Utilities;

/**
 * Created by reim on 18.02.17.
 */

public class PlayerResult implements Serializable{

    private int color;
    private int outCount;
    private int tileCount;
    private boolean KI;
    private int num;


    public PlayerResult(boolean ki, int num, int color, int outCount, int tileCount) {

        this.KI = ki;
        this.num = num;
        this.color = color;
        this.outCount = outCount;
        this.tileCount = tileCount;

    }

    public int getColor(){
        return color;
    }

    public int getOutCount() {
        return outCount;
    }

    public int getTileCount() {
        return tileCount;
    }

    public boolean isKI() {
        return KI;
    }

    public int getNum() {
        return num;
    }

    public String getLabel(Context context){
        if (this.KI){
            return context.getString(R.string.label_ai, num);
        } else {
            return context.getString(R.string.label_player, num);
        }
    }

    public Bitmap getBmpMain(Context context, int themeId){
        Resources res = context.getResources();
        TypedArray carts = res.obtainTypedArray(R.array.carts);
        Bitmap bitmap = Utilities.drawableToBitmap(carts.getDrawable(themeId));
        carts.recycle();
        return bitmap;
    }

    public Bitmap getBmpColor(Context context, int themeId){
        Resources res = context.getResources();
        TypedArray carts_color = res.obtainTypedArray(R.array.carts_color);
        Bitmap bitmap = Utilities.drawableToBitmap(carts_color.getDrawable(themeId));
        carts_color.recycle();
        return bitmap;
    }


}
