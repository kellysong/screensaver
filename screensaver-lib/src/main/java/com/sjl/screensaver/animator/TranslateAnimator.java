package com.sjl.screensaver.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.sjl.screensaver.AdvAnimationEnum;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * event.getX():表示的是触摸的点距离自身左边界的距离
 * event.getY():表示的是触摸的点距离自身上边界的距离
 * event.getRawX:表示的是触摸点距离屏幕左边界的距离
 * event.getRawY:表示的是触摸点距离屏幕上边界的距离
 * View.getWidth():表示的是当前控件的宽度，即getRight()-getLeft()
 * View.getHeight()：表示的是当前控件的高度，即getBottom()-getTop()
 * View.getTop():子View的顶部到父View顶部的距离
 * View.getRight():子View的右边界到父View的左边界的距离
 * View.getBottom():子View的底部到父View的顶部的距离
 * View.getLeft():子View的左边界到父View的左边界的距离
 * View.getTranslationX()计算的是该View在X轴的偏移量。初始值为0，向左偏移值为负，向右偏移值为正。
 * View.getTranslationY()计算的是该View在Y轴的偏移量。初始值为0，向上偏移为负，向下偏移为正。
 *
 * @author Kelly
 * @version 1.0.0
 * @filename TranslateAnimator
 * @time 2020/11/7 16:39
 * @copyright(C) 2020 song
 */
public class TranslateAnimator extends AdvAnimator {

    public TranslateAnimator(AdvAnimationEnum advAnimationEnum) {
        super(advAnimationEnum);
    }

    @Override
    public void animateShow() {
        ObjectAnimator transHide = null, transShow = null;
        switch (advAnimationEnum) {
            case TranslateFromRight: {
                int hideX = -((View) hideView.getParent()).getMeasuredWidth();
                int showX = ((View) showView.getParent()).getMeasuredWidth();
                transHide = ObjectAnimator.ofFloat(hideView, "translationX", 0, hideX);
                transShow = ObjectAnimator.ofFloat(showView, "translationX", showX, 0);
                break;
            }
            case TranslateFromBottom: {
                int hideY= -((View) hideView.getParent()).getMeasuredHeight();
                int showY = ((View) showView.getParent()).getMeasuredHeight();
                transHide = ObjectAnimator.ofFloat(hideView, "translationY", 0, hideY);
                transShow = ObjectAnimator.ofFloat(showView, "translationY", showY, 0);
                break;
            }
            default:
                break;
        }
        if (transHide == null || transShow == null) {
            if (advAnimationCallback != null) {
                advAnimationCallback.onEnd();
            }
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(getDuration());
        animatorSet.play(transHide).with(transShow);
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //重置被隐藏的view的位置到右边
                animateDismiss(hideView);
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

    private void animateDismiss(View hideView) {
        switch (advAnimationEnum) {
            case TranslateFromRight: {
                int hideX = ((View) hideView.getParent()).getMeasuredWidth();
                hideView.setTranslationX(2 * hideX);
                break;
            }
            case TranslateFromBottom: {
                int hideY = ((View) hideView.getParent()).getMeasuredHeight();
                hideView.setTranslationY(2 * hideY);
                break;
            }
            default:
                break;
        }
    }

}
