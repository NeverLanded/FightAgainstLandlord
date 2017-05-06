package com.edu.bjtu.fightagainstlandlord.Data.State;

import android.util.SparseArray;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.Data.Player.SystemPlayer;
import com.edu.bjtu.fightagainstlandlord.System.CardSystem;

import java.util.List;

public abstract class State {
    CardSystem cardSystem;
    //游戏阶段数据
    boolean gameOver;//false，游戏正在进行；true，游戏结束。
    boolean isGrab;//true，处于抢地主阶段；false，处于出牌阶段
    int turn;//0，用户玩家出牌，1，系统玩家1出牌，2，系统玩家2出牌
    int landlordTurn;//地主的顺序
    //上一轮出牌数据
    int lastTurn;//上一次出牌的人
    int lastType[];//上一次出牌的类型，
    SparseArray<List<CardData>> lastCardDataLists;//上一轮出牌的集合，null表示上一轮没有出牌

    public State() {
        lastType = new int[3];
        lastCardDataLists = new SparseArray<>();
    }

    public abstract boolean grab();

    public abstract boolean nextTurn();

    public abstract boolean nextTurn(int lastTurn, int lastType, List<CardData> lastCardDataList);

    public abstract boolean gameOver();

    public void setCardSystem(CardSystem cardSystem) {
        this.cardSystem = cardSystem;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setGrab(boolean grab) {
        isGrab = grab;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setLandlordTurn(int landlordTurn) {
        this.landlordTurn = landlordTurn;
    }

    public void setLastTurn(int lastTurn) {
        this.lastTurn = lastTurn;
    }

    public void setLastType(int[] lastType) {
        System.arraycopy(lastType,0,this.lastType,0,3);
    }

    public void setLastCardDataLists(SparseArray<List<CardData>> lastCardDataLists) {
        for (int i =0;i< 3; i++)
            this.lastCardDataLists.put(i,lastCardDataLists.get(i));
    }

    public CardSystem getCardSystem() {
        return cardSystem;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGrab() {
        return isGrab;
    }

    public int[] getLastType() {
        return lastType;
    }

    public SparseArray<List<CardData>> getLastCardDataLists() {
        return lastCardDataLists;
    }

    //获得顺序
    public int getTurn() {
        return turn;
    }

    //获得地主编号
    public int getLandlordTurn() {
        return landlordTurn;
    }

    //获得上一轮出牌的人
    public int getLastTurn() {
        return lastTurn;
    }

    //获得历史出牌的类型
    public int getLastType(int index) {
        if (index >= 0 && index <= 3)
            return lastType[index];
        return -1;//错误情况
    }

    //获得历史出牌的数据
    public List<CardData> getLastCardDataList(int index) {
        if (index >= 0 && index <= 3)
            return lastCardDataLists.get(index);
        return null;
    }


}
