package com.cv.customviews.KgSlidingMenu;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

import com.cv.customviews.R;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class KgSlidingMenu extends HorizontalScrollView{
    private static final String TAG = "KgSlidingMenu";
    private int mMenuWidth;
    //内容
    private View mContentView;
    //菜单
    private View mMenuView;

    //菜单是否打开
    private boolean mMenuIsOpen = false;
    //是否拦截
    private boolean isIntercept = false;
    // 手指快速滑动 - 手势处理类
    private GestureDetector mGestureDetector;

    public KgSlidingMenu(Context context) {
        this(context,null);
    }

    public KgSlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public KgSlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.KgSlidingMenu);
        float rightMargin = array.getDimension(R.styleable.KgSlidingMenu_menuRightMargin,dip2px(context,50));
        //菜单的宽度
        mMenuWidth = (int) (getScreenWidth(context) - rightMargin);
        array.recycle();

        mGestureDetector = new GestureDetector(context,mGestureListener);
    }

    private GestureDetector.OnGestureListener mGestureListener =
            new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    //只关注快速滑动，只要快速滑动就会回调
                    //条件:打开的时候往左边快速滑动(关闭)，关闭的时候往右边快速滑动(打开)
                    Log.e(TAG, "onFling: x"+velocityX+",,"+velocityY);
                    if(Math.abs(velocityY) > Math.abs(velocityX)){
                        // 代表上下快速划  这个时候不做处理
                        return super.onFling(e1, e2, velocityX, velocityX);
                    }
                    if (mMenuIsOpen){
                        //打开的时候往左边快速滑动切换(关闭)
                        //快速往左边滑动是一个负数&& velocityY < 0 && velocityX > Math.abs(velocityY)
                        if (velocityX <0  ) {
                            closeMenu();
                            return true;
                        }
                    }else {
                        //关闭的时候往右边快速滑动(打开)
                        //快速往右边滑动是一个正数&& velocityY >0  && velocityX>Math.abs(velocityY)
                        if (velocityX >0 ) {
                            openMenu();
                            return true;
                        }
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            };
    //宽度不对，指定宽高
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 这个方法代表整个布局加载完毕也就是xml布局文件解析
        // 1.获取菜单和内容View
        // 这个获取的是 根布局  LinearLayout
        ViewGroup container = (ViewGroup) getChildAt(0);
        if (container.getChildCount() != 2){
            throw new RuntimeException("only two child views");
        }
        //内容
        mContentView = container.getChildAt(1);
        ViewGroup.LayoutParams contentParams = mContentView.getLayoutParams();
        contentParams.width = (int) getScreenWidth(getContext());
        mContentView.setLayoutParams(contentParams);

        //菜单 宽度等于 屏幕的宽度 - 右边的一小部分距离(自定义属性)
        mMenuView = container.getChildAt(0);
        ViewGroup.LayoutParams menuParams = mMenuView.getLayoutParams();
        menuParams.width = mMenuWidth;
        mMenuView.setLayoutParams(menuParams);


    }
    //处理缩放问题还有透明度
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //l从最大变为0
        //算一个梯度值
        float scale = 1f*l/mMenuWidth;
        //右边的缩放 最小是0.7f,最大的是1.0f
        float rightScale = 0.7f+0.3f*scale;
        //需要设置中心点
        ViewCompat.setPivotX(mContentView,0);
        ViewCompat.setPivotY(mContentView,mContentView.getHeight()/2);
        //设置右边的缩放
        ViewCompat.setScaleX(mContentView,rightScale);
        ViewCompat.setScaleY(mContentView,rightScale);

        //设置左边的缩放
        float leftScale = 0.7f+0.3f*(1 - scale);
        ViewCompat.setScaleX(mMenuView,leftScale);
        ViewCompat.setScaleY(mMenuView,leftScale);
        //透明度
        float alpha = 0.4f+(1-scale)*0.6f;
        ViewCompat.setAlpha(mMenuView,alpha);
        //最后一个效果 退出按钮刚开始是在右边，设置平移
        ViewCompat.setTranslationX(mMenuView,l*0.3f);
    }

    /**
     * 触摸事件
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //快速滑动执行，下面就不执行了
        if (mGestureDetector.onTouchEvent(ev)){
            return true;
        }
        if (isIntercept){
            return true;
        }
        //获取手指滑动的速率，当大于一定值就认为是快速滑动，GestureDetector
        //处理事件拦截 + ViewGroup事件分发
        //当菜单打开的时候，手指触摸右边内容部分需要关闭菜单，还需要拦截事件(打开情况下点击内容页不会响应点击事件)
        if (ev.getAction() == MotionEvent.ACTION_UP){
            int scrollWidth = getScrollX();
            if (scrollWidth < mMenuWidth/2){
                openMenu();
            }else {
                closeMenu();
            }
            //确保 super.onTouchEvent(ev)不会执行
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        isIntercept =false;
        //处理事件拦截 + ViewGroup事件分发
        //当菜单打开的时候，手指触摸右边内容部分需要关闭菜单，还需要拦截事件(打开情况下点击内容页不会响应点击事件)
        if (mMenuIsOpen){
            float currentX  = ev.getX();
            if (currentX > mMenuWidth){
                //关闭菜单
                closeMenu();
                //子View不需要响应任何事件(点击和触摸)，拦截子View的事件
                //如果返回true 代表我会拦截子View的事件，但是会响应自己的onTouch事件
                isIntercept = true;
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 关闭 滚动到最大的位置即mMenuWidth
     */
    private void closeMenu() {
        //有动画效果
        smoothScrollTo(mMenuWidth,0);
        mMenuIsOpen = false;
    }

    /**
     * 打开 滚动到0
     */
    private void openMenu() {
        smoothScrollTo(0,0);
        mMenuIsOpen = true;
    }

    /**
     * 初始化关闭
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 初始化进来是关闭的
        scrollTo(mMenuWidth, 0);
    }

    /**
     * 屏幕宽度
     * @param context
     * @return
     */
    private float getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * dip转px
     * @param context
     * @param dip
     * @return
     */
    private float dip2px(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dip*scale+0.5f;
    }


}
