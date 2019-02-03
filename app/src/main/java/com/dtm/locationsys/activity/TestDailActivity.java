package com.dtm.locationsys.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dtm.locationsys.R;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DialRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.Random;

public class TestDailActivity extends AppCompatActivity {

    public static final String TYPE = "type";

    CategorySeries category = new CategorySeries("Cpu freq 1");

    DialRenderer mRenderer = new DialRenderer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_test_dail);

        // 设置图表显示的位置
        mRenderer.setMargins(new int[] { 30, 30, 30, 30 });
//      设置图表的X轴处于水平方向还是垂直方向
        mRenderer.setChartTitle("Cpu Freq 2");// 设置图表标题
        mRenderer.setChartTitleTextSize(60);// 设置图表标题文字的大小
        mRenderer.setLabelsTextSize(30);// 设置标签的文字大小 ,刻度文本的大小
        mRenderer.setLegendTextSize(50);//设置图例文本大小
        mRenderer.setShowGrid(true);// 显示网格
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.BLACK);

        // mRenderer.setXLabels(0);
        DialRenderer.Type[] types = { DialRenderer.Type.ARROW,
                DialRenderer.Type.NEEDLE, DialRenderer.Type.ARROW };
        mRenderer.setVisualTypes(types);
        mRenderer.setMinValue(1);
        mRenderer.setMaxValue(12);
        mRenderer.setShowLabels(true);

        Random r = new Random();
        int[] colors = { Color.BLUE, Color.RED, Color.GREEN };
        String[] titles = { "方向", "", "" };
        for (int i = 0; i < 3; i++) {
            category.add(titles[i], r.nextInt(12));
            // 点的绘制进行设置
            SimpleSeriesRenderer ssRenderer = new SimpleSeriesRenderer();
            // 设置颜色
            ssRenderer.setColor(colors[i]);
            // 是否显示值
            ssRenderer.setDisplayChartValues(true);
            mRenderer.addSeriesRenderer(ssRenderer);
        }
        Intent intent = ChartFactory.getDialChartIntent(this, category,
                mRenderer, "cpu频率333");
        startActivity(intent);

    }

}
