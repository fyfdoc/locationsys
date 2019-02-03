package com.dtm.locationsys.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.graphics.Paint.Align;
import android.graphics.Color;
import android.widget.Toast;

import com.dtm.locationsys.R;
import com.dtm.locationsys.event.DataSyncEvent;
import com.dtm.locationsys.msgDef.RptCellDetectResult;
import com.dtm.locationsys.msgDef.RptUlPowerMeasInfo;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 能量值柱状图
 */
public class EnergyChartFragment extends Fragment {
    private static final String MyTAG = "MainPageFragment";

    LinearLayout li1;
    View rootView;

    /**
     * 解析获取参数响应消息
     * @param bytesMsg
     */
    private void getParasInfoRps(byte[] bytesMsg){
        // 转换为dB后的测向功率
        double[] antPower = new double[RptUlPowerMeasInfo.CXX_MAX_UL_ANTENNA_NUM];
        // 数据格式化
        DecimalFormat df = new DecimalFormat("######0.00");

        // 用获取到的参数构造参数结构
        RptCellDetectResult cellDetectRs =
                new RptCellDetectResult(bytesMsg);

        // 获取中标测向数据
        RptUlPowerMeasInfo ulPower = cellDetectRs.ul_power_meas_info;
        // 将功率转换为dB
        for (int i = 0; i < RptUlPowerMeasInfo.CXX_MAX_UL_ANTENNA_NUM; i++) {
            double dB = 10 * Math.log10(ulPower.ant_power[i]) + 20 * (3 - ulPower.ul_rf_gain);
            antPower[i] = Double.valueOf(df.format(dB));
        }

        // 构造柱状图
        initView(antPower);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 注册事件
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 取消注册事件
        EventBus.getDefault().unregister(this);
    }

    /**
     * 事件订阅处理
     * @param dataSyncEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataUpdate(DataSyncEvent dataSyncEvent) {
        // 更新UI页面数据
        getParasInfoRps(dataSyncEvent.getBytes());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_energy_chart, container, false);
        //得到资源
        li1 = rootView.findViewById(R.id.energy_bar_chart);

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * 初始化数据
     */
    private void initView(double[] antPower) {
        //柱状图的两个序列的名字
        String[] titles = new String[] { "", "能量值" };
        //存放柱状图两个序列的值
        ArrayList<double[]> value = new ArrayList<>();
        // 用于背景显示
        double[] d1 = new double[] { 160, 160, 160, 160 };
        // 功率值
        double[] d2 = antPower;
        value.add(d1);
        value.add(d2);
        //两个状的颜色
        int[] colors = { Color.GRAY, Color.BLUE};

        //为li1添加柱状图
        li1.addView(
                xyChart(titles, value, colors, 5, 9,
                        new double[] { 0, 4.5, 0, 170 },
                        new int[] { 1, 2, 3, 4 },
                        "能量值表", true),
                new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
    }

    /**
     * 生成柱状图
     * @param titles 图形注释
     * @param value Y值
     * @param colors 柱状图颜色
     * @param x x轴坐标刻度个数
     * @param y y轴坐标刻度个数
     * @param range x轴的最小和最大刻度值，y轴的最小和最大刻度值
     * @param xLable x轴坐标刻度值
     * @param xtitle 标题
     * @param f 是否显示图形顶端数字
     * @return
     */
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
            for (int j = 0;j < yLable.length; j++) {
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
        renderer.setYLabelsAlign(Align.CENTER);
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
        renderer.setLegendTextSize(28);
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
        renderer.setLabelsTextSize(36);

        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(i);
            if(i == 0){
                ssr.setShowLegendItem(false);
                continue;
            }
            ssr.setChartValuesTextAlign(Align.RIGHT);
            // 柱状图顶端数字大小
            ssr.setChartValuesTextSize(36);
            ssr.setDisplayChartValues(f);

        }
        GraphicalView mChartView = ChartFactory.getBarChartView(getContext(),
                dataset, renderer, Type.STACKED);

        return mChartView;
    }

    /**
     * 自定义Toast
     * @param info
     */
    private void MyToast(String info){
        if(getActivity() != null){
            Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
        }
    }
}
