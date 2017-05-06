package com.edu.bjtu.fightagainstlandlord.ClickListener;

import android.util.Log;
import android.view.View;

import com.edu.bjtu.fightagainstlandlord.Data.View.CardView;


class CardClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        if (v instanceof CardView){
            ((CardView)v).click();
        }
        else Log.e("CardViewListener","非CardView类载入CardViewListener!");
    }
}
