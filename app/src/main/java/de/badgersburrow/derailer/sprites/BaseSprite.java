package de.badgersburrow.derailer.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import de.badgersburrow.derailer.GameView;

abstract class BaseSprite {

    GameView gameView;
    Bitmap bmp;
    int edge;
    int x = -1;
    int y = -1;

    public BaseSprite(GameView gameView, Bitmap bmp){
        this.gameView = gameView;
        this.edge = gameView.getEdge();
        this.bmp = bmp;
    }

    public BaseSprite(GameView gameView, Bitmap bmp, int x, int y){
        this.gameView = gameView;
        this.edge = gameView.getEdge();
        this.bmp = bmp;
        this.x = x;
        this.y = y;
    }


    abstract public void onDraw(Canvas canvas);
}
