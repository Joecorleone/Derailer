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
import java.util.Random;

import de.badgersburrow.derailer.R;

import de.badgersburrow.derailer.objects.MyButton;
import de.badgersburrow.derailer.objects.PlayerSelection;

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
    private ArrayList<ChoiceCardSprite> cards = new ArrayList<ChoiceCardSprite>();
    private ArrayList<StartSprite> startPositions = new ArrayList<StartSprite>();
    private final RectF rectF = new RectF();
    private SurfaceHolder surfaceHolder;
    GameLoopThread gameLoopThread;
    private long lastClick;
    String gamePhase;
    private ArrayList<ChoiceCardSprite> choiceCards = new ArrayList<ChoiceCardSprite>();
    Random randomGenerator = new Random();
    boolean virtual = false;


    final String gpStart = "Start";
    final String gpPlaying = "Playing";
    final String gpMoving = "Moving";
    private float density;
    int currentPlayer = 0;
    int boardSize = 6;
    int gameTurn = 0;
    int tiles = 8;

    private boolean dialogIsActive=false;

    int moveSteps = 20; // frames per animated move, used to synchronize all animations
    float scaleFactor = 1f; // scale all images with the same factor

    int edge;
    int bottomMargin;
    ArrayList<Player> players = new ArrayList<Player>();
    private GameTheme selectedTheme;
    // Test different Boards Fields
    //private ArrayList<ArrayList<PlayedCardSprite>>   playedCards = new ArrayList<ArrayList<PlayedCardSprite>>();
    //private PlayedCardSprite[][] playedCards = new PlayedCardSprite[boardSize][boardSize];
    private Map<String, PlayedCardSprite> playedCards = new HashMap<>();

    //Table<Integer, Integer, PlayedCardSprite> playedCards = new HashBasedTable
    ////
    private int cardSelected = -1;
    private  ArrayList<MyButton> buttons = new ArrayList<MyButton>();

    public GameView(Context context, ArrayList<PlayerSelection> players, int connections, GameTheme selectedTheme) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.start_screen);
        gameActivity = (GameActivity) context;
        edge = Math.round(getResources().getDimension(R.dimen.play_field_edge));
        bottomMargin = Math.round(getResources().getDimension(R.dimen.play_field_bottomMargin));
        gameLoopThread = new GameLoopThread(this);
        tiles = connections;
        virtual = false;

        this.selectedTheme = selectedTheme;
        gamePhase = gpStart;

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
                //Canvas theCanvas = surfaceHolder.lockCanvas(null);
                //onDraw(theCanvas);
                //surfaceHolder.unlockCanvasAndPost(theCanvas);
                gameLoopThread.setRunning(true);
                if (gameLoopThread.getState() == gameLoopThread.getState().NEW)
                    gameLoopThread.start();
            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {

            }
        });

        for (PlayerSelection player : players){
            if (!player.isUnselected()){
                this.players.add(new Player(this, this.selectedTheme,  player.getColor(), tiles, player.getSelection()));
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

    public int getKilledPlayers(){
        int killed = 0;
        for (int i=0; i<players.size();i++){
           if (players.get(i).aliveVirtual == false && players.get(i).alive == true){
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

        if (gamePhase.equals(gpStart)){
            if (players.get(currentPlayer).KI){
                int i = randomGenerator.nextInt(startPositions.size());
                StartSprite sprite = startPositions.get(i);
                Player player = players.get(currentPlayer);
                player.setXIndex(sprite.getXIndex());
                player.setYIndex(sprite.getYIndex());
                player.setPos(sprite.getPos());

                Log.d("X", String.valueOf(sprite.getXIndex()));
                Log.d("Y", String.valueOf(sprite.getYIndex()));
                Log.d("Pos", String.valueOf(sprite.getPos()));
                currentPlayer += 1;
                currentPlayer = currentPlayer%players.size();
                startPositions.remove(sprite);
                gameActivity.showNotification(players.get(currentPlayer));

                if (currentPlayer == 0) {
                    currentPlayer = players.size()-1;
                    gamePhase = gpPlaying;
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

        if (gamePhase.equals(gpPlaying)) {
            for (int i = 0; i < 3; i++) {
                drawChoiceCards(canvas);//cards.get(i).onDraw(canvas);
            }
        }

        if (gamePhase.equals(gpMoving)) {
            boolean allPlayerStopped = true;
            for (Player player : this.players) {
                if (player.isMoving()) {
                    allPlayerStopped = false;
                }
            }
            if (allPlayerStopped) {
                nextPlayer();
            } else {
                movePlayers();
            }
        }
    }

    public void drawPlayedCardsBottom(Canvas canvas){
        //for (int i = 0; i < playedCards.size(); i ++ ) {
        //    PlayedCardSprite sprite = playedCards.get(i);
        //    sprite.onDraw(canvas);
        //}
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

    public void drawButtons(Canvas canvas){
        if (gamePhase.equals(gpPlaying) & cardSelected != -1){
            for (int i=0; i<buttons.size(); i++){
                MyButton button = buttons.get(i);
                button.onDraw(canvas, true);
            }
        } else {
            for (int i=0; i<buttons.size(); i++){
                MyButton button = buttons.get(i);
                button.onDraw(canvas, false);
            }
        }
    }

    public float getDensity() {
        density = getResources().getDisplayMetrics().density;
        return density;
    }

    public void drawPlayerList(Canvas canvas){
        final float fontSize = getResources().getDimension(R.dimen.player_text_size);
        int yTextPos = (int) (canvas.getWidth()+bottomMargin +fontSize/2f);
        int dy = (int) (25*density);
        Typeface font = Typeface.create("Arial", Typeface.NORMAL);
        int x = (canvas.getWidth() * 1 / 8);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTypeface(font);
        paint.setTextSize(fontSize);
        paint.setAntiAlias(true);

        canvas.drawText("Current Player", (float) (x*2/3), (float) (yTextPos), paint);

        for (int i=0; i<players.size(); i++){
            int index = (currentPlayer +i)%players.size();
            Player player = players.get(index);
            if (player.alive) {
                paint.setColor(Color.BLACK);
                paint.setTypeface(font);
                paint.setTextSize(fontSize);
                paint.setAntiAlias(true);
                canvas.drawText(String.valueOf(player.getColor()),x, yTextPos + dy * (i + 1), paint);
            }
        }
    }

    public void init(){
        if (tiles == 4){
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(1032), this.selectedTheme.getCard_top(1032), Arrays.asList(1,0,3,2), 0));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(2301), this.selectedTheme.getCard_top(2301), Arrays.asList(2,3,0,1), 1));
            cards.add(new ChoiceCardSprite(this, this.selectedTheme.getCard_bottom(3210), this.selectedTheme.getCard_top(3210), Arrays.asList(3,2,1,0), 2));
            choiceCards =  new ArrayList<ChoiceCardSprite>();
            for (int i=0; i<3;  i++){
                ChoiceCardSprite card = cards.get(i);
                card.setPos(i);
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

            choiceCards =  new ArrayList<ChoiceCardSprite>();
            for (int i=0; i<3;  i++){
                int index = randomGenerator.nextInt(cards.size());
                ChoiceCardSprite card = cards.get(index);
                card.setPos(i);
                choiceCards.add(card);
            }
        }
        Bitmap blue_dot = BitmapFactory.decodeResource(getResources(), R.drawable.blue_dot);
        if (tiles == 4) {
            for (int i = 0; i < 6; i++) {
                startPositions.add(new StartSprite(this, blue_dot, 0, i, 3, 4));
                startPositions.add(new StartSprite(this, blue_dot, i, 0, 0, 4));
                startPositions.add(new StartSprite(this, blue_dot, 5, i, 1, 4));
                startPositions.add(new StartSprite(this, blue_dot, i, 5, 2, 4));
            }
        } else {
            for (int i = 0; i < 6; i++) {
                startPositions.add(new StartSprite(this, blue_dot, 0, i, 6, 8));
                startPositions.add(new StartSprite(this, blue_dot, 0, i, 7, 8));
                startPositions.add(new StartSprite(this, blue_dot, i, 0, 0, 8));
                startPositions.add(new StartSprite(this, blue_dot, i, 0, 1, 8));
                startPositions.add(new StartSprite(this, blue_dot, 5, i, 2, 8));
                startPositions.add(new StartSprite(this, blue_dot, 5, i, 3, 8));
                startPositions.add(new StartSprite(this, blue_dot, i, 5, 4, 8));
                startPositions.add(new StartSprite(this, blue_dot, i, 5, 5, 8));
            }
        }

        buttons.add(new MyButton(this, BitmapFactory.decodeResource(getResources(), R.drawable.play_button2), BitmapFactory.decodeResource(getResources(), R.drawable.play_button3), 900, 1780));

    }

    public void drawPlayers(Canvas canvas){
        for (int i = 0; i < players.size(); i ++ ) {
            Player sprite = players.get(i);
            sprite.onDraw(canvas);
        }
    }

    public void drawStartPositions(Canvas canvas){
        for (int i = 0; i < startPositions.size(); i ++ ) {
            StartSprite sprite = startPositions.get(i);
            sprite.onDraw(canvas);
        }
    }

    public void drawBackground(Canvas canvas){
        rectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawBitmap(background, null, rectF, null);

        //canvas.drawColor(0xff0000ff);
    }

    public void drawLines(Canvas canvas){
        Paint lineColor = new Paint();
        lineColor.setARGB(255, 255, 255, 255);
        int screenHeight = getHeight();
        int screenWidth = getWidth();
        int lineWidth = 10;
        int num_plates = 6;
        int plateWidth = (screenWidth - (2 * edge)) / 6;

        for (int i = 0; i <= num_plates; i ++ ) {
            canvas.drawRect(edge, edge+i*plateWidth-lineWidth/2, getWidth()-edge, edge+i*plateWidth+lineWidth/2, lineColor);
            canvas.drawRect(edge+i*plateWidth-lineWidth/2, edge, edge+i*plateWidth+lineWidth/2, getWidth()-edge, lineColor);
        }
    }

    public void setVirtual(boolean virtuel){
        this.virtual = virtuel;
    }
    public void movePlayers(){
        gamePhase = gpMoving;
        for (Player player : players) {
            player.setVirtual(virtual);
            if (player.reachedDest()){
                int pos = player.getPos();
                //Log.d("______________", "--------------");
                //Log.d("CardSelected", String.valueOf(cardSelected));
                //Log.d("GameTurn", String.valueOf(gameTurn));
                //Log.d("Position", "Pos: " + String.valueOf(pos) + ", X: " + String.valueOf(player.getXIndex()) + ", Y: " + String.valueOf(player.getYIndex()));

                PlayedCardSprite card =  playedCards.get(String.valueOf(player.getXIndex())+"-"+String.valueOf(player.getYIndex()));
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



                    /* / TODO Movement of Player */
                    if (player.getXIndex() + dx < 0 || player.getXIndex() + dx >= boardSize){
                        player.kill();
                        gameActivity.showNotification(player);

                    }
                    if (player.getYIndex() + dy < 0 || player.getYIndex() + dy >= boardSize){
                        player.kill();
                        gameActivity.showNotification(player);
                    }
                    player.startMoving(newPosOnTile, newPosNextTile, dx, dy);
                    //player.setPos(newPos);

                    //player.setXIndex(player.getXIndex() + dx);
                    //player.setYIndex(player.getYIndex() + dy);



                    Log.d("NewPos", String.valueOf(player.getPos()));
                    Log.d("NewX", String.valueOf(player.getXIndex()));
                    Log.d("NewY", String.valueOf(player.getYIndex()));

                    Log.d("-------------", "--------------");
                    for (int j = 0; j < players.size(); j++) {
                        Log.d("Pos", String.valueOf(players.get(j).getPos()));
                    }

                } else {
                    player.changed = false;
                    player.setMoving(false);
                }
            }
        }

        if (virtual) {
            boolean changed = false;
            for (int i=0; i<players.size(); i++){
                if (players.get(i).changed) changed = true;
            }
            if (!changed) gamePhase = gpPlaying;
        }
    }

    public void playCard(Integer i){
        Player player = players.get(currentPlayer);
        CardSprite card = choiceCards.get(i);
        cardSelected = i;
        int pos = player.getPos();
        PlayedCardSprite playedCard = new PlayedCardSprite(this, card.getBottomBMP(), card.getTopBMP(), card.getWays(), player.getXIndex(), player.getYIndex(), card.getRotation());
        playedCards.put(String.valueOf(player.getXIndex())+"-"+String.valueOf(player.getYIndex()), playedCard);

    }

    public void rotateCard(Integer i, int rotation){
        Player player = players.get(currentPlayer);
        CardSprite card = choiceCards.get(i);
        CardSprite playedCard = playedCards.get(String.valueOf(player.getXIndex())+"-"+String.valueOf(player.getYIndex()));
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
                for (int i=0; i<3; i++){
                    CardSprite card = choiceCards.get(i);
                    if (card.isTouched(event.getX(), event.getY()) && gamePhase.equals(gpPlaying)){
                        Player player = players.get(currentPlayer);
                        Log.d("CardSelected", String.valueOf(i));
                        if (i == cardSelected) {
                            rotateCard(i, -1);
                        }
                        else {
                            playCard(i);
                        }
                        Log.d("CardSelected", String.valueOf(cardSelected));
                    }
                }

                if (gamePhase.equals(gpPlaying) & cardSelected != -1 & buttons.get(0).isTouched(event.getX(), event.getY())){
                    movePlayers();
                }

                if (gamePhase.equals(gpStart)){

                    for (int i=0; i<startPositions.size(); i++){
                        StartSprite sprite = startPositions.get(i);
                        if (sprite.isTouched(event.getX(), event.getY())){
                            Player player = players.get(currentPlayer);
                            player.setXIndex(sprite.getXIndex());
                            player.setYIndex(sprite.getYIndex());
                            player.setPos(sprite.getPos());

                            Log.d("X", String.valueOf(sprite.getXIndex()));
                            Log.d("Y", String.valueOf(sprite.getYIndex()));
                            Log.d("Pos", String.valueOf(sprite.getPos()));
                            currentPlayer += 1;
                            currentPlayer = currentPlayer%players.size();
                            startPositions.remove(sprite);
                            gameActivity.showNotification(players.get(currentPlayer));

                            if (currentPlayer == 0) {
                                currentPlayer = players.size()-1;
                                gamePhase = gpPlaying;
                                nextPlayer();
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public void nextPlayer(){
        int number_live_players = 0;
        String playerAlive = "NoBody";
        for (int i=0; i<players.size(); i++){
            if (players.get(i).alive){
                number_live_players += 1;
                playerAlive = String.valueOf(players.get(i).getColor());
            }
        }

        if (number_live_players <= 1){
            this.gamePhase = "GameOver";
            gameLoopThread.setRunning(false);
            gameActivity.onGameOver(playerAlive);
        }

        choiceCards =  new ArrayList<ChoiceCardSprite>();
        ArrayList <Integer> indexes = new ArrayList<Integer>();
        for (int i=0; i<3;  i++){
            int index = -1;
            while (true) {
                index = randomGenerator.nextInt(cards.size());
                if (indexes.contains(index) == false){
                    indexes.add(index);
                    break;
                }
            }
            ChoiceCardSprite card = cards.get(index);
            card.setPos(i);
            Log.d("Card", card.ways.toString());
            choiceCards.add(card);
        }

        gamePhase = gpPlaying;
        int i = 0;
        gameTurn += 1;
        while (i < players.size()) {
            currentPlayer += 1;
            currentPlayer = currentPlayer % players.size();
            gameActivity.showNotification(players.get(currentPlayer));

            cardSelected = -1;
            if (players.get(currentPlayer).alive) {
                if (players.get(currentPlayer).KI) players.get(currentPlayer).makeMove(playedCards, choiceCards, this);
                return;
            }
        }
    }

    public void drawChoiceCards(Canvas canvas){
        if (gamePhase == gpStart) return;
        for (int i=0; i<3;  i++){
            choiceCards.get(i).onDraw(canvas);
            Log.d("Draw", Integer.toString(i)+": "+  choiceCards.get(i).ways.toString());
        }
    }


    // Getter methods
    public int getMoveSteps(){
        return moveSteps;
    }
    public float getScaleFactor(){ return scaleFactor; }
}
