package com.edu.bjtu.fightagainstlandlord.Strategy;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.R;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

//卡片操作适配器
public class CardOperationAdapter implements CardOperation {
    private Sort sort;
    private Find find;

    public CardOperationAdapter() {
        sort = new Sort();
        find = new Find();
    }

    @Override
    public int findSameCardLocation(int num, int[] card, int sameNum) {
        return find.findSameLocation(num, card, sameNum);
    }

    @Override
    public List<CardData> sortCardDec(List<CardData> list) {
        return sort.sortListDec(list);
    }


    @Override
    public int cardNumToID(int num) {
        String temp = "game_card_"+num;
        try {
            Field idField = R.drawable.class.getDeclaredField(temp);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}


class Find {
    int findSameLocation(int num, int card[], int sameNum) {
        boolean findSameNum;
        int i, j;
        for (i = 0; i < num - sameNum; i++) {
            findSameNum = true;
            for (j = i + 1; j < i + sameNum && j < num && findSameNum; j++) {
                if (card[j] != card[i]) findSameNum = false;
            }
            if (findSameNum) return i;
        }
        return -1;
    }
}

class Sort {
    List<CardData> sortListDec(List<CardData> list) {
        Collections.sort(list);
        Collections.reverse(list);
        return list;
    }
}
