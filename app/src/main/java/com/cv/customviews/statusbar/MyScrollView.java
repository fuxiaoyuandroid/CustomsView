package com.cv.customviews.statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2018/3/20 0020.
 */

public class MyScrollView extends ScrollView {

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null){
            mListener.onScroll(l, t, oldl, oldt);
        }
    }

    public interface ScrollChangedListener{
        public void onScroll(int l, int t, int oldl, int oldt);
    }

    private ScrollChangedListener mListener;

    public void setOnScrollChangedListener(ScrollChangedListener listener){
        this.mListener = listener;
    }
}
