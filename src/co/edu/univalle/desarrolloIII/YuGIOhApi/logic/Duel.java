package co.edu.univalle.desarrolloIII.YuGIOhApi.logic;

import co.edu.univalle.desarrolloIII.YuGIOhApi.Listener.BattleListener;
import co.edu.univalle.desarrolloIII.YuGIOhApi.model.Card;

import java.util.List;

public class Duel {

    private final List<Card> playerCards;
    private final List<Card> cpuCards;
    private final BattleListener listener;

    private int playerScore;
    private int cpuScore;
    private final int MAX_POINTS = 2;

    public Duel(List<Card> playerCards, List<Card> cpuCards, BattleListener listener) {
        this.playerCards = playerCards;
        this.cpuCards = cpuCards;
        this.listener = listener;
        this.playerScore = 0;
        this.cpuScore = 0;
    }

    public void addPointToPlayer() {
        playerScore++;
        listener.onScoreChanged(playerScore, cpuScore);
        checkEnd();
    }

    public void addPointToCpu() {
        cpuScore++;
        listener.onScoreChanged(playerScore, cpuScore);
        checkEnd();
    }

    private void checkEnd() {
        if (playerScore >= MAX_POINTS) {
            listener.onDuelEnded("Jugador");
        } else if (cpuScore >= MAX_POINTS) {
            listener.onDuelEnded("CPU");
        }
    }

    public boolean isFinished() {
        return playerScore >= 2 || cpuScore >= 2;
    }

    public void reset() {
        playerScore = 0;
        cpuScore = 0;
        listener.onScoreChanged(playerScore, cpuScore);
    }

}
