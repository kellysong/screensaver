package com.sjl.screensaver.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sjl.screensaver.AdvAnimationEnum;
import com.sjl.screensaver.AdvModel;
import com.sjl.screensaver.DefaultLoader;
import com.sjl.screensaver.ScreenSaverView;
import com.sjl.screensaver.animator.AdvAnimator;
import com.sjl.screensaver.animator.AlphaAnimator;
import com.sjl.screensaver.animator.FlipAnimator;
import com.sjl.screensaver.animator.Rotate3DAnimator;
import com.sjl.screensaver.animator.RotateTranslateAnimator;
import com.sjl.screensaver.animator.ScaleAlphaAnimator;
import com.sjl.screensaver.animator.TranslateAnimator;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ScreenSaverActivity extends AppCompatActivity {
    ScreenSaverView screenSaverView;
    Spinner spinner;
    AdvAnimationEnum[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_saver_activity);
        findViewById(R.id.ll_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        screenSaverView = findViewById(R.id.screenSaverView);
        spinner = findViewById(R.id.spinner);
        data = AdvAnimationEnum.values();
        spinner.setAdapter(new ArrayAdapter<AdvAnimationEnum>(this, android.R.layout.simple_list_item_1, data));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                spinner.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AdvAnimationEnum datum = data[position];
                        AdvAnimator advAnimator = getAdvAnimation(datum);
                        screenSaverView.setNewAdvAnimator(advAnimator);
                    }
                }, 200);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        List<AdvModel> advModels = buildAdvModels();

        //设置广告加载器
        screenSaverView.setAdvLoader(new DefaultLoader());

//        AdvAnimationEnum.Alpha;//建议
//        AdvAnimationEnum.TranslateFromBottom;
//        AdvAnimationEnum.TranslateFromRight;
//        AdvAnimationEnum.ScaleAlphaFromCenter;
//        AdvAnimationEnum.RotateTranslate;

        AdvAnimator advAnimator = getAdvAnimation(AdvAnimationEnum.Alpha);
        screenSaverView.setAdvAnimator(advAnimator);

        //设置播放间隔
        screenSaverView.setImgIntervalTime(4);
        //加载广告数据
        screenSaverView.load(advModels);
        //循环播放广告
        screenSaverView.play();
        //广告播放监听,用于统计播放次数
        screenSaverView.setPlayListener(new ScreenSaverView.PlayListener() {

            @Override
            public void onSingleFinish(int position) {
                Log.i("TestActivity", "单次播放结束:" + position);
            }

            @Override
            public void onPeriodFinish() {
                Log.i("TestActivity", "周期播放结束");
            }
        });

//        List<AdvModel> advModels = buildAdvModels();
//        screenSaverView.setNewData(advModels);

    }

    private List<AdvModel> buildAdvModels() {
        List<AdvModel> advModels = new ArrayList<>();
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_1));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_2));
//        advModels.add(new AdvModel(AdvModel.TYPE_VIDEO, R.raw.adv1598507723150));
//        advModels.add(new AdvModel(AdvModel.TYPE_VIDEO, R.raw.adv1603785483407));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_3));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_4));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_5));
        //追加测试
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_1));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_2));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_3));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_4));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_5));
        return advModels;
    }

    private AdvAnimator getAdvAnimation(AdvAnimationEnum advAnimationEnum) {
        //设置过渡动画
        AdvAnimator advAnimator = null;
        switch (advAnimationEnum) {
            case Alpha: {
                advAnimator = new AlphaAnimator();
                break;
            }
            case TranslateFromRight:
            case TranslateFromBottom: {
                advAnimator = new TranslateAnimator(advAnimationEnum);
                break;
            }
            case ScaleAlphaFromCenter: {
                advAnimator = new ScaleAlphaAnimator(advAnimationEnum);
                break;
            }
            case RotateTranslate: {
                advAnimator = new RotateTranslateAnimator();
                break;
            }
            case Flip: {
                advAnimator = new FlipAnimator();
                break;
            }
            case Rotate3D: {
                advAnimator = new Rotate3DAnimator();
                break;
            }
            default:
                break;
        }
        return advAnimator;
    }


    public void btnResume(View view) {
        screenSaverView.resume();
    }
    public void btnPause(View view) {
        screenSaverView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        screenSaverView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        screenSaverView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        screenSaverView.destroy();
    }


}
