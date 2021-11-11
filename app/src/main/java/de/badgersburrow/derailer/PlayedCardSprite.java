package de.badgersburrow.derailer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.List;

/**
 * Created by cetty on 30.07.16.
 */
public class PlayedCardSprite extends CardSprite {

    private int xIndex = 0;
    private int yIndex = 0;
    private int rotation = 0;

    public PlayedCardSprite(GameView gameView, Bitmap bottomBmp, Bitmap topBmp,List<Integer> ways, int x, int y, int rotation){
        super(gameView, bottomBmp, topBmp, ways, rotation);
        this.xIndex = x;
        this.yIndex = y;
    }

    public void onDraw(Canvas canvas){
        if (x == -1) {
            int screenWidth = gameView.getWidth();
            this.width = (screenWidth - (2 * edge)) / 6;
            x = edge + (this.xIndex) * width ;
            y = edge + (this.yIndex) * width;
            this.dest = new Rect(x, y, x+ width, y+ width);
        }

        super.drawBottom(canvas);
    }

    public void onDrawTop(Canvas canvas){
        if (x == -1) {
            int screenWidth = gameView.getWidth();
            this.width = (screenWidth - (2 * edge)) / 6;
            x = edge + (this.xIndex) * width ;
            y = edge + (this.yIndex) * width;
            this.dest = new Rect(x, y, x+ width, y+ width);
        }

        super.drawTop(canvas);
    }
}
