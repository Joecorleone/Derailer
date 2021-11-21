package de.badgersburrow.derailer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class ObstacleCardSprite{

    protected Bitmap bmp;
    protected GameView gameView;
    protected int xIndex = -1;
    protected int yIndex = -1;
    protected int width;
    Rect dest;
    protected int edge = 0;

    public ObstacleCardSprite(GameView gameView, Bitmap bmp, int x, int y){
        this.gameView = gameView;
        this.bmp = bmp;
        this.xIndex = x;
        this.yIndex = y;
        this.edge = gameView.edge;
    }

    public void onDraw(Canvas canvas){
        int screenWidth = gameView.getWidth();
        this.width = (screenWidth - (2 * edge)) / 6;
        int x = edge + (this.xIndex) * width ;
        int y = edge + (this.yIndex) * width;
        this.dest = new Rect(x, y, x+ width, y+ width);

        canvas.drawBitmap(bmp, null, dest, null);

    }

}
