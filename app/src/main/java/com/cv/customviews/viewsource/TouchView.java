package com.cv.customviews.viewsource;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * View的Touch事件分析
 * Created by Administrator on 2018/3/9 0009.
 */

public class TouchView extends View{
    private static final String TAG = "TouchView";
    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: "+event.getAction());
        return super.onTouchEvent(event);
        /*return true;*/
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //Log.e(TAG, "dispatchTouchEvent: "+event.getAction());
        super.dispatchTouchEvent(event);//如果有这句话，所有的都会走
        return true;//dispatchTouchEvent就不执行了
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        Log.e(TAG, "dispatchTouchEvent: "+event.getAction());
        return super.dispatchTouchEvent(event);
    }
}
