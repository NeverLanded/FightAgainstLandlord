package com.edu.bjtu.fightagainstlandlord.Strategy.CommitCard;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.Data.Iterator.CardDataIterator;
import com.edu.bjtu.fightagainstlandlord.Data.Player.Player;
import com.edu.bjtu.fightagainstlandlord.Data.State.State;
import com.edu.bjtu.fightagainstlandlord.System.CardSystem;

import java.util.ArrayList;
import java.util.List;

class FindSameTypeMinimum implements CommitCard{
    /**
     * 查找同类型下最小的出牌选择
     * @return 放回选择的出牌数据集合，如果找不到，返回null
     * TODO 完善查找牌的类型选择
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
        CardDataIterator beforeIterator = null;
        if (lastCardDataList != null) {
            beforeIterator = new CardDataIterator(lastCardDataList);
        }
        List<CardData> list = new ArrayList<>();

        //判断单牌
        if (type == 0 || nowTurn == lastTurn || beforeIterator == null) {
            //如果是同一顺序，直接出最小的牌
            if (nowTurn == lastTurn || beforeIterator == null){
                list.add(iterator.getPreviousItem());
                return list;
            }
            while (!iterator.isFirst() && iterator.getPreviousItem().getNum() != 53 && iterator.getPreviousItem().getNum()/4 <= beforeIterator.getNextItem().getNum()/4) {
                iterator.previous();//往前走
            }
            if (!iterator.isFirst() && (iterator.getPreviousItem().getNum()/4 > beforeIterator.getNextItem().getNum()/4|| iterator.getNextItem().getNum() == 53)) {
                list.add(iterator.getPreviousItem());
                return list;
            }
            return null;
        }
        //判断双牌
        if (type == 1) {
            CardData temp1 = iterator.getPreviousItem(), temp2;
            iterator.previous();
            temp2 = iterator.getPreviousItem();
            iterator.previous();
            while (!iterator.isFirst() && (temp1.getNum() <= beforeIterator.getNextItem().getNum() || temp1.getNum() != temp2.getNum())) {
                temp1 = temp2;
                temp2 = iterator.getPreviousItem();
                iterator.previous();
            }
            if (!iterator.isFirst()) {
                list.add(temp1);
                list.add(temp2);
                return list;
            }
            return null;
        }
        return null;//没有查找到数据
    }
}
