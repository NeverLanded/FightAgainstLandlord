package com.edu.bjtu.fightagainstlandlord.Data;

import android.support.annotation.NonNull;

public class CardData implements Comparable<CardData>{
    private int num;//卡片编号
    private boolean choose;//是否选中

    public CardData(int num){
        this.num = num;
        this.choose = false;
    }

    public void setChoose(boolean choose){
        this.choose = choose;
    }

    public int getNum(){
        return num;
    }

    public boolean getChoose(){
        return choose;
    }

    @Override
    public int compareTo(@NonNull CardData o) {
        return this.num - o.getNum();
    }
}
