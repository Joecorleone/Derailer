package com.example.cetty.derailer;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import android.view.Display;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnClickListener{

    Button bLocal;
    Button bTheme;

    public static Typeface customtf;

    // Animation using transition
    TransitionSet mStaggeredTransition;
    static ViewGroup mSceneRoot;

    public static String keyUnselected = "None";
    public static String keyPlayer = "Player";
    public static String keyKIEasy = "Easy";
    public static String keyKINormal = "Normal";
    public static String keyKIHard = "Hard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utilities.FullScreencall(this);

        customtf = Typeface.createFromAsset(getAssets(), "fonts/Acme-Regular.ttf");

        mSceneRoot = (ViewGroup) findViewById(R.id.activity_main);

        bLocal = (Button) findViewById(R.id.bLocal);
        bLocal.setOnClickListener(this);
        bLocal.setTypeface(customtf);
        bTheme = (Button) findViewById(R.id.bTheme);
        bTheme.setOnClickListener(this);
        bTheme.setTypeface(customtf);

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
        }
    }


}
