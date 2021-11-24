package de.badgersburrow.derailer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class GameTextButton extends androidx.appcompat.widget.AppCompatButton {

    private static String TAG = "GameTextButton";

    private int res_disabled = -1;
    private int res_pressed = -1;
    private int res_normal = -1;

    public GameTextButton(Context context) {
        super(context);
    }

    public GameTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    protected void drawableStateChanged() {
        Log.d(TAG, String.format("button focused: %s, enabled: %s, pressed: %s",
                isFocused() ? "yes" : "no", isEnabled() ? "yes" : "no", isPressed() ? "yes" : "no"));
        super.drawableStateChanged();
        if (isPressed()){
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

    }
}
