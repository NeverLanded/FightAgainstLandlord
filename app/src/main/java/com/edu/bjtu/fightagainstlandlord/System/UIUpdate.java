package com.edu.bjtu.fightagainstlandlord.System;


import com.edu.bjtu.fightagainstlandlord.Data.CardData;

import java.util.List;

interface UIUpdate {
    void updateUI(int msg);

    void updateUI(int msg, int turn);

    void updateUI(int msg, int turn, List<CardData> commitCardDataList);
}
