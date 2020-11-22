package com.sjl.screensaver;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sjl.screensaver.animator.AdvAnimator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.sjl.screensaver.AdvModel.TYPE_IMG;
import static com.sjl.screensaver.AdvModel.TYPE_VIDEO;


/**
 * 屏保view
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ScreenSaverView
 * @time 2020/10/27 17:01
 * @copyright(C) 2020 song
 */
public class ScreenSaverView extends FrameLayout {
    private static final String TAG = ScreenSaverView.class.getSimpleName();

    private List<AdvModel> advModels;
    private int advCount;
    private int index = 0;
    private boolean stopPlay;
    private Object obj = new Object();

    /**
     * 间隔时间默认60s
     */
    private static final int INTERVAL_TIME = 60 * 1000;
    private int imgIntervalTime;
    private int intervalTime;
    private int currentPosition;


    private AdvLoader advLoader;
    private PlayListener playListener;
    private LoaderListener loaderListener;
    private AdvAnimator advAnimator;

    public ScreenSaverView(@NonNull Context context) {
        super(context);
        init();
    }

    public ScreenSaverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        loaderListener = new MyLoaderListener();
        imgIntervalTime = INTERVAL_TIME;
        currentPosition = 0;
        advModels = new ArrayList<>();
    }

    public void load(List<AdvModel> advModels) {
        int size = advModels.size();
        if (advModels == null || size == 0) {
            throw new NullPointerException("advModels is null.");
        }
        for (AdvModel advModel : advModels) {
            if (!(advModel.getType() == TYPE_IMG || advModel.getType() == TYPE_VIDEO)) {
                throw new IllegalArgumentException("adv type非法，type:" + advModel.getType());
            }
            if (advModel.getModel() == null) {
                throw new IllegalArgumentException("advModel 不能为空");
            }
        }
        this.advModels = advModels;
        this.advCount = size;
        removeAllViews();
        for (int i = 1; i >= 0; i--) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            addView(imageView);
            imageView.setVisibility(View.GONE);
        }
        SurfaceView surfaceView = new SurfaceView(getContext());
        surfaceView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(surfaceView);
        surfaceView.setVisibility(View.GONE);
        initFirstMode(advModels.get(0));
    }


    public int getAdvCount() {
        return advCount;
    }

    public void play() {
        if (stopPlay) {
            return;
        }
        if (getAdvCount() == 0) {
            return;
        }
        Log.i(TAG, "intervalTime:" + intervalTime);
        if (intervalTime == 0) {
            performPlay();//立即播放
        } else {
            postDelayed(runnable, intervalTime);
        }

    }


    private boolean checkAdvModelType(AdvModel advModel) {
        if (advModel.getType() == TYPE_IMG) {
            return true;
        }
        return false;
    }

    private void initFirstMode(AdvModel advModel) {
        evaluateIntervalTime();
        SurfaceView surfaceView = (SurfaceView) getChildAt(2);
        ImageView hideView = (ImageView) getChildAt(1);
        ImageView showView = (ImageView) getChildAt(0);
        hideView.setVisibility(View.GONE);
        Object model = advModel.getModel();
        if (checkAdvModelType(advModel)) {
            showView.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.GONE);
            advLoader.loadImage(showView.getContext(), model, showView);
        } else {//视频
            showView.setVisibility(View.GONE);
            surfaceView.setVisibility(View.VISIBLE);
            advLoader.loadVideo(surfaceView.getContext(), model, surfaceView);
        }
    }

    private void resetView() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            v.animate().cancel();
            v.setAlpha(1f);
            v.setTranslationX(0f);
            v.setTranslationY(0f);
            v.setRotation(0f);
            v.setScaleX(1f);
            v.setScaleY(1f);
//                v.setPivotX(0);
//                v.setPivotY(0);
        }
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            performPlay();
        }
    };

    private void performPlay() {
        if (stopPlay) {
            return;
        }
        int currentIndex;
        int nextIndex;
        synchronized (obj) {
            if (index == advCount - 1) {
                currentIndex = advCount - 1;
                nextIndex = 0;
//                    nextIndex = currentIndex - 1;
                if (playListener != null) {
                    playListener.onPeriodFinish();
                }
            } else {
                currentIndex = index;
                nextIndex = currentIndex + 1;
            }
        }
        Log.i(TAG, "currentIndex:" + currentIndex + ",nextIndex:" + nextIndex + ",index:" + index);
        SurfaceView videoView = (SurfaceView) getChildAt(2);//SurfaceView不支持平移，缩放，旋转等动画
        ImageView hideView = (ImageView) getChildAt(1);
        ImageView showView = (ImageView) getChildAt(0);
        AdvModel hideAdvModel = advModels.get(currentIndex);
        AdvModel showAdvModel = advModels.get(nextIndex);
        currentPosition = currentIndex;
        int hideType = hideAdvModel.getType();
        int showType = showAdvModel.getType();
        Object hideModel = hideAdvModel.getModel();
        Object showModel = showAdvModel.getModel();

        if (hideType == TYPE_IMG && showType == TYPE_IMG) {
            //图片->图片
            hideView.setVisibility(View.VISIBLE);
            showView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);

            advLoader.loadImage(hideView.getContext(), hideModel, hideView);
            advLoader.loadImage(showView.getContext(), showModel, showView);

            startAnim(TYPE_IMG, hideView, showView);
        } else if (hideType == TYPE_IMG && showType == TYPE_VIDEO) {
            //图片->视频
            hideView.setVisibility(View.GONE);
            showView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);

            advLoader.loadImage(showView.getContext(), hideModel, showView);
            advLoader.loadVideo(videoView.getContext(), showModel, videoView);

            startAnim(TYPE_VIDEO, showView, videoView);
        } else if (hideType == TYPE_VIDEO && showType == TYPE_IMG) {
            //视频->图片
            hideView.setVisibility(View.GONE);
            showView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            advLoader.loadImage(showView.getContext(), showModel, showView);
            startAnim(TYPE_IMG, videoView, showView);
        } else if (hideType == TYPE_VIDEO && showType == TYPE_VIDEO) {
            //视频->视频
            hideView.setVisibility(View.GONE);
            showView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            advLoader.loadVideo(videoView.getContext(), showModel, videoView);
//            startAnim(TYPE_VIDEO, hideView, videoView);
            notifySingleFinish(TYPE_VIDEO);
        }

    }

    public void resume() {
        if (!stopPlay) {
            return;
        }
        this.stopPlay = false;
        play();
    }

    public void pause() {
        this.stopPlay = true;
        if (advLoader != null) {
            advLoader.stop();
        }
        removeCallbacks(runnable);

    }

    /**
     * 调用之后，需要重新初始化
     */
    public void destroy() {
        pause();
        if (advLoader != null) {
            advLoader.clear();
        }
        removeAllViews();
    }

    /**
     * 开始动画
     *
     * @param type
     * @param hideView
     * @param showView
     */
    private void startAnim(final int type, final View hideView, final View showView) {
        if (advAnimator == null) {
            hideView.setVisibility(View.GONE);
            showView.setVisibility(View.VISIBLE);
            notifySingleFinish(type);
            return;
        }
        advAnimator.setTargetView(hideView, showView);
        advAnimator.setAdvAnimationCallback(new AdvAnimationCallback() {
            @Override
            public void onEnd() {
                hideView.setVisibility(View.GONE);
                showView.setVisibility(View.VISIBLE);
                notifySingleFinish(type);
            }
        });
        advAnimator.animateShow();
    }

    private void notifySingleFinish(int type) {
        synchronized (obj) {
            index++;
            if (index > advCount - 1) {
                //重新开始
                index = 0;
            }
        }
        advLoader.setLoaderListener(type, loaderListener);


    }

    public void append(AdvModel advModel) {
        synchronized (obj) {
            this.advModels.add(advModel);
            this.advCount = this.advModels.size();
        }
    }


    public void append(List<AdvModel> advModels) {
        synchronized (obj) {
            this.advModels.addAll(advModels);
            this.advCount = this.advModels.size();

        }
    }

    public void setNewData(List<AdvModel> advModels) {
        synchronized (obj) {
            pause();
            this.advModels = advModels == null ? new ArrayList<AdvModel>() : advModels;
            this.advCount = this.advModels.size();
            this.currentPosition = 0;
            this.stopPlay = false;
            resetView();
            initFirstMode(advModels.get(0));
            play();
        }
    }


    private class MyLoaderListener implements LoaderListener {

        @Override
        public void onFinish() {
            evaluateIntervalTime();
            play();
            if (playListener != null) {
                playListener.onSingleFinish(currentPosition);
            }
        }
    }

    /**
     * 计算下一次定时时间间隔
     */
    private void evaluateIntervalTime() {
        int advCount = getAdvCount();
        if (advCount == 1) {
            AdvModel advModel = this.advModels.get(currentPosition);
            if (checkAdvModelType(advModel)) {
                intervalTime = imgIntervalTime;
            } else {
                intervalTime = 0;
            }
            return;
        }

        int nextPosition = currentPosition + 1;
        if (nextPosition > advCount - 1) {
            nextPosition = 0;
        }
        AdvModel nextAdvModel = this.advModels.get(nextPosition);
        if (checkAdvModelType(nextAdvModel)) {
            intervalTime = imgIntervalTime;
        } else {
            intervalTime = 0;
        }
    }


    /**
     * 设置播放时间或停留时间，针对图片有效
     *
     * @param interval 单位 秒
     */
    public void setImgIntervalTime(int interval) {
        if (interval < 4) {
            this.imgIntervalTime = INTERVAL_TIME;
            return;
        }
        this.imgIntervalTime = 1000 * interval;
    }

    public void setAdvLoader(AdvLoader advLoader) {
        if (advLoader == null) {
            this.advLoader = new DefaultLoader();
            return;
        }
        this.advLoader = advLoader;
    }

    public interface PlayListener {
        /**
         * 单个广告播放结束单次回调
         *
         * @param position 被隐藏的广告的索引
         */
        void onSingleFinish(int position);

        /**
         * 所有广告播放结束周期回调
         */
        void onPeriodFinish();
    }

    public void setPlayListener(PlayListener playListener) {
        this.playListener = playListener;
    }

    /**
     * 设置过渡动画
     *
     * @param advAnimator 为null时无动画
     */
    public void setAdvAnimator(AdvAnimator advAnimator) {
        this.advAnimator = advAnimator;
    }

    /**
     * 设置过渡动画
     *
     * @param advAnimator 为null时无动画
     */
    public void setNewAdvAnimator(AdvAnimator advAnimator) {
        resetView();
        setAdvAnimator(advAnimator);
    }
}