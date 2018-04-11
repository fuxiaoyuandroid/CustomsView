package com.cv.customviews.statusbar;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class TranslationBehavior extends FloatingActionButton.Behavior{
    private boolean isOut = false;
    public TranslationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 当CoordinatorLayout的子View试图开始嵌套滑动的时候被调用，当true的时候表明CoordinatorLayout充
     * 当NestedScroll的parent处理这次滑动，只有当true时，Behavior才能收到后面的一些NestedScroll事件
     * 回调。nestedScrollAxes表明处理的滑动方向
     * @param coordinatorLayout 和Behavior 绑定的View的父CoordinatorLayout
     * @param child 和Behavior 绑定的View
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes  嵌套滑动 应用的滑动方向，看 {@link ViewCompat#SCROLL_AXIS_HORIZONTAL}
     *                           {@link ViewCompat#SCROLL_AXIS_VERTICAL}
     * @return
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    /**
     * 进行嵌套滚动时被调用
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed 已经消费的x方向的距离
     * @param dyConsumed 已经消费的y方向的距离
     * @param dxUnconsumed x方向剩下的滚动距离
     * @param dyUnconsumed y方向剩下的滚动距离
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.e("TAG","dyConsumed -> "+dyConsumed+" dyUnconsumed -> "+dyUnconsumed);
        // 而且向上的时候是出来，向下是隐藏
        if (dyConsumed > 0){
            //child需要滑动的高度
            if(!isOut) {
                int translationY = ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).bottomMargin
                        + child.getMeasuredHeight();
                child.animate().translationY(translationY).setDuration(500).start();
                isOut = true;
            }
        }else {
            if(isOut) {
                child.animate().translationY(0).setDuration(500).start();
                isOut = false;
            }
        }
    }
}
