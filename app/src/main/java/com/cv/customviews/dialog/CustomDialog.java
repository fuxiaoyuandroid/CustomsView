package com.cv.customviews.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 自定义Dialog
 */

public class CustomDialog extends Dialog {

    public CustomDialog(Context context, int width, int height, View layoutView, int style) {
        super(context,style);
        setContentView(layoutView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity  = Gravity.CENTER;
        window.setAttributes(params);
    }
}
