package com.cv.customviews.statusbar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class StatusBarUtils {
    /**
     *
     * @param activity
     * @param color
     */
   public static void setStatusBarColor(Activity activity,int color){
        //版本判断
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
           activity.getWindow().setStatusBarColor(color);
       }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
           //设置全屏
           //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置这个的结果是状态都不显示了
           activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
           //添加布局
           View view = new View(activity);
           view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity)));
           view.setBackgroundColor(color);
           ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
           decorView.addView(view);
           //android:fitsSystemWindows="true"
           ViewGroup contentView = activity.findViewById(android.R.id.content);
           contentView.setPadding(0,getStatusBarHeight(activity),0,0);
           /*View childView = contentView.getChildAt(0);
           childView.setPadding(0,getStatusBarHeight(activity),0,0);
           childView.setFitsSystemWindows(true);*/
       }
   }

    /**
     * 获取状态栏高度
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int statusHeightId = resources.getIdentifier("status_bar_height","dimen","android");
        return resources.getDimensionPixelOffset(statusHeightId);
    }

    /**
     * 设置背景透明
     * @param activity
     */
    public static void setStatusBarTranslucent(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
