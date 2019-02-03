package com.dtm.locationsys.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dtm.locationsys.R;

public class ModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        // 确认按钮
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);

        // 确认按钮事件监听
//        btnConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 跳转到主界面
//                Intent intent = new Intent(ModeActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}
