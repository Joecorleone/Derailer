package de.badgersburrow.derailer.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import de.badgersburrow.derailer.R;

public class GameSignButton extends RelativeLayout {

    private final static String TAG = "GameSignButton";

    public enum Position {start, center, end}

    private Position position;
    private int resSign;
    private int resShadow;

    private View v;
    private ImageView iv_sign, iv_shadow;


    public GameSignButton(Context context) {
        super(context);
    }

    public GameSignButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray tarry = context.obtainStyledAttributes(attrs, R.styleable.GameSignButton);
        try {
            position = Position.values()[tarry.getInt(R.styleable.GameSignButton_position, 1)];
            resSign = tarry.getResourceId(R.styleable.GameSignButton_sign, -1);
            resShadow = tarry.getResourceId(R.styleable.GameSignButton_shadow, -1);
        } finally {
            tarry.recycle();
        }
        initView();
    }

    public GameSignButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GameSignButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initView(){
        v = inflate(getContext(), R.layout.game_sign_button, this);

        Bitmap sign = getBitmapFromVectorDrawable(getContext(), resSign);
        Bitmap shadow = getBitmapFromVectorDrawable(getContext(), resShadow);

        Matrix matrix = new Matrix();

        iv_sign = v.findViewById(R.id.iv_sign);
        iv_shadow = v.findViewById(R.id.iv_shadow);

        switch (position){
            case start:
                matrix.postRotate(-10);
                break;
            case center:
                matrix.postRotate(0);
                break;
            case end:
                matrix.postRotate(10);
                break;
        }

        Bitmap sign_rot = Bitmap.createBitmap(sign, 0, 0, sign.getWidth(), sign.getHeight(), matrix, true);
        Bitmap sign_shadow = Bitmap.createBitmap(shadow, 0, 0, shadow.getWidth(), shadow.getHeight(), matrix, true);

        iv_sign.setImageBitmap(sign_rot);
        iv_shadow.setImageBitmap(sign_shadow);

        //iv_sign.setImageResource(resSign);
        //iv_shadow.setImageResource(resShadow);
    }

    public void setImageRotation(float rot_x, float rot_y){


        iv_sign.setRotationX(rot_x);
        iv_shadow.setRotationX(rot_x);

        iv_sign.setRotationY(rot_y);
        iv_shadow.setRotationY(rot_y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            switch(position){
                case start:
                    setImageRotation(0,-20);
                    break;
                case center:
                    setImageRotation(20,0);
                    break;
                case end:
                    setImageRotation(0,20);
                    break;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            setImageRotation(0, 0);
        }

        return super.onTouchEvent(event);
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }



}
