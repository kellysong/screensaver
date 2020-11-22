package com.sjl.screensaver.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.sjl.screensaver.AdvAnimationEnum;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ScaleAlphaAnimator
 * @time 2020/11/7 17:05
 * @copyright(C) 2020 song
 */
public class ScaleAlphaAnimator extends AdvAnimator {

    public ScaleAlphaAnimator(AdvAnimationEnum advAnimationEnum) {
        super(advAnimationEnum);
    }

    @Override
    public void animateShow() {
        // 设置动画参考点
        setPivotPoint(hideView, showView);
        ObjectAnimator alphaHide = null, alphaShow = null;
        ObjectAnimator scaleXHide = null, scaleYHide = null;
        ObjectAnimator scaleXShow = null, scaleYShow = null;

        switch (advAnimationEnum) {
            case ScaleAlphaFromCenter:
                alphaHide = ObjectAnimator.ofFloat(hideView, "alpha", 1.0f, 0.0f);
                alphaHide.setInterpolator(new FastOutSlowInInterpolator());

                scaleXHide = ObjectAnimator.ofFloat(hideView, "scaleX", 1.0f, 0.0f);
                scaleYHide = ObjectAnimator.ofFloat(hideView, "scaleY", 1.0f, 0.0f);
                scaleXHide.setInterpolator(new FastOutSlowInInterpolator());
                scaleYHide.setInterpolator(new FastOutSlowInInterpolator());

                alphaShow = ObjectAnimator.ofFloat(showView, "alpha", 0.5f, 1.0f);
                alphaShow.setInterpolator(new OvershootInterpolator(1f));

                scaleXShow = ObjectAnimator.ofFloat(showView, "scaleX", 0.0f, 1.0f);
                scaleYShow = ObjectAnimator.ofFloat(showView, "scaleY", 0.0f, 1.0f);
                scaleXShow.setInterpolator(new OvershootInterpolator(1f));
                scaleYShow.setInterpolator(new OvershootInterpolator(1f));
                break;
            default:
                break;
        }
        if (alphaHide == null || alphaShow == null
                || scaleXHide == null || scaleYHide == null || scaleXShow == null || scaleYShow == null) {
            if (advAnimationCallback != null) {
                advAnimationCallback.onEnd();
            }
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(getDuration());
        animatorSet.playTogether(alphaHide, alphaShow, scaleXHide, scaleYHide, scaleXShow, scaleYShow);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (advAnimationCallback != null) {
                    advAnimationCallback.onEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {


            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void setPivotPoint(View... view) {
        if (view != null) {
            for (int i = 0; i < view.length; ++i) {
                final View v = view[i];
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        //拿到实际宽高
                        int measuredWidth = v.getMeasuredWidth();
//                        System.out.println("measuredWidth:"+measuredWidth);
                        v.setPivotX(v.getMeasuredWidth() / 2);
                        v.setPivotY(v.getMeasuredHeight() / 2);
                    }
                });

            }

        }
    }


}
