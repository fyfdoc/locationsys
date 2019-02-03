package com.dtm.locationsys.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.graphics.Paint.Align;

import com.dtm.locationsys.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


public class EnergyValueBarChartActivity extends Activity {
    LinearLayout li1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //得到资源
        li1 = (LinearLayout) findViewById(R.id.energy_bar_chart);
        //初始化柱状图
        initView();

    }


    private void initView() {
        //柱状图的两个序列的名字
        String[] titles = new String[] { "", "能量值" };
        //存放柱状图两个序列的值
        ArrayList<double[]> value = new ArrayList<double[]>();
        double[] d1 = new double[] { 1000, 1000, 1000, 1000 };
        double[] d2 = new double[] { 200, 150, 250, 410 };
        value.add(d1);
        value.add(d2);
        //两个状的颜色
        int[] colors = { Color.GRAY, Color.BLUE};

        //为li1添加柱状图
        li1.addView(
                xyChart(titles, value, colors, 5, 5,
                        new double[] { 0, 4.5, 0, 1000 },
                        new int[] { 1, 2, 3, 4 },
                        "能量值表", true),
                        new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT));
    }

    public GraphicalView xyChart(String[] titles, ArrayList<double[]> value,
                                int[] colors, int x, int y,double[] range,
                                int []xLable ,String xtitle, boolean f) {
        //多个渲染
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        //多个序列的数据集
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        //构建数据集以及渲染
        for (int i = 0; i < titles.length; i++) {

            XYSeries series = new XYSeries(titles[i]);
            double [] yLable= value.get(i);
            for (int j=0;j<yLable.length;j++) {
                series.add(xLable[j],yLable[j]);
            }
            dataset.addSeries(series);
            XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
            // 设置颜色
            xyRenderer.setColor(colors[i]);
            // 设置点的样式 //
            xyRenderer.setPointStyle(PointStyle.SQUARE);
            // 将要绘制的点添加到坐标绘制中
            renderer.addSeriesRenderer(xyRenderer);
        }
        //设置x轴标签数
        renderer.setXLabels(0);
        //设置Y轴标签数
        renderer.setYLabels(y);
        //设置x轴的最大值
//        renderer.setXAxisMax(x - 0.5);
        //设置轴的颜色
        renderer.setAxesColor(Color.BLACK);
        //设置x轴和y轴的标签对齐方式
        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setYLabelsAlign(Align.RIGHT);
        // 设置现实网格
        renderer.setShowGrid(true);
        renderer.setShowGridY(false);
        renderer.setZoomEnabled(false, false);
        // s是否显示坐标轴
        renderer.setShowAxes(false);

        // 设置条形图之间的距离
        renderer.setBarSpacing(0.5);
        renderer.setInScroll(false);
        renderer.setPanEnabled(false, false);
        renderer.setClickEnabled(false);
        //设置x轴和y轴标签的颜色
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0,Color.BLACK);
        // 坐标轴刻度文字大小
//        renderer.setLabelsTextSize(16);

        int length = renderer.getSeriesRendererCount();
        //设置图标的标题
//        renderer.setChartTitle(xtitle);
//        renderer.setLabelsColor(Color.BLACK);

        //设置图例的字体大小
        renderer.setLegendTextSize(18);
        //设置x轴和y轴的最大最小值
        renderer.setRange(range);
        renderer.setMarginsColor(0x00888888);
        //设置是否在柱体上方显示值
//        renderer.setDisplayValues(true);
        // 调整合适的位置
//        renderer.setFitLegend(false);
        renderer.addXTextLabel(1, "前方");
        renderer.addXTextLabel(2, "右方");
        renderer.addXTextLabel(3, "后方");
        renderer.addXTextLabel(4, "左方");


        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(i);
            if(i == 0){
                ssr.setShowLegendItem(false);
                continue;
            }
            ssr.setChartValuesTextAlign(Align.RIGHT);
            ssr.setChartValuesTextSize(12);
            ssr.setDisplayChartValues(f);

        }
        GraphicalView mChartView = ChartFactory.getBarChartView(getApplicationContext(),
                dataset, renderer, Type.STACKED);

        return mChartView;

    }
}
