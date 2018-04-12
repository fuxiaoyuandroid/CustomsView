package com.cv.customviews.morechoose;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/4/11 0011.
 *
 *   // 挤到一堆去了 ，菜单的 Tab 不见了(解决) ，宽度不是等宽，weight要是为1
 // 内容的高度应该不是全部  应该是整个 View的 75%
 // 进来的时候阴影不显示 ，内容也是不显示的（把它移上去）
 // 内容还没有显示出来,打开的时候显示当前位置的菜单，关闭的时候隐藏，阴影点击应该要关闭菜单
 // 动画在执行的情况下就不要在响应动画事件
 // 打开和关闭 变化tab的显示 ， 肯定不能把代码写到 ListDataScreen 里面来
 // 当菜单是打开的状态 不要执行动画只要切换
 */

public class ListDataScreenView extends LinearLayout implements View.OnClickListener {

    private LinearLayout mMenuTabView;

    private FrameLayout mMenuMiddleView;

    private View mShadowView;

    private Context mContext;

    private int mShadowColor = Color.parseColor("#999999");

    private FrameLayout mMenuContainerView;

    private BaseMenuAdapter mMenuAdapter;

    private int menuContainerHeight;

    private long duration_time = 500;

    private int mCurrentPosition = -1;

    private boolean isAnimator = false;


    public ListDataScreenView(Context context) {
        this(context,null);
    }

    public ListDataScreenView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ListDataScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initLayout();
    }

    /**
     * 1.布局实例化(组合控件)
     */
    private void initLayout() {
        //1.创建一个xml布局,加载,findViewById
        setOrientation(VERTICAL);
        //2.简单的效果用代码去创建
        //2.1 创建头部存放tab
        mMenuTabView = new LinearLayout(mContext);
        mMenuTabView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mMenuTabView);
        //2.2 创建FrameLayout用来存放阴影加菜单内容布局
        mMenuMiddleView = new FrameLayout(mContext);
        mMenuMiddleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        addView(mMenuMiddleView);
        //创建阴影可以不设置LayoutParams 默认就是
        mShadowView = new View(mContext);
        mShadowView.setBackgroundColor(mShadowColor);
        mShadowView.setAlpha(0f);
        mShadowView.setVisibility(GONE);
        //阴影点击事件
        mShadowView.setOnClickListener(this);
        mMenuMiddleView.addView(mShadowView);
        //创建菜单用来存放菜单内容
        mMenuContainerView = new FrameLayout(mContext);
        mMenuContainerView.setBackgroundColor(Color.WHITE);


        mMenuMiddleView.addView(mMenuContainerView);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取当前高度
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d("tag", "onMeasure: menuContainerHeight--->"+menuContainerHeight+",height---->"+height);
        if (menuContainerHeight == 0 && height > 0) {
            Log.d("tag", "onMeasure: menuContainerHeight == 0 && height > 0");
            //截取比例高度
            menuContainerHeight = (int) (height * 75f / 100);
            //设置高度
            ViewGroup.LayoutParams params = mMenuContainerView.getLayoutParams();
            params.height = menuContainerHeight;
            mMenuContainerView.setLayoutParams(params);
            //一开始隐藏
            mMenuContainerView.setTranslationY(-menuContainerHeight);
        }
    }

    /**
     * 适配器设置
     * @param adapter
     */
    public void setAdapter(BaseMenuAdapter adapter){
        //判断有无适配器  抛异常
        if (adapter == null){
            throw new NullPointerException("no adapter");
        }
        this.mMenuAdapter = adapter;

        //获取tab数
        int count = mMenuAdapter.getCount();

        for (int i = 0; i < count; i++) {
            //获取tabView
            View tabView = mMenuAdapter.getTabView(i,mMenuTabView);
            mMenuTabView.addView(tabView);
            //让tab等比宽度
            LinearLayout.LayoutParams params  = (LayoutParams) tabView.getLayoutParams();
            params.weight = 1;
            tabView.setLayoutParams(params);
            //点击事件
            setTabViewClick(tabView,i);
            //获取内容
            View menuView = mMenuAdapter.getMenuView(i,mMenuContainerView);
            //一开始设置内容为不可见
            menuView.setVisibility(GONE);
            mMenuContainerView.addView(menuView);
        }
    }

    /**
     * tab点击事件
     * @param tabView
     * @param position
     */
    private void setTabViewClick(final View tabView, final int position) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition == -1){
                    openMenu(tabView,position);
                }else {
                    if (mCurrentPosition == position){
                        //点击同一个tab 打开的状态下关闭
                        closeMenu();
                    }else {
                        // 切换一下显示
                        if (!isAnimator) {
                            View currentMenu = mMenuContainerView.getChildAt(mCurrentPosition);
                            currentMenu.setVisibility(View.GONE);
                            mMenuAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
                            mCurrentPosition = position;
                            currentMenu = mMenuContainerView.getChildAt(mCurrentPosition);
                            currentMenu.setVisibility(View.VISIBLE);
                            mMenuAdapter.menuOpen(mMenuTabView.getChildAt(mCurrentPosition));
                        }
                    }
                }
            }
        });



    }

    private void openMenu(final View tabView, final int position) {

        if (isAnimator){
            return;
        }
        mShadowView.setVisibility(View.VISIBLE);
        View menuView = mMenuContainerView.getChildAt(position);
        menuView.setVisibility(View.VISIBLE);
        ObjectAnimator translation = ObjectAnimator.ofFloat(mMenuContainerView,"translationY",-menuContainerHeight,0);

        translation.setDuration(duration_time);

        translation.start();

        ObjectAnimator alpha = ObjectAnimator.ofFloat(mShadowView,"alpha",0f,1f);

        alpha.setDuration(duration_time);

        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentPosition = position;

                isAnimator = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isAnimator = true;
                mMenuAdapter.menuOpen(tabView);
            }
        });
        alpha.start();
    }

    /**
     * 关闭菜单
     */
    private void closeMenu(){
        //判断是否已经有动画
        if (isAnimator){
            return;
        }
        //滑动动画
        ObjectAnimator translation = ObjectAnimator.ofFloat(mMenuContainerView,"translationY",0,-menuContainerHeight);

        translation.setDuration(duration_time);

        translation.start();

        mShadowView.setVisibility(View.VISIBLE);
        //透明度动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mShadowView,"alpha",1f,0f);
        alpha.setDuration(duration_time);
        //监听动画
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //获取当前展示的内容
                View menuView = mMenuContainerView.getChildAt(mCurrentPosition);
                menuView.setVisibility(View.GONE);
                mShadowView.setVisibility(GONE);
                mCurrentPosition = -1;
                isAnimator = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isAnimator = true;
                mMenuAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
            }
        });
        alpha.start();
    }

    @Override
    public void onClick(View v) {
        closeMenu();
    }
}
