package de.badgersburrow.derailer.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import de.badgersburrow.derailer.GameView;

/**
 * Created by cetty on 29.07.16.
 * This is an indicator for a possible start position selectable by the player
 */
public class StartSprite extends BaseSprite{

    private int currentFrame = 0;
    private int width;
    private int screenWidth;
    private int x_index = 0;
    private int y_index = 0;
    private int pos = 0;
    private int tiles = 4;
    private Rect dest;

    public StartSprite(GameView gameView, Bitmap bmp, int x, int y, int pos, int tiles){
        super(gameView, bmp);
        this.x_index = x;
        this.y_index = y;
        this.pos = pos;
        this.bmp = bmp;
        this.tiles = tiles;
    }

    @Override
    public void onDraw(Canvas canvas){
        if (dest == null){
            screenWidth = gameView.getWidth();
            width = (screenWidth - (2 * edge)) / 6;
            x = edge + (this.x_index) * width + width / 2 - bmp.getHeight() / 2;
            y = edge + (this.y_index) * width + width / 2 - bmp.getHeight() / 2;
            if (tiles == 4){
                if (pos == 0) y = y - width / 2;
                else if (pos == 1) x = x + width / 2;
                else if (pos == 2) y = y + width / 2;
                else x = x - width / 2;
            } else {
                if (pos == 6) {
                    y = y + width / 4;
                    x = x - width / 2;
                } else if (pos == 7) {
                    y = y - width / 4;
                    x = x - width / 2;
                } else if (pos == 0) {
                    y = y - width / 2;
                    x = x - width / 4;
                } else if (pos == 1) {
                    y = y - width / 2;
                    x = x + width / 4;
                } else if (pos == 2) {
                    y = y - width / 4;
                    x = x + width / 2;
                } else if (pos == 3) {
                    y = y + width / 4;
                    x = x + width / 2;
                } else if (pos == 4) {
                    y = y + width / 2;
                    x = x + width / 4;
                } else if (pos == 5) {
                    y = y + width / 2;
                    x = x - width / 4;
                }
            }

            dest = new Rect(x, y, x+bmp.getHeight(), y+bmp.getHeight());
        }

        canvas.drawBitmap(bmp, null, dest, null);
    }

    public int getXIndex(){
        return this.x_index;
    }

    public int getYIndex(){
        return this.y_index;
    }

    public int getPos(){
        return this.pos;
    }

    public boolean isTouched(float x2, float y2){
        return x2 > x && x2 < x + bmp.getHeight() && y2 > y && y2 < y + bmp.getHeight();
    }
}
