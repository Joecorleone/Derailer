package com.example.cetty.derailer.objects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.cetty.derailer.R;

import java.util.ArrayList;
import android.os.Handler;
import android.view.animation.OvershootInterpolator;

/**
 * Created by reim on 20.02.17.
 */

public class SettingCard extends AppCompatImageView implements
        View.OnClickListener{

    private ArrayList<Integer> resid_images = new ArrayList<>();
    private String title = "";
    private int state = 0;
    private View.OnClickListener clickListener;
    private Context mContext;
    private boolean animating = false;
    private Handler handler;
    private int anim_duration;
    private int topPadding;

    public SettingCard(Context context) {
        super(context);
        this.mContext = context;
        setOnClickListener(this);
        anim_duration = getResources().getInteger(R.integer.card_flip_time_half);
        topPadding = Math.round(getResources().getDimension(R.dimen.activity_vertical_margin_small));
    }

    public SettingCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setOnClickListener(this);
        topPadding = Math.round(getResources().getDimension(R.dimen.activity_vertical_margin_small));
    }

    public SettingCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        setOnClickListener(this);
        topPadding = Math.round(getResources().getDimension(R.dimen.activity_vertical_margin_small));
    }

    public void init(String title, ArrayList<Integer> resids, int state){
        this.title = title;
        this.resid_images = resids;
        this.setImageResource(this.getResidImage());
        this.state = state;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setState(int state){
        this.state = state;
    }

    public int getResidImage(){
        return resid_images.get(state);
    }

    public int getState(){
        return state;
    }

    public int getTopPadding() { return topPadding;}

    public void toggleState(){

        //AnimatorSet animSet =
        //        (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.card_flip_left_out);
        //animSet.setTarget(this);

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(this, "rotationY", 0, 90);
        anim1.setDuration(anim_duration);

        final ObjectAnimator anim2 = ObjectAnimator.ofFloat(this, "rotationY", -90, 0);
        anim2.setDuration(anim_duration);
        anim2.setInterpolator(new OvershootInterpolator());
        anim2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }
            @Override
            public void onAnimationEnd(Animator animator) {
                animating = false;
            }
            @Override
            public void onAnimationCancel(Animator animator) { }
            @Override
            public void onAnimationRepeat(Animator animator) { }
        });

        anim1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                animating = true;
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                state = (state+1)%resid_images.size();
                setImageResource(getResidImage());
                anim2.start();
            }
            @Override
            public void onAnimationCancel(Animator animator) { }
            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
        anim1.start();
    }

    public int getNumStates(){
        return resid_images.size();
    }

    public String getTitle(){
        return title;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l == this) {
            super.setOnClickListener(l);
        } else {
            clickListener = l;
        }
    }

    @Override
    public void onClick(View v) {
        // start the Animation...
        // handle click event yourself and pass the event to supplied
        // listener also...
        if (!animating){
            this.toggleState();
        }

        if (clickListener != null) {
            clickListener.onClick(this);
        }
    }
}
