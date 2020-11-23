package com.sjl.screensaver.test;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
    TextView textView;

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }


    @Override
    protected void initView() {
        textView = findViewById(R.id.tv_msg);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void _onTick(long millisUntilFinished) {
        textView.setText("倒计时：" + (millisUntilFinished / 1000) + "s跳转屏保");
    }

    public void btnOpen(View view) {
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        startActivity(new Intent(context, ScreenSaverActivity.class));
    }
}
