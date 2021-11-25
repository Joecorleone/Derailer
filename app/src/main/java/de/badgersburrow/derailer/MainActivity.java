package de.badgersburrow.derailer;

import static de.badgersburrow.derailer.Keys.setting_music_default;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;

import androidx.appcompat.widget.SwitchCompat;
import androidx.transition.TransitionSet;

import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.badgersburrow.derailer.views.GameSignButton;

public class MainActivity extends Activity implements OnClickListener{

    Button bLocal;
    Button bTheme;

    SharedPreferences SP;
    SharedPreferences.Editor SPE;

    public static Typeface customtf_normal;
    public static Typeface customtf_bold;

    // Animation using transition
    TransitionSet mStaggeredTransition;
    ViewGroup mSceneRoot;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utilities.FullScreencall(this);

        customtf_normal = Typeface.createFromAsset(getAssets(), "fonts/Acme-Regular.ttf" );
        customtf_bold = Typeface.create(Typeface.createFromAsset(getAssets(),"fonts/Acme-Regular.ttf"), Typeface.BOLD);

        mSceneRoot = (ViewGroup) findViewById(R.id.activity_main);

        SP = PreferenceManager.getDefaultSharedPreferences(this);
        SPE = SP.edit();

        bLocal = (Button) findViewById(R.id.bLocal);
        bLocal.setOnClickListener(this);
        bLocal.setTypeface(customtf_normal);
        bTheme = (Button) findViewById(R.id.bTheme);
        bTheme.setOnClickListener(this);
        bTheme.setTypeface(customtf_normal);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point(); display.getSize(size);
        int parentWidth = size.x;
        int parentHeight = size.y;

        RelativeLayout.LayoutParams bStart_params = (RelativeLayout.LayoutParams)
                bLocal.getLayoutParams();
        bLocal.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int bStart_width = bLocal.getMeasuredWidth();
        bStart_params.setMargins(-1 * bStart_width, 0, 0, 0);
        bLocal.setLayoutParams(bStart_params);

        ObjectAnimator bStart_animX = ObjectAnimator.ofFloat(bLocal,
                "x",-1 * bStart_width,  parentWidth/2-bStart_width/2);
        bStart_animX.setDuration(1200);
        bStart_animX.start();

        RelativeLayout.LayoutParams bTheme_params = (RelativeLayout.LayoutParams)
                bTheme.getLayoutParams();
        bTheme.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int btheme_width = bTheme.getMeasuredWidth();
        bTheme_params.setMargins(-1 * btheme_width, 0, 0, 0);
        bTheme.setLayoutParams(bTheme_params);
        bTheme.setX(-1.2f * btheme_width);
        ObjectAnimator bTheme_animX = ObjectAnimator.ofFloat(bTheme,
                "x",-1 * btheme_width,  parentWidth/2-btheme_width/2);
        bTheme_animX.setDuration(1200);
        bTheme_animX.setStartDelay(500);
        bTheme_animX.start();

        GameSignButton gsb_exit = findViewById(R.id.gsb_exit);
        gsb_exit.setOnClickListener(this);

        GameSignButton gsb_sound = findViewById(R.id.gsb_sound);
        gsb_sound.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.bLocal:
                Intent startMenu = new Intent(this, StartMenuActivity.class);
                startActivity(startMenu);
                //this.finish();
                break;
            case R.id.bTheme:
                Intent theme = new Intent(this, ThemeActivity.class);
                startActivity(theme);
                break;
            case R.id.gsb_exit:
                this.finish();
                break;
            case R.id.gsb_sound:
                showDialog();
                break;

        }
    }

    public void showDialog() {

        final Dialog dialog = new Dialog(this, R.style.AlertDialogCustom);
        dialog.setContentView(R.layout.dialog_sound_setting);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_title = dialog.findViewById(R.id.tv_title);
        tv_title.setTypeface(MainActivity.customtf_normal);

        SwitchCompat cb_music = dialog.findViewById(R.id.sw_music);
        cb_music.setTypeface(MainActivity.customtf_normal);
        cb_music.setChecked(SP.getBoolean(Keys.setting_music, Keys.setting_music_default));
        cb_music.setOnCheckedChangeListener((compoundButton, b) -> {
            SPE.putBoolean(Keys.setting_music, b);
            SPE.apply();
        });

        SwitchCompat cb_sfx = dialog.findViewById(R.id.sw_sfx);
        cb_sfx.setTypeface(MainActivity.customtf_normal);
        cb_sfx.setChecked(SP.getBoolean(Keys.setting_sfx, Keys.setting_sfx_default));
        cb_sfx.setOnCheckedChangeListener((compoundButton, b) -> {
            SPE.putBoolean(Keys.setting_sfx, b);
            SPE.apply();
        });

        ImageView iv_ok = dialog.findViewById(R.id.iv_ok);
        iv_ok.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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


