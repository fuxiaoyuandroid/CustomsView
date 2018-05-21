package com.cv.customviews.copyskimmerfm;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cv.customviews.R;

public class SkimmerFMActivity extends AppCompatActivity {
    private ClickView mClickView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skimmer_fm);
        mClickView = findViewById(R.id.click_view);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mClickView.startOne();
            }
        }, 5000);

    }
}
