package com.edu.bjtu.fightagainstlandlord.System;

import android.util.Log;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.Data.Player.Player;
import com.edu.bjtu.fightagainstlandlord.Data.Player.SystemPlayer;
import com.edu.bjtu.fightagainstlandlord.Data.Player.UserPlayer;
import com.edu.bjtu.fightagainstlandlord.Data.State.CommitState;
import com.edu.bjtu.fightagainstlandlord.Data.State.GrabState;
import com.edu.bjtu.fightagainstlandlord.Data.State.OverState;
import com.edu.bjtu.fightagainstlandlord.Data.State.State;
import com.edu.bjtu.fightagainstlandlord.Memento.GameMemento;
import com.edu.bjtu.fightagainstlandlord.Memento.GameMementoCaretaker;
import com.edu.bjtu.fightagainstlandlord.R;

import java.util.Random;

public class CardSystem {
    private static CardSystem instance;//单例模式
    private Player player[];//玩家
    private int coveredCard[];//底牌
    private State state;//游戏状态

    private GameMementoCaretaker caretaker;

    private CardSystem() {
        //创建用户,一个用户玩家和两个系统玩家
        player = new Player[3];
        player[0] = new UserPlayer();
        player[1] = new SystemPlayer();
        player[2] = new SystemPlayer();
        //游戏状态，处于抢地主阶段
        state = new GrabState(this);
        //分配卡片
        divideCard();
        //生成备忘录
        caretaker = new GameMementoCaretaker();
    }

    //创建系统
    public static void createInstance() {
        instance = new CardSystem();
    }

    //获得系统
    public static CardSystem getInstance() {
        return instance;
    }

    //发牌
    private void divideCard() {
        coveredCard = new int[3];//创建覆盖的牌的数据
        //随机数生成器
        Random random = new Random();
        int i, j, temp, num[] = new int[54];
        //所有牌未分配
        for (i = 0; i < 54; i++)
            num[i] = 0;
        //分配底牌
        j = 0;
        while (j < 3) {
            temp = random.nextInt(54);
            while (num[temp] == 1)
                temp = random.nextInt(54);
            num[temp] = 1;
            coveredCard[j++] = temp;
        }

        //给玩家分配牌
        for (i = 0; i < 3; i++) {
            j = 0;
            while (j < 17) {
                temp = random.nextInt(54);
                while (num[temp] == 1)
                    temp = random.nextInt(54);
                num[temp] = 1;
                player[i].pushCard(new CardData(temp));
                j++;
            }
        }
    }

    //获得覆盖的牌
    public int[] getCoveredCard() {
        return coveredCard;
    }

    //获得玩家
    public Player getPlayer(int index) {
        return player[index];
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void notifyPlayer() {
        //游戏结束
        if (state instanceof OverState) {
            UICenter.getInstance().updateUI(R.integer.toastWin);
        }
        //唤醒玩家
        else if (state.getTurn() == 0) {
            UICenter.getInstance().updateUI(R.integer.showButton, 0);
            Log.i("game", "等待用户玩家操作");
        }
        //唤醒系统
        else {
            Log.i("game", "唤醒系统玩家" + state.getTurn());
            ((SystemPlayer) player[state.getTurn()]).commit(state.getTurn());
        }
    }

    //保存状态
    public void save() {
        caretaker.setGameMemento(new GameMemento(player, state));
    }

    //恢复状态
    public void restore() {
        //恢复数据
        System.arraycopy(caretaker.getGameMemento().getPlayer(), 0, player, 0, 3);//恢复玩家
        this.state = caretaker.getGameMemento().getState();
        //恢复界面
        UICenter uiCenter = UICenter.getInstance();
        if (state instanceof GrabState) {//覆盖地主牌
            uiCenter.updateUI(R.integer.coverLandlordCard);
        }
        uiCenter.updateUI(R.integer.updateUserCardLayout);//更新用户手牌
        if (state instanceof CommitState) {//更新玩家出牌
            for (int i = 0; i < 3; i++) {
                if (this.state.getLastCardDataList(i) == null || this.state.getLastCardDataList(i).size() == 0)
                    uiCenter.updateUI(R.integer.notCommitCard, i);
                else
                    uiCenter.updateUI(R.integer.updateDeskLayout, i, this.state.getLastCardDataList(i));
            }
        }
        //唤醒玩家
        CardSystem.getInstance().notifyPlayer();
    }
}
