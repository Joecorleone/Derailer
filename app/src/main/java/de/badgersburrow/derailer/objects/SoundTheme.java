package de.badgersburrow.derailer.objects;

import android.content.Context;
import android.media.SoundPool;

import de.badgersburrow.derailer.ActivityMain;
import de.badgersburrow.derailer.Keys;

public class SoundTheme {

    private int id;
    private int sound_id_move = 0;
    private int sound_id_horn = 0;

    public SoundTheme(int id, Context context, SoundPool soundPool, int sound_res_move, int sound_res_horn) {
        sound_id_move = soundPool.load(context, sound_res_move, 1);
        sound_id_horn = soundPool.load(context, sound_res_horn, 1);
    }

    public int playMoving(SoundPool soundPool, float volume){

        float leftVolumn = volume;
        float rightVolumn = volume;
        // Play sound objects destroyed. Returns the ID of the new stream.
        if (sound_id_move == 0){
            return 0;
        }
        return soundPool.play(sound_id_move,leftVolumn, rightVolumn, 1, -1, 1f);
    }

    public void playHorn(SoundPool soundPool, float volume) {
        float leftVolumn = volume;
        float rightVolumn = volume;

        if (sound_id_horn != 0){
            soundPool.play(sound_id_horn, leftVolumn, rightVolumn, 1, 0, 1f);
        }

    }



}
