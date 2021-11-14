package de.badgersburrow.derailer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.List;

/**
 * Created by cetty on 30.07.16.
 */
public class ChoiceCardSprite extends CardSprite {

    private int pos = 0;

    public ChoiceCardSprite(GameView gameView, Bitmap bottomBmp, Bitmap topBmp, List<Integer> ways, int rotation){
        super(gameView, bottomBmp, topBmp, ways, rotation);
        this.pos = 0;
    }

    public void onDraw(Canvas canvas){
        if (this.x == -1){
            int screenWidth = this.gameView.getWidth();
            this.width = (screenWidth - (2 * this.edge)) / 6;
            this.x = screenWidth/2+pos*(width+20)-50;
            this.y = canvas.getWidth()+gameView.bottomMargin;
            this.dest = new Rect(x, y, x+ width, y+width);
        }
        super.drawTopBottom(canvas);
    }

    public int getPos() {
        return pos;
    }

    public ChoiceCardSprite getCopy(int pos){
        ChoiceCardSprite card = new ChoiceCardSprite(gameView, bottomBmp, topBmp, ways, rotation);
        card.pos = pos;
        return card;
    }
}
