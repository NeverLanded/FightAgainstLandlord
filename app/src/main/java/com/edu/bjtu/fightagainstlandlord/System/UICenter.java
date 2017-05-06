package com.edu.bjtu.fightagainstlandlord.System;

import android.os.Handler;
import android.os.Message;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.GameActivity;

import java.util.List;

public class UICenter implements UIUpdate {
    private static UICenter instance;
    private Handler handler;

    private UICenter() {
        handler = GameActivity.getHandler();
    }

    public static void createInstance(){
        instance = new UICenter();
    }
    public static UICenter getInstance() {
        return instance;
    }

    public void updateUI(int msg){
        updateUI(msg,-1,null);
    }

    public void updateUI(int msg, int turn) {
        updateUI(msg, turn, null);
    }

    public void updateUI(int msg, int turn, List<CardData> commitCardDataList) {
        Message message = new Message();
        message.what = msg;
        message.arg1 = turn;
        message.obj = commitCardDataList;
        handler.sendMessage(message);
    }
}
