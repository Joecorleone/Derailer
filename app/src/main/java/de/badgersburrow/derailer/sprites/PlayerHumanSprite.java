package de.badgersburrow.derailer.sprites;

import android.content.Context;

import de.badgersburrow.derailer.GameView;
import de.badgersburrow.derailer.R;
import de.badgersburrow.derailer.objects.GameTheme;

public class PlayerHumanSprite extends PlayerSprite{


    public PlayerHumanSprite(GameView gameView, GameTheme theme, int num, int color, int tiles) {
        super(gameView, theme, num, color, tiles);
    }

    @Override
    public boolean isKI() {
        return false;
    }

    public String getLabel(Context context){
        return context.getString(R.string.label_player, num);
    }
}
