package com.cv.customviews.bezier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cv.customviews.R;

import static com.cv.customviews.bezier.BubbleUtils.getStatusBarHeight;


/**
 * Created by Administrator on 2018/4/18 0018.
 * 监听当前View的触摸控件
 */

public class BubbleMessageTouchListener implements View.OnTouchListener, MessageBubbleView.MessageBubbleListener {
    //原来需要拖动爆炸的View
    private View mStaticView;

    private WindowManager mWindowManager;

    private MessageBubbleView messageBubbleView;

    private WindowManager.LayoutParams mParams;

    private Context mContext;

    private FrameLayout mBombFrame;

    private ImageView mBombImageView;

    private BubbleDisappearListener mListener;

    public BubbleMessageTouchListener(View view, Context context,BubbleDisappearListener listener) {
        mStaticView = view;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        messageBubbleView = new MessageBubbleView(context);
        messageBubbleView.setMessageBubbleListener(this);
        mParams = new WindowManager.LayoutParams();
        //背景要透明
        mParams.format = PixelFormat.TRANSPARENT;

        mContext = context;

        mBombFrame = new FrameLayout(mContext);
        mBombImageView = new ImageView(mContext);
        mBombImageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mBombFrame.addView(mBombImageView);
        mListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Bitmap bitmap = getBitmapByView(mStaticView);

                //要在WindowManager上面搞一个View
                mWindowManager.addView(messageBubbleView,mParams);
                //注意获取的是屏幕的位置，以及减去状态栏的高度
                //保证固定圆的中心在View的中心
                int[] location = new int[2];
                mStaticView.getLocationOnScreen(location);
                messageBubbleView.initFixedDragPoints(location[0] + bitmap.getWidth()/2,
                        location[1] + bitmap.getHeight()/2 - getStatusBarHeight(mContext));

                messageBubbleView.setDragBitmap(bitmap);
                //设置隐藏  会有抖动
                mStaticView.setVisibility(View.INVISIBLE);
                break;
            case MotionEvent.ACTION_MOVE:
                //注意获取的是屏幕的位置，以及减去状态栏的高度
                messageBubbleView.moveDragPoint(event.getRawX(),
                        event.getRawY() - getStatusBarHeight(mContext));
                break;
            case MotionEvent.ACTION_UP:

                messageBubbleView.handleHandUp();
                break;
        }
        return true;
    }

    /**
     * 从View中获取Bitmap
     * @param mView
     * @return
     */
    private Bitmap getBitmapByView(View mView) {
        mView.buildDrawingCache();
        Bitmap bitmap = mView.getDrawingCache();
        return bitmap;
    }

    @Override
    public void restore() {
        //移除
        mWindowManager.removeView(messageBubbleView);
        //把原来的View显示
        mStaticView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss(PointF pointF) {
        //执行爆炸动画
        //原来的View移除
        mWindowManager.removeView(messageBubbleView);
        //在WindowManager上添加一个爆炸动画
        mWindowManager.addView(mBombFrame,mParams);
        mBombImageView.setBackgroundResource(R.drawable.anim_bubble_pop);

        AnimationDrawable drawable = (AnimationDrawable) mBombImageView.getBackground();
        mBombImageView.setX(pointF.x - drawable.getIntrinsicWidth()/2);
        mBombImageView.setY(pointF.y - drawable.getIntrinsicHeight()/2);
        drawable.start();
        //执行完之后移除掉爆炸动画
        mBombImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWindowManager.removeView(mBombFrame);
                //通知一下外面，消失了
                if (mListener != null){
                    mListener.dismiss(mStaticView);
                }
            }
        },getAnimationDrawableTime(drawable));
    }

    private long getAnimationDrawableTime(AnimationDrawable drawable) {
        int numberOfFrame = drawable.getNumberOfFrames();
        long time = 0;
        for (int i = 0; i < numberOfFrame; i++) {
            time += drawable.getDuration(i);
        }
        return time;
    }

    public interface BubbleDisappearListener{
        void dismiss(View view);
    }
}
