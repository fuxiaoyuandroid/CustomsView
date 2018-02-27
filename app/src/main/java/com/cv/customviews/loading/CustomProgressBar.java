package com.cv.customviews.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.cv.customviews.R;


/**
 * Created by Administrator on 2018/2/2 0002.
 * 自定义圆形进度条
 */

public class CustomProgressBar extends View {
    private int mInBackground = Color.BLACK;
    private int mOutBackground = Color.BLACK;
    private int mBarTextColor = Color.BLACK;
    private int mBarTextSize = 12;
    private float mRoundWidth = 5f;
    private int mAllLoading = 100;
    private int mCurrentLoading = 0;
    private Paint mInPaint;
    private Paint mOutPaint;
    private Paint mTextPaint;
    public CustomProgressBar(Context context) {
        this(context,null);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);

        mInBackground = array.getColor(R.styleable.CustomProgressBar_inBackground,mInBackground);

        mOutBackground = array.getColor(R.styleable.CustomProgressBar_outBackground,mOutBackground);

        mBarTextColor = array.getColor(R.styleable.CustomProgressBar_barTextColor,mBarTextColor);

        mBarTextSize = array.getDimensionPixelSize(R.styleable.CustomProgressBar_barTextSize,sp2px(mBarTextSize));

        mRoundWidth = array.getDimension(R.styleable.CustomProgressBar_roundWidth,dip2px(mRoundWidth));

        array.recycle();

        mInPaint = createPaint(mInPaint,mInBackground);

        mOutPaint = createPaint(mOutPaint,mOutBackground);


        mTextPaint = new Paint();
        mTextPaint.setColor(mBarTextColor);
        mTextPaint.setTextSize(mBarTextSize);
        mTextPaint.setAntiAlias(true);
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }

    private float dip2px(float  dip) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResources().getDisplayMetrics());
    }

    /**
     * 圆
     * @param paint
     * @param color
     */
    private Paint createPaint(Paint paint,int color) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mRoundWidth);
        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //保证正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width>height? height:width,height>width?width:height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画整圆
        int center = getWidth()/2;
        canvas.drawCircle(center,center,center-mRoundWidth/2,mInPaint);

        //画圆弧
        RectF rectF = new RectF(mRoundWidth/2,mRoundWidth/2,getWidth()-mRoundWidth/2,getHeight()-mRoundWidth/2);
        if (mCurrentLoading == 0){
            return;
        }
        float percent = (float) mCurrentLoading/mAllLoading;
        canvas.drawArc(rectF,0,percent*360,false,mOutPaint);
        //内部显示内容
        String text = ((int)(percent*100))+"%";
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(text,0,text.length(),bounds);
        int start = getWidth()/2 - bounds.width()/2;

        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
        int baseline = getHeight()/2 + dy;
        canvas.drawText(text,start,baseline,mTextPaint);
    }

    public synchronized void setProgress(int progress){
        if (progress > mAllLoading || progress < 0){

        }
        this.mCurrentLoading = progress;
        //刷新
        invalidate();
    }

    /**
     * 设置最大值
     * @param max
     */
    public synchronized void setAllLoad(int max){
        if (max < 0){
            //判断是否
        }
        this.mAllLoading = max;
    }
}
