package com.cv.customviews.stream;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cv.customviews.R;

import java.util.ArrayList;
import java.util.List;

public class StreamActivity extends AppCompatActivity {
    private StreamLayout streamLayout;
    private List<String> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steam);
        streamLayout = findViewById(R.id.sl);
        mList = new ArrayList<>();
        mList.add("德玛西亚之力");
        mList.add("德玛西亚皇子");
        mList.add("德邦总管");
        mList.add("光辉女郎");
        mList.add("纳尔");
        mList.add("锤石");
        mList.add("熔岩巨兽");
        mList.add("披甲龙龟");
        mList.add("提莫");
        mList.add("寒冰射手艾希");
        mList.add("伊泽瑞尔");
        mList.add("WER");
        mList.add("白银");
        mList.add("盖伦");
        streamLayout.setAdapter(new StreamAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public View getView(final int position, ViewGroup parent) {
                TextView streamTv = (TextView) LayoutInflater.from(StreamActivity.this).inflate(R.layout.item_stream,parent,false);
                streamTv.setText(mList.get(position).toString());
                streamTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(StreamActivity.this,mList.get(position).toString(),Toast.LENGTH_SHORT).show();
                    }
                });
                return streamTv;
            }
        });
    }
}
