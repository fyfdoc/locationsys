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
import android.widget.Switch;

import com.dtm.locationsys.cust.CustTableTextView;
import com.dtm.locationsys.R;


/**
 * TAC数据表
 */
public class TacTableFragment extends Fragment {
    // 显示数据表的Layout
    private LinearLayout cellLayout;
    private RelativeLayout cellLineLayout;
    private String[] name={"发", "TAC/CID/PCI","CH/BAND"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tac_table, container, false);
        // 获取显示表格的Layout
        cellLayout = view.findViewById(R.id.cellTable);

        // 初始化数据
        initData();

        return view;
    }


    //绑定数据
    private void initData() {
        //初始化标题,
        cellLineLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.table_header_tac, null);
        // 发
        CustTableTextView title =  cellLineLayout.findViewById(R.id.list_1_1);
        title.setText(name[0]);
        title.setTextColor(Color.BLUE);
        // TAC/CID/PCI
        title = cellLineLayout.findViewById(R.id.list_1_2);
        title.setText(name[1]);
        title.setTextColor(Color.BLUE);
        // PCI
        title =  cellLineLayout.findViewById(R.id.list_1_3);
        title.setText(name[2]);
        title.setTextColor(Color.BLUE);

        cellLayout.addView(cellLineLayout);

        //初始化内容
        int number = 1;
        for (int i=0; i < 5; i++) {
            cellLineLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.table_body_tac, null);
            // 发
            Switch swhSend = cellLineLayout.findViewById(R.id.list_1_1);
            swhSend.setChecked(true);
            // TAC/CID/PCI
            CustTableTextView txt =  cellLineLayout.findViewById(R.id.list_1_2);
            txt.setText("534/7896" +  String.valueOf(number));
            // PCI
            txt =  cellLineLayout.findViewById(R.id.list_1_3);
            int tmpInt = -110 + number;
            txt.setText(String.valueOf(tmpInt));

            cellLayout.addView(cellLineLayout);
            number++;
        }
    }
}
