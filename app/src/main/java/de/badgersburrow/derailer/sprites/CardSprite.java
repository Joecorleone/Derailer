package de.badgersburrow.derailer.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.List;

import de.badgersburrow.derailer.GameView;

/**
 * Created by cetty on 29.07.16.
 */
public abstract class CardSprite extends BaseSprite{
    protected Bitmap topBmp;
    private int currentFrame = 0;
    protected int width;
    private int screenWidth;
    protected int rotation = 0;
    protected List<Integer> ways;
    Rect dest;


    public CardSprite(GameView gameView, Bitmap bottomBmp, Bitmap topBmp, List<Integer> ways, int rotation){
        super(gameView, bottomBmp);
        this.topBmp = topBmp;
        this.ways = ways;
        this.rotation = rotation;
    }

    public void drawTopBottom(Canvas canvas){
        int rotate = 90*rotation;
        canvas.save();
        canvas.rotate(rotate, x + (width / 2), y + (width / 2));
        canvas.drawBitmap(bmp, null, dest, null);
        canvas.drawBitmap(topBmp, null, dest, null);
        canvas.restore();
    }

    public void drawBottom(Canvas canvas){
        int rotate = 90*rotation;
        canvas.save();
        canvas.rotate(rotate, x + (width / 2), y + (width / 2));
        canvas.drawBitmap(bmp, null, dest, null);
        canvas.restore();
    }

    public void drawTop(Canvas canvas){
        int rotate = 90*rotation;
        canvas.save();
        canvas.rotate(rotate, x + (width / 2), y + (width / 2));
        canvas.drawBitmap(topBmp, null, dest, null);
        canvas.restore();
    }

    public boolean isTouched(float x2, float y2){
        return x2 > x && x2 < x + width && y2 > y && y2 < y + width;
    }

    public int getRotation(){
        return rotation;
    }

    public void setRotation(int i){
        rotation = i;
    }

    public Bitmap getBottomBMP(){
        return bmp;
    }

    public Bitmap getTopBMP() { return topBmp;}

    public void rotate(){
        rotation  += 1;
        rotation = rotation%4;
    }

    public List<Integer> getWays(){
        return this.ways;
    }
}
