package com.cv.customviews.carhome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cv.customviews.R;

import java.util.ArrayList;
import java.util.List;

public class VerticalDragActivity extends AppCompatActivity {
    private ListView mItems;
    private List<String> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_drag);
        mItems = findViewById(R.id.lv);
        mList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            mList.add("clearLove"+i);
        }
        mItems.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView itemView = (TextView) LayoutInflater.from(VerticalDragActivity.this).inflate(R.layout.item_two_car,parent,false);
                itemView.setText(mList.get(position).toString());
                return itemView;
            }
        });
    }
}
