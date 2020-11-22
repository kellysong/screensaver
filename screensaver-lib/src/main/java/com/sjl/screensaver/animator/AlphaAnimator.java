package com.sjl.screensaver.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename AlphaAnimator
 * @time 2020/11/7 16:54
 * @copyright(C) 2020 song
 */
public class AlphaAnimator extends AdvAnimator {



    @Override
    public void animateShow() {
        ObjectAnimator alphaHide = ObjectAnimator.ofFloat(hideView, "alpha", 1.0f, 0.0f);
        ObjectAnimator alphaShow = ObjectAnimator.ofFloat(showView, "alpha", 0.5f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(getDuration());
        animatorSet.play(alphaHide).with(alphaShow);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
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
        animatorSet.start();
    }


}
