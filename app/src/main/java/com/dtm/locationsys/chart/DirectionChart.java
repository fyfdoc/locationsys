package com.dtm.locationsys.chart;

import java.util.HashMap;
import java.util.Map;

import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DialRenderer;
import org.achartengine.renderer.DialRenderer.Type;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class DirectionChart {
	Context context;
	// 父容器
	private ViewGroup container;
	/** The chart view that displays the data. */
	private GraphicalView mChartView;
	private CategorySeries category;
	private DialRenderer renderer;
	private int mArrowCount;
	private Map<String, Integer> mapNameIndex;
	private Integer mDataCount;
	private String title;
	private float val;

	public DirectionChart(Context context, ViewGroup container, String title) {
		this.context = context;
		this.container = container;
		category = new CategorySeries(title);
		renderer = new DialRenderer();
		mapNameIndex = new HashMap<String, Integer>();
		mArrowCount = 0;
		mDataCount = 0;
		init();
	}

	protected void init() {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		val = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, metrics);
		renderer.setChartTitle(title);
		renderer.setChartTitleTextSize(val);
		// renderer.setAxesColor(Color.BLACK);
		renderer.setLabelsColor(Color.BLACK);
		renderer.setLabelsTextSize(val);

		renderer.setLegendTextSize(val);
		// renderer.setMargins(new int[] {20, 30, 15, 0});

		renderer.setMajorTicksSpacing(3);
		renderer.setMinorTicksSpacing(1);
		renderer.setAngleMax(0);
		renderer.setAngleMin(360);
		renderer.setMinValue(0);
		renderer.setMaxValue(12);

		renderer.setShowLegend(false);
		// renderer.setMargins(new int[]{0,0,0,0});
		renderer.setPanEnabled(false);
		renderer.setZoomEnabled(false);
	}

	// 添加指针类型
	public DirectionChart addArrow(int color) {
		mArrowCount += 1;
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(color);
		renderer.addSeriesRenderer(r);
		Type[] temp = new Type[mArrowCount];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = Type.ARROW;
		}
		renderer.setVisualTypes(temp);

		return this;
	}

	// 添加数据
	public DirectionChart addData(double value, String name) {
		Integer index = mapNameIndex.get(name);
		if (index == null) {

			category.add(name, value);
			mapNameIndex.put(name, mDataCount);
			mDataCount += 1;
		} else {
			category.set(index, name, value);
		}
		return this;
	}

	public DirectionChart addData(double value) {

		return this;
	}

	public void show() {
		if (mChartView == null) {
			mChartView = ClockChart.getClockChartView(context, category, renderer);
			container.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			// 顺时针旋转180度，api 11
			// container.setRotation(180);
		} else {
			mChartView.repaint();
		}
	}

}
