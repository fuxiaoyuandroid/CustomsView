package com.cv.customviews.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/2/2 0002.
 */

public class ShapeChangeView extends View {
    private Shape mCurrentShape = Shape.Circle;
    private Paint mPaint;
    private Path mPath;
    public ShapeChangeView(Context context) {
        this(context,null);
    }

    public ShapeChangeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapeChangeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width,height),Math.min(width,height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        switch (mCurrentShape){
            //圆
            case Circle:
                int center = getWidth()/2;
                mPaint.setColor(Color.RED);
                canvas.drawCircle(center,center,center,mPaint);
                break;
            //正方形
            case Square:
                mPaint.setColor(Color.GREEN);
                canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
                break;
            //三角
            case Triangle:
                mPaint.setColor(Color.BLUE);
                //path
                if (mPath == null){
                    Path path = new Path();
                    mPath = path;
                    mPath.moveTo(getWidth()/2,0);
                    mPath.lineTo(0,(float) ((getWidth()/2)*Math.sqrt(3)));
                    mPath.lineTo(getWidth(),(float) ((getWidth()/2)*Math.sqrt(3)));
                    mPath.close();
                }
                canvas.drawPath(mPath,mPaint);
                break;

        }
        invalidate();
    }

    public void exchange() {
        switch (mCurrentShape){
            //圆
            case Circle:
                mCurrentShape = Shape.Square;
                break;
            //正方形
            case Square:
                mCurrentShape = Shape.Triangle;
                break;
            //三角
            case Triangle:
                mCurrentShape = Shape.Circle;
                break;
        }
    }

    //枚举，三种形状
    public enum Shape{
        Circle,Square,Triangle
    }
}
