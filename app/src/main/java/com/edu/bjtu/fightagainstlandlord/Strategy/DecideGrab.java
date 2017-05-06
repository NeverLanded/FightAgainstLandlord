package com.edu.bjtu.fightagainstlandlord.Strategy;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.Data.Iterator.CardDataIterator;

import java.util.List;

/**
 * 决定抢不抢地主
 */
public class DecideGrab {
    public static boolean ifGrab(List<CardData> cardDataList){
        CardDataIterator iterator = new CardDataIterator(cardDataList);
        int sum = 0;
        while (!iterator.isLast()){
            sum += iterator.getNextItem().getNum() / 4;
            iterator.next();
        }
        return (sum / iterator.getSize() >= 7);
    }
}
