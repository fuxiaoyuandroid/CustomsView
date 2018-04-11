package com.cv.customviews.statusbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.cv.customviews.R;

public class StatusBarChangeActivity extends AppCompatActivity {
    private static final String TAG = "StatusBarChangeActivity";
    private View mTitleBar;
    private MyScrollView mScrollView;
    private ImageView imageView;
    private int ivHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar_change);
        //StatusBarUtils.setStatusBarColor(this, Color.BLUE);
        StatusBarUtils.setStatusBarTranslucent(this);

        imageView = findViewById(R.id.iv);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                ivHeight = imageView.getMeasuredHeight();
            }
        });

        //刚进来背景完全透明
        mTitleBar = findViewById(R.id.tb);
        mTitleBar.getBackground().setAlpha(0);
        mScrollView = findViewById(R.id.sv);
        //不断监听滚动  判断当前滚动的位置跟头部的ImageView比较计算背景透明度
        mScrollView.setOnScrollChangedListener(new MyScrollView.ScrollChangedListener() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                if (ivHeight == 0) return;
                //获取图片的高度，根据当前滚动的位置，计算alpha值
                float alpha = (float) t/(ivHeight-120);
                Log.d(TAG, "onScroll: "+alpha);
                if (alpha <= 0){
                    alpha = 0;
                }
                if (alpha > 1){
                    alpha = 1;
                }
                mTitleBar.getBackground().setAlpha((int) alpha*255);
            }
        });
    }
}
