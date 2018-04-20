package com.cv.customviews.rating;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cv.customviews.R;

public class RatingActivity extends AppCompatActivity {
    private CustomRatingBar mCustomRatingBar;
   /* int i = 4;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        mCustomRatingBar = findViewById(R.id.crb);
        /*mCustomRatingBar.setCurrentGrade(i);*/
    }
}
