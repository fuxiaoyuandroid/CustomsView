package com.cv.customviews.parallaxloading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;

import com.cv.customviews.R;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

public class ParallaxLoadingView extends View{
    //画笔
    private Paint mPaint;
    // 整体的颜色背景
    private int mSplashColor = Color.WHITE;
    //圆初始半径
    private int mCircleRadius;
    //圆颜色列表
    private int[] mCircleColors;
    //旋转动画执行时间
    private final long ROTATION_ANIMATION_TIME = 3000;
    //旋转角度
    private float mCurrentRotationAngle = 0F;
    //初始旋转半径
    private int mRotationRadius;
    //旋转中心点
    private int mCenterX,mCenterY;

    //初始动画
    private LoadingState loadingState;
    //是否初始化了
    private boolean mInitParams = false;
    // 当前大圆的半径 不断变化
    private float mCurrentRotationRadius = mRotationRadius;

    // 空心圆初始半径
    private float mHoleRadius = 0F;

    // 屏幕对角线的一半
    private float mDiagonalDist;

    public ParallaxLoadingView(Context context) {
        this(context,null);
    }

    public ParallaxLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ParallaxLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCircleColors = getContext().getResources().getIntArray(R.array.splash_circle_colors);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mInitParams){
            initParams();
        }

        if (loadingState == null){
            loadingState = new RotationState();
        }
        loadingState.draw(canvas);
    }

    /**
     * 初始化用到的参数
     */
    private void initParams() {
        mInitParams = true;
        //半径
        mRotationRadius = getMeasuredWidth()/4;
        mCircleRadius = mRotationRadius/8;
        //画笔初始化
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        //中心点
        mCenterX = getMeasuredWidth()/2;
        mCenterY = getMeasuredHeight()/2;

        mDiagonalDist = (float) Math.sqrt(mCenterX * mCenterX + mCenterY * mCenterY);
    }

    public void disappear(){
        //关闭旋转动画
        if (loadingState instanceof RotationState){
            RotationState state = (RotationState) loadingState;
            state.cancel();
        }
        loadingState = new MergeState();
    }

    //定义抽象类加载状态类
    private abstract class LoadingState{
        public abstract void draw(Canvas canvas);
    }

    //初始旋转类
    public class RotationState extends LoadingState{
        private ValueAnimator valueAnimator;
        public RotationState(){
            valueAnimator = ObjectAnimator.ofFloat(0f,2*(float)Math.PI);
            valueAnimator.setDuration(ROTATION_ANIMATION_TIME);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationAngle = (float) animation.getAnimatedValue();
                    //重新绘制
                    invalidate();
                }
            });
            //线性插值器
            valueAnimator.setInterpolator(new LinearInterpolator());
            //不断反复执行
            valueAnimator.setRepeatCount(-1);
            valueAnimator.start();
        }
        @Override
        public void draw(Canvas canvas) {
            canvas.drawColor(mSplashColor);
            double percentAngle = Math.PI * 2/mCircleColors.length;
            for (int i = 0; i < mCircleColors.length; i++) {
                mPaint.setColor(mCircleColors[i]);
                double currentAngle = percentAngle * i + mCurrentRotationAngle;

                int cx = (int) (mCenterX + mRotationRadius * Math.cos(currentAngle));
                int cy = (int) (mCenterY + mRotationRadius * Math.sin(currentAngle));

                canvas.drawCircle(cx,cy,mCircleRadius,mPaint);
            }
        }
        public void cancel(){
            valueAnimator.cancel();
        }
    }

    //聚合类
    public class MergeState extends LoadingState{
        private ValueAnimator mAnimator;
        public MergeState() {
            mAnimator = ObjectAnimator.ofFloat(mRotationRadius, 0);
            mAnimator.setDuration(ROTATION_ANIMATION_TIME / 2);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationRadius = (float) animation.getAnimatedValue();// 最大半径到 0
                    // 重新绘制
                    invalidate();
                }
            });
            // 开始的时候向后然后向前甩
            mAnimator.setInterpolator(new AnticipateInterpolator(3f));
            // 等聚合完毕画展开
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loadingState = new ExpandState();
                }
            });
            mAnimator.start();
        }

        @Override
        public void draw(Canvas canvas) {
            // 画一个背景 白色
            canvas.drawColor(mSplashColor);
            // 开始写聚合动画
            // 画六个圆  每份角度
            double percentAngle = Math.PI * 2 / mCircleColors.length;
            for (int i = 0; i < mCircleColors.length; i++) {
                mPaint.setColor(mCircleColors[i]);
                // 当前的角度 = 初始角度 + 旋转的角度
                double currentAngle = percentAngle * i + mCurrentRotationAngle;
                int cx = (int) (mCenterX + mCurrentRotationRadius * Math.cos(currentAngle));
                int cy = (int) (mCenterY + mCurrentRotationRadius * Math.sin(currentAngle));
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
            }
        }
    }

    //展开类
    public class ExpandState extends LoadingState{
        private ValueAnimator mAnimator;
        public ExpandState() {
            mAnimator = ObjectAnimator.ofFloat(0, mDiagonalDist);
            mAnimator.setDuration(ROTATION_ANIMATION_TIME / 2);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mHoleRadius = (float) animation.getAnimatedValue(); // 0 - 对角线的一半
                    invalidate();
                }
            });
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.start();
        }

        @Override
        public void draw(Canvas canvas) {
            // 画笔的宽度 对角线宽度的一半减去已经扩散开的半径
            float strokeWidth = mDiagonalDist - mHoleRadius;
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setColor(mSplashColor);
            mPaint.setStyle(Paint.Style.STROKE);
            //半径是已经扩散开的空心圆半径 + 画笔的宽度除以2
            float radius = strokeWidth / 2 + mHoleRadius;
            //绘制圆
            canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);
        }
    }

}
