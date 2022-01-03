package de.badgersburrow.derailer.sprites;

import static java.lang.Math.abs;
import static java.lang.Math.min;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.badgersburrow.derailer.GameView;
import de.badgersburrow.derailer.Keys;
import de.badgersburrow.derailer.R;
import de.badgersburrow.derailer.objects.GameTheme;

public class PlayerAiSprite extends PlayerSprite{

    private final static String TAG = "PlayerAiSprite";

    private int searchDepth = 3;
    String KIStrength;
    String aiPhase;

    // Points for score calculation
    int distancePoints = 10;
    int fieldPoints = 1;
    int killPoints = 50;

    private final int max_dist = 1;

    public PlayerAiSprite(GameView gameView, GameTheme theme, int num, int color, int tiles, String selection) {
        super(gameView, theme, num, color, tiles);
        this.KIStrength = selection;
        this.aiPhase = Keys.ppIdle;
    }


    @Override
    public boolean isKI() {
        return true;
    }

    public String getLabel(Context context){
        return context.getString(R.string.label_ai, num);
    }

    public void makeMove(Map<String, PlayedCardSprite> playedCards, ArrayList<ChoiceCardSprite> choiceCards, GameView gameView){
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
                            if (!gameView.getGamePhase().equals(Keys.gpMoving)) break;
                        } else {
                            break;
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
                    do {
                        gameView.movePlayers();
                    } while (gameView.getGamePhase().equals(Keys.gpMoving));
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
                    do {
                        gameView.movePlayers();
                    } while (gameView.getGamePhase().equals(Keys.gpMoving));

                    if (aliveVirtual){
                        int fieldScore = 1;
                        List<String> calcCards = new ArrayList<>();
                        calcCards.add(Integer.toString(xIndexVirtual)+"-"+Integer.toString(yIndexVirtual));
                        fieldScore += calcFieldScore(playedCards, xIndexVirtual+1, yIndexVirtual,calcCards);
                        fieldScore += calcFieldScore(playedCards, xIndexVirtual, yIndexVirtual+1,calcCards);
                        fieldScore += calcFieldScore(playedCards, xIndexVirtual-1, yIndexVirtual,calcCards);
                        fieldScore += calcFieldScore(playedCards, xIndexVirtual, yIndexVirtual-1,calcCards);

                        fieldScore += distancePoints*min(gameView.getBoardSize()-abs(xIndexVirtual-gameView.getBoardSize()/2), gameView.getBoardSize()-abs(yIndexVirtual-gameView.getBoardSize()/2));
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

    public void setStartPosition(ArrayList<StartSprite> startPositions, ArrayList<ObstacleCardSprite> obstacles){
        Random randomGenerator = new Random();
        ArrayList<Integer> scores = new ArrayList<>();

        // determine tiles without occupied start positions
        ArrayList<StartSprite> freePositions = new ArrayList<>();
        for (int i=0; i<startPositions.size(); i++){
            StartSprite start = startPositions.get(i);
            int count = 0;
            for (int j=i; j<startPositions.size(); j++){
                StartSprite startCompare = startPositions.get(j);
                if (start.getXIndex() == startCompare.getXIndex() && start.getYIndex() == startCompare.getYIndex()){
                    count++;
                }
                if ((start.getXIndex() == 0 || start.getXIndex() == gameView.getBoardSize()-1) && (start.getYIndex() == 0 || start.getYIndex() == gameView.getBoardSize()-1)){
                    if ((count == 4 && gameView.getTiles()==8) || (count == 2 && gameView.getTiles()==4)){
                        freePositions.add(start);
                    }
                } else {
                    if ((count == 2 && gameView.getTiles()==8) || (count == 1 && gameView.getTiles()==4)){
                        freePositions.add(start);
                    }
                }
            }
        }
        Log.d(TAG, "freePositions: " + freePositions.size());

        for (StartSprite start : startPositions){
            scores.add(calcStartScore(start, freePositions, obstacles));
        }
        int max = Collections.max(scores);
        Log.d(TAG, "scores: " + scores + ", max: " + max);

        ArrayList<Integer> bestPositions = new ArrayList<>();
        for (int i=0; i<scores.size(); i++){
            if (scores.get(i) == max){
                bestPositions.add(i);
            }
        }

        int pos = randomGenerator.nextInt(bestPositions.size());

        StartSprite sprite = startPositions.get(bestPositions.get(pos));

        setXIndex(sprite.getXIndex());
        setYIndex(sprite.getYIndex());
        setPos(sprite.getPos());

        startPositions.remove(sprite);
    }

    private int calcStartScore(StartSprite start, ArrayList<StartSprite> startPositions, ArrayList<ObstacleCardSprite> obstacles){
        int score = 0;
        //Log.d(TAG, "start.xIndex: " + start.getXIndex() + ", start.yIndex: " + start.getYIndex());
        for (int x=0; x<gameView.getBoardSize(); x++){
            if (abs(start.getXIndex() - x) > max_dist){
                continue;
            }
            for (int y=0; y<gameView.getBoardSize(); y++){
                if (abs(start.getYIndex() - y) > max_dist){
                    continue;
                }

                // has no obstacle on it
                score += distanceWeight(start, x, y);
                for (ObstacleCardSprite obstacle: obstacles){
                    if (x == obstacle.getXIndex() && y == obstacle.getYIndex()){
                        score -= distanceWeight(start, x, y);
                        break;
                    }
                }

                // occupied start positions
                for (StartSprite startSprite: startPositions){
                    if (x == startSprite.getXIndex() && y == startSprite.getYIndex()){
                        score += distanceWeight(start,startSprite.getXIndex(), startSprite.getYIndex());
                    }
                }

            }
        }

        return score;
    }

    private int distanceWeight(StartSprite s1, int x2, int y2){
        int x1 = s1.getXIndex();
        int y1 = s1.getYIndex();

        return 2*max_dist - abs(x1-x2) - abs(y1-y2);
    }



    private int calcFieldScore(Map<String, PlayedCardSprite> playedCards, int x, int y, List<String> calcCards){
        int score = 0;
        if (x < 0 || x>= gameView.getBoardSize() || y <0 || y >= gameView.getBoardSize()) return 0;
        if (!playedCards.containsKey(x + "-" + y)){
            if (!calcCards.contains(x + "-" + y)){
                score += fieldPoints;
                calcCards.add(x +"-"+ y);
                score += calcFieldScore(playedCards, x+1, y,calcCards);
                score += calcFieldScore(playedCards, x, y+1,calcCards);
                score += calcFieldScore(playedCards, x-1, y,calcCards);
                score += calcFieldScore(playedCards, x, y-1,calcCards);
            }
        }
        return score;
    }

    public String getAiPhase() {
        return aiPhase;
    }
    public void setAiPhase(String aiPhase) {
        this.aiPhase = aiPhase;
    }
}
