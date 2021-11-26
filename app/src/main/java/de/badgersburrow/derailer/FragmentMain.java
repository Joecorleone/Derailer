package de.badgersburrow.derailer;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionSet;

import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.badgersburrow.derailer.views.GameSignButton;

public class FragmentMain extends Fragment implements OnClickListener{

    Button bLocal;
    Button bTheme;


    // Animation using transition
    TransitionSet mStaggeredTransition;
    //ViewGroup mSceneRoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //mSceneRoot = rootView.findViewById(R.id.activity_main);

        ActivityMain.customtf_normal = Typeface.createFromAsset(getContext().getAssets(), "fonts/Acme-Regular.ttf" );
        ActivityMain.customtf_bold = Typeface.create(ActivityMain.customtf_normal, Typeface.BOLD);

        bLocal = rootView.findViewById(R.id.bLocal);
        bLocal.setOnClickListener(this);
        bLocal.setTypeface(ActivityMain.customtf_normal);
        bTheme = rootView.findViewById(R.id.bTheme);
        bTheme.setOnClickListener(this);
        bTheme.setTypeface(ActivityMain.customtf_normal);

        Display display = getAct().getWindowManager().getDefaultDisplay();
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

        GameSignButton gsb_exit = rootView.findViewById(R.id.gsb_exit);
        gsb_exit.setOnClickListener(this);

        GameSignButton gsb_sound = rootView.findViewById(R.id.gsb_sound);
        gsb_sound.setOnClickListener(this);

        return rootView;
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.bLocal:
                //Intent startMenu = new Intent(getAct(), FragmentGameSettings.class);
                //startActivity(startMenu);
                getAct().showGameMenu();
                //this.finish();
                break;
            case R.id.bTheme:
                //Intent theme = new Intent(getAct(), FragmentThemeSelection.class);
                //startActivity(theme);
                getAct().showThemeSelection();
                break;
            case R.id.gsb_exit:
                //this.finish();
                getAct().playSoundSign();
                getAct().onBackPressed();
                break;
            case R.id.gsb_sound:
                getAct().playSoundSign();
                showDialog();
                break;

        }
    }

    public void showDialog() {

        final Dialog dialog = new Dialog(getContext(), R.style.AlertDialogCustom);
        dialog.setContentView(R.layout.dialog_sound_setting);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_title = dialog.findViewById(R.id.tv_title);
        tv_title.setTypeface(ActivityMain.customtf_normal);

        SwitchCompat cb_music = dialog.findViewById(R.id.sw_music);
        cb_music.setTypeface(ActivityMain.customtf_normal);
        cb_music.setChecked(getAct().SP.getBoolean(Keys.setting_music, Keys.setting_music_default));
        cb_music.setOnCheckedChangeListener((compoundButton, b) -> {
            getAct().playSoundSwitch();
            getAct().SPE.putBoolean(Keys.setting_music, b);
            getAct().SPE.apply();
        });

        SwitchCompat cb_sfx = dialog.findViewById(R.id.sw_sfx);
        cb_sfx.setTypeface(getAct().customtf_normal);
        cb_sfx.setChecked(getAct().SP.getBoolean(Keys.setting_sfx, Keys.setting_sfx_default));
        cb_sfx.setOnCheckedChangeListener((compoundButton, b) -> {
            getAct().playSoundSwitch();
            getAct().SPE.putBoolean(Keys.setting_sfx, b);
            getAct().SPE.apply();
        });

        ImageView iv_ok = dialog.findViewById(R.id.iv_ok);
        iv_ok.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    ActivityMain getAct(){
        return (ActivityMain) getActivity();
    }



}


