package com.edu.bjtu.fightagainstlandlord.Strategy.CommitCard;

import com.edu.bjtu.fightagainstlandlord.Data.State.State;
import com.edu.bjtu.fightagainstlandlord.System.CardSystem;
import com.edu.bjtu.fightagainstlandlord.Data.CardData;

import java.util.List;

public class DecideCommit {
    public static List<CardData> decideCommitWhichCard(){
        State state = CardSystem.getInstance().getState();
        int landlordTurn = state.getLandlordTurn();
        int nowTurn = state.getTurn();
        int lastTurn = state.getLastTurn();
        int nextTurn = (nowTurn + 1) % 3;
        CommitCard commitCard;
        if (nowTurn == lastTurn || nowTurn == landlordTurn ||  nextTurn != landlordTurn){
            commitCard = new FindSameTypeMinimum();
        }
        else commitCard = new FindSameTypeMaximum();
        return commitCard.commitCard();
    }
}
