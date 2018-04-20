package com.cv.customviews.bezier;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cv.customviews.R;

public class BezierActivity extends AppCompatActivity {
    private TextView bezierTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE); //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        */
        setContentView(R.layout.activity_bezier);
        bezierTv = findViewById(R.id.bezierTv);
        MessageBubbleView.attach(bezierTv, new BubbleMessageTouchListener.BubbleDisappearListener() {
            @Override
            public void dismiss(View view) {
                Toast.makeText(BezierActivity.this,"爆炸消失",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
