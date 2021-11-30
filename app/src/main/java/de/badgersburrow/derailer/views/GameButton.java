package de.badgersburrow.derailer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import de.badgersburrow.derailer.objects.SoundListener;

public class GameButton extends androidx.appcompat.widget.AppCompatButton {

    private static String TAG = "GameButton";


    private SoundListener listener;

    public GameButton(Context context) {
        super(context);
    }

    public GameButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSoundListener(SoundListener soundListener){
        this.listener = soundListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (listener != null && isEnabled()){
                listener.playSoundButton();
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

        }

        return super.onTouchEvent(event);
    }


}
