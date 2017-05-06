package com.edu.bjtu.fightagainstlandlord.ClickListener;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.Data.Iterator.CardDataIterator;
import com.edu.bjtu.fightagainstlandlord.Data.Player.Player;
import com.edu.bjtu.fightagainstlandlord.R;
import com.edu.bjtu.fightagainstlandlord.Strategy.CardComparator;
import com.edu.bjtu.fightagainstlandlord.System.CardSystem;
import com.edu.bjtu.fightagainstlandlord.System.UICenter;

import java.util.ArrayList;
import java.util.List;

class CommitClickListener implements OnClickListener {
    @Override
    public void onClick(View v) {
        CardSystem cardSystem = CardSystem.getInstance();
        switch (v.getId()) {
            case R.id.game_button_commit:
                //找出用户要出的牌
                Player player = cardSystem.getPlayer(0);
                CardDataIterator iterator = new CardDataIterator(player.getCardDataList());
                List<CardData> commitCardDataList = new ArrayList<>();
                CardData cardData;
                while (!iterator.isLast()) {
                    cardData = iterator.getNextItem();
                    if (cardData.getChoose()) {
                        commitCardDataList.add(cardData);
                    }
                    iterator.next();
                }
                //判断出牌是否正确
                if (CardComparator.ifCardCanCommit(commitCardDataList)) {
                    //保存状态
                    cardSystem.save();
                    //更新数据
                    player.removeCards(commitCardDataList);//移除玩家手中的牌
                    if (player.getCardNum() <= 0) {//牌全部出完
                        cardSystem.getState().gameOver();//更新游戏状态为游戏结束
                    }
                    cardSystem.getState().nextTurn(0,CardComparator.judgeCardType(commitCardDataList), commitCardDataList);//出牌顺序往下走
                    //更新视图
                    UICenter uiCenter = UICenter.getInstance();
                    uiCenter.updateUI(R.integer.updateUserCardLayout);
                    uiCenter.updateUI(R.integer.updateDeskLayout, 0, commitCardDataList);//更新桌子视图
                    uiCenter.updateUI(R.integer.hideButton);//隐藏按钮
                    //唤醒下一个玩家
                    cardSystem.notifyPlayer();
                } else {//出牌不正确情况
                    Toast.makeText(v.getContext(), "您选择的牌有误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.game_button_cancel:
                //用户不出牌时
                if (cardSystem.getState().getLastTurn() == 0) {
                    Toast.makeText(v.getContext(), "您必须出牌", Toast.LENGTH_SHORT).show();
                } else {
                    //保存状态
                    cardSystem.save();
                    //更新数据
                    cardSystem.getState().nextTurn();
                    //更新视图
                    UICenter.getInstance().updateUI(R.integer.hideButton);//隐藏按钮
                    UICenter.getInstance().updateUI(R.integer.updateDeskLayout, 0,null);//删除桌子的牌
                    //唤醒下一个玩家
                    cardSystem.notifyPlayer();
                }
                break;
        }
    }
}
