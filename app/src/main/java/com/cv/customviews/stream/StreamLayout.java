package com.cv.customviews.stream;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/7 0007.
 *
 */

public class StreamLayout extends ViewGroup {

    private List<List<View>> mChildList = new ArrayList<>();
    //适配器
    private StreamAdapter mStreamAdapter;

    public StreamLayout(Context context) {
        this(context,null);
    }

    public StreamLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StreamLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mChildList.clear();
        int childCount = getChildCount();
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //高度需要计算
        int height = getPaddingTop()+getPaddingBottom();

        int childWidth = getPaddingLeft();
        ArrayList<View> childViews = new ArrayList<>();
        mChildList.add(childViews);
        int maxHeight = 0;//子View高度不一致
        //for循环获取所有子View
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //执行以后可以获取子View的宽高，因为会调用子View的onMeasure()
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
            //LinearLayout中有
            ViewGroup.MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            //根据子View计算和指定自己的布局
            if (childWidth + childView.getMeasuredWidth() + params.rightMargin + params.leftMargin > width){
                //换行  也要考虑margin 加上一行的最大高度
                height += maxHeight;
                //最后一个如果没加margin，正好挨着边
                childWidth = childView.getMeasuredWidth() + params.rightMargin + params.leftMargin;
                childViews = new ArrayList<>();
                mChildList.add(childViews);

            }else {
                childWidth += childView.getMeasuredWidth() + params.rightMargin + params.leftMargin;

                maxHeight = Math.max(childView.getMeasuredHeight() + params.bottomMargin + params.topMargin,maxHeight);
            }
            childViews.add(childView);
        }
        height += maxHeight;
        //指定宽高
        setMeasuredDimension(width,height);
    }

    /**
     *重写此方法，为了获取margin
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    /**
     * 摆放子view
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left,top = getPaddingTop(),right,bottom;
        for (List<View> views : mChildList) {
            //循环后重新赋值left
            left = getPaddingLeft();
            int maxHeight = 0;
            //for循环各行
            for (View view : views) {
                if (view.getVisibility() == GONE){
                    continue;
                }
                ViewGroup.MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
                left += params.leftMargin;
                int childTop = top+params.topMargin;
                right = left + view.getMeasuredWidth();
                bottom = childTop + view.getMeasuredHeight();
                //摆放
                view.layout(left,childTop,right,bottom);
                //累加left
                left += view.getMeasuredWidth()+ params.rightMargin;
                //子View高度
                int childHeight = view.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                //最大高度
                maxHeight = Math.max(childHeight,maxHeight);
            }
            //不断叠加高度
            //ViewGroup.MarginLayoutParams params = (MarginLayoutParams) views.get(0).getLayoutParams();
            top += maxHeight;
        }
    }

    /**
     * 设置adapter
     * @param adapter
     */
    public void setAdapter(StreamAdapter adapter){
        if (adapter == null){
            throw new NullPointerException("空指针");
        }
        //每一次都要先清空子View
        removeAllViews();

        mStreamAdapter = adapter;
        //获取数量
        int childCount = mStreamAdapter.getCount();

        for (int i = 0; i < childCount; i++) {
            //通过位置获取View
            View childView = mStreamAdapter.getView(i,this);
            //添加
            addView(childView);
        }
    }
}
