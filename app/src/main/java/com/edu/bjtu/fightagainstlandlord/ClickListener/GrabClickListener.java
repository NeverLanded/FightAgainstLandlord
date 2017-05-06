package com.edu.bjtu.fightagainstlandlord.ClickListener;

import android.view.View;

import com.edu.bjtu.fightagainstlandlord.R;
import com.edu.bjtu.fightagainstlandlord.System.CardSystem;
import com.edu.bjtu.fightagainstlandlord.System.UICenter;

class GrabClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        UICenter uiCenter = UICenter.getInstance();
        switch (v.getId()) {
            case R.id.game_button_commit:
                //保存状态
                CardSystem.getInstance().save();
                //更新数据
                CardSystem.getInstance().getPlayer(0).pushLandlordCards();//插入地主牌
                CardSystem.getInstance().getState().grab();//状态数据更新
                //更新界面
                uiCenter.updateUI(R.integer.hideButton);//隐藏按钮
                uiCenter.updateUI(R.integer.grabLandlord);//翻开底牌且更换按钮监听
                uiCenter.updateUI(R.integer.updateUserCardLayout);//更新用户手牌
                uiCenter.updateUI(R.integer.showButton);//显示按钮
                break;
            case R.id.game_button_cancel:
                //保存状态
                CardSystem.getInstance().save();
                //更新数据
                CardSystem.getInstance().getState().nextTurn();//顺序下移
                //更新界面
                uiCenter.updateUI(R.integer.hideButton);//隐藏按钮
                //唤醒下一个玩家
                CardSystem.getInstance().notifyPlayer();
                break;
        }
    }
}
