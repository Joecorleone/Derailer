package de.badgersburrow.derailer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import androidx.core.util.Pair;
import android.util.Log;

import de.badgersburrow.derailer.objects.MoveAnimSecondary;
import de.badgersburrow.derailer.objects.PlayerResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * Created by cetty on 29.07.16.
 */
public class Player implements Serializable {

    private static String TAG = "Player";
    private static int counter = 0;

    private Bitmap bmp_main;
    private Bitmap bmp_color;
    private GameView gameView;
    private int screenWidth;
    private int width;
    private int edge = 0;
    private int xIndex = -1;
    private  int yIndex = -1;
    private int xIndexVirtual = -1;
    private int yIndexVirtual = -1;
    private int searchDepth = 3;
    int x = -1;
    int y = -1;
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
    private int num;
    private int color;
    private Paint color_paint = new Paint();
    private int tiles = 4;
    boolean KI = false;
    boolean virtual = false;
    boolean aliveVirtual = true;
    String KIStrength = Keys.aiHard;
    Random randomGenerator = new Random();

    private int outCount = 100;
    private int tileCount = 0;

    private MoveAnimSecondary animSecondary;
    private float scaleFactor;

    // Points for score calculation
    int distancePoints = 10;
    int fieldPoints = 1;
    int killPoints = 50;
    private int _id;

    public Player(GameView gameView, GameTheme theme, int num, int color, int tiles, String selection){
        _id = counter;
        counter++;
        this.gameView = gameView;
        this.animSecondary = theme.getMoveAnimSecondary(gameView);
        this.bmp_main = theme.getCart();
        this.bmp_color = theme.getCart_color();
        this.virtual = false;

        if (selection.equals(Keys.player)){
            this.KI = false;

        } else {
            this.KI = true;
            this.KIStrength = selection;
        }
        Log.d("Player",String.valueOf(color) + " is " + selection);
        this.edge = gameView.edge;
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
        if (xIndex == -1 || yIndex == -1 || pos == -1) return;

        screenWidth = gameView.getWidth();
        scaleFactor = gameView.getScaleFactor();
        width = (screenWidth - (2 * edge)) / 6;

        int scaledWidth = Math.round(scaleFactor* bmp_main.getWidth());
        int scaledHeight = Math.round(scaleFactor* bmp_main.getHeight());

        float rotation = 0;

        if (currentStep<moveSteps){
            //Log.d("______________", "--------------");
            //Log.d("animation",String.valueOf(currentStep));
            //Log.d("Rotation",String.valueOf(rotation));

            x = edge + (this.xIndex) * width;
            y = edge + (this.yIndex) * width;

            Pair p = GameActivity.animPath.getPosTan(pos, destPosOnTile, currentStep*1f/moveSteps);
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

            int cur_centerX = x + dx;
            int cur_centerY = y + dy;

            //Log.d("CurrX",String.valueOf(cur_centerX));
            //Log.d("CurrY",String.valueOf(cur_centerY));
            //Log.d("scaleFactor",String.valueOf(scaleFactor));
            //Log.d("CurrRotation",String.valueOf(angle));

            Matrix matrix = new Matrix();
            matrix.postScale(scaleFactor,scaleFactor);

            matrix.postRotate(angle, scaledWidth/2, scaledHeight/2);
            matrix.postTranslate(cur_centerX - scaledWidth / 2,cur_centerY - scaledHeight / 2);
            canvas.drawBitmap(bmp_color, matrix, color_paint);
            canvas.drawBitmap(bmp_main, matrix, null);

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
                if (pos == 6) {
                    y = y + Math.round(width * 0.2f);
                    x = x - width / 2;
                    rotation = 0;
                } else if (pos == 7) {
                    y = y - Math.round(width * 0.2f);
                    x = x - width / 2;
                    rotation = 0;
                } else if (pos == 0) {
                    y = y - width / 2;
                    x = x - Math.round(width * 0.2f);
                    rotation = 90;
                } else if (pos == 1) {
                    y = y - width / 2;
                    x = x + Math.round(width * 0.2f);
                    rotation = 90;
                } else if (pos == 2) {
                    y = y - Math.round(width * 0.2f);
                    x = x + width / 2;
                    rotation = 180;
                } else if (pos == 3) {
                    y = y + Math.round(width * 0.2f);
                    x = x + width / 2;
                    rotation = 180;
                } else if (pos == 4) {
                    y = y + width / 2;
                    x = x + Math.round(width * 0.2f);
                    rotation = 270;
                } else if (pos == 5) {
                    y = y + width / 2;
                    x = x - Math.round(width * 0.2f);
                    rotation = 270;
                }
            }

            int centerX = x + scaledWidth/2;
            int centerY = y + scaledHeight/2;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleFactor,scaleFactor);
            matrix.postRotate(rotation, scaledWidth/2, scaledHeight/2);
            matrix.postTranslate(centerX-scaledWidth/2,centerY-scaledHeight/2);
            canvas.drawBitmap(bmp_color, matrix, color_paint);
            canvas.drawBitmap(bmp_main, matrix, null);
            this.animSecondary.onDraw(canvas, currentStep, centerX, centerY, rotation);
        }
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

    public void kill(){

        if (virtual){
            aliveVirtual = false;
        } else {
            alive = false;
            outCount = gameView.getKilledCount();
            Log.d(TAG, "kill - outCount: " + outCount);
        }
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

    public String getLabel(Context context){
        if (this.KI){
            return context.getString(R.string.label_ai, num);
        } else {
            return context.getString(R.string.label_player, num);
        }
    }

    public int getTileCount(){
        return tileCount;
    }
    public int getOutCount(){
        return outCount;
    }
    public int getColor(){
        return color;
    }

    public void setVirtual(boolean virtual){ this.virtual = virtual;}

    public Bitmap getBmpMain(){
        return bmp_main;
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

    public void makeMove(Map playedCards, ArrayList choiceCards, GameView gameView){
        if (KIStrength.equals(Keys.aiEasy)) {
            for (int i=0; i< choiceCards.size(); i++){
                for (int rot=0; rot<4; rot++){
                    aliveVirtual = true;
                    xIndexVirtual = xIndex;
                    yIndexVirtual = yIndex;
                    posVirtual = pos;
                    gameView.setVirtual(true);
                    gameView.playCard(i);
                    gameView.rotateCard(i, rot);
                    int depth = 0;
                    while (true){
                        if (depth < searchDepth) {
                            gameView.movePlayers();
                            if (!gameView.gamePhase.equals(gameView.gpMoving)) break;
                        }
                        depth += 1;
                    }
                    if (aliveVirtual) {
                        gameView.setVirtual(false);
                        gameView.movePlayers();
                        return;
                    }
                }
            }
            gameView.setVirtual(false);
            gameView.movePlayers();
        } else if (KIStrength.equals(Keys.aiNormal)){
            gameView.setVirtual(true);
            for (int i=0; i< choiceCards.size(); i++){
                for (int rot=0; rot<4; rot++){
                    aliveVirtual = true;
                    xIndexVirtual = xIndex;
                    yIndexVirtual = yIndex;
                    posVirtual = pos;
                    gameView.setVirtual(true);
                    gameView.playCard(i);
                    gameView.rotateCard(i, rot);
                    while (true){
                        gameView.movePlayers();
                        if (!gameView.gamePhase.equals(gameView.gpMoving)) break;
                    }
                    if (aliveVirtual) {
                        gameView.setVirtual(false);
                        gameView.movePlayers();
                        return;
                    }
                }
            }
            gameView.setVirtual(false);
            gameView.movePlayers();
        } else if (KIStrength.equals(Keys.aiHard)){
            gameView.setVirtual(true);

            int bestScore = -1;
            int bestCard = 0;
            int bestRotation = 0;
            for (int i=0; i< choiceCards.size(); i++){
                for (int rot=0; rot<4; rot++){
                    aliveVirtual = true;
                    xIndexVirtual = xIndex;
                    yIndexVirtual = yIndex;
                    posVirtual = pos;
                    gameView.playCard(i);
                    gameView.rotateCard(i, rot);
                    while (true){
                        gameView.movePlayers();
                        if (!gameView.gamePhase.equals(gameView.gpMoving)) break;
                    }

                    if (aliveVirtual){
                        int fieldScore = 1;
                        List<String> calcCards = new ArrayList<String>();
                        calcCards.add(Integer.toString(xIndexVirtual)+"-"+Integer.toString(yIndexVirtual));
                        fieldScore += calcFieldScore(playedCards, xIndexVirtual+1, yIndexVirtual,calcCards);
                        fieldScore += calcFieldScore(playedCards, xIndexVirtual, yIndexVirtual+1,calcCards);
                        fieldScore += calcFieldScore(playedCards, xIndexVirtual-1, yIndexVirtual,calcCards);
                        fieldScore += calcFieldScore(playedCards, xIndexVirtual, yIndexVirtual-1,calcCards);

                        fieldScore += distancePoints*min(gameView.boardSize-abs(xIndexVirtual-gameView.boardSize/2), gameView.boardSize-abs(yIndexVirtual-gameView.boardSize/2));
                        int score = gameView.getKilledPlayers()*killPoints+fieldScore;
                        if (score > bestScore){
                            bestCard = i;
                            bestRotation = rot;
                            bestScore = score;
                        }
                    }
                }
            }

            aliveVirtual = true;
            xIndexVirtual = xIndex;
            yIndexVirtual = yIndex;
            posVirtual = pos;
            gameView.playCard(bestCard);
            gameView.rotateCard(bestCard, bestRotation);
            gameView.setVirtual(false);
            gameView.movePlayers();
        }
    }

    private int calcFieldScore(Map playedCards, int x, int y, List calcCards){
        int score = 0;
        if (x < 0 || x>= gameView.boardSize || y <0 || y >= gameView.boardSize) return 0;
        if (playedCards.containsKey(Integer.toString(x)+"-"+Integer.toString(y)) == false){
            if (calcCards.contains(Integer.toString(x)+"-"+Integer.toString(y)) == false){
                score += fieldPoints;
                calcCards.add(Integer.toString(x)+"-"+Integer.toString(y));
                score += calcFieldScore(playedCards, x+1, y,calcCards);
                score += calcFieldScore(playedCards, x, y+1,calcCards);
                score += calcFieldScore(playedCards, x-1, y,calcCards);
                score += calcFieldScore(playedCards, x, y-1,calcCards);
            }
        }
        return score;
    }

    PlayerResult getResult(){
        return new PlayerResult(KI, num, color, outCount, tileCount);
    }

}

