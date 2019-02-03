package com.dtm.locationsys.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dtm.locationsys.R;
import com.dtm.locationsys.chart.ClockView;
import com.dtm.locationsys.chart.HistogramView;
import com.dtm.locationsys.cust.AntennaDirection;
import com.dtm.locationsys.cust.CircleView;
import com.dtm.locationsys.event.DataSyncEvent;
import com.dtm.locationsys.msgDef.RptCellDetectResult;
import com.dtm.locationsys.msgDef.RptUlPowerMeasInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 测向
 */
public class DetectedUserFragment extends Fragment {
    private final static int COLUMN_MAX_VALUE = 160;

    private HistogramView mFrontPowerColumn;

    private HistogramView mBackPowerColumn;

    private HistogramView mLeftPowerColumn;

    private HistogramView mRightPowerColumn;

    private CircleView mCircleView;


    public DetectedUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册数据同步事件
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销数据同步事件
        EventBus.getDefault().unregister(this);
    }

    /**
     * 事件订阅处理
     * @param dataSyncEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataUpdate(DataSyncEvent dataSyncEvent) {
        // 更新UI页面数据
        updateDisplay(dataSyncEvent.getBytes());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detected_user, container, false);

/*        mFrontPowerColumn = view.findViewById(R.id.front_power_column);
        mFrontPowerColumn.setMaxData(COLUMN_MAX_VALUE);

        mBackPowerColumn = view.findViewById(R.id.back_power_column);
        mBackPowerColumn.setMaxData(COLUMN_MAX_VALUE);

        mLeftPowerColumn = view.findViewById(R.id.left_power_column);
        mLeftPowerColumn.setMaxData(COLUMN_MAX_VALUE);

        mRightPowerColumn = view.findViewById(R.id.right_power_column);
        mRightPowerColumn.setMaxData(COLUMN_MAX_VALUE);

        mClockView = view.findViewById(R.id.location_clock);

        RptUlPowerMeasInfo updateDetectedUser = new RptUlPowerMeasInfo(1,2,3,4,new long[4]);

        updateDisplay(updateDetectedUser);*/

        mCircleView = view.findViewById(R.id.location_circle_view);

        return view;
    }


    /**
     * 更新页面数据
     * @param bytes
     */
    private void updateDisplay(byte[] bytes) {
        // 用获取到的参数构造参数结构
        RptCellDetectResult cellDetectResult = new RptCellDetectResult(bytes);
        long[] powers = cellDetectResult.getUl_power_meas_info().getAnt_power();
        // 更新方向仪表盘
        mCircleView.updateData(powers);
    }






}
