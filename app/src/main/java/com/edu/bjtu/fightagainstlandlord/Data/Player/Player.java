package com.edu.bjtu.fightagainstlandlord.Data.Player;

import com.edu.bjtu.fightagainstlandlord.Strategy.CardOperation;
import com.edu.bjtu.fightagainstlandlord.Strategy.CardOperationAdapter;
import com.edu.bjtu.fightagainstlandlord.System.CardSystem;
import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.Data.Iterator.CardDataIterator;

import java.util.ArrayList;
import java.util.List;

public class Player implements Play{
    List<CardData> cardDataList;//允许同包访问，

    Player(){
        cardDataList = new ArrayList<>();
    }

    //放置一张扑克牌
    public void pushCard(CardData cardData){
        cardDataList.add(cardData);
        sortCardDec();
    }

    //放置多张扑克牌
    public void pushCards(List<CardData> cardDataList){
        CardDataIterator iterator = new CardDataIterator(cardDataList);
        while (!iterator.isLast()){
            this.cardDataList.add(iterator.getNextItem());
            iterator.next();
        }
        sortCardDec();
    }

    //接收地主牌
    public void pushLandlordCards(){
        CardSystem cardSystem = CardSystem.getInstance();
        int coveredCard[] = cardSystem.getCoveredCard();
        for (int i = 0;i < 3; i++){
            pushCard(new CardData(coveredCard[i]));
        }
    }

    //一张牌移除
    public void removeCard(CardData cardData){
        if (cardData != null) {
            CardDataIterator cardDataIterator = new CardDataIterator(cardDataList);
            while (!cardDataIterator.isLast()) {
                if (cardDataIterator.getNextItem().getNum() == cardData.getNum()) {
                    cardDataIterator.removeNextItem();
                    return;
                } else cardDataIterator.next();
            }
        }
    }

    //一连串牌移除
    public void removeCards(List<CardData> commitCardDataList){
        //得到迭代器
        CardDataIterator removeItreator = new CardDataIterator(commitCardDataList);
        while (!removeItreator.isLast()) {
            removeCard(removeItreator.getNextItem());
            removeItreator.next();
        }
    }

    //将牌按从大到小的顺序排列
    private void sortCardDec(){
        CardOperation cardOperation = new CardOperationAdapter();
        cardOperation.sortCardDec(cardDataList);
    }

    //获得牌的数量
    public int getCardNum(){
        return cardDataList.size();
    }

    //获得牌容器
    public List<CardData> getCardDataList(){
        return cardDataList;
    }

}
