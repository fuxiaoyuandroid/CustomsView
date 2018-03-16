package com.cv.customviews.carhome;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ListViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class VerticalDragListView extends FrameLayout {
    private static final String TAG = "VerticalDragListView";
    //系统给我们写好的工具类
    private ViewDragHelper mDragHelper;
    //1 拖动的子View
    private View mDragListView;
    //2 菜单的高度
    private int mMenuHeight;
    //菜单是否显示
    private boolean mMenuIsOpen = false;
    public VerticalDragListView(@NonNull Context context) {
        this(context,null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragHelper = ViewDragHelper.create(this,mDragHelperCallBack);
    }
    //布局
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //1 子View的数量
        int childCount = getChildCount();
        //1 做个判断
        if (childCount != 2){
            throw new NullPointerException("only two child view");
        }
        //1 获取到拖动的子View
        mDragListView = getChildAt(1);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed){
            //2 获取菜单的高度
            mMenuHeight = getChildAt(0).getMeasuredHeight();
        }
    }

    //1.拖动我们的子View
    private ViewDragHelper.Callback mDragHelperCallBack = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //指定该子View是否可以拖动
            return mDragListView == child;//如果子View是能拖动的，返回true
        }
        //复写该方法表示只能垂直拖动
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //垂直拖动移动的位置
            if (top < 0){
                top = 0;
            }else if (top > mMenuHeight){
                top = mMenuHeight;
            }
            return top ;
        }

        //手指松开，打开或关闭菜单
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //super.onViewReleased(releasedChild, xvel, yvel);
            if (releasedChild == mDragListView) {
                if (mDragListView.getTop() > mMenuHeight / 2) {
                    mDragHelper.settleCapturedViewAt(0, mMenuHeight);
                    mMenuIsOpen = true;
                } else {
                    mDragHelper.settleCapturedViewAt(0, 0);
                    mMenuIsOpen = false;
                }
                invalidate();
            }
        }
    };

    /**
     * 响应滚动
     */
    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    //触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }
    //问题:ListView可以滑动，但是菜单滑动没有效果
    private float mDownY ;
    //父.onInterceptTouchEvent.down ->子.onTouch ->父.onInterceptTouchEvent.move
    // ->父.onTouchEvent.move
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //菜单打开要全部拦截
        if (mMenuIsOpen){
            return true;
        }
        //向下滑动拦截，不要给ListView做处理
        //谁拦截谁 父View拦截子View  但是子View可以调用requestDisallowInterceptTouchEvent，
        //请求父View不要拦截，改变的其实就是mGroupFlags的值
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                //为了mDragHelper拿一个完整的事件
                mDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveY = ev.getY();
                if ((mMoveY - mDownY)>0 && !canChildScrollUp()){
                    //向下滑动 并且滚动了顶部(不能向上滚)，拦截，不让ListView处理
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    //判断是否滚动到了顶部
    //参考SwipeRefreshLayout的代码
    public boolean canChildScrollUp() {

        if (mDragListView instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mDragListView, -1);
        }
        return mDragListView.canScrollVertically(-1);
    }
}
