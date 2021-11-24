package de.badgersburrow.derailer.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import de.badgersburrow.derailer.GameView;
import de.badgersburrow.derailer.R;

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