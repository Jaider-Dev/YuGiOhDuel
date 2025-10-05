package co.edu.univalle.desarrolloIII.YuGIOhApi.Listener;

import co.edu.univalle.desarrolloIII.YuGIOhApi.model.Card;

public interface BattleListener {
    void onTurn(Card playerCard, Card cpuCard, String Winner);

    void onScoreChanged(int playerScore, int cpuScore);

    void onDuelEnded(String winner);
}


