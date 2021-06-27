package com.example.cetty.derailer;

import android.graphics.Bitmap;

/**
 * Created by cetty on 29.07.16.
 */
public class PlayerSprite {

    private Bitmap bmp;
    private GameView gameView;
    private int width;
    private int screenWidth;
    private int edge = 0;
    private int x;
    private int y;

    public PlayerSprite(GameView gameView, Bitmap bmp, int x, int y){
        this.gameView = gameView;
        this.x = x;
        this.y = y;
        this.bmp = bmp;
        this.edge = gameView.edge;
    }

}
