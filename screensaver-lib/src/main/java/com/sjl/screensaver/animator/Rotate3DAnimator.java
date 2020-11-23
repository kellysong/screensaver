package com.sjl.screensaver.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename Rotate3DAnimator
 * @time 2020/11/23 22:29
 * @copyright(C) 2020 song
 */
public class Rotate3DAnimator extends AdvAnimator {


    @Override
    public void animateShow() {
        startAnimation();
    }

    public void startAnimation() {
        ObjectAnimator firstAnim = ObjectAnimator.ofFloat(hideView, "rotationY", 0, -90);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.play(firstAnim);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                hideView.setVisibility(View.INVISIBLE);
                showView.setVisibility(View.VISIBLE);
                ObjectAnimator hideAnim = ObjectAnimator.ofFloat(hideView, "rotationY", -90, 0);
                ObjectAnimator showAnim = ObjectAnimator.ofFloat(showView, "rotationY", 90, 0);
                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.setDuration(1000);

                animatorSet2.play(showAnim).with(hideAnim);
                animatorSet2.start();
                animatorSet2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setRotationY(0);
                        showView.setRotationY(0);
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
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

    }

}
