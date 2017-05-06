package com.edu.bjtu.fightagainstlandlord.Data.State;

import android.util.Log;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;

import java.util.List;

public class CommitState extends State {
    public CommitState(State state){
        this.cardSystem = state.cardSystem;
    }

    @Override
    public boolean grab() {
        Log.e("game", "不是抢地主状态");
        return false;
    }

    @Override
    public boolean nextTurn() {
        Log.i("game", turn + "不出牌");
        this.lastType[turn] = -1;
        this.lastCardDataLists.put(turn,null);
        turn = (turn + 1) % 3;
        return true;
    }

    @Override
    public boolean nextTurn(int lastTurn, int lastType, List<CardData> lastCardDataList) {
        this.lastTurn = lastTurn;
        this.lastType[lastTurn] = lastType;
        this.lastCardDataLists.put(lastTurn,lastCardDataList);
        Log.i("game", turn + "出牌了");
        turn = (turn + 1) % 3;
        return true;
    }

    @Override
    public boolean gameOver() {
        this.gameOver = true;
        this.cardSystem.setState(new OverState(this));
        return true;
    }

}
