package de.badgersburrow.derailer.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import de.badgersburrow.derailer.GameView;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by reim on 01.02.17.
 * Purpose: animate the cart while moving
 * Additional animation when the cart moves
 * Example: Steam locomotive smoke
 */

public class MoveAnimSecondary {

    private final static String TAG = "MoveAnimSecondary";

    private int ModeKeyNone = 1;
    private int ModeKeySingle = 2;
    private int ModeKeyFrames = 3;


    private int mode;
    private GameView gameView;
    private int drawableId; // image used in the animation, can be drawable set for a frame by frame animation
    private int moveSteps; // frames to complete a move from the start to the end of a tile
    private int numAnims; // number of animations started per complete move
    private int duration; // number of frames for a animation
    private float posStartX; // relative to the start of the cart in percent from 0 to 1, 0 -> front
    private float posStartY; // relative to the width of the cart in percent from -0.5 to 0.5, 0 -> mid
    //private float scaleFactor; // overall scaling of the image due to the size of the playing field
    private float scaleStart; // scaling of the image at animation start, e.g. 0.1
    private float scaleEnd; // scaling of the image at animation end, e.g. 1
    private boolean posFixed; // whether the animation is fixed to playing filed -> does not move when cart moves
    private boolean randRot; // randomize the rotation of the drawable
    private boolean randInterval;
    private int alphaStart;
    private int alphaEnd;

    private int frameCounter;
    private ArrayList<Integer> frames;

    private ArrayList<Item> items = new ArrayList<>(); // holds all instances of each created animation object
    private int cartWidth;
    private int cartHeight;

    public MoveAnimSecondary(){
        this.mode = ModeKeyNone;
    }

    public MoveAnimSecondary(int drawableId, int numAnims, int duration,
                             float posStartX, float posStartY, float scaleStart, float scaleEnd,
                             boolean posFixed, boolean randRot, boolean randInterval,
                             int alphaStart, int alphaEnd) {
        /*
        Constructor for an move animation object consisting of a single frame
         */
        this.mode = ModeKeySingle;
        this.drawableId = drawableId;
        this.numAnims = numAnims;
        this.duration = duration;
        this.posStartX = posStartX;
        this.posStartY = posStartY;
        this.scaleStart = scaleStart;
        this.scaleEnd = scaleEnd;
        this.posFixed = posFixed;
        this.randRot = randRot;
        this.randInterval = randInterval;
        this.alphaStart = alphaStart;
        this.alphaEnd = alphaEnd;
    }

    public MoveAnimSecondary(int drawableId, int numAnims,
                             float posStartX, float posStartY, ArrayList<Integer> frames){
        /*
        Constructor for an move animation object consisting of a frames
         */
        this.mode = ModeKeyFrames;
        this.drawableId = drawableId;
        this.numAnims = numAnims;
        this.posStartX = posStartX;
        this.posStartY = posStartY;
        this.frames = frames;
        this.frameCounter = frames.size();
    }

    public MoveAnimSecondary getCopy(){
        if (mode == ModeKeySingle){
            return new MoveAnimSecondary(drawableId, numAnims, duration,
                    posStartX, posStartY, scaleStart, scaleEnd,
                    posFixed, randRot, randInterval,
                    alphaStart, alphaEnd);
        } else if (mode == ModeKeyFrames){
            return new MoveAnimSecondary(drawableId, numAnims, posStartX, posStartY, frames);
        }

        return new MoveAnimSecondary();
    }

    public void onDraw(final Canvas canvas, int step, int centerX, int centerY, float rotation){
        // centerX, centerY is the center of the cart on the playfield
        // rotation is the current rotation of the cart in deg

        if (mode == ModeKeySingle) {
            float scaleFactor = gameView.getScaleFactor();

            // center of cart is 0,0
            // start of cart is 1,0

            int deltaX_x = Math.round((float) Math.cos(Math.toRadians(rotation)) * scaleFactor * cartWidth * posStartX/2);
            int deltaX_y = Math.round((float) Math.sin(Math.toRadians(rotation)) * scaleFactor * cartWidth * posStartX/2);
            int deltaY_x = Math.round(-(float) Math.sin(Math.toRadians(rotation)) * scaleFactor * cartHeight * posStartY/2);
            int deltaY_y = Math.round((float) Math.cos(Math.toRadians(rotation)) * scaleFactor * cartHeight * posStartY/2);

            Log.d(TAG, "onDraw - rotation: " + rotation + ", deltaX_x: " + deltaX_x + ", deltaX_y: " + deltaX_y);

            int startEach = Math.round(moveSteps / numAnims);
            if (step < moveSteps) {
                if (step % startEach == 1) {
                    this.items.add(new Item(centerX, centerY, rotation + ((randRot) ? (float) Math.random() * 360f : 0)));
                }
            }

            int alphaStep = (alphaEnd - alphaStart)/duration;

            for (int i = 0; i < this.items.size(); i++) {
                Item item = this.items.get(i);
                if (item.step < duration) {

                    int alpha = Math.min(Math.max(alphaStart + alphaStep,0),255);
                    if (alpha == 0){
                        continue;
                    }

                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setAlpha(alpha);

                    float fullScaleFactor = scaleFactor * ( scaleEnd / duration * item.step + scaleStart / duration * (duration - item.step));

                    int scaledWidth = Math.round(fullScaleFactor* item.getBmp().getWidth());
                    int scaledHeight = Math.round(fullScaleFactor* item.getBmp().getHeight());

                    Matrix mat = new Matrix();
                    mat.postScale(fullScaleFactor, fullScaleFactor);

                    if (posFixed) {
                        mat.postRotate(item.rot, scaledWidth / 2f, scaledHeight / 2f);
                        mat.postTranslate(item.x + deltaX_x + deltaY_x - scaledWidth/2f, item.y + deltaX_y + deltaY_y - scaledHeight/2f);
                    } else {
                        mat.postRotate(rotation, scaledWidth / 2f, scaledHeight / 2f);
                        mat.postTranslate(centerX + deltaX_x + deltaY_x - scaledWidth/2f, centerY + deltaX_y + deltaY_y - scaledHeight/2f);
                    }

                    canvas.drawBitmap(item.getBmp(), mat, paint);

                    item.nextStep();
                } else {
                    this.items.remove(item);
                    i--;
                }
            }
            /*Paint pointPaint = new Paint();
            pointPaint.setColor(0xFFFF00FF);
            pointPaint.setStrokeWidth(5);
            canvas.drawPoints(new float[]{centerX + deltaX_x + deltaY_x, centerY + deltaX_y + deltaY_y}, pointPaint);*/
        } else if (mode == ModeKeyFrames) {
            Log.d(TAG, "onDraw: " + mode);
            float scaleFactor = gameView.getScaleFactor();

            // center of cart is 0,0
            // start of cart is 1,0

            int deltaX_x = Math.round((float) Math.cos(Math.toRadians(rotation)) * scaleFactor * cartWidth * posStartX/2);
            int deltaX_y = Math.round((float) Math.sin(Math.toRadians(rotation)) * scaleFactor * cartWidth * posStartX/2);
            int deltaY_x = Math.round(-(float) Math.sin(Math.toRadians(rotation)) * scaleFactor * cartHeight * posStartY/2);
            int deltaY_y = Math.round((float) Math.cos(Math.toRadians(rotation)) * scaleFactor * cartHeight * posStartY/2);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            Matrix mat = new Matrix();
            mat.postScale(scaleFactor, scaleFactor);

            Bitmap bmp;

            if (frameCounter<frames.size()){
                frameCounter++;
            } else {
                if (Math.random() < 0.02){
                    frameCounter = 0;
                }
            }
            if (frameCounter<frames.size()){
                bmp = getBitmap(gameView.getContext(), frames.get(frameCounter));
            } else {
                bmp = getBitmap(gameView.getContext(), drawableId);
            }


            int scaledWidth = Math.round(scaleFactor* bmp.getWidth());
            int scaledHeight = Math.round(scaleFactor* bmp.getHeight());



            mat.postRotate(rotation, scaledWidth / 2f, scaledHeight / 2f);
            mat.postTranslate(centerX + deltaX_x + deltaY_x - scaledWidth/2f, centerY + deltaX_y + deltaY_y - scaledHeight/2f);

            canvas.drawBitmap(bmp, mat, paint);
        }
    }

    public class Item {
        private Bitmap bmp;
        int step = 0;
        int x = 0;
        int y = 0;
        float rot = 0;

        public Item(int x, int y, float rot) {
            this.bmp = getBitmap(gameView.getContext(), drawableId);//BitmapFactory.decodeResource(gameView.getResources(),
            this.x = x;
            this.y = y;
            this.rot = rot;
        }

        public Bitmap getBmp(){ return this.bmp; }

        public void nextStep(){ this.step += 1; }
    }

    public void attachGameView(GameView gameView, int width, int height){
        this.gameView = gameView;
        this.moveSteps = gameView.getMoveSteps();
        this.cartWidth = width;
        this.cartHeight = height;
    }

    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

}
