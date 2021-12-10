package de.badgersburrow.derailer;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.HashMap;

import de.badgersburrow.derailer.databinding.ActivityMainBinding;
import de.badgersburrow.derailer.objects.SoundListener;
import de.badgersburrow.derailer.objects.SoundRef;
import de.badgersburrow.derailer.objects.SoundTheme;

public class ActivityMain extends AppCompatActivity implements SoundListener {

    private final static String TAG = "ActivityMain";

    private ActivityMainBinding binding;

    SharedPreferences SP;
    SharedPreferences.Editor SPE;

    public static Typeface customtf_normal;
    public static Typeface customtf_bold;

    MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private AudioManager audioManager;
    // Maximumn sound stream.
    private static final int MAX_STREAMS = 5;

    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;

    private float volume;

    private HashMap<Integer, SoundRef> soundRefs;
    private HashMap<Integer, SoundTheme> soundThemes;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Utilities.FullScreencall(this);


        SP = PreferenceManager.getDefaultSharedPreferences(this);
        SPE = SP.edit();


        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Current volumn Index of particular stream type.
        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);

        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(streamType);

        // Volumn (0 --> 1)
        this.volume = 1;//currentVolumeIndex / maxVolumeIndex;

        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        this.setVolumeControlStream(streamType);

        // For Android SDK >= 21
        if (Build.VERSION.SDK_INT >= 21 ) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }
        // for Android SDK < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        // When Sound Pool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundLoaded(sampleId);
            }
        });

        // Load sound files into SoundPool.
        soundLoad(R.raw.sound_button01,1);
        soundLoad(R.raw.sound_sign01,1);
        soundLoad(R.raw.sound_explosion01,1);
        soundLoad(R.raw.sound_swipe01,1);
        soundLoad(R.raw.sound_swipe02,1);
        soundLoad(R.raw.sound_klonk01,1);
        soundLoad(R.raw.sound_thump01,1);
        soundLoad(R.raw.sound_option01,1);
        soundLoad(R.raw.sound_dialog01,1);
        soundLoad(R.raw.sound_metal_pickup01,1);
        soundLoad(R.raw.sound_metal_drop02,1);
        soundLoad(R.raw.sound_victory01,1);

        // combine sounds for themes
        soundThemes = new HashMap<>();
        soundThemes.put(0, new SoundTheme(0, this, this.soundPool, R.raw.sound_train_steam01, R.raw.sound_horn_steam01));
        soundThemes.put(1, new SoundTheme(1, this, this.soundPool, R.raw.sound_train_elec01, R.raw.sound_horn_elec01));
        soundThemes.put(2, new SoundTheme(2, this, this.soundPool, R.raw.sound_car_vintage01, R.raw.sound_horn_vintage01));
        soundThemes.put(3, new SoundTheme(3, this, this.soundPool, R.raw.sound_car_cadillac01, R.raw.sound_horn_cadillac01));


    }

    private void soundLoad(int resId, int priority){
        if (soundRefs == null){
            soundRefs = new HashMap<>();
        }
        soundRefs.put(resId, new SoundRef(resId, this.soundPool.load(this, resId, priority)));
    }

    private void soundLoaded(int resId){
        for (SoundRef soundRef : soundRefs.values()){
            if (soundRef.getSoundId() == resId){
                soundRef.setLoaded(true);
            }
        }
        //if (soundRefs.containsKey(resId)){
        //    soundRefs.get(resId).setLoaded(true);
        //}
    }


    public void musicPrepare(){
        mediaPlayer = MediaPlayer.create(this, R.raw.music_background01);
        mediaPlayer.setLooping(true); // Set looping
        //mediaPlayer.setVolume(volume*.5f, volume*.5f);
    }


    public void musicPlay(){
        if (mediaPlayer == null){
            musicPrepare();
            musicVolume();
        }
        if (mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    public void musicVolume(){
        int v = Keys.setting_music_volume_default;
        try {
            v = SP.getInt(Keys.setting_music_volume, Keys.setting_music_volume_default);
        }catch (ClassCastException e){
            Log.d(TAG, "setting_music_volume - wrong val");
        }
        if (mediaPlayer != null){
            mediaPlayer.setVolume(volume*v/100,volume*v/100);
            if (v <= 0){
                mediaPlayer.pause();
            } else if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }
    public void musicVolume(int v){
        if (mediaPlayer != null){
            mediaPlayer.setVolume(volume*v/100,volume*v/100);
            if (v <= 0){
                mediaPlayer.pause();
            } else if (!mediaPlayer.isPlaying()) {
                musicPrepare();
                mediaPlayer.start();
            }
        }
    }
    public void musicPause(){
        if (mediaPlayer != null){
            mediaPlayer.pause();
        }
    }

    public void musicStop(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // When users click on a button
    public void playSoundButton(){
        playSoundEffect(R.raw.sound_button01, false);
    }

    // When users click on a sign
    public void playSoundSign(){
        playSoundEffect(R.raw.sound_sign01, false);
    }

    // When an explosion occurs
    public void playSoundExplosion(){
        playSoundEffect(R.raw.sound_explosion01, false);
    }

    // When an option is turned
    public void playSoundOption(){
        playSoundEffect(R.raw.sound_option01, false);
    }

    // When a switch is changed
    public void playSoundSwitch(){
        playSoundEffect(R.raw.sound_swipe01, false);
    }

    // When a switch is changed
    public void playSoundToggle(){
        playSoundEffect(R.raw.sound_klonk01, false);
    }

    // When a switch is changed
    public void playSoundSwosh(){
        playSoundEffect(R.raw.sound_swipe02, false);
    }

    // When a menu item is selected
    public void playSoundThump(){
        playSoundEffect(R.raw.sound_thump01, false);
    }

    // When is picked up
    public void playSoundPickup(){
        playSoundEffect(R.raw.sound_metal_pickup01, false);
    }

    // When a player is dropped on cart
    public void playSoundDrop(){
        playSoundEffect(R.raw.sound_metal_drop02, false);
    }

    // When a dialog is closed
    public void playSoundDialog(){
        playSoundEffect(R.raw.sound_dialog01, false);
    }

    // When a dialog is closed
    public void playSoundVictory(){
        playSoundEffect(R.raw.sound_victory01, false);
    }

    // When a dialog is closed
    public void playSoundHorn(int theme){
        if (soundThemes.containsKey(theme) && SP.getBoolean(Keys.setting_sfx, Keys.setting_sfx_default)){
            soundThemes.get(theme).playHorn(soundPool, volume);
        }
    }

    public int playSoundMoving(int theme){
        if (soundThemes.containsKey(theme) && SP.getBoolean(Keys.setting_sfx, Keys.setting_sfx_default)){
            return soundThemes.get(theme).playMoving(soundPool, volume);
        }
        return 0;
    }


    public int playSoundEffect(int resId, boolean loop){
        if(soundRefs.containsKey(resId) && soundRefs.get(resId).isLoaded() && SP.getBoolean(Keys.setting_sfx, Keys.setting_sfx_default))  {
            float leftVolumn = volume;
            float rightVolumn = volume;
            // Play sound objects destroyed. Returns the ID of the new stream.
            if (loop){
                return this.soundPool.play(soundRefs.get(resId).getSoundId(),leftVolumn, rightVolumn, 1, -1, 1f);
            } else {
                return this.soundPool.play(soundRefs.get(resId).getSoundId(),leftVolumn, rightVolumn, 1, 0, 1f);
            }
        }
        return 0;
    }

    public void pauseSoundStream(int streamId){
        this.soundPool.pause(streamId);
    }

    public void resumeSoundStream(int streamId){
        this.soundPool.resume(streamId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    public void showThemeSelection(){
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_theme_selection);
    }

    public void showGameMenu(){
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.navigation_game_settings);
    }

    public void showGame(Bundle b){
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.popBackStack();
        navController.navigate(R.id.navigation_game, b);
    }

    public void showGameOver(Bundle b){
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.popBackStack();
        navController.navigate(R.id.navigation_game_over, b);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        musicPlay();
    }



    @Override
    public void onPause(){
        musicStop();
        super.onPause();
    }
}
