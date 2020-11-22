package com.sjl.screensaver.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename BaseActivity
 * @time 2020/10/17 11:32
 * @copyright(C) 2020 泰中科技
 */
public abstract class BaseActivity extends AppCompatActivity {

    public CountDownTimer countDownTimer;
    private long advertisingTime = 10 * 1000;//定时跳转广告时间
    public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置屏幕长亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        setContentView(getLayoutId());
        initView();
        initData();

    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getLayoutId();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //有按下动作时取消定时
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起时启动定时
                startAD();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 跳轉廣告
     */
    public void startAD() {
        if (countDownTimer == null) {
            long advertisingTime = getAdvertisingTime();
            if (advertisingTime >  0){
                this.advertisingTime = advertisingTime;
            }
            countDownTimer = new CountDownTimer(this.advertisingTime, 1000L) {
                @Override
                public void onTick(long millisUntilFinished) {
                    _onTick(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    //定时完成后的操作
                    //跳转到广告页面
                    startActivity(new Intent(context, ScreenSaverActivity.class));
                }
            };
            countDownTimer.start();
        } else {
            countDownTimer.start();
        }
    }

    protected long getAdvertisingTime() {
        return advertisingTime;
    }

    protected void _onTick(long millisUntilFinished) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        //显示是启动定时
        startAD();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //当activity不在前台是停止定时
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁时停止定时
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}