package com.cv.customviews.fontcolorchange;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cv.customviews.R;

/**
 * Created by Administrator on 2018/1/30 0030.
 */

@SuppressLint("AppCompatCustomView")
public class ColorChangeTextView extends TextView {
    //原色
    private Paint mOriginalPaint;
    //变色
    private Paint mChangePaint;

    private float mCurrentChange = 0.0f;

    private Direction mDirection = Direction.LEFT_TO_RIGHT;
    //改变颜色
    public void setChangeColor(int changeColor) {
        this.mChangePaint.setColor(changeColor);
    }
    //原始颜色
    public void setOriginalColor(int color) {
        this.mOriginalPaint.setColor(color);
    }
    //枚举
    public enum Direction{
        LEFT_TO_RIGHT,RIGHT_TO_LEFT
    }

    public ColorChangeTextView(Context context) {
        this(context,null);
    }

    public ColorChangeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorChangeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint(context,attrs);
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private void initPaint(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorChangeTextView);

        int originalColor = array.getColor(R.styleable.ColorChangeTextView_originalColor,getTextColors().getDefaultColor());

        int changeColor = array.getColor(R.styleable.ColorChangeTextView_changeColor,getTextColors().getDefaultColor());

        mOriginalPaint = getPaintByColor(originalColor);
        mChangePaint = getPaintByColor(changeColor);
        //回收
        array.recycle();
    }

    /**
     * 根据颜色获取画笔
     * @param color
     * @return
     */
    private Paint getPaintByColor(int color) {
        Paint mPaint = new Paint();
        //颜色
        mPaint.setColor(color);
        //抗锯齿
        mPaint.setAntiAlias(true);
        //防抖动
        mPaint.setDither(true);
        //大小
        mPaint.setTextSize(getTextSize());
        return mPaint;
    }

    //一个字体两种颜色
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);自己画，不要调用系统的
        //计算变化
        int middle = (int) (mCurrentChange * getWidth());

        if (mDirection == Direction.LEFT_TO_RIGHT) {
            //1.原始颜色
            drawTextByClip(canvas, mChangePaint, 0, middle);
            //2.变化颜色
            drawTextByClip(canvas, mOriginalPaint, middle, getWidth());
        }else {
            //1.原始颜色
            drawTextByClip(canvas, mChangePaint, getWidth()- middle, getWidth());
            //2.变化颜色
            drawTextByClip(canvas, mOriginalPaint,0 , getWidth() - middle);
        }
    }

    /**
     * 画字体的方法
     * @param canvas  画布
     * @param paint  画笔
     * @param start  起点
     * @param end   终点
     */
    private void drawTextByClip(Canvas canvas, Paint paint, int start, int end) {
        canvas.save();//保存画布
        Rect rect = new Rect(start,0,end,getHeight());
        //裁剪
        canvas.clipRect(rect);
        //字内容
        String text = getText().toString();
        Rect bounds = new Rect();
        paint.getTextBounds(text,0,text.length(),bounds);
        //起点
        int fontStart = getWidth()/2 - bounds.width()/2;
        //基线
        Paint.FontMetricsInt font = paint.getFontMetricsInt();
        int dy = (font.bottom - font.top)/2 - font.bottom;
        int baseline = getHeight()/2+dy;
        //赋值
        canvas.drawText(text,fontStart,baseline,paint);
        canvas.restore();//释放画布
    }

    public void setDirection(Direction direction){
        this.mDirection = direction;
    }

    public void setCurrentChange(float currentChange){
        this.mCurrentChange = currentChange;
        invalidate();
    }
}
