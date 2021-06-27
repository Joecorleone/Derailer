package com.example.cetty.derailer.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;


import com.example.cetty.derailer.GameView;
import com.example.cetty.derailer.R;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by cetty on 29.07.16.
 */
public class Smoke {
    private Bitmap bmp;
    private GameView gameView;
    int step = 0;
    int x = 0;
    int y = 0;
    float rot = 0;

    public Smoke(GameView gameView, int x, int y, float rot) {
        this.gameView = gameView;
        this.bmp = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.smoke2);
        this.x = x;
        this.y = y;
        this.rot = rot;
    }

    public Bitmap getBmp(){ return this.bmp;}

    public void nextStep(){this.step += 1;}
}