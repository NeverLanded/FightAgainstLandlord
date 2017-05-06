package com.edu.bjtu.fightagainstlandlord.Data.View;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;

import java.util.Hashtable;

public class CardViewFactory {
    private Hashtable<Integer,CardView> ht;

    private static CardViewFactory instance;

    public static void createInstance(){
        instance = new CardViewFactory();
    }

    public static CardViewFactory getInstance(){
        return instance;
    }

    private CardViewFactory(){
        ht = new Hashtable<>();
    }

    //获得卡片视图
    public CardView getCardView(CardData cardData){
        return ht.get(cardData.getNum());
    }

    //存放卡片视图
    public void putCardView(CardData cardData, CardView cardView){
        ht.put(cardData.getNum(), cardView);
    }

    //移除卡片视图
    public void removeCardView(CardData cardData){
        ht.remove(cardData.getNum());
    }

}
