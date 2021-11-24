package de.badgersburrow.derailer.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.List;

import de.badgersburrow.derailer.GameView;

/**
 * Created by cetty on 30.07.16.
 */
public class PlayedCardSprite extends CardSprite {

    private int xIndex;
    private int yIndex;
    private int rotation = 0;

    public PlayedCardSprite(GameView gameView, Bitmap bottomBmp, Bitmap topBmp, List<Integer> ways, int x, int y, int rotation){
        super(gameView, bottomBmp, topBmp, ways, rotation);
        this.xIndex = x;
        this.yIndex = y;
    }

    @Override
    public void onDraw(Canvas canvas){
        if (x == -1) {
            int screenWidth = gameView.getWidth();
            this.width = (screenWidth - (2 * edge)) / 6;
            this.x = edge + (this.xIndex) * width ;
            this.y = edge + (this.yIndex) * width;
            this.dest = new Rect(x, y, x+ width, y+ width);
        }

        super.drawBottom(canvas);
    }

    public void onDrawTop(Canvas canvas){
        if (x == -1) {
            int screenWidth = gameView.getWidth();
            this.width = (screenWidth - (2 * edge)) / 6;
            this.x = edge + (this.xIndex) * width ;
            this.y = edge + (this.yIndex) * width;
            this.dest = new Rect(x, y, x+ width, y+ width);
        }

        super.drawTop(canvas);
    }
}
