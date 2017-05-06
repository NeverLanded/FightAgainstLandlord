package com.edu.bjtu.fightagainstlandlord.Data.State;

import android.util.Log;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;

import java.util.List;

public class OverState extends State {

    public OverState(State state){
        this.cardSystem = state.cardSystem;
    }

    @Override
    public boolean grab() {
        Log.e("game","游戏已结束");
        return false;
    }

    @Override
    public boolean nextTurn() {
        Log.e("game","游戏已结束");
        return false;
    }

    @Override
    public boolean nextTurn(int lastTurn, int lastType, List<CardData> lastCardDataList) {
        Log.e("game","游戏已结束");
        return false;
    }

    @Override
    public boolean gameOver() {
        Log.e("game","游戏已结束");
        return false;
    }

}
