package de.badgersburrow.derailer.sprites;

import static java.lang.Math.abs;
import static java.lang.Math.min;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.badgersburrow.derailer.GameView;
import de.badgersburrow.derailer.Keys;
import de.badgersburrow.derailer.R;
import de.badgersburrow.derailer.objects.GameTheme;

public class PlayerAiSprite extends PlayerSprite{


    private int searchDepth = 3;
    String KIStrength;

    // Points for score calculation
    int distancePoints = 10;
    int fieldPoints = 1;
    int killPoints = 50;

    public PlayerAiSprite(GameView gameView, GameTheme theme, int num, int color, int tiles, String selection) {
        super(gameView, theme, num, color, tiles);
        this.KIStrength = selection;
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
}
