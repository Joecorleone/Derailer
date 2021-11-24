package de.badgersburrow.derailer.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

import de.badgersburrow.derailer.GameView;
import de.badgersburrow.derailer.R;
import de.badgersburrow.derailer.Utilities;

public class ExplosionSprite extends BaseSprite{

    int num;
    int count;
    final static int stepSize = 3;
    boolean finished;

    static ArrayList<Integer> explosion_list = new ArrayList<>();
    static{
        explosion_list.add(R.drawable.explosion_01);
        explosion_list.add(R.drawable.explosion_02);
        explosion_list.add(R.drawable.explosion_03);
        explosion_list.add(R.drawable.explosion_04);
        explosion_list.add(R.drawable.explosion_05);
        explosion_list.add(R.drawable.explosion_06);
    }

    public ExplosionSprite(GameView gameView, int x, int y){
        super(gameView, null, x, y);
        this.num = 0;
        this.count = 1;
        this.finished = false;
        this.bmp = Utilities.drawableToBitmap(gameView.getContext().getDrawable(explosion_list.get(num)));
    }

    @Override
    public void onDraw(Canvas canvas){
        if (finished){
            return;
        }

        if (count%stepSize == 0){
            num++;
            if (num>=explosion_list.size()){
                finished = true;
                return;
            }
            this.bmp = Utilities.drawableToBitmap(gameView.getContext().getDrawable(explosion_list.get(num)));
        }

        float scaleFactor = gameView.getScaleFactor();

        int scaledWidth = Math.round(scaleFactor* bmp.getWidth());
        int scaledHeight = Math.round(scaleFactor* bmp.getHeight());

        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor,scaleFactor);
        //matrix.postRotate(rotation, scaledWidth/2, scaledHeight/2);
        matrix.postTranslate(x-scaledWidth/2,y-scaledHeight/2);
        canvas.drawBitmap(bmp, matrix, null);

        count++;
    }

    public boolean isFinished() {
        return finished;
    }
}
