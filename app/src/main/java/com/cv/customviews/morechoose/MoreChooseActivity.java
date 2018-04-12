package com.cv.customviews.morechoose;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cv.customviews.R;

/**
 * 多条目选择
 *
 * 适配器模式
 *
 * 布局  LinearLayout = LinearLayout(放tab点击的View)+阴影(View透明度变化)+菜单的内容(FrameLayout)
 *
 * 步骤:
 * 1.布局实例化(组合控件)
 *
 * 2.引入Adapter设计模式去适配
 *
 * 3.引入动画
 *
 * 4.测试(观察者设计模式介绍)
 */
public class MoreChooseActivity extends AppCompatActivity {
    private ListDataScreenView mListDataScreenView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_choose);
        mListDataScreenView = findViewById(R.id.list_data_screen_view);
        mListDataScreenView.setAdapter(new ListDataMenuAdapter(this));
    }

    public void testShadow(View view) {
        Toast.makeText(this,"测试阴影",Toast.LENGTH_SHORT).show();
    }
}
