package com.cv.customviews;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.cv.customviews.dialog.CustomDialog;

/**
 * 1.自定义dialog 并跳转到权限界面
 */

public class MainActivity extends AppCompatActivity {
    private Button cs_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cs_btn = findViewById(R.id.cs_btn);
        cs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*new AlertDialog.Builder(MainActivity.this).setTitle("权限说明")//设置对话框标题
                        .setMessage("您拒绝的该权限只是获取手机设备ID作为推送警报信息的根据,不会泄露用户私人信息,请您在下一次弹出时选择允许!")//设置显示的内容
                        .setPositiveButton("好,我明白了", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                dialog.dismiss();
                            }
                        }).show();*/
                View view = getLayoutInflater().inflate(R.layout.login_custom_dialog,null);
                Button button = view.findViewById(R.id.close_dialog);
                final CustomDialog builder = new CustomDialog(MainActivity.this,0,0,view,R.style.CustomDialogTheme);
                builder.setCancelable(true);
                builder.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        builder.cancel();
                    }
                });


            }
        });
    }

}
