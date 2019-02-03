package com.dtm.locationsys.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dtm.locationsys.R;
import com.dtm.locationsys.chart.DirectionChart;

/**
 *
 */
public class TestFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test, container, false);

        // 显示图形的layout
        ViewGroup viewGroup = view.findViewById(R.id.layout_clock);


        DirectionChart directionChart = new DirectionChart(getActivity(), viewGroup, "Ajjjjj");
        directionChart.addArrow(Color.GREEN);
        directionChart.addData(10.5, "A");
        directionChart.show();



        return view;
    }


}
