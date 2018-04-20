package com.cv.customviews.bezier;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class MessageBubbleView extends View{
    private static final String TAG = "MessageBubbleView";
    //固定圆和拖拽圆
    private PointF mFixedPoint,mDragPoint;
    //拖拽圆的半径
    private int mDragRadius = 20;
    //画笔
    private Paint mDragPaint;
    //固定圆的初始半径
    private int mFixedRadiusInit = 20;
    //随着拖拽固定圆的实时半径
    private int mFixedRadius;
    //最小半径，小于这个半径不画
    private int mFixedRadiusMin = 10;
    private Bitmap mDragBitmap;

    public MessageBubbleView(Context context) {
        this(context,null);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragRadius = dip2px(mDragRadius);
        mFixedRadiusInit = dip2px(mFixedRadiusInit);
        mFixedRadiusMin = dip2px(mFixedRadiusMin);
        //画笔
        mDragPaint = new Paint();
        mDragPaint.setAntiAlias(true);
        mDragPaint.setDither(true);
        mDragPaint.setColor(Color.RED);
    }
    //转换
    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip,getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mFixedPoint == null || mDragPoint == null){
            return;
        }
        //Log.d(TAG, "onDraw: "+mDragRadius);
        canvas.drawCircle(mDragPoint.x,mDragPoint.y,mDragRadius,mDragPaint);

        Path bezierPath = getBezierPath();

        //Log.d(TAG, "onDraw: distance  "+distance+",mFixedRadius   "+mFixedRadius);

        if (bezierPath != null) {
            canvas.drawCircle(mFixedPoint.x, mFixedPoint.y, mFixedRadius, mDragPaint);

            canvas.drawPath(bezierPath,mDragPaint);
        }
        if (mDragBitmap != null){
            //为了防止抖动，可以搞一个渐变动画
            canvas.drawBitmap(mDragBitmap,mDragPoint.x - mDragBitmap.getWidth()/2,
                    mDragPoint.y - mDragBitmap.getHeight()/2,null);
        }
    }

    /**
     * 线
     * @return
     */
    private Path getBezierPath() {
        double distance = getDistance(mFixedPoint,mDragPoint);
        mFixedRadius = (int) (mFixedRadiusInit - distance/10);

        //如果半径太小就不用画了
        if (mFixedRadius < mFixedRadiusMin){
            return null;
        }
        Path bezierPath = new Path();
        //求斜率
        float dy = mDragPoint.y - mFixedPoint.y;
        float dx = mDragPoint.x - mFixedPoint.x;
        float tanA = dy/dx;
        //反tan求角A
        double arcTanA = Math.atan(tanA);
        //q0点
        float q0x = (float) (mFixedPoint.x + mFixedRadius*Math.sin(arcTanA));
        float q0y = (float) (mFixedPoint.y - mFixedRadius*Math.cos(arcTanA));

        //q1点
        float q1x = (float) (mDragPoint.x + mDragRadius*Math.sin(arcTanA));
        float q1y = (float) (mDragPoint.y - mDragRadius*Math.cos(arcTanA));

        //q2点
        float q2x = (float) (mDragPoint.x - mDragRadius*Math.sin(arcTanA));
        float q2y = (float) (mDragPoint.y + mDragRadius*Math.cos(arcTanA));

        //q3点
        float q3x = (float) (mFixedPoint.x - mFixedRadius*Math.sin(arcTanA));
        float q3y = (float) (mFixedPoint.y + mFixedRadius*Math.cos(arcTanA));
        //控制点
        PointF controlF = getControlPoint();
        bezierPath.moveTo(q0x,q0y);
        bezierPath.quadTo(controlF.x,controlF.y,q1x,q1y);
        bezierPath.lineTo(q2x,q2y);
        bezierPath.quadTo(controlF.x,controlF.y,q3x,q3y);
        bezierPath.close();
        return bezierPath;
    }

    private PointF getControlPoint() {

        return new PointF((mDragPoint.x + mFixedPoint.x)/2,(mDragPoint.y + mFixedPoint.y)/2);
    }


    /**
     * 得到两圆之间的距离
     * @param mFixedPoint
     * @param mDragPoint
     * @return
     */
    private double getDistance(PointF mFixedPoint, PointF mDragPoint) {
        return Math.sqrt((mFixedPoint.x - mDragPoint.x)*(mFixedPoint.x -
                mDragPoint.x)+(mFixedPoint.y - mDragPoint.y)*(mFixedPoint.y - mDragPoint.y));
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                initFixedDragPoints(downX,downY);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                moveDragPoint(moveX,moveY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }*/
    /**
     * 拖拽圆移动
     */
    public void moveDragPoint(float moveX, float moveY) {
        mDragPoint.x = moveX;
        mDragPoint.y = moveY;
        //重新绘制
        invalidate();
    }

    /**
     * 初始化固定圆和拖拽圆
     * @param downX
     * @param downY
     */
    public void initFixedDragPoints(float downX, float downY) {
        mFixedPoint = new PointF(downX,downY);
        mDragPoint = new PointF(downX,downY);
        invalidate();
    }

    /**
     * 绑定可以拖拽的控件
     * @param view
     * @param disappearListener
     */
    public static void attach(View view, BubbleMessageTouchListener.BubbleDisappearListener disappearListener) {
        if (view != null) {
            view.setOnTouchListener(new BubbleMessageTouchListener(view, view.getContext(),disappearListener));
        }
    }

    public void setDragBitmap(Bitmap dragBitmap) {
        this.mDragBitmap = dragBitmap;

    }

    public void handleHandUp() {
        //当半径大于最小半径时
        if (mFixedRadius > mFixedRadiusMin){
            //回弹  从拖拽点回到固定点
            final ValueAnimator animator = ObjectAnimator.ofFloat(1);
            animator.setDuration(350);
            final PointF start = new PointF(mDragPoint.x,mDragPoint.y);
            final PointF end = new PointF(mFixedPoint.x,mFixedPoint.y);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    //调用工具类获取当前点
                    PointF pointF = BubbleUtils.getPointByPercent(start,end,percent);
                    //调用方法绘制当前点
                    moveDragPoint(pointF.x,pointF.y);
                }
            });
            //插值器
            animator.setInterpolator(new OvershootInterpolator(3f));
            animator.start();
            //还要通知TouchListener  移除当前View，显示隐藏的静态View
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mListener != null) {
                        mListener.restore();
                    }
                }
            });
        }else {
            //爆炸
            if (mListener != null) {
                mListener.dismiss(mDragPoint);
            }
        }
    }

    private MessageBubbleListener mListener;

    public void setMessageBubbleListener(MessageBubbleListener listener) {
        this.mListener = listener;
    }

    public interface MessageBubbleListener{
        //还原
        void restore();
        // 消失爆炸
        void dismiss(PointF pointF);
    }

}
