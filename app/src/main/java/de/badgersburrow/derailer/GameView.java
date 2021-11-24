package de.badgersburrow.derailer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import de.badgersburrow.derailer.objects.GameTheme;
import de.badgersburrow.derailer.objects.MyButton;
import de.badgersburrow.derailer.sprites.PlayerAiSprite;
import de.badgersburrow.derailer.sprites.PlayerHumanSprite;
import de.badgersburrow.derailer.sprites.PlayerSprite;
import de.badgersburrow.derailer.objects.PlayerSelection;
import de.badgersburrow.derailer.sprites.CardSprite;
import de.badgersburrow.derailer.sprites.ChoiceCardSprite;
import de.badgersburrow.derailer.sprites.ObstacleCardSprite;
import de.badgersburrow.derailer.sprites.PlayedCardSprite;
import de.badgersburrow.derailer.sprites.StartSprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cetty on 28.07.16.
 */
public class GameView extends SurfaceView {

    private GameActivity gameActivity = new GameActivity();
    private Bitmap background;
    Bitmap rotateIndicator;
    private ArrayList<ChoiceCardSprite> cards = new ArrayList<>();
    private ArrayList<StartSprite> startPositions = new ArrayList<>();
    private final RectF rectF = new RectF();
    private SurfaceHolder surfaceHolder;
    GameLoopThread gameLoopThread;
    private long lastClick;
    String gamePhase;
    String gameMainPhase;
    private ArrayList<ChoiceCardSprite> choiceCards = new ArrayList<>();
    Random randomGenerator = new Random();
    boolean virtual = false;

    ArrayList<String> options;

    private float density;
    int currentPlayer = 0;
    int boardSize = 6;
    int collisionDistance = 10;
    int gameTurn = 0;
    int tiles = 8;
    int thinkCounter = 0;
    int obstacleNumber = 0;

    private boolean dialogIsActive=false;

    int moveSteps = 20; // frames per animated move, used to synchronize all animations
    float scaleFactor = 1f; // scale all images with the same factor

    ArrayList<Integer> playersMoved = new ArrayList<>();

    int edge;
    int bottomMargin;
    ArrayList<PlayerSprite> playerSprites = new ArrayList<>();
    ArrayList<Integer> obstacleX = new ArrayList<>();
    ArrayList<Integer> obstacleY = new ArrayList<>();
    private GameTheme selectedTheme;
    // Test different Boards Fields
    private Map<String, PlayedCardSprite> playedCards = new HashMap<>();

    private int cardSelected = -1;
    private  ArrayList<MyButton> buttons = new ArrayList<>();
    private  ArrayList<ObstacleCardSprite> obstacles = new ArrayList<>();

    private Button bt_play;

    public GameView(Context context, ArrayList<PlayerSelection> players, ArrayList<String> options, int connections, GameTheme selectedTheme) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.start_screen);
        rotateIndicator = BitmapFactory.decodeResource(getResources(), R.drawable.tile_rotate);
        gameActivity = (GameActivity) context;
        edge = Math.round(getResources().getDimension(R.dimen.play_field_edge));
        bottomMargin = Math.round(getResources().getDimension(R.dimen.play_field_bottomMargin));
        gameLoopThread = new GameLoopThread(this);
        this.options = options;
        tiles = connections;
        virtual = false;

        if (options.contains(Keys.option_obstacle_few)){
            obstacleNumber = randomGenerator.nextInt(3) + 1;
        }

        if (options.contains(Keys.option_obstacle_many)){
            obstacleNumber = randomGenerator.nextInt(3) + 4;
        }

        for (int j = 0; j < obstacleNumber; j ++ ) {
            int x;
            int y;
            while (true){
                x = randomGenerator.nextInt(6);
                y = randomGenerator.nextInt(6);
                if (obstacleX.contains(x) && obstacleY.contains(y)){
                    continue;
                }
                obstacleX.add(x);
                obstacleY.add(y);
                break;
            }

            ObstacleCardSprite obstacle = new ObstacleCardSprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.option_obstacle_many), x, y);
            obstacles.add(obstacle);
        }

        this.selectedTheme = selectedTheme;
        gamePhase = Keys.gpStart;
        gameMainPhase = Keys.gpStart;

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.interrupt();
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {

                    }
                }
            }

            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                if (gameLoopThread.getState() == gameLoopThread.getState().NEW)
                    gameLoopThread.start();
            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {

            }
        });

        int numHuman = 0;
        int numAI = 0;

        for (PlayerSelection player : players){

            if (!player.isUnselected()){
                if (player.isPlayer()){
                    numHuman++;
                    this.playerSprites.add(new PlayerHumanSprite(this, this.selectedTheme,  numHuman, player.getColor(), tiles));
                } else {
                    numAI++;
                    this.playerSprites.add(new PlayerAiSprite(this, this.selectedTheme,  numAI, player.getColor(), tiles, player.getSelection()));
                }
            }
        }
        getDensity();
        init();
    }

    public void pauseThread() {
        gameLoopThread.setRunning(false);
    }

    public void resumeThread() {
        gameLoopThread = new GameLoopThread(this);
        gameLoopThread.setRunning(true);
        gameLoopThread.start();
    }

    public void setPlayButton(Button bt_play){
        this.bt_play = bt_play;
        this.bt_play.setOnClickListener(v -> movePlayers());
    }

    public int getKilledCount(){
        int killed = 0;
        for (PlayerSprite p : playerSprites){
            if (!p.isAlive()){
                killed++;
            }
        }
        return killed;
    }

    public int getKilledPlayers(){
        int killed = 0;
        for (PlayerSprite p : playerSprites){
           if (!p.isAliveVirtual() && p.isAlive()){
               killed +=1;
           }
        }
        return killed;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawBackground(canvas);
        drawLines(canvas);
        drawObstacles(canvas);
        if (gameMainPhase.equals(Keys.gpStart)){
            PlayerSprite p = playerSprites.get(currentPlayer);
            if (p.isKI() && p.getPhase().equals(Keys.ppIdle)){
                gameMainPhase = Keys.gpStart;
                startThinking();
            }
            if (p.isKI() && p.getPhase().equals(Keys.ppFinishedThinking)){
                int i = randomGenerator.nextInt(startPositions.size());
                StartSprite sprite = startPositions.get(i);
                PlayerSprite playerSprite = playerSprites.get(currentPlayer);
                playerSprite.setXIndex(sprite.getXIndex());
                playerSprite.setYIndex(sprite.getYIndex());
                playerSprite.setPos(sprite.getPos());

                Log.d("X", String.valueOf(sprite.getXIndex()));
                Log.d("Y", String.valueOf(sprite.getYIndex()));
                Log.d("Pos", String.valueOf(sprite.getPos()));
                currentPlayer += 1;
                currentPlayer = currentPlayer% playerSprites.size();
                startPositions.remove(sprite);
                gameActivity.showNotification(playerSprites.get(currentPlayer));

                if (currentPlayer == 0) {
                    currentPlayer = playerSprites.size()-1;
                    gamePhase = Keys.gpPlaying;
                    gameMainPhase = Keys.gpPlaying;
                    nextPlayer();
                }
            }
            drawStartPositions(canvas);
        }

        if (this.gamePhase.equals("GameOver")){
            //"GAME OVER"
            final int fontSize = (int) (25 * density);
            int yTextPos = (int) (780 * density);
            int dy = (int) (25*density);
            Typeface font = Typeface.create("Arial", Typeface.NORMAL);
            int x = (canvas.getWidth() * 1 / 7);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTypeface(font);
            paint.setTextSize(fontSize);
            paint.setAntiAlias(true);
            String name = "Game Over";
            canvas.drawText(name, x, yTextPos, paint);
            //gameActivity.onGameOver();
        }

        scaleFactor = ((canvas.getWidth()- (2 * edge)) / 6f)/this.selectedTheme.getCard_top(1032).getWidth();

        drawPlayedCardsBottom(canvas);
        drawPlayers(canvas);
        drawPlayedCardsTop(canvas);
        drawButtons(canvas);
        drawPlayerList(canvas);


        if (gamePhase.equals(Keys.gpPlaying)) {
            drawChoiceCards(canvas);//cards.get(i).onDraw(canvas);
        }

        if (gamePhase.equals(Keys.gpMoving)) {
            boolean allPlayerStopped = true;
            for (PlayerSprite playerSprite : this.playerSprites) {
                if (playerSprite.isMoving()) {
                    allPlayerStopped = false;
                }
            }
            if (allPlayerStopped) {
                nextPlayer();
            } else {
                movePlayers();
            }
        }

        if (gamePhase.equals(Keys.gpThinking)){
            if (thinkCounter>0){
                thinkCounter--;
            } else {
                PlayerSprite p = playerSprites.get(currentPlayer);
                p.setPhase(Keys.ppFinishedThinking);
                if (!gameMainPhase.equals(Keys.gpStart)) {
                    gamePhase = Keys.gpPlaying;

                    ((PlayerAiSprite) p).makeMove(playedCards, choiceCards, this);
                } else {
                    gamePhase = gameMainPhase;
                }
            }

        }
    }

    public void drawPlayedCardsBottom(Canvas canvas){
        for (int i = 0; i < boardSize; i ++ ) {
            for (int j = 0; j < boardSize; j ++ ) {
                PlayedCardSprite sprite = playedCards.get(String.valueOf(i)+"-"+String.valueOf(j));
                if (sprite != null) {
                    sprite.onDraw(canvas);
                }
            }
        }
    }

    public void drawPlayedCardsTop(Canvas canvas){

        for (int i = 0; i < boardSize; i ++ ) {
            for (int j = 0; j < boardSize; j ++ ) {
                PlayedCardSprite sprite = playedCards.get(String.valueOf(i)+"-"+String.valueOf(j));
                if (sprite != null) {
                    sprite.onDrawTop(canvas);
                }
            }
        }
    }

    public void drawObstacles(Canvas canvas){
        for (ObstacleCardSprite p : obstacles){
            p.onDraw(canvas);
        }
    }

    public void drawExplosion(Canvas canvas){
        return;
    }

    public void drawButtons(Canvas canvas){
        if (gamePhase.equals(Keys.gpPlaying) & cardSelected != -1){
            for (int i=0; i<buttons.size(); i++){
                MyButton button = buttons.get(i);
                //button.onDraw(canvas, true);
            }
            if (bt_play != null){
                gameActivity.runOnUiThread(() -> bt_play.setEnabled(true));
            }
        } else {
            for (int i=0; i<buttons.size(); i++){
                MyButton button = buttons.get(i);
                //button.onDraw(canvas, false);
            }
            if (bt_play != null){
                gameActivity.runOnUiThread(() -> bt_play.setEnabled(false));
            }
        }
    }

    public float getDensity() {
        density = getResources().getDisplayMetrics().density;
        return density;
    }

    public void drawPlayerList(Canvas canvas){
        final float fontSize = getResources().getDimension(R.dimen.player_text_size);
        final float fontSizeCurrent = getResources().getDimension(R.dimen.player_current_text_size);
        int yTextPos = (int) (canvas.getWidth()+bottomMargin +fontSize/2f);
        int dy = (int) (25*density);
        int x = (canvas.getWidth() * 1 / 8);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTypeface(MainActivity.customtf_bold);
        paint.setTextSize(fontSizeCurrent);
        paint.setAntiAlias(true);

        int count = 0;
        int countAlive = 0;
        // get current player
        for (; countAlive == 0 && count< playerSprites.size(); count++){
            int index = (currentPlayer +count)% playerSprites.size();
            PlayerSprite playerSprite = playerSprites.get(index);
            if (playerSprite.isAlive()) {
                Paint pPlayer = new Paint();
                pPlayer.setColor(playerSprite.getColor());
                pPlayer.setTypeface(MainActivity.customtf_bold);
                pPlayer.setFakeBoldText(true);
                pPlayer.setTextSize(fontSizeCurrent);
                pPlayer.setAntiAlias(true);

                canvas.drawText(playerSprite.getLabel(getContext()),(float) (x*2/3), yTextPos , pPlayer);
                if (options.contains(Keys.option_victory_distance)){
                    canvas.drawText(String.valueOf(playerSprite.getTileCount()),(float) x + 200, yTextPos , pPlayer);
                }
                countAlive++;
            }
        }

        canvas.drawText("Next", (float) (x*2/3), (float) (yTextPos + dy * 2), paint);

        for (; count< playerSprites.size(); count++){
            int index = (currentPlayer +count)% playerSprites.size();
            PlayerSprite playerSprite = playerSprites.get(index);
            if (playerSprite.isAlive()) {
                Paint pPlayer = new Paint();
                pPlayer.setColor(playerSprite.getColor());
                pPlayer.setTypeface(MainActivity.customtf_normal);
                pPlayer.setFakeBoldText(true);
                pPlayer.setTextSize(fontSize);
                pPlayer.setAntiAlias(true);

                canvas.drawText(playerSprite.getLabel(getContext()),x, yTextPos + dy * (countAlive + 2), pPlayer);
                if (options.contains(Keys.option_victory_distance)){
                    canvas.drawText(String.valueOf(playerSprite.getTileCount()),(float) x + 200, yTextPos + dy * (countAlive + 2) , pPlayer);
                }
                countAlive++;
            }
        }
    }

    public void init(){
        if (tiles == 4){
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(1032), this.selectedTheme.getCard_top(1032), Arrays.asList(1,0,3,2), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(2301), this.selectedTheme.getCard_top(2301), Arrays.asList(2,3,0,1), 1));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(3210), this.selectedTheme.getCard_top(3210), Arrays.asList(3,2,1,0), 2));
            choiceCards =  new ArrayList<>();
            for (int i=0; i<3;  i++){
                ChoiceCardSprite card = cards.get(i).getCopy(i);
                choiceCards.add(card);
            }
        } else {
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10325476), this.selectedTheme.getCard_top(10325476), Arrays.asList(1,0,3,2,5,4,7,6), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10326745), this.selectedTheme.getCard_top(10326745), Arrays.asList(1,0,3,2,6,7,4,5), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10327654), this.selectedTheme.getCard_top(10327654), Arrays.asList(1,0,3,2,7,6,5,4), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10462735), this.selectedTheme.getCard_top(10462735), Arrays.asList(1,0,4,6,2,7,3,5), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10472653), this.selectedTheme.getCard_top(10472653), Arrays.asList(1,0,4,7,2,6,5,3), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10567234), this.selectedTheme.getCard_top(10567234), Arrays.asList(1,0,5,6,7,2,3,4), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10576243), this.selectedTheme.getCard_top(10576243), Arrays.asList(1,0,5,7,6,2,4,3), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10643725), this.selectedTheme.getCard_top(10643725), Arrays.asList(1,0,6,4,3,7,2,5), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10657324), this.selectedTheme.getCard_top(10657324), Arrays.asList(1,0,6,5,7,3,2,4), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10675423), this.selectedTheme.getCard_top(10675423), Arrays.asList(1,0,6,7,5,4,2,3), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10743652), this.selectedTheme.getCard_top(10743652), Arrays.asList(1,0,7,4,3,6,5,2), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10756342), this.selectedTheme.getCard_top(10756342), Arrays.asList(1,0,7,5,6,3,4,2), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(10765432), this.selectedTheme.getCard_top(10765432), Arrays.asList(1,0,7,6,5,4,3,2), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(23016745), this.selectedTheme.getCard_top(23016745), Arrays.asList(2,3,0,1,6,7,4,5), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(23017654), this.selectedTheme.getCard_top(23017654), Arrays.asList(2,3,0,1,7,6,5,4), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(24061735), this.selectedTheme.getCard_top(24061735), Arrays.asList(2,4,0,6,1,7,3,5), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(24071653), this.selectedTheme.getCard_top(24071653), Arrays.asList(2,4,0,7,1,6,5,3), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(25067134), this.selectedTheme.getCard_top(25067134), Arrays.asList(2,5,0,6,7,1,3,4), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(25076143), this.selectedTheme.getCard_top(25076143), Arrays.asList(2,5,0,7,6,1,4,3), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(26043715), this.selectedTheme.getCard_top(26043715), Arrays.asList(2,6,0,4,3,7,1,5), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(26057314), this.selectedTheme.getCard_top(26057314), Arrays.asList(2,6,0,5,7,3,1,4), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(27043651), this.selectedTheme.getCard_top(27043651), Arrays.asList(2,7,0,4,3,6,5,1), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(27056341), this.selectedTheme.getCard_top(27056341), Arrays.asList(2,7,0,5,6,3,4,1), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(32107654), this.selectedTheme.getCard_top(32107654), Arrays.asList(3,2,1,0,7,6,5,4), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(34601725), this.selectedTheme.getCard_top(34601725), Arrays.asList(3,4,6,0,1,7,2,5), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(34701652), this.selectedTheme.getCard_top(34701652), Arrays.asList(3,4,7,0,1,6,5,2), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(35607124), this.selectedTheme.getCard_top(35607124), Arrays.asList(3,5,6,0,7,1,2,4), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(36507214), this.selectedTheme.getCard_top(36507214), Arrays.asList(3,6,5,0,7,2,1,4), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(42160735), this.selectedTheme.getCard_top(42160735), Arrays.asList(4,2,1,6,0,7,3,5), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(42170653), this.selectedTheme.getCard_top(42170653), Arrays.asList(4,2,1,7,0,6,5,3), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(43610725), this.selectedTheme.getCard_top(43610725), Arrays.asList(4,3,6,1,0,7,2,5), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(45670123), this.selectedTheme.getCard_top(45670123), Arrays.asList(4,5,6,7,0,1,2,3), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(45760132), this.selectedTheme.getCard_top(45760132), Arrays.asList(4,5,7,6,0,1,3,2), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(54761032), this.selectedTheme.getCard_top(54761032), Arrays.asList(5,4,7,6,1,0,3,2), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(72143650), this.selectedTheme.getCard_top(72143650), Arrays.asList(7,2,1,4,3,6,5,0), 0));

            updateChoiceCards();
        }
        Bitmap blue_dot = BitmapFactory.decodeResource(getResources(), R.drawable.blue_dot);
        if (tiles == 4) {
            for (int i = 0; i < 6; i++) {
                if (checkObstacleColision(0, i)) {
                    startPositions.add(new StartSprite(this, blue_dot, 0, i, 3, 4));
                }
                if (checkObstacleColision(i, 0)) {
                    startPositions.add(new StartSprite(this, blue_dot, i, 0, 0, 4));
                }
                if (checkObstacleColision(5, i)) {
                    startPositions.add(new StartSprite(this, blue_dot, 5, i, 1, 4));
                }
                if (checkObstacleColision(i, 5)) {
                    startPositions.add(new StartSprite(this, blue_dot, i, 5, 2, 4));
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                if (checkObstacleColision(0, i)) {
                    startPositions.add(new StartSprite(this, blue_dot, 0, i, 6, 8));
                    startPositions.add(new StartSprite(this, blue_dot, 0, i, 7, 8));
                }
                if (checkObstacleColision(i, 0)) {
                    startPositions.add(new StartSprite(this, blue_dot, i, 0, 0, 8));
                    startPositions.add(new StartSprite(this, blue_dot, i, 0, 1, 8));
                }
                if (checkObstacleColision(5, i)) {
                    startPositions.add(new StartSprite(this, blue_dot, 5, i, 2, 8));
                    startPositions.add(new StartSprite(this, blue_dot, 5, i, 3, 8));
                }
                if (checkObstacleColision(i, 5)) {
                    startPositions.add(new StartSprite(this, blue_dot, i, 5, 4, 8));
                    startPositions.add(new StartSprite(this, blue_dot, i, 5, 5, 8));
                }
            }
        }
    }

    private boolean checkObstacleColision(int x, int y){
        for (ObstacleCardSprite obstacle : obstacles) {
            if (obstacle.getXIndex() == x && obstacle.getYIndex() == y) {
                return false;
            }
        }
        return true;
    }

    public void drawPlayers(Canvas canvas){
        ArrayList<ArrayList<Integer>> positions = new ArrayList<>(playerSprites.size());
        for (int i = 0; i < playerSprites.size(); i ++ ) {
            PlayerSprite sprite = playerSprites.get(i);
            if (sprite.isAlive()) {
                ArrayList<Integer> pos = sprite.onDraw(canvas);
                positions.add(i, pos);
            } else {
                ArrayList pos = new ArrayList();
                pos.add(-1);
                pos.add(-1);
                positions.add(i, pos);
            }
        }


        for (int i = 0; i < playerSprites.size(); i ++ ) {
            if (positions.get(i).get(0) == -1) continue;
            for (int j = i + 1; j < playerSprites.size(); j ++ ) {
                if (Math.abs(positions.get(i).get(1) - positions.get(j).get(1)) < collisionDistance){
                    if (Math.abs(positions.get(i).get(0) - positions.get(j).get(0)) < collisionDistance){
                        playerSprites.get(i).kill();
                        gameActivity.showNotification(playerSprites.get(i));
                        playerSprites.get(j).kill();
                        gameActivity.showNotification(playerSprites.get(j));
                        drawExplosion(canvas);
                        checkForGameOver();
                    }
                }

            }
        }
        for (int i = 0; i < playerSprites.size(); i ++ ) {
            if (playerSprites.get(i).killAfterMovement && !playerSprites.get(i).isMoving()){
                playerSprites.get(i).kill();
                drawExplosion(canvas);
                gameActivity.showNotification(playerSprites.get(i));
            }
        }

    }

    public void drawStartPositions(Canvas canvas){
        for (int i = 0; i < startPositions.size(); i ++ ) {
            StartSprite sprite = startPositions.get(i);
            sprite.onDraw(canvas);
        }
    }

    public void drawBackground(Canvas canvas){
        canvas.drawColor(getContext().getResources().getColor(R.color.backgroundGame));
    }

    public void drawLines(Canvas canvas){
        Paint bgColor = new Paint();
        bgColor.setStyle(Paint.Style.FILL);
        bgColor.setColor(getContext().getResources().getColor(R.color.gras));

        Paint lineColor = new Paint();
        lineColor.setARGB(255, 255, 255, 255);
        int screenHeight = getHeight();
        int screenWidth = getWidth();
        int lineWidth = 4;
        int num_plates = 6;
        int plateWidth = (screenWidth - (2 * edge)) / num_plates;

        canvas.drawRect(edge, edge, screenWidth-edge - lineWidth/2, screenWidth-edge - lineWidth/2, bgColor);

        for (int i = 0; i <= num_plates; i ++ ) {
            canvas.drawRect(edge, edge+i*plateWidth-lineWidth/2, getWidth()-edge - lineWidth/2, edge+i*plateWidth+lineWidth/2, lineColor);
            canvas.drawRect(edge+i*plateWidth-lineWidth/2, edge, edge+i*plateWidth+lineWidth/2, getWidth()-edge -lineWidth/2, lineColor);
        }
    }

    public void setVirtual(boolean virtuel){
        this.virtual = virtuel;
    }
    public void movePlayers(){
        gamePhase = Keys.gpMoving;
        for (PlayerSprite playerSprite : playerSprites) {
            playerSprite.setVirtual(virtual);
            if (playerSprite.reachedDest()){
                int pos = playerSprite.getPos();
                //Log.d("______________", "--------------");
                //Log.d("CardSelected", String.valueOf(cardSelected));
                //Log.d("GameTurn", String.valueOf(gameTurn));
                //Log.d("Position", "Pos: " + String.valueOf(pos) + ", X: " + String.valueOf(player.getXIndex()) + ", Y: " + String.valueOf(player.getYIndex()));

                PlayedCardSprite card =  playedCards.get(String.valueOf(playerSprite.getXIndex())+"-"+String.valueOf(playerSprite.getYIndex()));
                if (card != null) {
                    List<Integer> ways = card.getWays();
                    //Log.d("X", String.valueOf(player.getXIndex()));
                    //Log.d("Y", String.valueOf(player.getYIndex()));
                    //Log.d("Ways", String.valueOf(ways));
                    //Log.d("Position", "Pos: " + String.valueOf(pos) + ", X: " + String.valueOf(player.getXIndex()) + ", Y: " + String.valueOf(player.getYIndex()));
                    pos = (pos-ways.size()/4*card.getRotation()+ways.size())%ways.size();
                    //Log.d("Position Rot", "Pos: " + String.valueOf(pos) + ", X: " + String.valueOf(player.getXIndex()) + ", Y: " + String.valueOf(player.getYIndex()));
                    int newPos = ways.get(pos);
                    //Log.d("New Position", "Pos: " + String.valueOf(newPos) + ", X: " + String.valueOf(player.getXIndex()) + ", Y: " + String.valueOf(player.getYIndex()));
                    int newPosOnTile = (newPos+ways.size()/4*card.getRotation()+ways.size())%ways.size();
                    //Log.d("New Position Rot", "Pos: " + String.valueOf(newPos) + ", X: " + String.valueOf(player.getXIndex()) + ", Y: " + String.valueOf(player.getYIndex()));
                    int dx = 0;
                    int dy = 0;

                    int newPosNextTile = 0;

                    if (tiles == 4) {
                        switch (newPosOnTile) {
                            case 0:
                                dy = -1;
                                newPosNextTile = 2;
                                break;
                            case 1:
                                dx = 1;
                                newPosNextTile = 3;
                                break;
                            case 2:
                                dy = 1;
                                newPosNextTile = 0;
                                break;
                            case 3:
                                dx = -1;
                                newPosNextTile = 1;
                                break;
                        }
                    } else {
                        switch (newPosOnTile) {
                            case 0:
                                dy = -1;
                                newPosNextTile = 5;
                                break;
                            case 1:
                                dy = -1;
                                newPosNextTile = 4;
                                break;
                            case 2:
                                dx = 1;
                                newPosNextTile = 7;
                                break;
                            case 3:
                                dx = 1;
                                newPosNextTile = 6;
                                break;
                            case 4:
                                dy = 1;
                                newPosNextTile = 1;
                                break;
                            case 5:
                                dy = 1;
                                newPosNextTile = 0;
                                break;
                            case 6:
                                dx = -1;
                                newPosNextTile = 3;
                                break;
                            case 7:
                                dx = -1;
                                newPosNextTile = 2;
                                break;
                        }
                    }

                    for (ObstacleCardSprite obstacle: obstacles){
                        if (playerSprite.getXIndex() + dx == obstacle.getXIndex() && playerSprite.getYIndex() + dy == obstacle.getYIndex()){
                            playerSprite.killAfterMovement();
                            //player.kill();
                            //gameActivity.showNotification(player);
                        }
                    }


                    /* / TODO Movement of Player */
                    if (playerSprite.getXIndex() + dx < 0 || playerSprite.getXIndex() + dx >= boardSize){
                        playerSprite.killAfterMovement();
                        //gameActivity.showNotification(player);
                    }
                    if (playerSprite.getYIndex() + dy < 0 || playerSprite.getYIndex() + dy >= boardSize){
                        playerSprite.killAfterMovement();
                        //gameActivity.showNotification(player);
                    }
                    playerSprite.startMoving(newPosOnTile, newPosNextTile, dx, dy);
                    //player.setPos(newPos);

                    //player.setXIndex(player.getXIndex() + dx);
                    //player.setYIndex(player.getYIndex() + dy);



                    Log.d("NewPos", String.valueOf(playerSprite.getPos()));
                    Log.d("NewX", String.valueOf(playerSprite.getXIndex()));
                    Log.d("NewY", String.valueOf(playerSprite.getYIndex()));

                    Log.d("-------------", "--------------");
                    for (int j = 0; j < playerSprites.size(); j++) {
                        Log.d("Pos", String.valueOf(playerSprites.get(j).getPos()));
                    }

                } else {
                    playerSprite.setChanged(false);
                    playerSprite.setMoving(false);
                }
            }
        }

        if (virtual) {
            boolean changed = false;
            for (int i = 0; i< playerSprites.size(); i++){
                if (playerSprites.get(i).isChanged()) changed = true;
            }
            if (!changed) gamePhase = Keys.gpPlaying;
        }
    }

    public void playCard(Integer i){
        PlayerSprite playerSprite = playerSprites.get(currentPlayer);
        ChoiceCardSprite card = choiceCards.get(i);
        cardSelected = i;
        int pos = playerSprite.getPos();
        PlayedCardSprite playedCard = new PlayedCardSprite(this, card.getBottomBMP(), card.getTopBMP(), card.getWays(), playerSprite.getXIndex(), playerSprite.getYIndex(), card.getRotation());
        playedCards.put(String.valueOf(playerSprite.getXIndex())+"-"+String.valueOf(playerSprite.getYIndex()), playedCard);

    }

    public void rotateCard(Integer i, int rotation){
        PlayerSprite playerSprite = playerSprites.get(currentPlayer);
        CardSprite card = choiceCards.get(i);
        CardSprite playedCard = playedCards.get(String.valueOf(playerSprite.getXIndex())+"-"+String.valueOf(playerSprite.getYIndex()));
        if (rotation == -1) {
            playedCard.rotate();
            card.rotate();
        } else {
            playedCard.setRotation(rotation);
            card.setRotation(rotation);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 200 & event.getAction() == MotionEvent.ACTION_UP) {
            lastClick = System.currentTimeMillis();
            synchronized (getHolder()) {
                for (int i=0; i<choiceCards.size(); i++){
                    ChoiceCardSprite card = choiceCards.get(i);
                    if (card.isTouched(event.getX(), event.getY()) && gamePhase.equals(Keys.gpPlaying)){
                        for (ChoiceCardSprite c : choiceCards){
                            c.setShow(card == c);
                        }
                        card.setShow(true);
                        PlayerSprite playerSprite = playerSprites.get(currentPlayer);
                        Log.d("CardSelected", String.valueOf(i));
                        if (i == cardSelected) {
                            rotateCard(i, -1);
                        } else {
                            playCard(i);
                        }
                        Log.d("CardSelected", String.valueOf(cardSelected));
                    }
                }

                /*if (gamePhase.equals(gpPlaying) & cardSelected != -1 & buttons.get(0).isTouched(event.getX(), event.getY())){
                    movePlayers();
                }*/

                if (gamePhase.equals(Keys.gpStart)){

                    for (int i=0; i<startPositions.size(); i++){
                        StartSprite sprite = startPositions.get(i);
                        if (sprite.isTouched(event.getX(), event.getY())){
                            PlayerSprite playerSprite = playerSprites.get(currentPlayer);
                            playerSprite.setXIndex(sprite.getXIndex());
                            playerSprite.setYIndex(sprite.getYIndex());
                            playerSprite.setPos(sprite.getPos());

                            Log.d("X", String.valueOf(sprite.getXIndex()));
                            Log.d("Y", String.valueOf(sprite.getYIndex()));
                            Log.d("Pos", String.valueOf(sprite.getPos()));
                            currentPlayer += 1;
                            currentPlayer = currentPlayer% playerSprites.size();
                            startPositions.remove(sprite);
                            gameActivity.showNotification(playerSprites.get(currentPlayer));

                            if (currentPlayer == 0) {
                                currentPlayer = playerSprites.size()-1;
                                gamePhase = Keys.gpPlaying;
                                gameMainPhase = Keys.gpPlaying;
                                nextPlayer();
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public int getNumberOfLivePlayer(){
        int number_live_players = 0;
        for (int i = 0; i < playerSprites.size(); i++) {
            if (playerSprites.get(i).isAlive()) {
                number_live_players += 1;
            }
        }
        return number_live_players;
    }

    public void checkForGameOver(){
        int number_live_players = getNumberOfLivePlayer();
        if (number_live_players <= 1) {
            this.gamePhase = "GameOver";
            gameLoopThread.setRunning(false);
            gameActivity.onGameOver();
        }
    }

    public void nextPlayer(){
        int number_live_players = 0;
        String playerAliveLabel = "NoBody";
        int playerAliveColor = 0xFF000000;
        for (int i = 0; i < playerSprites.size(); i++) {
            if (playerSprites.get(i).isAlive()) {
                number_live_players += 1;
                playerAliveLabel = playerSprites.get(i).getLabel(getContext());
                playerAliveColor = playerSprites.get(i).getColor();
            }
        }

        if (number_live_players <= 1){
            checkForGameOver();
        } else {

            gamePhase = Keys.gpPlaying;
            int i = 0;
            gameTurn += 1;

            boolean someNotMoved = false;
            for (PlayerSprite playerSprite : playerSprites){
                int id = playerSprite.id();
                if (!playersMoved.contains(id) && playerSprite.isAlive()){
                    someNotMoved = true;
                }
            }

            if(!someNotMoved){
                playersMoved = new ArrayList<>();
            }

            PlayerSprite playerSprite = playerSprites.get(currentPlayer);
            if (cardSelected != -1){// && options.contains(Keys.option_draw_01)) {
                choiceCards.remove(cardSelected);
            }

            while (i < playerSprites.size()) {

                if (options.contains(Keys.option_order_same)) {
                    currentPlayer += 1;
                    currentPlayer = currentPlayer % playerSprites.size();
                } else {
                    do {
                        currentPlayer = randomGenerator.nextInt(playerSprites.size());
                        currentPlayer = currentPlayer % playerSprites.size();
                    } while (playersMoved.contains(playerSprites.get(currentPlayer).id()));
                }


            PlayerSprite p = playerSprites.get(currentPlayer);
            playersMoved.add(p.id());
            cardSelected = -1;
            if (p.isAlive()) {
                gameActivity.showNotification(p);
                if (p.isKI()){
                    startThinking();
                }
                updateChoiceCards();
                return;
                }
            }
        }
    }

    public void startThinking(){
        PlayerSprite p = playerSprites.get(currentPlayer);
        p.setPhase(Keys.ppThinking);
        gamePhase = Keys.gpThinking;
        thinkCounter = 25;
    }

    public void updateChoiceCards(){
        choiceCards = new ArrayList<>();
        ArrayList<Integer> selected = new ArrayList<>();
        if (options.contains(Keys.option_draw_new)) {
            while (choiceCards.size() < 3) {
                int index = randomGenerator.nextInt(cards.size());
                if (!selected.contains(index)) {
                    selected.add(index);
                    ChoiceCardSprite card = cards.get(index).getCopy(choiceCards.size());
                    choiceCards.add(card);
                }
            }
        } else {
            PlayerSprite p = playerSprites.get(currentPlayer);
            while (p.choiceCards.size() < 3) {
                int index = randomGenerator.nextInt(cards.size());
                if (!selected.contains(index)) {
                    selected.add(index);
                    int pos = p.getEmptyCardSlot();

                    ChoiceCardSprite card = cards.get(index).getCopy(pos);
                    p.choiceCards.add(card);
                }
            }
            choiceCards = p.choiceCards;
        }
    }

    public void drawChoiceCards(Canvas canvas){
        if (gamePhase.equals(Keys.gpStart)) return;
        for (ChoiceCardSprite card : choiceCards){
            card.onDraw(canvas);
            Log.d("Draw", Integer.toString(card.getPos())+": "+  card.getWays().toString());
        }
    }


    // Getter methods
    public int getMoveSteps(){
        return moveSteps;
    }
    public float getScaleFactor(){ return scaleFactor; }

    public int getEdge() {
        return edge;
    }

    public String getGamePhase() {
        return gamePhase;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public Bitmap getRotateIndicator() {
        return rotateIndicator;
    }
}
