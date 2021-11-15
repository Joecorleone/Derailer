package de.badgersburrow.derailer.objects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import de.badgersburrow.derailer.MainActivity;
import de.badgersburrow.derailer.R;

import java.util.ArrayList;
import android.os.Handler;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by reim on 20.02.17.
 */

public class SettingCard extends LinearLayout implements
        View.OnClickListener{

    private ArrayList<Choice> choices = new ArrayList<>();
    private String title = "";
    private String key;

    private View v;

    private View.OnClickListener clickListener;
    private Context mContext;
    private boolean animating = false;
    private Handler handler;
    private int anim_duration;
    private int topPadding;

    private TextView tv_option;
    private ImageView iv_option;

    public SettingCard(Context context) {
        super(context);
        this.mContext = context;
        this.choices = new ArrayList<>();
        setOnClickListener(this);
        anim_duration = getResources().getInteger(R.integer.card_flip_time_half);
        topPadding = Math.round(getResources().getDimension(R.dimen.activity_vertical_margin_small));
    }

    public SettingCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(String title, String key){
        this.title = title;
        this.key = key;
        initView();
    }

    private void initView() {
        v = inflate(mContext, R.layout.view_setting_card, this);
        tv_option = v.findViewById(R.id.tv_option);
        iv_option = v.findViewById(R.id.iv_option);
        tv_option.setTypeface(MainActivity.customtf_normal);
        setContent();
    }

    private void setContent(){
        tv_option.setText(this.getOptionLabel());
        iv_option.setImageResource(this.getResidImage());
    }

    public void addChoice(Choice choice){
        this.choices.add(choice);
    }

    public void setChoiceNext(){
        int state = 0;
        for (int i = 0; i<choices.size(); i++){
            if (choices.get(i).key.equals(key)){
                state = i;
            }
        }
        int nextState = (state+1)%choices.size();
        key = choices.get(nextState).key;
        setContent();
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getResidImage(){
        for (Choice c : choices){
            if (c.key.equals(key)){
                return c.image;
            }
        }
        return -1;
    }

    public String getOptionLabel(){
        for (Choice c : choices){
            if (c.key.equals(key)){
                return c.label;
            }
        }
        return "";
    }

    public String getKey(){
        return key;
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
                setChoiceNext();
                anim2.start();
            }
            @Override
            public void onAnimationCancel(Animator animator) { }
            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
        anim1.start();
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

    public static class Choice{
        String key;
        String label;
        int image;

        public Choice(String key, String label, int image){
            this.key = key;
            this.label = label;
            this.image = image;
        }

    }
}
