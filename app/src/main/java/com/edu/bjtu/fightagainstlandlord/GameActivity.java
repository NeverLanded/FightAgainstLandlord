package com.edu.bjtu.fightagainstlandlord;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.bjtu.fightagainstlandlord.ClickListener.ClickListenerFactory;
import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.Data.Iterator.CardDataIterator;
import com.edu.bjtu.fightagainstlandlord.Data.Player.Player;
import com.edu.bjtu.fightagainstlandlord.Data.View.CardView;
import com.edu.bjtu.fightagainstlandlord.Strategy.CardOperation;
import com.edu.bjtu.fightagainstlandlord.Strategy.CardOperationAdapter;
import com.edu.bjtu.fightagainstlandlord.System.CardSystem;
import com.edu.bjtu.fightagainstlandlord.System.UICenter;

import java.util.List;

/**
 * 游戏界面设计
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView[] view_coveredCard = new ImageView[3];//3张底牌
    private Button button_commit;//提交按钮
    private Button button_cancel;//取消按钮
    private LinearLayout user_cardLayout;//用户手上的牌存放区
    private LinearLayout deskLayout[];//桌上扑克牌存放区
    private ImageView view_system_user_tip[];
    private TextView text_system_user_num[];
    private CardOperation cardOperation;
    private static Handler handler;//用于和系统玩家接收、发送信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initComponents();
        //初始化消息处理句柄
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                updateSystemCardNum();
                switch (msg.what) {
                    case R.integer.showButton:
                        showButton();
                        break;
                    case R.integer.hideButton:
                        hideButton();
                        break;
                    case R.integer.updateUserCardLayout:
                        updateUserCardLayout();
                        break;
                    case R.integer.updateDeskLayout:
                        updateDeskLayout(msg.arg1, (List<CardData>) msg.obj);
                        break;
                    case R.integer.grabLandlord:
                        showCoveredCards();
                        break;
                    case R.integer.notGrabLandlord:
                        updateTip(msg.what, msg.arg1);
                        break;
                    case R.integer.commitCard:
                        updateDeskLayout(msg.arg1, (List<CardData>) msg.obj);
                        break;
                    case R.integer.notCommitCard:
                        updateTip(msg.what, msg.arg1);
                        break;
                    case R.integer.save:
                        CardSystem.getInstance().save();
                        break;
                    case R.integer.coverLandlordCard:
                        coverLandlordCards();
                    default:
                        break;
                }
            }
        };

        CardSystem.createInstance();
        UICenter.createInstance();

        cardOperation = new CardOperationAdapter();
        updateUserCardLayout();
        hideButton();//先隐藏按钮
        Log.i("GameActivity", "onCreate");
    }

    @Override
    protected void onStart() {
        Log.i("GameActivity","onStart");
        super.onStart();
        Log.i("game","游戏开始");
        CardSystem.getInstance().notifyPlayer();//唤醒玩家
    }

    @Override
    protected void onResume() {
        //将界面横屏
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
        Log.i("GameActivity", "onDestroy");
    }

    //初始化控件
    private void initComponents() {
        //底牌初始化
        Log.i("game","初始化控件");
        view_coveredCard[0] = (ImageView) findViewById(R.id.game_view_covered_card1);
        view_coveredCard[1] = (ImageView) findViewById(R.id.game_view_covered_card2);
        view_coveredCard[2] = (ImageView) findViewById(R.id.game_view_covered_card3);
        //按钮初始化
        Button button_back = (Button) findViewById(R.id.game_button_back);
        Button button_past = (Button) findViewById(R.id.game_button_past);
        button_commit = (Button) findViewById(R.id.game_button_commit);
        button_cancel = (Button) findViewById(R.id.game_button_cancel);
        button_commit.setBackgroundResource(R.drawable.game_button_grab);
        button_cancel.setBackgroundResource(R.drawable.game_button_notgrab);
        button_back.setOnClickListener(this);
        button_past.setOnClickListener(this);
        button_commit.setOnClickListener(ClickListenerFactory.getInstance().getClickListener("Grab"));
        button_cancel.setOnClickListener(ClickListenerFactory.getInstance().getClickListener("Grab"));
        //初始化系统玩家桌子布局
        deskLayout = new LinearLayout[3];
        deskLayout[0] = (LinearLayout) findViewById(R.id.game_user_deskLayout);
        deskLayout[1] = (LinearLayout) findViewById(R.id.game_system1_deskLayout);
        deskLayout[2] = (LinearLayout) findViewById(R.id.game_system2_deskLayout);
        user_cardLayout = (LinearLayout) findViewById(R.id.game_user_cardLayout);
        //牌数提示初始化
        text_system_user_num = new TextView[2];
        text_system_user_num[0] = (TextView) findViewById(R.id.game_system1_num);
        text_system_user_num[1] = (TextView) findViewById(R.id.game_system2_num);
        //文字提示初始化
        view_system_user_tip = new ImageView[2];
        view_system_user_tip[0] = (ImageView) findViewById(R.id.game_system1_tip);
        view_system_user_tip[1] = (ImageView) findViewById(R.id.game_system2_tip);
        //隐藏按钮
        hideButton();
    }

    //更新并显示用户玩家扑克牌
    public void updateUserCardLayout() {
        //移除显示区所有东西
        user_cardLayout.removeAllViews();
        Player user = CardSystem.getInstance().getPlayer(0);
        List<CardData> cardDataList = user.getCardDataList();
        CardDataIterator iterator = new CardDataIterator(cardDataList);
        CardData cardData;
        CardView cardView;
        while (!iterator.isLast()) {
            cardData = iterator.getNextItem();
            //cardView = viewFactory.getCardView(cardData);
            //
            //if (null == cardView) {
                cardView = new CardView(this, cardData);
            //    viewFactory.putCardView(cardData, cardView);
            //}
            user_cardLayout.addView(cardView);//将卡片添加到布局中
            iterator.next();
        }
    }

    //更新桌子卡片出牌区
    public void updateDeskLayout(int turn, List<CardData> commitCardDataList) {
        if (turn >= 0 && turn <= 3) {
            Log.i("game", turn + "更新桌子牌");
            deskLayout[turn].removeAllViews();
            CardDataIterator iterator = new CardDataIterator(commitCardDataList);
            CardData cardData;
            while (!iterator.isLast()) {
                cardData = iterator.getNextItem();
                CardView cardView = new CardView(this, cardData);
                cardView.cancelClickListener();//取消牌的监听事件
                deskLayout[turn].addView(cardView);
                iterator.next();
            }
            updateSystemCardNum();
            if (CardSystem.getInstance().getPlayer(turn).getCardNum() <= 0) {
                CardSystem.getInstance().getState().gameOver();
                toastWin();
            }
        }
        if (turn > 0 && turn <= 2) view_system_user_tip[turn - 1].setVisibility(View.INVISIBLE);
    }

    //更新系统玩家牌数
    private void updateSystemCardNum() {
        int num;
        for (int i = 1; i <= 2; i++) {
            num = CardSystem.getInstance().getPlayer(i).getCardNum();
            String number = num + "";
            text_system_user_num[i - 1].setText(number);
        }
    }

    //更新系统玩家提示
    public void updateTip(int msg, int turn) {
        if (turn > 0 && turn <= 2) {
            switch (msg) {
                case R.integer.notGrabLandlord:
                    view_system_user_tip[turn - 1].setImageResource(R.drawable.game_tip_notgrab);
                    view_system_user_tip[turn - 1].setVisibility(View.VISIBLE);
                    Log.i("game", "系统玩家" + turn + "不抢地主");
                    break;
                case R.integer.grabLandlord:
                    view_system_user_tip[turn - 1].setImageResource(R.drawable.game_tip_grab);
                    view_system_user_tip[turn - 1].setVisibility(View.VISIBLE);
                    Log.i("game", "系统玩家" + turn + "抢地主");
                    break;
                case R.integer.notCommitCard:
                    view_system_user_tip[turn - 1].setImageResource(R.drawable.game_tip_notcommit);
                    view_system_user_tip[turn - 1].setVisibility(View.VISIBLE);
                    deskLayout[turn].removeAllViews();
                    Log.i("game", "系统玩家" + turn + "不出牌");
                    break;
                default:
                    break;
            }
        }
    }

    //隐藏按钮
    public void hideButton() {
        button_cancel.setVisibility(View.INVISIBLE);
        button_commit.setVisibility(View.INVISIBLE);
    }

    //显示按钮
    public void showButton() {
        button_cancel.setVisibility(View.VISIBLE);
        button_commit.setVisibility(View.VISIBLE);
    }

    //抢地主后更新视图，翻开底牌，更改按钮，更新用户手牌
    public void showCoveredCards() {
        CardSystem cardSystem = CardSystem.getInstance();
        int coveredCard[] = cardSystem.getCoveredCard();
        for (int i = 0; i < 3; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), cardOperation.cardNumToID(coveredCard[i]));
            view_coveredCard[i].setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth() / 3, bitmap.getHeight() / 3));
        }

        //修改按钮
        button_commit.setBackgroundResource(R.drawable.game_button_submit);
        button_cancel.setBackgroundResource(R.drawable.game_button_cancel);
        button_commit.setOnClickListener(ClickListenerFactory.getInstance().getClickListener("Commit"));
        button_cancel.setOnClickListener(ClickListenerFactory.getInstance().getClickListener("Commit"));
    }

    public void coverLandlordCards(){
        CardSystem cardSystem = CardSystem.getInstance();
        int coveredCard[] = cardSystem.getCoveredCard();
        for (int i = 0; i < 3; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.game_card_covered);
            view_coveredCard[i].setImageBitmap(bitmap);
        }

        //修改按钮
        button_commit.setBackgroundResource(R.drawable.game_button_grab);
        button_cancel.setBackgroundResource(R.drawable.game_button_notgrab);
        button_commit.setOnClickListener(ClickListenerFactory.getInstance().getClickListener("Grab"));
        button_cancel.setOnClickListener(ClickListenerFactory.getInstance().getClickListener("Grab"));

    }

    //抢地主监听事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_button_back:
                finish();
                break;
            case R.id.game_button_past:
                CardSystem.getInstance().restore();
                break;
        }
    }

    public static Handler getHandler() {
        return handler;
    }

    //监听返回键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            //添加监听器
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                            finish();
                            break;
                        case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                            break;
                        default:
                            break;
                    }
                }
            };
            // 添加选择按钮并注册监听
            isExit.setButton(AlertDialog.BUTTON_POSITIVE, "确定", listener);
            isExit.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", listener);
            // 显示对话框
            isExit.show();
        }
        return true;
    }

    public void toastWin() {
        // 创建退出对话框
        AlertDialog gameOver = new AlertDialog.Builder(this).create();
        // 设置对话框标题
        gameOver.setTitle("游戏结束");
        // 设置对话框消息
        gameOver.setMessage("您要重新开始游戏吗？");
        //添加监听器
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                        reBegin();
                        break;
                    case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
        // 添加选择按钮并注册监听
        gameOver.setButton(AlertDialog.BUTTON_POSITIVE, "是，重新开始游戏", listener);
        gameOver.setButton(AlertDialog.BUTTON_NEGATIVE, "否，我要退出游戏", listener);
        // 显示对话框
        gameOver.show();
    }

    //重新开始游戏
    private void reBegin() {
        int i;
        user_cardLayout.removeAllViews();
        for (i = 0; i < 3; i++)
            deskLayout[i].removeAllViews();

        for (i = 0; i < 2; i++) {
            text_system_user_num[i].setText(R.string.initialNum);
            view_system_user_tip[i].setVisibility(View.INVISIBLE);
        }

        button_cancel.setOnClickListener(ClickListenerFactory.getInstance().getClickListener("Grab"));
        button_commit.setOnClickListener(ClickListenerFactory.getInstance().getClickListener("Grab"));
        button_commit.setBackgroundResource(R.drawable.game_button_grab);
        button_cancel.setBackgroundResource(R.drawable.game_button_notgrab);

        for (i = 0; i < 3; i++)
            view_coveredCard[i].setImageResource(R.drawable.game_card_covered);

        CardSystem.createInstance();
        updateUserCardLayout();//更新用户视图
        hideButton();//先隐藏按钮
        CardSystem.getInstance().notifyPlayer();//唤醒玩家
    }

}
