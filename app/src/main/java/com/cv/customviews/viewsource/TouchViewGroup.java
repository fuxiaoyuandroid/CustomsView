package com.cv.customviews.viewsource;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * ViewGroup的Touch事件分析
 * Created by Administrator on 2018/3/12 0012.
 */

public class TouchViewGroup extends LinearLayout {
    private static final String TAG = "TouchViewGroup";
    public TouchViewGroup(Context context) {
        super(context);
    }

    public TouchViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: "+ev.getAction());
        return super.dispatchTouchEvent(ev);
    }
    //事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: "+ev.getAction());
        return super.onInterceptTouchEvent(ev);
        //return true;
    }
    //事件触摸
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: "+event.getAction());
        return super.onTouchEvent(event);
    }
}
