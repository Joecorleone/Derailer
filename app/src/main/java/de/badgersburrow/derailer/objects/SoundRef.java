package de.badgersburrow.derailer.objects;

import de.badgersburrow.derailer.R;

public class SoundRef {

    int soundRes;
    int soundId;
    boolean loaded;

    public SoundRef(int soundRes, int soundId) {
        this.soundRes = soundRes;
        this.soundId = soundId;
    }

    public int getSoundId() {
        return soundId;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
