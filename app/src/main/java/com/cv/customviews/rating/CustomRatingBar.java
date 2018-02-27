package com.cv.customviews.rating;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cv.customviews.R;

/**
 * Created by Administrator on 2018/2/27 0027.
 */

public class CustomRatingBar extends View {
    private static final String TAG = "CustomRatingBar";
    private Bitmap starNormalBitmap,starFocusBitmap;
    private int starCount = 5;
    private int mCurrentGrade = 0;
    public CustomRatingBar(Context context) {
        this(context,null);
    }

    public CustomRatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomRatingBar);
        int starNormalId = array.getResourceId(R.styleable.CustomRatingBar_starNormal,0);
        if (starNormalId == 0){
            throw new NullPointerException("请设置starNormal");
        }
        starNormalBitmap = BitmapFactory.decodeResource(getResources(),starNormalId);

        int starFocusId = array.getResourceId(R.styleable.CustomRatingBar_starFocus,0);
        if (starFocusId == 0){
            throw new NullPointerException("请设置starFocus");
        }
        starFocusBitmap = BitmapFactory.decodeResource(getResources(),starFocusId);
        starCount = array.getInt(R.styleable.CustomRatingBar_starCount,0);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = starNormalBitmap.getWidth()*starCount+getPaddingLeft()*4;
        int height = starNormalBitmap.getHeight()+getPaddingTop()+getPaddingBottom();
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < starCount; i++) {
            if (mCurrentGrade > i){
                canvas.drawBitmap(starFocusBitmap,starNormalBitmap.getWidth()*i+getPaddingLeft()*i,getPaddingTop(),null);
            }else {
                canvas.drawBitmap(starNormalBitmap,starNormalBitmap.getWidth()*i+getPaddingLeft()*i,getPaddingTop(),null);
            }

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            //case MotionEvent.ACTION_DOWN:减少onDraw的使用
                //break;
            case MotionEvent.ACTION_MOVE:
                //break;
            //case MotionEvent.ACTION_UP:减少onDraw的使用
                //break;
                float moveX = event.getX();//event.getX()相当于当前控件的位置，event.getRawX()相当于当前屏幕的位置
                Log.e(TAG, "onTouchEvent: "+moveX);
                int currentGrade  = (int) (moveX/(starNormalBitmap.getWidth()+getPaddingLeft())+1);

                if (currentGrade < 0){
                    currentGrade = 0;
                }
                if (currentGrade > starCount){
                    currentGrade = starCount;
                }
                //分数相同的情况下不要绘制
                if (currentGrade == mCurrentGrade){
                    return true;
                }
                mCurrentGrade = currentGrade;
                invalidate();
                break;
        }
        return true;//滑动没效果  默认返回false   不消费  不会再次进入ACTION_MOVE
    }
}
