package com.edu.bjtu.fightagainstlandlord.Memento;

import com.edu.bjtu.fightagainstlandlord.Data.Player.Player;
import com.edu.bjtu.fightagainstlandlord.Data.Player.SystemPlayer;
import com.edu.bjtu.fightagainstlandlord.Data.Player.UserPlayer;
import com.edu.bjtu.fightagainstlandlord.Data.State.CommitState;
import com.edu.bjtu.fightagainstlandlord.Data.State.GrabState;
import com.edu.bjtu.fightagainstlandlord.Data.State.OverState;
import com.edu.bjtu.fightagainstlandlord.Data.State.State;

//TODO
public class GameMemento {
    private Player player[];
    private State state;//游戏状态

    public GameMemento(Player[] player, State state) {
        this.player = new Player[3];
        this.player[0] = new UserPlayer();
        this.player[1] = new SystemPlayer();
        this.player[2] = new SystemPlayer();
        for (int i = 0; i < 3; i++)
            this.player[i].pushCards(player[i].getCardDataList());

        if (state instanceof OverState) this.state = new OverState(state);
        else if (state instanceof CommitState) this.state = new CommitState(state);
        else this.state = new GrabState(state.getCardSystem());

        this.state.setGameOver(state.isGameOver());
        this.state.setGrab(state.isGrab());

        this.state.setLandlordTurn(state.getLandlordTurn());
        this.state.setTurn(state.getTurn());

        this.state.setLastTurn(state.getLastTurn());
        this.state.setLastType(state.getLastType());
        this.state.setLastCardDataLists(state.getLastCardDataLists());
    }

    public Player[] getPlayer() {
        return player;
    }

    public State getState() {
        return state;
    }
}
