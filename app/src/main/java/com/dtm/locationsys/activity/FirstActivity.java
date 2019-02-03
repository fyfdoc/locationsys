package com.dtm.locationsys.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dtm.locationsys.R;
import com.dtm.locationsys.event.DataSyncEventTest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FirstActivity extends AppCompatActivity {
    private TextView txvfirst;
    private Button btnFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        txvfirst = (TextView) findViewById(R.id.txt_first);
        txvfirst.setText("first view");

        btnFirst = (Button) findViewById(R.id.btn_first);

        btnFirst.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstActivity.this, SecondActivity.class));
            }
        });

        // 注册事件
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消注册事件
        EventBus.getDefault().unregister(this);
    }

    /**
     * 事件订阅处理事件
     * @param dataSyncEventTest
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateData(DataSyncEventTest dataSyncEventTest) {
        txvfirst.setText(dataSyncEventTest.getMsg());
    }
}
