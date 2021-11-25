package de.badgersburrow.derailer.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import androidx.core.util.Pair;
import android.util.Log;

import de.badgersburrow.derailer.FragmentGame;
import de.badgersburrow.derailer.GameView;
import de.badgersburrow.derailer.objects.GameTheme;
import de.badgersburrow.derailer.objects.MoveAnimSecondary;
import de.badgersburrow.derailer.objects.PlayerResult;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by cetty on 29.07.16.
 */
public abstract class PlayerSprite extends BaseSprite implements Serializable {

    private static String TAG = "Player";
    private static int counter = 0;

    public ArrayList<ChoiceCardSprite> choiceCards;

    private Bitmap bmp_color;
    GameView gameView;
    private int screenWidth;
    private int width;

    int xIndex = -1;
    int yIndex = -1;
    int xIndexVirtual = -1;
    int yIndexVirtual = -1;
    private int cur_centerX = -1;
    private int cur_centerY = -1;

    private int destXIndex = 0;
    private int destYIndex = 0;

    private int moveSteps;
    private int currentStep = 1000;
    int pos = -1;
    int posVirtual = -1;
    boolean changed = false;
    private int destPosOnTile = -1;
    private int destPosNextTile = -1;
    boolean alive = true;
    private boolean moving = false;
    int num;
    private int color;
    public boolean killAfterMovement = false;
    private Paint color_paint = new Paint();
    private int tiles = 4;
    boolean virtual = false;
    boolean aliveVirtual = true;

    private int outCount = 100;
    private int tileCount = 0;

    private MoveAnimSecondary animSecondary;
    private float scaleFactor;

    private int _id;

    public PlayerSprite(GameView gameView, GameTheme theme, int num, int color, int tiles){
        super(gameView, theme.getCart());
        _id = counter;
        counter++;
        choiceCards = new ArrayList<>();
        this.gameView = gameView;
        this.animSecondary = theme.getMoveAnimSecondary(gameView);
        this.bmp_color = theme.getCart_color();
        this.virtual = false;

        this.num = num;
        this.color = color;
        this.color_paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        this.tiles = tiles;
        this.aliveVirtual = true;
        this.moveSteps = gameView.getMoveSteps();
        this.currentStep = gameView.getMoveSteps();
        this.scaleFactor = gameView.getScaleFactor();
    }

    public int id(){
        return _id;
    }

    public void onDraw(final Canvas canvas){
        if ((xIndex == -1 && yIndex == -1 ) || pos == -1) {
            return;
        }

        screenWidth = gameView.getWidth();
        scaleFactor = gameView.getScaleFactor();
        width = (screenWidth - (2 * edge)) / 6;

        int scaledWidth = Math.round(scaleFactor* bmp.getWidth());
        int scaledHeight = Math.round(scaleFactor* bmp.getHeight());

        float rotation = 0;

        if (currentStep<moveSteps){
            //Log.d("______________", "--------------");
            //Log.d("animation",String.valueOf(currentStep));
            //Log.d("Rotation",String.valueOf(rotation));

            x = edge + (this.xIndex) * width;
            y = edge + (this.yIndex) * width;

            Pair<float[],float[]> p = FragmentGame.animPath.getPosTan(pos, destPosOnTile, currentStep*1f/moveSteps);
            float[] posPath = (float[]) p.first;
            float[] tanPath = (float[]) p.second;

            int dx = Math.round(posPath[0]*width);
            int dy = Math.round(posPath[1]*width);

            float tx = tanPath[0];
            float ty = tanPath[1];

            // get angle from tan
            float angle;
            if (tx >= 0 && ty>=0){
                angle = (float) (Math.acos(tx)*180./Math.PI);
            } else if (tx <= 0 && ty>=0){
                angle = (float) (Math.acos(ty)*180./Math.PI + 90); //+290
            } else if (tx <= 0 && ty<=0){
                angle = (float) (Math.asin(tx)*180./Math.PI + 270); //+270
            } else {
                angle = (float) (Math.asin(tx)*180./Math.PI + 270); //+270
            }

            cur_centerX = x + dx;
            cur_centerY = y + dy;

            //Log.d("CurrX",String.valueOf(cur_centerX));
            //Log.d("CurrY",String.valueOf(cur_centerY));
            //Log.d("scaleFactor",String.valueOf(scaleFactor));
            //Log.d("CurrRotation",String.valueOf(angle));

            Matrix matrix = new Matrix();
            matrix.postScale(scaleFactor,scaleFactor);

            matrix.postRotate(angle, scaledWidth/2, scaledHeight/2);
            matrix.postTranslate(cur_centerX - scaledWidth / 2,cur_centerY - scaledHeight / 2);
            canvas.drawBitmap(bmp_color, matrix, color_paint);
            canvas.drawBitmap(bmp, matrix, null);

            this.animSecondary.onDraw(canvas, currentStep, cur_centerX, cur_centerY, angle);
            currentStep +=1;
            if (currentStep == moveSteps){
                pos = destPosNextTile;
                xIndex = destXIndex;
                yIndex = destYIndex;
            }

        } else{
            x = edge + (this.xIndex) * width + width / 2 - scaledWidth / 2;
            y = edge + (this.yIndex) * width + width / 2 - scaledHeight / 2;

            if (tiles == 4) {
                switch (pos){
                    case 0:
                        y = y - width / 2;
                        rotation = 90;
                        break;
                    case 1:
                        x = x + width / 2;
                        rotation = 180;
                        break;
                    case 2:
                        y = y + width / 2;
                        rotation = 270;
                        break;
                    case 3:
                        x = x - width / 2;
                        rotation = 0;
                        break;
                    default:
                        break;
                }
            } else {
                switch (pos) {
                    case 6:
                        y = y + Math.round(width * 0.2f);
                        x = x - width / 2;
                        rotation = 0;
                        break;
                    case 7:
                        y = y - Math.round(width * 0.2f);
                        x = x - width / 2;
                        rotation = 0;
                        break;
                    case 0:
                        y = y - width / 2;
                        x = x - Math.round(width * 0.2f);
                        rotation = 90;
                        break;
                    case 1:
                        y = y - width / 2;
                        x = x + Math.round(width * 0.2f);
                        rotation = 90;
                        break;
                    case 2:
                        y = y - Math.round(width * 0.2f);
                        x = x + width / 2;
                        rotation = 180;
                        break;
                    case 3:
                        y = y + Math.round(width * 0.2f);
                        x = x + width / 2;
                        rotation = 180;
                        break;
                    case 4:
                        y = y + width / 2;
                        x = x + Math.round(width * 0.2f);
                        rotation = 270;
                        break;
                    case 5:
                        y = y + width / 2;
                        x = x - Math.round(width * 0.2f);
                        rotation = 270;
                        break;
                }
            }

            cur_centerX = x + scaledWidth/2;
            cur_centerY = y + scaledHeight/2;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleFactor,scaleFactor);
            matrix.postRotate(rotation, scaledWidth/2, scaledHeight/2);
            matrix.postTranslate(cur_centerX-scaledWidth/2,cur_centerY-scaledHeight/2);
            canvas.drawBitmap(bmp_color, matrix, color_paint);
            canvas.drawBitmap(bmp, matrix, null);
            this.animSecondary.onDraw(canvas, currentStep, cur_centerX, cur_centerY, rotation);
        }
    }

    public Pair<Integer, Integer> getCenter(){
        if ((xIndex == -1 && yIndex == -1 ) || pos == -1) {
            return new Pair<>(-1, -1);
        }
        return new Pair<>(cur_centerX, cur_centerY);
    }

    public void setXIndex(int x){
        this.xIndex = x;
        this.xIndexVirtual = x;
    }

    public void setYIndex(int y){
        this.yIndex = y;
        this.yIndexVirtual = y;
    }

    public void setPos(int pos){
        this.pos = pos;
        this.posVirtual = pos;
    }

    public int getXIndex(){
        if (virtual) return xIndexVirtual;
        else return xIndex;
    }

    public int getYIndex(){
        if (virtual) return yIndexVirtual;
        else return yIndex;
    }

    public int getPos(){
        if (virtual) return posVirtual;
        else return pos;
    }

    public void killAfterMovement(){

        if (virtual) {
            aliveVirtual = false;
        } else {
            killAfterMovement = true;
        }
    }

    public void kill(){

        if (virtual){
            aliveVirtual = false;
            killAfterMovement = false;
        } else {
            alive = false;
            moving = false;
            changed = false;
            killAfterMovement = false;
            outCount = gameView.getKilledCount();
            Log.d(TAG, "kill - outCount: " + outCount);
        }
    }

    public int getEmptyCardSlot(){
        ArrayList<Integer> positions = new ArrayList<>();
        for (ChoiceCardSprite card: choiceCards){
            positions.add(card.getPos());
        }
        for (int i=0; i<3; i++){
            if (!positions.contains(i)){
                return i;
            }
        }
        return -1;
    }

    public void startMoving(int newPosOnTile, int newPosNextTile, int dx, int dy) {
        if (virtual){
            this.xIndexVirtual = this.xIndexVirtual + dx;
            this.yIndexVirtual = this.yIndexVirtual + dy;
            this.posVirtual = newPosNextTile;
            changed = true;
        } else {
            this.destXIndex = getXIndex() + dx;
            this.destYIndex = getYIndex() + dy;
            this.destPosOnTile = newPosOnTile;
            this.destPosNextTile = newPosNextTile;
            this.currentStep = 1;
            this.moving = true;
            this.tileCount++;
        }
    }

    abstract public String getLabel(Context context);


    public PlayerResult getResult(){
        return new PlayerResult(isKI(), num, color, outCount, tileCount);
    }


    // getter and setter

    public int getTileCount(){
        return tileCount;
    }
    public int getOutCount(){
        return outCount;
    }
    public int getColor(){
        return color;
    }

    public boolean isAlive() {
        return alive;
    }
    public boolean isAliveVirtual() {
        return aliveVirtual;
    }

    public boolean isVirtual() {
        return virtual;
    }
    public void setVirtual(boolean virtual){ this.virtual = virtual;}


    public Bitmap getBmpMain(){
        return bmp;
    }

    public Bitmap getBmpColor(){
        return bmp_color;
    }

    public boolean reachedDest(){
        return currentStep == moveSteps;
    }

    public boolean isMoving(){
        return moving;
    }
    public void setMoving(boolean moving){
        this.moving = moving;
    }

    public abstract boolean isKI();

    public boolean isChanged() {
        return changed;
    }
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

}

