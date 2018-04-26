package com.cv.customviews.parallax;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.cv.customviews.R;

public class ParallaxActivity extends AppCompatActivity {
    private ParallaxViewPager mParallaxViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_parallax);
        mParallaxViewPager = findViewById(R.id.parallax_vp);
        //自定义ViewPager中自定义绑定方法
        mParallaxViewPager.attach(getSupportFragmentManager(),
                new int[]{R.layout.fragment_page_first,
                          R.layout.fragment_page_second,
                          R.layout.fragment_page_third });
    }

}
