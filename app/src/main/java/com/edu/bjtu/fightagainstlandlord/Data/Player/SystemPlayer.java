package com.edu.bjtu.fightagainstlandlord.Data.Player;


import android.util.Log;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.Data.State.CommitState;
import com.edu.bjtu.fightagainstlandlord.Data.State.GrabState;
import com.edu.bjtu.fightagainstlandlord.Data.State.OverState;
import com.edu.bjtu.fightagainstlandlord.Data.State.State;
import com.edu.bjtu.fightagainstlandlord.R;
import com.edu.bjtu.fightagainstlandlord.Strategy.CardComparator;
import com.edu.bjtu.fightagainstlandlord.Strategy.CommitCard.DecideCommit;
import com.edu.bjtu.fightagainstlandlord.Strategy.DecideGrab;
import com.edu.bjtu.fightagainstlandlord.System.CardSystem;
import com.edu.bjtu.fightagainstlandlord.System.UICenter;

import java.util.List;

public class SystemPlayer extends Player {
    public SystemPlayer() {
        super();
    }

    //自动决定抢地主、出牌
    public void commit(int turn) {
        Log.i("game", turn + "自动打牌");
        //获取数据
        CardSystem cardSystem = CardSystem.getInstance();//游戏系统
        State state = cardSystem.getState();//游戏状态
        UICenter uiCenter = UICenter.getInstance();//
        //若游戏已经结束
        if (state instanceof OverState) return;//直接结束函数
        //若处于抢地主阶段
        if (state instanceof GrabState) {
            if (decideGrabLandlord()) {//决定抢地主
                //更新数据
                this.pushLandlordCards();//插入地主牌
                state.grab();//抢地主了
                state = cardSystem.getState();//游戏状态
                //更新UI
                uiCenter.updateUI(R.integer.grabLandlord);
                //唤醒下一个玩家
                //注：由于该玩家抢地主，所以该玩家继续出牌，无需唤醒下一个玩家
            } else {//不抢地主
                //更新数据
                state.nextTurn();
                //更新UI
                uiCenter.updateUI(R.integer.notGrabLandlord, turn, null);//提示系统不抢地主
                //唤醒下一个玩家
                cardSystem.notifyPlayer();
            }
        }
        //处于出牌阶段
        if (state instanceof CommitState) {
            //获取数据
            List<CardData> commitCardDataList = commitCard();//出牌
            //更新数据
            if (commitCardDataList != null && commitCardDataList.size() != 0) {//系统玩家出牌了
                state.nextTurn(turn, CardComparator.judgeCardType(commitCardDataList), commitCardDataList);
                if (getCardNum() <= 0) state.gameOver();//如果牌出完了，游戏结束
            } else {//系统玩家没出牌
                state.nextTurn();
            }
            //更新UI
            if (commitCardDataList != null && commitCardDataList.size() != 0) {
                uiCenter.updateUI(R.integer.commitCard, turn, commitCardDataList);
            } else
                uiCenter.updateUI(R.integer.notCommitCard, turn, null);//通知用户，系统玩家没有出牌
            //唤醒下一个玩家
            cardSystem.notifyPlayer();//下一轮
        }
    }

    //系统玩家自动出牌
    private List<CardData> commitCard() {
        List<CardData> commitCardDataList = DecideCommit.decideCommitWhichCard();
        removeCards(commitCardDataList);
        return commitCardDataList;
    }

    //系统玩家自动抢地主
    private boolean decideGrabLandlord() {
        return DecideGrab.ifGrab(cardDataList);
    }
}
