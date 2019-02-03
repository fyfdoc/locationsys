package com.dtm.locationsys.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dtm.locationsys.cust.CustTableStationTextView;
import com.dtm.locationsys.R;


public class StationFragment extends Fragment {

    // 显示数据表的Layout
    private LinearLayout cellLayout;
    private RelativeLayout cellLineLayout;
    private String[] name={"Time","Band","EARFCN","PCI","UL/DL", "MCC-MNC-TAC-ECI(十六进制)"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station, container, false);
        // 获取显示表格的Layout
        cellLayout = view.findViewById(R.id.cellTable);

        // 初始化数据
        initData();

        return view;
    }


    //绑定数据
    private void initData() {
        //初始化标题,
        cellLineLayout = (RelativeLayout) LayoutInflater.from(
                getActivity()).inflate(R.layout.table_header_station, null);
        // "Time"
        CustTableStationTextView title =  cellLineLayout.findViewById(R.id.list_1_1);
        title.setText(name[0]);
        title.setTextColor(Color.BLUE);
        // "Band"
        title = cellLineLayout.findViewById(R.id.list_1_2);
        title.setText(name[1]);
        title.setTextColor(Color.BLUE);
        // "EARFCN"
        title =  cellLineLayout.findViewById(R.id.list_1_3);
        title.setText(name[2]);
        title.setTextColor(Color.BLUE);
        // "PCI"
        title =  cellLineLayout.findViewById(R.id.list_1_4);
        title.setText(name[3]);
        title.setTextColor(Color.BLUE);
        // "UL/DL"
        title =  cellLineLayout.findViewById(R.id.list_1_5);
        title.setText(name[4]);
        title.setTextColor(Color.BLUE);
        // "MCC-MNC-TAC-ECI(十六进制)"
        title =  cellLineLayout.findViewById(R.id.list_1_6);
        title.setText(name[5]);
        title.setTextColor(Color.BLUE);

        cellLayout.addView(cellLineLayout);

        //初始化内容
        int number = 1;
        for (int i=0; i < 10; i++) {
            cellLineLayout = (RelativeLayout) LayoutInflater.from(
                    getActivity()).inflate(R.layout.table_header_station, null);
            // "Time"
            CustTableStationTextView txt = cellLineLayout.findViewById(R.id.list_1_1);
            String tmpVal = "16:06:0" + String.valueOf(number);
            txt.setText(tmpVal);
            // "Band"
            txt =  cellLineLayout.findViewById(R.id.list_1_2);
            txt.setText("3" +  String.valueOf(number));
            // "EARFCN"
            txt =  cellLineLayout.findViewById(R.id.list_1_3);
            int tmpInt = -110 + number;
            txt.setText("3840" + String.valueOf(tmpInt));
            // "PCI"
            txt =  cellLineLayout.findViewById(R.id.list_1_4);
            txt.setText("357");
            // "UL/DL"
            txt =  cellLineLayout.findViewById(R.id.list_1_5);
            txt.setText("100/100");
            // "MCC-MNC-TAC-ECI(十六进制)"
            txt =  cellLineLayout.findViewById(R.id.list_1_6);
            txt.setText("460-00-10de-112a00b(1120-00-4318-1799783)");

            cellLayout.addView(cellLineLayout);
            number++;
        }
    }
}
