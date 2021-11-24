package de.badgersburrow.derailer.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.List;

import de.badgersburrow.derailer.GameView;

/**
 * Created by cetty on 30.07.16.
 */
public class ChoiceCardSprite extends CardSprite {

    private int pos = 0;
    private boolean show = false;

    public ChoiceCardSprite(GameView gameView, Bitmap bottomBmp, Bitmap topBmp, List<Integer> ways, int rotation){
        super(gameView, bottomBmp, topBmp, ways, rotation);
        this.pos = 0;
    }

    @Override
    public void onDraw(Canvas canvas){
        if (this.x == -1){
            int screenWidth = gameView.getWidth();
            this.width = (screenWidth - (2 * this.edge)) / 6;
            this.x = screenWidth/2+pos*(width+20)-50;
            this.y = canvas.getWidth()+gameView.getBottomMargin();
            this.dest = new Rect(x, y, x+ width, y+width);
        }
        super.drawTopBottom(canvas);

        if (show) {
            canvas.save();
            canvas.drawBitmap(gameView.getRotateIndicator(), null, dest, null);
            canvas.restore();
        }
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getPos() {
        return pos;
    }

    public ChoiceCardSprite getCopy(int pos){
        ChoiceCardSprite card = new ChoiceCardSprite(gameView, bmp, topBmp, ways, rotation);
        card.pos = pos;
        return card;
    }
}
