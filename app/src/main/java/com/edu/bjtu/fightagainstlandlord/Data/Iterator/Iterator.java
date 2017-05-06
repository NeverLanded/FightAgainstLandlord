package com.edu.bjtu.fightagainstlandlord.Data.Iterator;


interface Iterator {
    void next();

    void previous();

    boolean isFirst();

    boolean isLast();

    Object getNextItem();

    Object getPreviousItem();

    void removeNextItem();

    int getSize();
}
