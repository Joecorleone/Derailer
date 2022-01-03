package de.badgersburrow.derailer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import de.badgersburrow.derailer.objects.SoundListener;

public class GameTextButton extends androidx.appcompat.widget.AppCompatButton
        implements View.OnClickListener {

    private static String TAG = "GameTextButton";

    private int res_disabled = -1;
    private int res_pressed = -1;
    private int res_normal = -1;

    private boolean played = false;

    private SoundListener listener;

    public GameTextButton(Context context) {
        super(context);
    }

    public GameTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSoundListener(SoundListener soundListener){
        this.listener = soundListener;
    }

    public void setDrawableNormal(int resId){
        res_normal = resId;
    }

    public void setDrawableDisabled(int resId){
        res_disabled = resId;
    }

    public void setDrawablePressed(int resId){
        res_pressed = resId;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (listener != null && isEnabled()){
                listener.playSoundButton();
            }


        } else if (event.getAction() == MotionEvent.ACTION_UP && isPressed()) {
            float x = event.getX();
            float y = event.getY();

            if (0 < x && x < getMeasuredWidth() && 0 < y && y < getMeasuredHeight()){
                played = true;
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void drawableStateChanged() {
        Log.d(TAG, String.format("button focused: %s, enabled: %s, pressed: %s",
                isFocused() ? "yes" : "no", isEnabled() ? "yes" : "no", isPressed() ? "yes" : "no"));

        if (isPressed() || played){
            if (res_pressed > 0){
                setBackgroundResource(res_pressed);
            }
        } else if (isEnabled()){
            if (res_normal > 0){
                setBackgroundResource(res_normal);
            }
        } else {
            if (res_disabled > 0){
                setBackgroundResource(res_disabled);
            }
        }
        super.drawableStateChanged();
    }

    @Override
    public void onClick(View v) {
        super.callOnClick();
    }

    public void reset(){
        played = false;
        drawableStateChanged();
    }

}
