package com.cv.customviews.viewsource;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cv.customviews.R;

public class TouchActivity extends AppCompatActivity {
    private TouchView mTouchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        mTouchView = findViewById(R.id.tv);
        mTouchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TouchView", "onClick: ");
            }
        });
        mTouchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("TouchView", "onTouch: "+event.getAction());
                return false;
            }
        });
    }
}
