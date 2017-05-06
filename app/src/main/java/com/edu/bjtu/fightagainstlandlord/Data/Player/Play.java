package com.edu.bjtu.fightagainstlandlord.Data.Player;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;

import java.util.List;

interface Play {
    void pushCard(CardData cardData);
    void pushCards(List<CardData> cardDataList);
    void pushLandlordCards();
    void removeCard(CardData cardData);
    void removeCards(List<CardData> cardDataList);
    List<CardData> getCardDataList();
}
