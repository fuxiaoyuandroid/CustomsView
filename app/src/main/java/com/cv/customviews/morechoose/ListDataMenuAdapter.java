package com.cv.customviews.morechoose;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cv.customviews.R;

/**
 *
 * Created by Administrator on 2018/4/11 0011.
 */

public class ListDataMenuAdapter extends BaseMenuAdapter {

    private String[] mItems = {"类型","品牌","价格","更多"};

    private Context mContext;

    public ListDataMenuAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public View getTabView(int position, ViewGroup parent) {
        TextView tabView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.ui_list_data_screen_tab,parent,false);
        tabView.setText(mItems[position]);
        return tabView;
    }

    @Override
    public View getMenuView(final int position, ViewGroup parent) {
        //真正的开发过程中，不同的位置显示的布局不一样
        final TextView menuView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.ui_list_data_screen_view,parent,false);
        menuView.setText(mItems[position]);
        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,mItems[position],Toast.LENGTH_SHORT).show();
                closeMenu();
                Toast.makeText(mContext,"1111111",Toast.LENGTH_SHORT).show();
            }
        });
        return menuView;
    }

    @Override
    public void menuOpen(View tabView) {
        TextView tabTv = (TextView) tabView;
        tabTv.setTextColor(Color.RED);
    }

    @Override
    public void menuClose(View view) {
        TextView tabTv = (TextView) view;
        tabTv.setTextColor(Color.BLACK);
    }
}
