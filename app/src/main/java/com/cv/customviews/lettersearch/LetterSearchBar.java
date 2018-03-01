package com.cv.customviews.lettersearch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.cv.customviews.R;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class LetterSearchBar extends View{
    // 定义26个字母+1个特殊符号
    public static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    //当前触摸的字母
    private String currentTouchLetter,historyTouchLetter;
    private Paint mPaint,touchPaint;
    private int letterColor = Color.RED;
    private float letterSize = 12.0f;


    public LetterSearchBar(Context context) {
        super(context,null);
    }

    public LetterSearchBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LetterSearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LetterSearchBar);
        letterColor = array.getColor(R.styleable.LetterSearchBar_letterColor,letterColor);
        letterSize = array.getDimension(R.styleable.LetterSearchBar_letterSize,letterSize);
        array.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //字体大小和颜色不固定，自定义属性
        mPaint.setTextSize(sp2px(letterSize));
        mPaint.setColor(letterColor);

        touchPaint = new Paint();
        touchPaint.setAntiAlias(true);
        touchPaint.setTextSize(sp2px(letterSize));
        touchPaint.setColor(Color.RED);

        currentTouchLetter = "";
        historyTouchLetter = "";
    }
    //sp转成px
    private float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int textWidth = (int) mPaint.measureText("A");
        //宽度
        int width = getPaddingLeft()+getPaddingRight()+textWidth;
        //高度直接获取
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    /**
     * 画26个字母 + #
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom())/letters.length;
        for (int i = 0; i < letters.length; i++) {
            int x = (int) (getWidth()/2 - mPaint.measureText(letters[i])/2);
            //知道每个字母的中心位置  1 字母高度的一半   2 字母高度的一半+前面字符的高度
            int centerY = itemHeight/2 + i * itemHeight + getPaddingTop();
            //基线 基于中心位置
            Paint.FontMetricsInt font = mPaint.getFontMetricsInt();
            int dy = (font.bottom - font.top)/2 - font.bottom;
            int baseline = centerY + dy;
            //当前字母高亮  用两个画笔或用两个颜色，推荐使用两个画笔，因为设置颜色是走native方法
            if (letters[i].equals(currentTouchLetter)){
                canvas.drawText(letters[i],x,baseline,touchPaint);
            }else {
                canvas.drawText(letters[i], x, baseline, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算出当前的字母
                float currentY = event.getY();
                //currentY /字母的高度  通过位置获取字母
                int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom())/letters.length;
                int currentPosition = (int) (currentY/itemHeight);
                if (currentPosition < 0){
                    currentPosition = 0;
                }
                if (currentPosition > letters.length -1){
                    currentPosition = letters.length - 1;
                }
                currentTouchLetter = letters[currentPosition];

                if (mListener != null){
                    mListener.touch(currentTouchLetter,true);
                }
                //判断优化
                if (currentTouchLetter.equals(historyTouchLetter)){

                    Log.e("same", "onTouchEvent: "+currentTouchLetter+","+historyTouchLetter);
                    return true;
                }
                historyTouchLetter = currentTouchLetter;
                //重新绘制
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                try {
                    Thread.sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (mListener != null){
                    mListener.touch(currentTouchLetter,false);
                }
                break;
        }
        return true;
    }
    private LetterTouchListener mListener;
    public void setOnLetterTouchListener(LetterTouchListener listener){
        this.mListener = listener;
    }

    public interface LetterTouchListener{
        void touch(CharSequence letter,boolean isTouch);
    }
}
