package com.edu.bjtu.fightagainstlandlord.Data.State;

import android.util.Log;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.System.CardSystem;

import java.util.List;
import java.util.Random;

public class GrabState extends State {
    public GrabState(CardSystem cardSystem) {
        this.cardSystem = cardSystem;
        //随机抢地主开始顺序
        Random random = new Random();
        turn = random.nextInt(3);

        gameOver = false;//false，游戏正在进行；true，游戏结束。
        isGrab = true;//true，处于抢地主阶段；false，处于出牌阶段

        landlordTurn = -1;
        //上一轮出牌数据
        lastTurn = -1;//上一次出牌者，出牌者-1表示没有上一次出牌的人
        for (int i = 0;i < 3; i++) {
            lastType[i] = -1;//每个人上一次出牌的类型，
            lastCardDataLists.put(i,null);//每个人上一次出牌的集合，null表示上一轮没有出牌
        }
    }

    @Override
    public boolean grab() {
        landlordTurn = turn;
        isGrab = false;
        cardSystem.setState(new CommitState(this));//状态更改
        return true;
    }

    @Override
    public boolean nextTurn() {
        turn = (turn + 1) % 3;
        return true;
    }

    @Override
    public boolean nextTurn(int lastTurn, int lastType, List<CardData> lastCardDataList) {
        Log.e("game","正处于抢地主阶段");
        return false;
    }

    @Override
    public boolean gameOver() {
        Log.e("game","正处于抢地主阶段");
        return false;
    }
}
