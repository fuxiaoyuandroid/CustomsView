package com.cv.customviews.parallaxloading;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cv.customviews.R;

public class ParallaxLoadingActivity extends AppCompatActivity {
    private ParallaxLoadingView parallaxLoadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax_loading);
        parallaxLoadingView = findViewById(R.id.loadView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                parallaxLoadingView.disappear();
            }
        },2000);
    }
}
