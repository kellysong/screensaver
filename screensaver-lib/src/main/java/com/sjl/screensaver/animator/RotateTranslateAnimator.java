package com.sjl.screensaver.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename RotateTranslateAnimator
 * @time 2020/11/8 13:28
 * @copyright(C) 2020 song
 */
public class RotateTranslateAnimator extends AdvAnimator {
    @Override
    public void animateShow() {
        int hideX = -((View) hideView.getParent()).getMeasuredWidth();
        int showX = ((View) showView.getParent()).getMeasuredWidth();
        ObjectAnimator  transHide = ObjectAnimator.ofFloat(hideView, "translationX", 0, hideX);
        ObjectAnimator  transShow = ObjectAnimator.ofFloat(showView, "translationX", showX, 0);

        ObjectAnimator rotationHide = ObjectAnimator.ofFloat(hideView, "rotation", 0f, -360f);
        ObjectAnimator rotationShow = ObjectAnimator.ofFloat(showView, "rotation", 360f, 0f);

        ObjectAnimator alphaHide = ObjectAnimator.ofFloat(hideView, "alpha", 1.0f, 0.0f);
        ObjectAnimator alphaShow = ObjectAnimator.ofFloat(showView, "alpha", 0.5f, 1.0f);

        ObjectAnimator scaleXHide = ObjectAnimator.ofFloat(hideView, "scaleX", 1.0f, 0.0f);
        ObjectAnimator scaleYHide = ObjectAnimator.ofFloat(hideView, "scaleY", 1.0f, 0.0f);

        ObjectAnimator  scaleXShow = ObjectAnimator.ofFloat(showView, "scaleX", 0.0f, 1.0f);
        ObjectAnimator scaleYShow = ObjectAnimator.ofFloat(showView, "scaleY", 0.0f, 1.0f);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(getDuration());
        animatorSet.playTogether(rotationHide,rotationShow,transHide,transShow,alphaHide,alphaShow
        , scaleXHide, scaleYHide, scaleXShow, scaleYShow);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int hideX = ((View) hideView.getParent()).getMeasuredWidth();
                hideView.setTranslationX(2 * hideX);
                if (advAnimationCallback != null){
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
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());
        animatorSet.start();
    }
}
