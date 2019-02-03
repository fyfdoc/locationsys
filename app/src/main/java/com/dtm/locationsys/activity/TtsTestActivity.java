package com.dtm.locationsys.activity;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dtm.locationsys.R;

import java.util.Locale;

public class TtsTestActivity extends AppCompatActivity {

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts_test);

        Button btnTtsTest = (Button)findViewById(R.id.btn_tts_test);

        // 初始化TTS（因为此方法是异步的，所以要提前初始化）
        tts = new TextToSpeech(TtsTestActivity.this, new MyOnInitListener());


        // 按钮监听事件
        btnTtsTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textSpeak = "你已进入小区1";

                int tls = tts.isLanguageAvailable(Locale.CHINA);
                if(tls != TextToSpeech.LANG_COUNTRY_AVAILABLE
                        && tls != TextToSpeech.LANG_AVAILABLE){
                    MyToast("没有安装中文TTS，或者TTS设置有误2");
                }else {
                    tts.speak(textSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }

                tts.speak(textSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    // 初始化TTS类
    class MyOnInitListener implements TextToSpeech.OnInitListener {
        @Override
        public void onInit(int status) {
            // 如果装载TTS引擎成功
            if(status == TextToSpeech.SUCCESS){
                // 设置地区（语言）
                int rs = tts.setLanguage(Locale.CHINESE);
                if(rs != TextToSpeech.LANG_COUNTRY_AVAILABLE
                        && rs != TextToSpeech.LANG_AVAILABLE ) {
                    MyToast("没有安装中文TTS，或者TTS设置有误1");
                }
                tts.setSpeechRate((float) 1.4);
            }

        }
    }

    /**
     * 自定义Toast
     * @param info
     */
    private void MyToast(String info){
        Toast.makeText(TtsTestActivity.this, info, Toast.LENGTH_SHORT).show();

    }


}
