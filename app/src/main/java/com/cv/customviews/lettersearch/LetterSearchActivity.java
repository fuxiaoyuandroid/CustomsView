package com.cv.customviews.lettersearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cv.customviews.R;

/**
 * 字母索引表
 */
public class LetterSearchActivity extends AppCompatActivity implements LetterSearchBar.LetterTouchListener {
    private TextView letterTv;
    private LetterSearchBar letterSearchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_search);
        letterTv = findViewById(R.id.ltv);
        letterSearchBar = findViewById(R.id.lsb);
        letterSearchBar.setOnLetterTouchListener(this);
    }


    @Override
    public void touch(CharSequence letter, boolean isTouch) {
        if (isTouch) {
            letterTv.setText(letter);
            letterTv.setVisibility(View.VISIBLE);
        }else {
            letterTv.setVisibility(View.GONE);
        }
    }
}
