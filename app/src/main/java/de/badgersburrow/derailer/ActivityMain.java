package de.badgersburrow.derailer;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ActivityMain extends AppCompatActivity{

    SharedPreferences SP;
    SharedPreferences.Editor SPE;

    public static Typeface customtf_normal;
    public static Typeface customtf_bold;

    private SoundPool soundPool;

    private AudioManager audioManager;
    // Maximumn sound stream.
    private static final int MAX_STREAMS = 5;

    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;

    private boolean loaded;

    private int soundIdButton;
    private int soundIdSign;
    private float volume;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utilities.FullScreencall(this);

        customtf_normal = Typeface.createFromAsset(getAssets(), "fonts/Acme-Regular.ttf" );
        customtf_bold = Typeface.create(Typeface.createFromAsset(getAssets(),"fonts/Acme-Regular.ttf"), Typeface.BOLD);

        SP = PreferenceManager.getDefaultSharedPreferences(this);
        SPE = SP.edit();

        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Current volumn Index of particular stream type.
        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);

        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(streamType);

        // Volumn (0 --> 1)
        this.volume = currentVolumeIndex / maxVolumeIndex;

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
                loaded = true;
            }
        });

        // Load sound file (destroy.wav) into SoundPool.
        this.soundIdButton = this.soundPool.load(this, R.raw.sound_button01,1);

        // Load sound file (gun.wav) into SoundPool.
        this.soundIdSign = this.soundPool.load(this, R.raw.sound_sign01,1);
    }

    // When users click on a button
    public void playSoundButton()  {
        if(loaded)  {
            float leftVolumn = volume;
            float rightVolumn = volume;

            // Play sound objects destroyed. Returns the ID of the new stream.
            int streamId = this.soundPool.play(this.soundIdButton,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }

    // When users click on a sign
    public void playSoundSign()  {
        if(loaded)  {
            float leftVolumn = volume;
            float rightVolumn = volume;

            // Play sound objects destroyed. Returns the ID of the new stream.
            int streamId = this.soundPool.play(this.soundIdSign,leftVolumn, rightVolumn, 1, 0, 1f);
        }
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
}
