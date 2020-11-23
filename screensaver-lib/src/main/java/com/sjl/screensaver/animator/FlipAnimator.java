package com.sjl.screensaver.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.OvershootInterpolator;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FlipAnimator
 * @time 2020/11/23 21:51
 * @copyright(C) 2020 song
 */
public class FlipAnimator  extends AdvAnimator {
    @Override
    public void animateShow() {
        ObjectAnimator hideAnim = ObjectAnimator.ofFloat(hideView, "rotationX", 0, 90);
        final ObjectAnimator showAnim = ObjectAnimator.ofFloat(showView, "rotationX", -90, 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(getDuration());
        //动画即将完成时会有一个弹性回弹的效果
        animatorSet.setInterpolator(new OvershootInterpolator(1.0f));
        animatorSet.play(hideAnim).with(showAnim);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                hideView.setRotationX(0);
                showView.setRotationX(0);
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
