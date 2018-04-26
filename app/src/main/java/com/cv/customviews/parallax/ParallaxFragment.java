package com.cv.customviews.parallax;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.cv.customviews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/23 0023.
 * 视差动画的Fragment
 */

public class ParallaxFragment extends Fragment implements LayoutInflaterFactory {
    public static final String LAYOUT_ID_KEY = "layout_id_key";
    private CompatViewInflater mCompatViewInflater;
    //需要解析的属性数组
    private int[] mParallaxAttrs = new int[]{R.attr.translationXIn,
            R.attr.translationXOut, R.attr.translationYIn, R.attr.translationYOut};

    private List<View> mParallaxViews = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取ViewPager中传过来的bundle中的layoutId
        int layoutId = getArguments().getInt(LAYOUT_ID_KEY);
        //****把所有需要移动的属性解析出来
        // View创建的时候 我们去解析属性  这里传 inflater 有没有问题？ 单例设计模式 代表着所有的View的创建都会是该 Fragment 去创建的
        inflater = inflater.cloneInContext(getActivity());// 克隆一个出来
        LayoutInflaterCompat.setFactory(inflater,this);
        return inflater.inflate(layoutId,container,false);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // View都会来这里,创建View
        // 拦截到View的创建  获取View之后要去解析
        // 1. 创建View
        // If the Factory didn't handle it, let our createView() method try
        View view = createView(parent, name, context, attrs);
        if (view != null) {
            // Log.e("TAG", "我来创建View");
            // 解析所有的我们自己关注属性
            analysisAttrs(view, context, attrs);
        }
        return view;
    }

    private void analysisAttrs(View view, Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs,mParallaxAttrs);
        if (array != null && array.getIndexCount() != 0){
            int n = array.getIndexCount();
            ParallaxTag tag = new ParallaxTag();
            for (int i = 0; i < n; i++) {
                int attr = array.getIndex(i);
                switch (attr){
                    case 0:
                        tag.translationXIn = array.getFloat(attr,0f);
                        break;
                    case 1:
                        tag.translationXOut = array.getFloat(attr,0f);
                        break;
                    case 2:
                        tag.translationYIn = array.getFloat(attr,0f);
                        break;
                    case 3:
                        tag.translationYOut = array.getFloat(attr,0f);
                        break;
                }
            }
            view.setTag(R.id.parallax_tag,tag);
            mParallaxViews.add(view);
        }
        array.recycle();

    }

    private View createView(View parent, String name, Context context, AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mCompatViewInflater == null) {
            mCompatViewInflater = new CompatViewInflater();
        }

        // We only want the View to inherit it's context if we're running pre-v21
        final boolean inheritContext = isPre21 && true
                && shouldInheritContext((ViewParent) parent);

        return mCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true /* Read read app:theme as a fallback at all times for legacy reasons */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (!(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    /**
     * 获取View的集合
     * @return
     */
    public List<View> getParallaxViews() {
        return mParallaxViews;
    }
}
