package com.sjl.screensaver.animator;

import android.view.View;

import com.sjl.screensaver.AdvAnimationCallback;
import com.sjl.screensaver.AdvAnimationEnum;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename AdvAnimator
 * @time 2020/11/7 16:37
 * @copyright(C) 2020 song
 */
public abstract class AdvAnimator {
    public View hideView, showView;
    public AdvAnimationEnum advAnimationEnum;
    public AdvAnimationCallback advAnimationCallback;

    public AdvAnimator() {
        this(null);
    }

    public AdvAnimator(AdvAnimationEnum advAnimationEnum) {
        this.advAnimationEnum = advAnimationEnum;
    }

    public void setTargetView(View hideView, View showView) {
        this.hideView = hideView;
        this.showView = showView;
    }
    /**
     * 动画显示
     */
    public abstract void animateShow();

    public void setAdvAnimationCallback(AdvAnimationCallback advAnimationCallback) {
        this.advAnimationCallback = advAnimationCallback;
    }

    public int getDuration() {
        return 2500;
    }


}
