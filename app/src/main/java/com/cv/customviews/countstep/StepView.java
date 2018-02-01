package com.cv.customviews.countstep;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cv.customviews.R;

/**
 * Created by Administrator on 2018/1/3 0003.
 *  问题  1.自定义属性定义以及获取
 */

public class StepView extends View {
    //颜色
    private int mOuterColor = Color.BLUE;
    private int mInnerColor = Color.RED;
    private int mStepTextColor = Color.BLACK;
    //字体大小
    private float mStepTextSize = 13.0f;
    //宽度
    private float mBorderWidth = 7.0f;

    //画笔
    private Paint mOuterPaint;

    private Paint mInnerPaint;

    private Paint mTextPaint;

    //总
    private int mStepMax = 100;
    //当前
    private int mCurrentStep = 50;

    public StepView(Context context) {
        this(context,null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    //确定自定义属性
    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StepView);

        mOuterColor = array.getColor(R.styleable.StepView_outerColor,mOuterColor);
        mInnerColor = array.getColor(R.styleable.StepView_innerColor,mInnerColor);
        mStepTextColor = array.getColor(R.styleable.StepView_stepTextColor,mStepTextColor);
        //? 为什么没有提示
        mStepTextSize = array.getDimension(R.styleable.StepView_stepTextSize,mStepTextSize);
        mBorderWidth = (int) array.getDimension(R.styleable.StepView_borderWidth,mBorderWidth);
        array.recycle();

        mOuterPaint = new Paint();
        mOuterPaint.setStrokeWidth(mBorderWidth);
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        //设置起终点为圆弧
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);


        mInnerPaint = new Paint();
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(mStepTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //wrap_content? 宽高不一致?
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //取小 正方形
        setMeasuredDimension(width>height?height:width,width>height?height:width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //中心店
        //int center = getWidth()/2;
        //半径
        //int radius = getWidth()/2-mBorderWidth/2;
        //画外圆弧
        RectF rectF = new RectF(mBorderWidth/2,mBorderWidth/2,
                getWidth()-mBorderWidth/2,getHeight()-mBorderWidth/2);

        canvas.drawArc(rectF,135,270,false,mOuterPaint);
        //画内圆弧
        if (mStepMax == 0){
            return;//防止为0时
        }else {
            float sweepAngle = (float) mCurrentStep/mStepMax;
            canvas.drawArc(rectF,135,sweepAngle*270,false,mInnerPaint);
        }


        //文字
        String stepText = mCurrentStep +"";
        Rect textRect = new Rect();
        mTextPaint.getTextBounds(stepText,0,stepText.length(),textRect);
        int dx = getWidth()/2 - textRect.width()/2;//文字的起点  中心-文字的长度的一半
        //基线
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
        int baseLine = getHeight()/2 + dy;
        canvas.drawText(stepText,dx,baseLine,mTextPaint);
    }

    //其他
    public synchronized void setStepMax(int stepMax){
        this.mStepMax = stepMax;
    }


    public synchronized void setCurrentStep(int currentStep){
        this.mCurrentStep = currentStep;
        invalidate();//不断绘制  源码
    }
}
