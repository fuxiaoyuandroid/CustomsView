package com.cv.customviews.pointpraise;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by Administrator on 2018/4/23 0023.
 * 自定义路径属性动画
 */

public class LoveTypeEvaluator implements TypeEvaluator<PointF> {
    private PointF pointFirst,pointSecond;

    public LoveTypeEvaluator(PointF first, PointF second) {
        this.pointFirst = first;
        this.pointSecond = second;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
       PointF pointF = new PointF();
       pointF.x = startValue.x * (1 - fraction)*(1 - fraction)*(1 - fraction)
                 + 3*pointFirst.x * fraction * (1 - fraction)*(1 - fraction)
                 + 3*pointSecond.x * fraction * fraction * (1 - fraction)
                 + endValue.x * fraction * fraction * fraction;
       pointF.y = startValue.y * (1 - fraction)*(1 - fraction)*(1 - fraction)
               + 3*pointFirst.y * fraction * (1 - fraction)*(1 - fraction)
               + 3*pointSecond.y * fraction * fraction * (1 - fraction)
               + endValue.y * fraction * fraction * fraction;
        return pointF;
    }
}
