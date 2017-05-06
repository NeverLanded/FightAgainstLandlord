package com.edu.bjtu.fightagainstlandlord.Data.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.edu.bjtu.fightagainstlandlord.Strategy.CardOperationAdapter;
import com.edu.bjtu.fightagainstlandlord.ClickListener.ClickListenerFactory;
import com.edu.bjtu.fightagainstlandlord.Data.CardData;

public class CardView extends android.support.v7.widget.AppCompatImageView {
    private CardData cardData;
    public CardView(Context context){
        super(context);
    }

    public CardView(Context context, CardData cardData) {
        super(context);
        this.cardData = cardData;
        //获取卡片背景
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), new CardOperationAdapter().cardNumToID(cardData.getNum()));
        //创建卡片视图
        setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, 50, 200));
        setAdjustViewBounds(true);
        //卡片下移
        moveDown();
        //添加卡片监听事件
        setOnClickListener(ClickListenerFactory.getInstance().getClickListener("Card"));
    }

    public void click(){
        if (cardData.getChoose()){
            moveDown();
        }
        else moveUp();
    }

    //获得卡片是否选中
    public boolean getChoose(){
        return cardData.getChoose();
    }

    //获得卡片数字
    public int getNum(){
        return cardData.getNum();
    }

    //设置卡片上移
    private void moveUp(){
        setPadding(0,0,0,10);
        cardData.setChoose(true);
    }

    //设置卡片下移
    private void moveDown(){
        setPadding(0,10,0,0);
        cardData.setChoose(false);
    }

    //取消监听事件
    public void cancelClickListener(){
        setOnClickListener(null);
    }
}
