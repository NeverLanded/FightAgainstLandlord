package com.edu.bjtu.fightagainstlandlord.ClickListener;

import android.view.View;
import java.util.Hashtable;

public class ClickListenerFactory {
    private static ClickListenerFactory instance = new ClickListenerFactory();
    private Hashtable<String,View.OnClickListener> ht;

    private ClickListenerFactory(){
        ht = new Hashtable<>();

        View.OnClickListener grabClickListener,commitClickListener, cardClickListener;
        grabClickListener = new GrabClickListener();
        commitClickListener = new CommitClickListener();
        cardClickListener = new CardClickListener();

        ht.put("Grab",grabClickListener);
        ht.put("Card",cardClickListener);
        ht.put("Commit",commitClickListener);
    }

    public static ClickListenerFactory getInstance(){
        return instance;
    }

    public View.OnClickListener getClickListener(String type){
        return ht.get(type);
    }
}
