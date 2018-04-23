package com.cv.customviews.pointpraise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cv.customviews.R;

public class PointPraiseActivity extends AppCompatActivity {
    private LoveLayout mLoveLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_praise);
        mLoveLayout = findViewById(R.id.love_layout);
    }
    //点击事件
    public void pointPraise(View view) {
        //Toast.makeText(this,"测试",Toast.LENGTH_SHORT).show();
        mLoveLayout.addLove();
    }
}
