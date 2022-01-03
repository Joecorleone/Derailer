package de.badgersburrow.derailer.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

import de.badgersburrow.derailer.ActivityMain;
import de.badgersburrow.derailer.FragmentMain;
import de.badgersburrow.derailer.R;
import de.badgersburrow.derailer.adapters.AdapterOptions;

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
    private String keyDefault;
    private String keyChosen;

    private View v;

    ChangeListener listener;

    private View.OnClickListener clickListener;
    private Context mContext;
    private boolean animating = false;
    private Handler handler;
    private int anim_duration;
    private int topPadding;

    private TextView tv_option;
    private ImageView iv_option;

    private boolean enabled;

    public SettingCard(Context context, String key) {
        super(context);
        this.mContext = context;
        this.choices = new ArrayList<>();
        this.key = key;
        setOnClickListener(this);
        anim_duration = getResources().getInteger(R.integer.card_flip_time_half);
        topPadding = 0;//Math.round(getResources().getDimension(R.dimen.activity_vertical_margin_small));
        enabled = true;
    }

    public SettingCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(String title, String keyDefault){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.title = title;
        this.keyDefault = keyDefault;
        this.keyChosen = SP.getString(this.key, keyDefault);
        initView();
    }

    private void initView() {
        v = inflate(mContext, R.layout.view_setting_card, this);
        tv_option = v.findViewById(R.id.tv_option);
        iv_option = v.findViewById(R.id.iv_option);
        tv_option.setTypeface(ActivityMain.customtf_normal);

        setContent();
    }

    private void setContent(){
        tv_option.setText(this.getOptionLabel());
        iv_option.setImageResource(this.getResidImage());
        if (enabled){
            tv_option.setTextColor(mContext.getResources().getColor(R.color.option_enabled));
            iv_option.clearColorFilter();
        } else {
            tv_option.setTextColor(mContext.getResources().getColor(R.color.option_disabled));
            iv_option.setColorFilter(mContext.getResources().getColor(R.color.option_image_disabled), PorterDuff.Mode.LIGHTEN);
        }
    }

    public void addChoice(Choice choice){
        this.choices.add(choice);
    }

    public void setChoiceNext(){
        int state = 0;
        for (int i = 0; i<choices.size(); i++){
            if (choices.get(i).key.equals(keyChosen)){
                state = i;
            }
        }
        int nextState = (state+1)%choices.size();
        keyChosen = choices.get(nextState).key;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(mContext);
        SP.edit().putString(key, keyChosen).apply();
        setContent();
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getResidImage(){
        for (Choice c : choices){
            if (c.key.equals(keyChosen)){
                return c.image;
            }
        }
        return -1;
    }

    public String getOptionLabel(){
        for (Choice c : choices){
            if (c.key.equals(keyChosen)){
                return c.label;
            }
        }
        return "";
    }

    public String getKey(){
        return key;
    }
    public String getKeyDefault(){
        return keyDefault;
    }
    public String getKeyChosen(){
        return keyChosen;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setContent();
    }

    public int getTopPadding() { return topPadding;}

    public void toggleState(){

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
                listener.changedTo(getKeyChosen());
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
        if (enabled){
            if (!animating){
                this.toggleState();
                if (listener != null){
                    listener.pressed();
                }
            }

            if (clickListener != null) {
                clickListener.onClick(this);
            }
        }
    }

    public void setChangeListener(ChangeListener listener){
        this.listener = listener;
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

    public interface ChangeListener{
        void pressed();
        void changedTo(String key);
    }
}
