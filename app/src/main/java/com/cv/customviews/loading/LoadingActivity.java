package com.cv.customviews.loading;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewAnimator;

import com.cv.customviews.R;

public class LoadingActivity extends AppCompatActivity {
   /* private CustomProgressBar mCustomProgressBar;*/
    private ShapeChangeView changeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        /*mCustomProgressBar = findViewById(R.id.cpb);
        mCustomProgressBar.setAllLoad(1000);

        ValueAnimator animator = ObjectAnimator.ofFloat(0,1000);
        animator.setDuration(5000);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                mCustomProgressBar.setProgress((int) progress);
            }
        });*/
        changeView = findViewById(R.id.scv);
    }

    public void exchangeView(View view) {
       new Thread(new Runnable() {
           @Override
           public void run() {
                while (true){
                    changeView.exchange();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

           }
       }).start();
    }
}
