package de.badgersburrow.derailer.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import de.badgersburrow.derailer.GameView;

public class ObstacleCardSprite extends BaseSprite{

    protected int xIndex;
    protected int yIndex;
    protected int width;
    Rect dest;

    public ObstacleCardSprite(GameView gameView, Bitmap bmp, int x, int y){
        super(gameView, bmp);
        this.xIndex = x;
        this.yIndex = y;
    }

    @Override
    public void onDraw(Canvas canvas){
        if (dest == null){
            int screenWidth = gameView.getWidth();
            this.width = (screenWidth - (2 * edge)) / 6;
            this.x = edge + (this.xIndex) * width ;
            this.y = edge + (this.yIndex) * width;
            this.dest = new Rect(x, y, x+ width, y+ width);
        }

        canvas.drawBitmap(bmp, null, dest, null);
    }

    public int getXIndex() {
        return xIndex;
    }

    public int getYIndex() {
        return yIndex;
    }
}
