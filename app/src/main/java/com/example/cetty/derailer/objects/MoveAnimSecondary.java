package com.example.cetty.derailer.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.cetty.derailer.GameView;

import java.util.ArrayList;

/**
 * Created by reim on 01.02.17.
 * Purpose: animate the cart while moving
 * Additional animation when the cart moves
 * Example: Steam locomotive smoke
 */

public class MoveAnimSecondary {

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
    private ArrayList<Item> items = new ArrayList<>(); // holds all instances of each created animation object
    private int cartWidth;
    private int cartHeight;

    public MoveAnimSecondary(){
        this.mode = ModeKeyNone;
    }

    public MoveAnimSecondary(int drawableId, int numAnims, int duration,
                             float posStartX, float posStartY, float scaleStart, float scaleEnd,
                             boolean posFixed, boolean randRot, boolean randInterval) {
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
    }

    public void onDraw(final Canvas canvas, int step, int centerX, int centerY, float rotation){
        // centerX, centerY is the center of the cart on the playfield
        // rotation is the current rotation of the cart
        if (mode!=ModeKeyNone) {
            float scaleFactor = gameView.getScaleFactor();
            int deltaX_x = Math.round((float) Math.cos(Math.toRadians(rotation)) * scaleFactor * cartWidth / 2 * (1.f - posStartX));
            int deltaX_y = Math.round((float) Math.sin(Math.toRadians(rotation)) * scaleFactor * cartWidth / 2 * (1.f - posStartX));
            int deltaY_x = Math.round((float) Math.sin(Math.toRadians(rotation)) * scaleFactor * cartHeight * posStartY);
            int deltaY_y = Math.round((float) Math.cos(Math.toRadians(rotation)) * scaleFactor * cartHeight * posStartY);
            int pos_x = centerX + deltaX_x + deltaY_x;
            int pos_y = centerY + deltaX_y + deltaY_y;

            int startEach = Math.round(moveSteps / numAnims);
            if (step < moveSteps) {
                if (step % startEach == 1) {

                    this.items.add(new Item(pos_x, pos_y, (float) Math.random() * 360f));
                }
                for (int i = 0; i < this.items.size(); i++) {
                    Item item = this.items.get(i);
                    if (item.step < duration) {
                        Paint paint = new Paint();
                        paint.setAlpha(200 - item.step * 16);
                        //int size = smoke.getBmp().getWToidth()/ (maxStep-smoke.step);

                        Matrix mat = new Matrix();

                        int calc_width = item.getBmp().getWidth() / (duration - item.step);
                        mat.postScale(1.f / (duration - item.step), 1.f / (duration - item.step));

                        mat.postRotate(item.rot, calc_width / 2, calc_width / 2);
                        if (posFixed) {
                            mat.postTranslate(item.x, item.y);
                        } else {
                            mat.postTranslate(pos_x, pos_y);
                        }

                        canvas.drawBitmap(item.getBmp(), mat, paint);
                        item.nextStep();
                    } else {
                        this.items.remove(item);
                        i--;
                    }
                }
            } else {
                for (int i = 0; i < this.items.size(); i++) {
                    int maxStep = 10;
                    Item item = this.items.get(i);
                    if (item.step < maxStep) {
                        Paint paint = new Paint();
                        paint.setAlpha(200 - item.step * 16);
                        //int size = smoke.getBmp().getWidth()/ (maxStep-smoke.step);

                        Matrix mat = new Matrix();
                        int calc_width = item.getBmp().getWidth() / (maxStep - item.step);
                        mat.postScale(1.f / (maxStep - item.step), 1.f / (maxStep - item.step));

                        mat.postRotate(item.rot, calc_width / 2, calc_width / 2);
                        mat.postTranslate(item.x - calc_width / 2, item.y - calc_width / 2);
                        canvas.drawBitmap(item.getBmp(), mat, paint);
                        item.nextStep();
                    } else {
                        this.items.remove(item);
                        i--;
                    }
                }
            }
        }
    }

    public class Item {
        private Bitmap bmp;
        int step = 0;
        int x = 0;
        int y = 0;
        float rot = 0;

        public Item(int x, int y, float rot) {
            this.bmp = BitmapFactory.decodeResource(gameView.getResources(), drawableId);
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

}
