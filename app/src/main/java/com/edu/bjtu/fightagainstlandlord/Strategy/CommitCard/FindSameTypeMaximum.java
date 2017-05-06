package com.edu.bjtu.fightagainstlandlord.Strategy.CommitCard;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.Data.Iterator.CardDataIterator;
import com.edu.bjtu.fightagainstlandlord.Data.Player.Player;
import com.edu.bjtu.fightagainstlandlord.Data.State.State;
import com.edu.bjtu.fightagainstlandlord.System.CardSystem;

import java.util.ArrayList;
import java.util.List;

class FindSameTypeMaximum implements CommitCard{
    /**
     * 找到同一类型的最大出牌选择
     * @return 返回同一类型的最大出牌选择，如果找不到，返回null
     * TODO 其它类型的牌的查找
     */
    @Override
    public List<CardData> commitCard() {
        //获取数据
        State state = CardSystem.getInstance().getState();
        int nowTurn = state.getTurn();
        int lastTurn = state.getLastTurn();
        Player systemPlayer = CardSystem.getInstance().getPlayer(nowTurn);
        List<CardData> cardDataList = systemPlayer.getCardDataList();
        List<CardData> lastCardDataList = state.getLastCardDataList(lastTurn);
        int type = state.getLastType(lastTurn);

        //迭代器
        CardDataIterator iterator = new CardDataIterator(cardDataList);
        CardDataIterator beforeIterator = new CardDataIterator(lastCardDataList);
        List<CardData> list = new ArrayList<>();

        //判断单牌
        if (type == 0 || nowTurn == lastTurn) {
            //如果是同一个顺序,直接出最大牌
            if (nowTurn == lastTurn) {
                list.add(iterator.getNextItem());
                return list;
            }
            if (iterator.getNextItem().getNum()/4 > beforeIterator.getNextItem().getNum()/4 || iterator.getNextItem().getNum() == 53) {
                list.add(iterator.getNextItem());
                return list;
            }
            else return null;
        }
        //判断双牌
        if (type == 1) {
            CardData temp1 = iterator.getNextItem(), temp2;
            iterator.next();
            while (!iterator.isLast() && temp1.getNum() > beforeIterator.getNextItem().getNum()) {
                temp2 = iterator.getNextItem();
                //如果两张牌相等
                if (temp1.getNum() == temp2.getNum()) {
                    list.add(temp1);
                    list.add(temp2);
                    return list;
                }
                //继续往下找
                temp1 = temp2;
                iterator.next();
            }
            return null;
        }
        return null;//没有查找到数据
    }
}
