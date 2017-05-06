package com.edu.bjtu.fightagainstlandlord.Strategy;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;

import java.util.List;

public interface CardOperation {
    int findSameCardLocation(int num, int card[], int sameNum);
    List<CardData> sortCardDec(List<CardData> list);
    int cardNumToID(int num);
}

