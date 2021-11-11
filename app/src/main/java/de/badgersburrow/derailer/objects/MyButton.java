package de.badgersburrow.derailer.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import de.badgersburrow.derailer.GameView;

import java.util.List;

/**
 * Created by cetty on 30.07.16.
 */
public class MyButton {

    private Bitmap bmpActive;
    private Bitmap bmpDisabled;
    private GameView gameView;
    private int x;
    private int y;
    private List<Integer> ways;

    public MyButton(GameView gameView, Bitmap bmpActive, Bitmap bmpDisabled, int x, int y){
        this.gameView = gameView;
        this.bmpActive = bmpActive;
        this.bmpDisabled = bmpDisabled;
        this.x = x;
        this.y = y;
        this.ways = ways;
    }

    public void onDraw(Canvas canvas, boolean active){
        if (canvas.getWidth()< x+bmpActive.getWidth()){
            x = canvas.getWidth()/2 - bmpActive.getWidth()/2;
        }
        if (canvas.getHeight()< y+bmpActive.getHeight()){
            y = canvas.getHeight() - bmpActive.getHeight() - Math.round(30*gameView.getDensity());
        }

        Rect dest = new Rect(x, y, x+bmpActive.getWidth(), y+bmpActive.getHeight());
        if (active){
            canvas.drawBitmap(bmpActive, null, dest, null);
        } else {
            canvas.drawBitmap(bmpDisabled, null, dest, null);
        }

    }

    public boolean isTouched(float x2, float y2){
        return x2 > x && x2 < x + bmpActive.getWidth() && y2 > y && y2 < y + bmpActive.getHeight();
    }
}
