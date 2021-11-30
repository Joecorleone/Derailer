package de.badgersburrow.derailer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import de.badgersburrow.derailer.objects.SoundListener;

public class DialogButton extends androidx.appcompat.widget.AppCompatButton {

    private static String TAG = "DialogButton";


    private SoundListener listener;

    public DialogButton(Context context) {
        super(context);
    }

    public DialogButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSoundListener(SoundListener soundListener){
        this.listener = soundListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (listener != null && isEnabled()){
                listener.playSoundDialog();
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

        }

        return super.onTouchEvent(event);
    }


}
