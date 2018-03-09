package com.cv.customviews.stream;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/3/9 0009.
 * 适配器
 */

public abstract class StreamAdapter {
    //条目
    public abstract int getCount();

    //当前位置
    public abstract View getView(int position, ViewGroup parent);

    //刷新 观察者模式
    public void registerDataSetObserver(DataSetObserver observer){

    }

    public void unregisterDataSetObserver(DataSetObserver observer){

    }

}
