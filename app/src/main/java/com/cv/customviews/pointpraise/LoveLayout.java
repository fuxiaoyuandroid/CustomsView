package com.cv.customviews.pointpraise;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cv.customviews.R;

import java.util.Random;

/**
 * Created by Administrator on 2018/4/20 0020.
 */

public class LoveLayout extends RelativeLayout{
    private int[] mImageRes;
    private Random random;
    //控件和图片的宽和高
    private int mWidth,mHeight;
    private int mDrawableWidth, mDrawableHeight;
    //插值器
    private Interpolator[] mInterpolator;

    public LoveLayout(Context context) {
        this(context,null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        mImageRes = new int[]{R.drawable.pl_red,R.drawable.pl_yellow,R.drawable.pl_blue};
        mInterpolator = new Interpolator[]{new AccelerateDecelerateInterpolator(),new AccelerateInterpolator(),
                new DecelerateInterpolator(),new LinearInterpolator()};
        random = new Random();
        Drawable drawable = ContextCompat.getDrawable(context,R.drawable.pl_red);
        mDrawableWidth = drawable.getIntrinsicWidth();
        mDrawableHeight = drawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    /**
     * 添加控件
     */
    public void addLove(){
        final ImageView mLoveIv = new ImageView(getContext());
        //随机获取图片设置到控件
        mLoveIv.setBackgroundResource(mImageRes[random.nextInt(mImageRes.length)]);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //添加控件到底部中间
        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(CENTER_HORIZONTAL);
        //设置params
        mLoveIv.setLayoutParams(params);
        //添加
        addView(mLoveIv);

        //添加动画效果
        AnimatorSet set = getAnimator(mLoveIv);
        //监听
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //执行完毕,移除
                removeView(mLoveIv);
            }
        });
        //开始
        set.start();
    }

    /**
     * 给控件添加动画效果
     * @param iv
     * @return
     */
    private AnimatorSet getAnimator(ImageView iv) {
        AnimatorSet allAnimator = new AnimatorSet();
        //添加的效果  放大，透明度变化 属性动画
        AnimatorSet appearAnimator = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(iv,"alpha",0.3f,1.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv,"scaleX",0.3f,1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv,"scaleY",0.3f,1.0f);
        //一起执行
        appearAnimator.playTogether(alpha,scaleX,scaleY);
        appearAnimator.setDuration(350);

        //先后
        allAnimator.playSequentially(appearAnimator,getBezierAnimator(iv));
        return allAnimator;
    }

    /**
     * 贝塞尔路径的动画
     * @param iv
     * @return
     */
    private Animator getBezierAnimator(final ImageView iv) {
        PointF point0 = new PointF(mWidth/2 - mDrawableWidth/2 , mHeight - mDrawableHeight);
        //point1的y大于point2的y
        PointF point1 = getPointByIndex(1);
        PointF point2 = getPointByIndex(2);
        PointF point3 = new PointF(random.nextInt(mWidth) - mDrawableWidth,0);
        //贝塞尔三阶
        LoveTypeEvaluator typeEvaluator = new LoveTypeEvaluator(point1,point2);
        //动画
        ValueAnimator bezierAnimator = ObjectAnimator.ofObject(typeEvaluator,point0,point3);
        bezierAnimator.setInterpolator(mInterpolator[random.nextInt(mInterpolator.length)]);
        bezierAnimator.setDuration(3000);
        //设置监听
        bezierAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                iv.setX(pointF.x);
                iv.setY(pointF.y);
                //透明度变化
                float alphaSize = animation.getAnimatedFraction();
                iv.setAlpha(1 - alphaSize + 0.2f);
            }
        });
        return bezierAnimator;
    }

    /**
     * 贝塞尔曲线的控制点
     * @param i
     * @return
     */
    private PointF getPointByIndex(int i) {
        return new PointF(random.nextInt(mWidth) - mDrawableWidth,
                random.nextInt(mHeight/2)+(i - 1)*(mHeight/2));
    }

}
