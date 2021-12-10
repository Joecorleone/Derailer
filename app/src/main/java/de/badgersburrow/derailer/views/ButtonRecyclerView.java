package de.badgersburrow.derailer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Timer;
import java.util.TimerTask;

import de.badgersburrow.derailer.objects.SoundListener;

public class ButtonRecyclerView extends RecyclerView {

    Button bt_top;
    Button bt_left;
    Button bt_right;
    Button bt_bottom;

    int speed = 100;
    int wait = 100;

    Timer timer;

    SoundListener listener;



    public ButtonRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ButtonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void startScrolling(int dx, int dy){
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                smoothScrollBy(dx, dy);
            }
        }, 0, wait);
    }

    private void stopScrolling(){
        if (timer != null){
            timer.cancel();
        }
    }

    public void setListener(SoundListener listener) {
        this.listener = listener;
    }

    public void setBtTop(Button bt_top) {
        this.bt_top = bt_top;
        this.bt_top.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                startScrolling(0, speed);
                v.setPressed(true);
                if (listener != null){
                    listener.playSoundButton();
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                stopScrolling();
                v.setPressed(false);
            }

            return true;
        });
    }

    public void setBtLeft(Button bt_left) {
        this.bt_left = bt_left;

        this.bt_left.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                startScrolling(-speed, 0);
                v.setPressed(true);
                if (listener != null){
                    listener.playSoundButton();
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                stopScrolling();
                v.setPressed(false);
            }

            return true;
        });
    }

    public void setBtRight(Button bt_right) {
        this.bt_right = bt_right;

        this.bt_right.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                startScrolling(speed, 0);
                v.setPressed(true);
                if (listener != null){
                    listener.playSoundButton();
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                stopScrolling();
                v.setPressed(false);
            }

            return true;
        });
    }

    public void setBtBottom(Button bt_bottom) {
        this.bt_bottom = bt_bottom;

        this.bt_bottom.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                startScrolling(0, -speed);
                v.setPressed(true);
                if (listener != null){
                    listener.playSoundButton();
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                stopScrolling();
                v.setPressed(false);
            }
            return true;
        });
    }

    @Override
    public void onScrollStateChanged(int newState) {
        super.onScrollStateChanged( newState);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        if (getLayoutManager() instanceof LinearLayoutManager){
            LinearLayoutManager llm = (LinearLayoutManager) getLayoutManager();
            if (bt_top != null){
                if (llm.findFirstCompletelyVisibleItemPosition() == 0){
                    bt_top.setVisibility(View.GONE);
                } else {
                    bt_top.setVisibility(View.VISIBLE);
                }
            }

            if (bt_bottom != null){
                if (llm.findLastCompletelyVisibleItemPosition() == getAdapter().getItemCount()-1){
                    bt_bottom.setVisibility(View.GONE);
                } else {
                    bt_bottom.setVisibility(View.VISIBLE);
                }
            }

            if (bt_left != null){
                if (llm.findFirstCompletelyVisibleItemPosition() == 0){
                    bt_left.setVisibility(View.GONE);
                } else {
                    bt_left.setVisibility(View.VISIBLE);
                }
            }

            if (bt_right != null){
                if (llm.findLastCompletelyVisibleItemPosition() == getAdapter().getItemCount()-1){
                    bt_right.setVisibility(View.GONE);
                } else {
                    bt_right.setVisibility(View.VISIBLE);
                }
            }

        }
    }







}
