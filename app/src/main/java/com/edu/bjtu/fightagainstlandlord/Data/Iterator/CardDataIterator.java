package com.edu.bjtu.fightagainstlandlord.Data.Iterator;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.Data.Iterator.Iterator;

import java.util.List;

public class CardDataIterator implements Iterator {
    private List<CardData> list;
    private int cursor1; //定义一个游标，用于记录正向遍历的位置；
    private int cursor2; //定义一个游标，用于记录反向遍历的位置；

    public CardDataIterator(List<CardData> list) {
        this.list = list;
        cursor1 = 0;
        if (list != null)
            cursor2 = list.size() - 1;
        else cursor2 = -1;
    }

    @Override
    public void next() {
        if (list != null && cursor1 < list.size()) {
            cursor1++;
        }
    }

    @Override
    public void previous() {
        if (cursor2 > -1) {
            cursor2--;
        }
    }

    @Override
    public boolean isFirst() {
        return (cursor2 == -1);
    }

    @Override
    public boolean isLast() {
        return (list == null || cursor1 == list.size());
    }

    @Override
    public CardData getNextItem() {
        if (isLast()) return null;
        return list.get(cursor1);
    }

    @Override
    public CardData getPreviousItem() {
        if (isFirst()) return null;
        return list.get(cursor2);
    }

    @Override
    public void removeNextItem() {
        if (list == null || isLast()) return;
        list.remove(cursor1);
    }

    @Override
    public int getSize() {
        if (list == null) return 0;
        return list.size();
    }
}
