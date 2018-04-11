package com.cv.customviews.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.cv.customviews.R;

/**
 * Created by Administrator on 2018/4/10 0010.
 * 用来存放组合式布局的自定义控件
 */

public class LoadingView extends LinearLayout{
    private ShapeChangeView mShapeChangeView;
    private View mShadowView;
    //动画移动距离
    private int mTranslationDistance;
    //动画执行时间
    private final int ANIMATOR_DURATION = 1000;

    private boolean isAnimatorStop = false;
    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTranslationDistance = dip2px(80);
        initLayout();
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResources().getDisplayMetrics());
    }

    /**
     * 初始化加载布局
     */
    private void initLayout() {
        //加载布局 ui_loading_view
        //方法一
        //View view = inflate(getContext(), R.layout.ui_loading_view,null);
        //addView(view);
        //方法二
        inflate(getContext(),R.layout.ui_loading_view,this);

        //图形变换
        mShapeChangeView = findViewById(R.id.s_c_v);
        //阴影变化
        mShadowView = findViewById(R.id.l_s_b);

        post(new Runnable() {
            @Override
            public void run() {
                fallAnimator();
            }
        });

    }

    /**
     * 下落时候的动画
     */
    private void fallAnimator() {
        if (isAnimatorStop){
            return;
        }
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeChangeView,"translationY",0,mTranslationDistance);

        translationAnimator.setDuration(ANIMATOR_DURATION);

        //translationAnimator.start();

        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView,"scaleX",1f,0.3f);

        scaleAnimator.setDuration(ANIMATOR_DURATION);

        //scaleAnimator.start();

        AnimatorSet set = new AnimatorSet();
        set.playTogether(translationAnimator,scaleAnimator);
        set.setInterpolator(new AccelerateInterpolator());
        //先执行前面的动画,后执行后面的动画
        //set.playSequentially(translationAnimator,scaleAnimator);
        set.start();


        //下落完之后上抛
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mShapeChangeView.exchange();
                upAnimator();
            }
        });
    }

    /**
     * 上抛
     */
    private void upAnimator(){
        if (isAnimatorStop){
            return;
        }
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeChangeView,"translationY",mTranslationDistance,0);

        translationAnimator.setDuration(ANIMATOR_DURATION);

        //translationAnimator.start();

        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView,"scaleX",0.3f,1f);

        scaleAnimator.setDuration(ANIMATOR_DURATION);

        //scaleAnimator.start();

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(translationAnimator,scaleAnimator);
        //先执行前面的动画,后执行后面的动画
        //set.playSequentially(translationAnimator,scaleAnimator);

        //上抛完之后下落
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                fallAnimator();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                rotationAnimator();
            }
        });

        set.start();
    }

    private void rotationAnimator() {
        ObjectAnimator rotationAnimator = null;
        switch (mShapeChangeView.getCurrentShape()){
            case Circle:
            case Square:
                rotationAnimator = ObjectAnimator.ofFloat(mShapeChangeView,"rotation",0,180);
                break;
            case Triangle:
                rotationAnimator = ObjectAnimator.ofFloat(mShapeChangeView,"rotation",0,120);
                break;
        }

        rotationAnimator.setDuration(ANIMATOR_DURATION);
        rotationAnimator.start();
    }

    /**
     * 优化
     * @param visibility
     */
    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(View.INVISIBLE);//不要再去排放和计算，少走一些系统的源码（View的绘制流程）
        // 清理动画
        mShapeChangeView.clearAnimation();
        mShadowView.clearAnimation();
        // 把LoadingView从父布局移除
        ViewGroup parent = (ViewGroup) getParent();
        if(parent != null){
            parent.removeView(this);// 从父布局移除
            removeAllViews();// 移除自己所有的View
        }
        isAnimatorStop = true;

    }
}
