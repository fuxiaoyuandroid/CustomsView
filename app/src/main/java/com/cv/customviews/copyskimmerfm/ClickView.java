package com.cv.customviews.copyskimmerfm;

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
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import com.cv.customviews.R;

/**
 * Created by Administrator on 2018/5/17 0017.
 *
 * 2018  5  21
 *
 * 问题：1.收缩到一起变成实心圆
 *       2.扩散的时候逐个扩散
 *       3.循环动画
 */

public class ClickView extends View {

    private Paint mPaint;//画笔
    private float mCircleRadius;//圆半径
    private int[] mCircleColors; //圆颜色列表
    private int mCenterX,mCenterY; //中心

    private float mCurrentAngle = 0F;//圆位置角度
    private float mRotationRadius;//旋转半径

    private LoadingState loadingState; //初始动画

    private  float expandRadius;//扩展最大半径

    private final long ROTATION_ANIMATION_TIME = 3000; //旋转动画执行时间

    public ClickView(Context context) {
        this(context,null);
    }

    public ClickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCircleColors = getContext().getResources().getIntArray(R.array.splash_circle_colors);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initParams();
        if (loadingState == null){
            loadingState = new StaticLoadingState();
        }
        loadingState.draw(canvas);
    }
    /**
     * 初始化参数
     */
    private void initParams() {
        //中心点
        mCenterX = getMeasuredWidth()/2;
        mCenterY = getMeasuredHeight()/2;

        //小球半径
        mCircleRadius = getMeasuredWidth()/32;
        Log.e("click", "initParams: "+mCircleRadius);
        //画笔初始化
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

    }

    //定义抽象类加载状态类
    private abstract class LoadingState{
        public abstract void draw(Canvas canvas);
    }

    /**
     * 开始
     */
    public void startOne(){
        if (loadingState instanceof StaticLoadingState){
            StaticLoadingState staticLoadingState = (StaticLoadingState) loadingState;
        }
        loadingState = new RotationLoadingState();
    }

    /**
     * 静止 四个圆
     */
    private class StaticLoadingState extends LoadingState{

        @Override
        public void draw(Canvas canvas) {
            int cx;
            int cy;
            for (int i = 0; i < 4; i++) {
                mPaint.setColor(mCircleColors[i]);
                if (i ==0 || i == 3){
                    cx = mCenterX + 30;
                }else {
                    cx = mCenterX - 30;
                }
                if (i == 0 ||i ==1){

                    cy = mCenterY + 30;
                }else {
                    cy = mCenterY - 30;
                }
                canvas.drawCircle(cx,cy,mCircleRadius,mPaint);

            }
        }

    }

    /**
     * 逆时针旋转
     */
    private class RotationLoadingState extends LoadingState{
        private ValueAnimator valueAnimator;
        public RotationLoadingState(){
            valueAnimator = ObjectAnimator.ofFloat(0,(float)Math.PI);
            valueAnimator.setDuration(ROTATION_ANIMATION_TIME);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentAngle = -(float) animation.getAnimatedValue();
                    //重新绘制
                    invalidate();
                }

            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loadingState = new MergeLoadingState();
                }
            });
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.start();
        }
        @Override
        public void draw(Canvas canvas) {
            mRotationRadius = 20 + mCircleRadius;
            expandRadius = 20 + mCircleRadius;
            double percentAngle = Math.PI * 2/4;
            for (int i = 0; i < 4; i++) {
                mPaint.setColor(mCircleColors[i]);
                double currentAngle = percentAngle * i + Math.PI/4+mCurrentAngle;

                int cx = (int) (mCenterX + mRotationRadius * Math.cos(currentAngle));
                int cy = (int) (mCenterY + mRotationRadius * Math.sin(currentAngle));

                canvas.drawCircle(cx,cy,mCircleRadius,mPaint);
            }
        }
    }

    /**
     * 一起聚合
     */
    public class MergeLoadingState extends LoadingState{
        private ValueAnimator valueAnimator;
        public MergeLoadingState(){
            valueAnimator = ObjectAnimator.ofFloat(mRotationRadius,0);
            valueAnimator.setDuration(ROTATION_ANIMATION_TIME);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRotationRadius = (float) animation.getAnimatedValue();// 最大半径到 0
                    // 重新绘制
                    invalidate();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loadingState = new ExpandLoadingState();
                }
            });
            valueAnimator.start();
        }
        @Override
        public void draw(Canvas canvas) {
            double percentAngle = Math.PI * 2/4;
            for (int i = 0; i < 4; i++) {
                switch (i){
                    case 0:
                        mPaint.setColor(mCircleColors[2]);
                        break;
                    case 1:
                        mPaint.setColor(mCircleColors[3]);
                        break;
                    case 2:
                        mPaint.setColor(mCircleColors[0]);
                        break;
                    case 3:
                        mPaint.setColor(mCircleColors[1]);
                        break;
                }
                double currentAngle = percentAngle * i+Math.PI/4;

                int cx = (int) (mCenterX + mRotationRadius * Math.cos(currentAngle));
                int cy = (int) (mCenterY + mRotationRadius * Math.sin(currentAngle));

                canvas.drawCircle(cx,cy,mCircleRadius,mPaint);
            }
        }
    }

    /**
     * 按顺序扩展
     */
    public class ExpandLoadingState extends LoadingState{
        private ValueAnimator valueAnimator;
        public ExpandLoadingState(){
            valueAnimator = ObjectAnimator.ofFloat(0, expandRadius);
            valueAnimator.setDuration(ROTATION_ANIMATION_TIME / 2);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRotationRadius = (float) animation.getAnimatedValue(); // 0 - 对角线的一半
                    invalidate();
                }
            });
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.start();
        }
        @Override
        public void draw(Canvas canvas) {
            double percentAngle = Math.PI * 2/4;
            for (int i = 0; i < 4; i++) {
                mPaint.setColor(mCircleColors[i]);
                double currentAngle = percentAngle * i + Math.PI/4;

                int cx = (int) (mCenterX + mRotationRadius * Math.cos(currentAngle));
                int cy = (int) (mCenterY + mRotationRadius * Math.sin(currentAngle));

                canvas.drawCircle(cx,cy,mCircleRadius,mPaint);
            }
        }
    }

}
