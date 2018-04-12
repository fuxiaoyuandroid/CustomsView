package com.cv.customviews.morechoose;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public abstract class BaseMenuAdapter {
    //获取总共有多少条
    public abstract int getCount();

    //获取当前的TabView
    public abstract View getTabView(int position, ViewGroup parent);

    //获取当前的菜单内容
    public abstract View getMenuView(int position,ViewGroup parent);

    public abstract void menuOpen(View tabView);

    public abstract void menuClose(View view);
}
