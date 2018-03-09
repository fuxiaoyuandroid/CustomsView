package com.cv.customviews.viewsource;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cv.customviews.R;

public class ViewDemoActivity extends AppCompatActivity {
    private TextView mTextView;
    private static final String TAG = "ViewDemoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_demo);
        mTextView = findViewById(R.id.demo);
        Log.e(TAG, "onCreate: "+mTextView.getMeasuredHeight());//0
        mTextView.post(new Runnable() {
            //保存到queue中，什么都没干，会在dispatchAttachedToWindow后执行，
            // 该方法会测量完毕后调，executeActions()
            @Override
            public void run() {
                Log.e(TAG, "run: "+mTextView.getMeasuredHeight());//有高度
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: "+mTextView.getMeasuredHeight());//0
    }
}
