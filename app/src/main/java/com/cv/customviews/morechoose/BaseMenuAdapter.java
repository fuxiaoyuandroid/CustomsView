package com.cv.customviews.morechoose;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public abstract class BaseMenuAdapter {
    private MenuObserver mObserver;

    public void registerDataSetObserver(MenuObserver observer) {
        mObserver = observer;
    }

    public void unregisterDataSetObserver(MenuObserver observer) {
        mObserver = null;
    }

    public void closeMenu(){
        if (mObserver != null) {
            mObserver.closeMenu();
        }
    }
    //获取总共有多少条
    public abstract int getCount();

    //获取当前的TabView
    public abstract View getTabView(int position, ViewGroup parent);

    //获取当前的菜单内容
    public abstract View getMenuView(int position,ViewGroup parent);

    public abstract void menuOpen(View tabView);

    public abstract void menuClose(View view);
}
