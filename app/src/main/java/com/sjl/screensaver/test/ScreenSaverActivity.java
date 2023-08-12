package com.sjl.screensaver.test;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
    TextView currentIndex;
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
        currentIndex = findViewById(R.id.tv_index);
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
        //广告播放监听,用于统计播放次数
        screenSaverView.setPlayListener(new ScreenSaverView.PlayListener() {

            @Override
            public void onPlayChange(int position) {
                Log.i("TestActivity", "当前索引："+position);
                currentIndex.setText("当前索引："+position);
            }

            @Override
            public void onSingleFinish(int position) {
                Log.i("TestActivity", "单次播放结束:" + position);
            }

            @Override
            public void onPeriodFinish() {
                Log.i("TestActivity", "周期播放结束");
            }
        });
        //加载广告数据
        screenSaverView.load(advModels);
        //循环播放广告
        screenSaverView.play();

//        List<AdvModel> advModels = buildAdvModels();
//        screenSaverView.setNewData(advModels);

    }

    private List<AdvModel> buildAdvModels() {
        List<AdvModel> advModels = new ArrayList<>();
        //加载网络图片，建议下载到本地再加载
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, "https://pics5.baidu.com/feed/aa18972bd40735fa421aefed2dcff8b80e240822.jpeg@f_auto?token=337b0fc08979538e99c3bc166d36c095", 5 *1000));
        advModels.add(new AdvModel(AdvModel.TYPE_VIDEO, Uri.parse("https://vd2.bdstatic.com/mda-pd3aazh2u5t20pru/sc/cae_h264/1680593647501835295/mda-pd3aazh2u5t20pru.mp4?v_from_s=bdapp-bdappcore-feed-hna")));

        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_1, 2 *1000));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_2, 2 * 1000));
//        advModels.add(new AdvModel(AdvModel.TYPE_VIDEO, R.raw.adv1598507723150));
//        advModels.add(new AdvModel(AdvModel.TYPE_VIDEO, R.raw.adv1603785483407));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_3, 10 * 1000));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_4, 10 * 1000));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_5, 10 * 1000));
       /*
        //追加测试
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_1, 4000));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_2, 10 * 1000));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_3, 20 * 1000));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_4, 30 * 1000));
        advModels.add(new AdvModel(AdvModel.TYPE_IMG, R.mipmap.adv_5));*/
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
