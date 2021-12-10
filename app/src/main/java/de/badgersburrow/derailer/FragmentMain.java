package de.badgersburrow.derailer;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Point;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionSet;

import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import de.badgersburrow.derailer.databinding.DialogFewPlayerBinding;
import de.badgersburrow.derailer.databinding.DialogSoundSettingBinding;
import de.badgersburrow.derailer.databinding.FragmentGameSettingsBinding;
import de.badgersburrow.derailer.databinding.FragmentMainBinding;
import de.badgersburrow.derailer.views.DialogButton;
import de.badgersburrow.derailer.views.GameSignButton;

public class FragmentMain extends Fragment implements OnClickListener {

    FragmentMainBinding binding;

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

        binding = FragmentMainBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();

        //View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //mSceneRoot = rootView.findViewById(R.id.activity_main);

        ActivityMain.customtf_normal = Typeface.createFromAsset(getContext().getAssets(), "fonts/Acme-Regular.ttf" );
        ActivityMain.customtf_bold = Typeface.create(ActivityMain.customtf_normal, Typeface.BOLD);

        binding.bLocal.setOnClickListener(this);
        binding.bLocal.setTypeface(ActivityMain.customtf_normal);
        binding.bLocal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    getAct().playSoundThump();
                }
                return false;
            }
        });
        binding.bLocal.setBackgroundResource(R.drawable.menu_cart01);

        binding.bTheme.setOnClickListener(this);
        binding.bTheme.setTypeface(ActivityMain.customtf_normal);
        binding.bTheme.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    getAct().playSoundThump();
                }
                return false;
            }
        });
        binding.bTheme.setBackgroundResource(R.drawable.menu_cart02);

        Display display = getAct().getWindowManager().getDefaultDisplay();
        Point size = new Point(); display.getSize(size);
        int parentWidth = size.x;
        int parentHeight = size.y;

        RelativeLayout.LayoutParams bStart_params = (RelativeLayout.LayoutParams)
                binding.bLocal.getLayoutParams();
        binding.bLocal.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int bStart_width = binding.bLocal.getMeasuredWidth();
        bStart_params.setMargins(-1 * bStart_width, 0, 0, 0);
        binding.bLocal.setLayoutParams(bStart_params);

        ObjectAnimator bStart_animX = ObjectAnimator.ofFloat(binding.bLocal,
                "x",-1 * bStart_width,  parentWidth/2-bStart_width/2);
        bStart_animX.setDuration(1200);
        bStart_animX.start();

        RelativeLayout.LayoutParams bTheme_params = (RelativeLayout.LayoutParams)
                binding.bTheme.getLayoutParams();
        binding.bTheme.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int btheme_width = binding.bTheme.getMeasuredWidth();
        bTheme_params.setMargins(-1 * btheme_width, 0, 0, 0);
        binding.bTheme.setLayoutParams(bTheme_params);
        binding.bTheme.setX(-1.2f * btheme_width);
        ObjectAnimator bTheme_animX = ObjectAnimator.ofFloat(binding.bTheme,
                "x",-1 * btheme_width,  parentWidth/2-btheme_width/2);
        bTheme_animX.setDuration(1200);
        bTheme_animX.setStartDelay(500);
        bTheme_animX.start();

        binding.gsbExit.setOnClickListener(this);
        binding.gsbExit.setSoundListener(getAct());

        binding.gsbSound.setOnClickListener(this);
        binding.gsbSound.setSoundListener(getAct());

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
                getAct().onBackPressed();
                break;
            case R.id.gsb_sound:
                showDialog();
                break;

        }
    }


    /*
    android:text="@string/music"
    app:track="@drawable/switch_track"
    android:thumb="@drawable/switch_thumb"
     */

    public void showDialog() {

        final Dialog dialog = new Dialog(getContext(), R.style.AlertDialogCustom);

        DialogSoundSettingBinding binding = DialogSoundSettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        dialog.setContentView(view);
        //dialog.setContentView(R.layout.dialog_few_player);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        binding.tvTitle.setTypeface(ActivityMain.customtf_normal);
        binding.tvMusic.setTypeface(ActivityMain.customtf_normal);


        //sl_music.setTypeface(ActivityMain.customtf_normal);
        //sl_music.setChecked(getAct().SP.getBoolean(Keys.setting_music, Keys.setting_music_default));
        try {
            int v = getAct().SP.getInt(Keys.setting_music_volume, Keys.setting_music_volume_default);
            binding.sbMusic.setProgress(v);
            if (v==0){
                binding.sbMusic.setThumb(getResources().getDrawable(R.drawable.switch_thumb_unchecked));
            } else {
                binding.sbMusic.setThumb(getResources().getDrawable(R.drawable.switch_thumb_checked));
            }
        }catch (ClassCastException e){
        }

        binding.sbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress==0 && progressChangedValue>0){
                    seekBar.setThumb(getResources().getDrawable(R.drawable.switch_thumb_unchecked));
                } else if (progressChangedValue == 0){
                    seekBar.setThumb(getResources().getDrawable(R.drawable.switch_thumb_checked));
                }

                progressChangedValue = progress;
                getAct().SPE.putInt(Keys.setting_music_volume, progress);
                getAct().SPE.apply();
                getAct().musicVolume(progress);

                if (progress==0){
                    seekBar.setThumb(getResources().getDrawable(R.drawable.switch_thumb_unchecked));
                } else {
                    seekBar.setThumb(getResources().getDrawable(R.drawable.switch_thumb_checked));
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(MainActivity.this, "Seek bar progress is :" + progressChangedValue,
                //        Toast.LENGTH_SHORT).show();
            }
        });
        binding.sbMusic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        getAct().playSoundSwitch();
                        break;
                }
                return false;
            }
        });

        binding.swSfx.setTypeface(ActivityMain.customtf_normal);
        binding.swSfx.setChecked(getAct().SP.getBoolean(Keys.setting_sfx, Keys.setting_sfx_default));
        binding.swSfx.setOnCheckedChangeListener((compoundButton, b) -> {
            getAct().playSoundSwitch();
            getAct().SPE.putBoolean(Keys.setting_sfx, b);
            getAct().SPE.apply();
        });

        binding.dbOk.setOnClickListener(v -> dialog.dismiss());
        binding.dbOk.setSoundListener(getAct());

        dialog.show();
    }

    ActivityMain getAct(){
        return (ActivityMain) getActivity();
    }



}


