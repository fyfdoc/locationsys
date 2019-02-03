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

import com.dtm.locationsys.cust.CustTableUserTextView;
import com.dtm.locationsys.R;


/**
 * 用户Fragment
 */
public class UserFragment extends Fragment {

    // 显示数据表的Layout
    private LinearLayout cellLayout;
    private RelativeLayout cellLineLayout;
    private String[] name={"Time","ChanNo","Freq","PCI","RNTI"
            , "MMECod", "Tmsi", "Energy", "State"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
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
                getActivity()).inflate(R.layout.table_header_user, null);
        // "Time"
        CustTableUserTextView title =  cellLineLayout.findViewById(R.id.list_1_1);
        title.setText(name[0]);
        title.setTextColor(Color.BLUE);
        // "ChanNo"
        title = cellLineLayout.findViewById(R.id.list_1_2);
        title.setText(name[1]);
        title.setTextColor(Color.BLUE);
        // "Freq"
        title =  cellLineLayout.findViewById(R.id.list_1_3);
        title.setText(name[2]);
        title.setTextColor(Color.BLUE);
        // "PCI"
        title =  cellLineLayout.findViewById(R.id.list_1_4);
        title.setText(name[3]);
        title.setTextColor(Color.BLUE);
        // "RNTI"
        title =  cellLineLayout.findViewById(R.id.list_1_5);
        title.setText(name[4]);
        title.setTextColor(Color.BLUE);
        // "MMECod"
        title =  cellLineLayout.findViewById(R.id.list_1_6);
        title.setText(name[5]);
        title.setTextColor(Color.BLUE);
        // Tmsi
        title =  cellLineLayout.findViewById(R.id.list_1_7);
        title.setText(name[6]);
        title.setTextColor(Color.BLUE);
        // Energy
        title =  cellLineLayout.findViewById(R.id.list_1_8);
        title.setText(name[7]);
        title.setTextColor(Color.BLUE);
        // State
        title =  cellLineLayout.findViewById(R.id.list_1_9);
        title.setText(name[8]);
        title.setTextColor(Color.BLUE);

        cellLayout.addView(cellLineLayout);

        //初始化内容
        int number = 1;
        for (int i=0; i < 10; i++) {
            cellLineLayout = (RelativeLayout) LayoutInflater.from(
                    getActivity()).inflate(R.layout.table_header_user, null);
            // Time
            CustTableUserTextView txt = cellLineLayout.findViewById(R.id.list_1_1);
            String tmpVal = "上午\n16:06:0" + String.valueOf(number);
            txt.setText(tmpVal);
            // ChanNo
            txt =  cellLineLayout.findViewById(R.id.list_1_2);
            txt.setText(String.valueOf(number));
            // Freq
            txt =  cellLineLayout.findViewById(R.id.list_1_3);
            int tmpInt = -110 + number;
            txt.setText("3840" + String.valueOf(tmpInt));
            // PCI
            txt =  cellLineLayout.findViewById(R.id.list_1_4);
            txt.setText("357");
            // RNTI
            txt =  cellLineLayout.findViewById(R.id.list_1_5);
            txt.setText("1ce4");
            // MMECod
            txt =  cellLineLayout.findViewById(R.id.list_1_6);
            txt.setText("138");
            // Tmsi
            txt =  cellLineLayout.findViewById(R.id.list_1_7);
            txt.setText("112a00b");
            // Energy
            txt =  cellLineLayout.findViewById(R.id.list_1_8);
//            txt.setText("\uF0E133 \uF0E027-\uF0E238-\uF0DF30");
            txt.setText("33 27-38-30");
            // State
            txt =  cellLineLayout.findViewById(R.id.list_1_9);
            txt.setText("X");

            cellLayout.addView(cellLineLayout);
            number++;
        }
    }
}
