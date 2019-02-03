package com.dtm.locationsys.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dtm.locationsys.R;
import com.dtm.locationsys.event.DataSyncEventTest;

import org.greenrobot.eventbus.EventBus;

public class SecondActivity extends AppCompatActivity {
    TextView txvSecond;
    Button btnSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        txvSecond = (TextView) findViewById(R.id.txv_second);
        btnSecond = (Button) findViewById(R.id.btn_second);

        btnSecond.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new DataSyncEventTest("我是事件发布者"));
                finish();
            }
        });
    }
}
