package com.edu.bjtu.fightagainstlandlord;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 游戏主界面设计
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_beginGame;
    private Button button_exitGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        initEvents();
    }

    @Override
    protected void onResume() {
        //将界面横屏
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    /**
     * 初始化控件
     */
    private void initComponents(){
        button_beginGame= (Button) findViewById(R.id.main_button_beginGame);
        button_exitGame= (Button) findViewById(R.id.main_button_exitGame);
    }
    /**
     * 初始化事件监听
     */
    private void initEvents(){
        button_beginGame.setOnClickListener(this);
        button_exitGame.setOnClickListener(this);
    }

    /**
     * 点击事件处理
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_button_beginGame:
                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                startActivity(intent);
                break;
            case R.id.main_button_exitGame:
                finish();
        }
    }

}
