package com.sjl.screensaver;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename AdvAnimation
 * @time 2020/11/7 16:41
 * @copyright(C) 2020 song
 */
public enum AdvAnimationEnum {
    //渐变
    Alpha,
    // 平移
    TranslateFromRight,//从右平移进入，左边出去
    TranslateFromBottom,// 从下方平移进入，上方出去
    // 缩放 + 透明渐变
    ScaleAlphaFromCenter,  // 从中心进行缩放+透明渐变
    //旋转+ 平移(带渐变，缩放)
    RotateTranslate,
    //翻转
    Flip,
    //翻牌3D效果
    Rotate3D
}
