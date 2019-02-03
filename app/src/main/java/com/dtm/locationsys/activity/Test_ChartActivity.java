package com.dtm.locationsys.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dtm.locationsys.R;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class Test_ChartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] titles = new String[] { "天线一", "天线二" };
        List<double[]> x = new ArrayList<>();
        List<double[]> y = new ArrayList<double[]>();
        x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        y.add(new double[] { 3, 14, 5, 30, 20, 25, 40, 44, 17, 30 });
        y.add(new double[] { 18, 9, 21, 15, 10, 6, 40, 46, 23, 30 });
        XYMultipleSeriesDataset dataset = buildDataset(titles, x, y);

        int[] colors = new int[] { Color.BLUE, Color.GREEN };
        PointStyle[] styles = new PointStyle[] { PointStyle.TRIANGLE, PointStyle.DIAMOND };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);
        setChartSetting(renderer, "Line Chart Demo", "次数", "场强值", -1, 12, 0, 60, Color.RED, Color.WHITE);
        View Chart = ChartFactory.getLineChartView(this, dataset, renderer);
        setContentView(Chart);

    }

    protected XYMultipleSeriesDataset buildDataset(String[] titles
            , List<double[]> xValues, List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i]);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

    protected XYMultipleSeriesRenderer buildRenderer(int[] colors
            , PointStyle[] styles, boolean fill) {
        XYMultipleSeriesRenderer render = new XYMultipleSeriesRenderer();
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            r.setFillPoints(fill);
            render.addSeriesRenderer(r);
        }
        return render;
    }

    protected void setChartSetting(XYMultipleSeriesRenderer render, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        render.setChartTitle(title);
        render.setXTitle(xTitle);
        render.setYTitle(yTitle);
        render.setXAxisMin(xMin);
        render.setXAxisMax(xMax);
        render.setYAxisMin(yMin);
        render.setYAxisMax(yMax);
        render.setAxesColor(axesColor);
        render.setLabelsColor(labelsColor);

        render.setTextTypeface("sans_serif", Typeface.NORMAL); /* 设置字体 */
        render.setLabelsTextSize(16f); /* 横纵坐标刻度 */
        render.setAxisTitleTextSize(10);// 横纵坐标标题
        render.setLegendTextSize(15);// 图例
        render.setAxesColor(Color.BLACK);
        render.setBarWidth(1.2f);
        render.setDisplayChartValues(true);

    }
}
