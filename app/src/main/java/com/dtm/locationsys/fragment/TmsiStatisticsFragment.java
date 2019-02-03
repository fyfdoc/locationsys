package com.dtm.locationsys.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dtm.locationsys.R;
import com.dtm.locationsys.cust.TmsiStatisticsAdapter;
import com.dtm.locationsys.cust.TmsiStatisticsHolder;
import com.dtm.locationsys.event.DataSyncEvent;
import com.dtm.locationsys.model.TmsiStatisticsModel;
import com.dtm.locationsys.msgDef.RptCcmReportCountInfo;
import com.dtm.locationsys.msgDef.RptCellDetectResult;
import com.dtm.locationsys.msgDef.RptTmsiStatistics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Tmsi统计
 */
public class TmsiStatisticsFragment extends Fragment {
    // Tmsi统计adapter
    private TmsiStatisticsAdapter mTmsiStatisticsAdapter;

    // Tmsi统计ViewHolder
    private RecyclerView mTmsiStatisticsView;

    // Tmsi统计缓存
    private List<TmsiStatisticsModel> bufTmsiStatisticsModelList;

    public TmsiStatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tmsi_statistics, container, false);

        bufTmsiStatisticsModelList = new ArrayList<>();

        mTmsiStatisticsView = view.findViewById(R.id.tmsi_statistics_recycler_view);
        // 设置adapter
        if (null == mTmsiStatisticsAdapter) {
            createTmsiStatisticsViewAdapter();
        }
        // 设置表格分割线
        mTmsiStatisticsView.addItemDecoration(new DividerItemDecoration(getActivity()
                , DividerItemDecoration.VERTICAL));
        // 设置布局管理器
        mTmsiStatisticsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Inflate the layout for this fragment
        return view;
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
        // 取消注册事件
        EventBus.getDefault().unregister(this);
    }

    /**
     * 事件订阅处理
     * @param dataSyncEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataUpdate(DataSyncEvent dataSyncEvent) {
        // 整理数据
        makeTmsiStatisticsData(dataSyncEvent.getBytes());

        // 更新页面数据
        mTmsiStatisticsAdapter.update(bufTmsiStatisticsModelList);
    }

    /**
     * 收到上报的Tmsi统计信息，整理为页面显示数据
     * @param bytes
     */
    private void makeTmsiStatisticsData(byte[] bytes) {
        // 构造参数结构
        RptCellDetectResult cellDetectResult = new RptCellDetectResult(bytes);

        int ch;
        // TMSI次数统计
        RptCcmReportCountInfo rptCcmReportCountInfo;
        RptTmsiStatistics rptTmsiStatistics;
        for (int i = 0; i < RptCellDetectResult.CXX_MAX_CELL_NUM; i++) {
            // 设备底层统计消息
            rptCcmReportCountInfo = cellDetectResult.getReport_count_infoList().get(i);
            // 循环TMSI
            for (int j = 0; j < RptCcmReportCountInfo.CXX_MAX_TMSI_NUM; j++) {
                // 上报的Tmsi信息
                rptTmsiStatistics = rptCcmReportCountInfo.getTmsi_tableList().get(j);
                // 有效才统计
                if (rptTmsiStatistics.valid != 1) {
                    continue;
                }

                // 频点号
                ch = rptCcmReportCountInfo.getNrfcnN();
                TmsiStatisticsModel item = null;
                // 检查此TMSI是否上报过
                for (int x = 0; x < bufTmsiStatisticsModelList.size(); x++) {
                    TmsiStatisticsModel existItem = bufTmsiStatisticsModelList.get(x);
                    // PCI、CH、TMSI是key
                    if((existItem.getTmsi() != 0)
                            && existItem.getTmsi() == rptTmsiStatistics.getM_tmsi()
                            && existItem.getPci() == rptCcmReportCountInfo.getCellID()
                            && existItem.getCh() == ch){ // 统计过
                        item = existItem;
                        break;
                    }
/*                    System.out.println("existItem.getTmsi()=" + existItem.getTmsi()
                            + ",tmsiInfo.m_tmsi=" + rptTmsiStatistics.getM_tmsi()
                            + ",existItem.getPci()=" + existItem.getPci()
                            + ",rptCcmReportCountInfo.cellID=" + rptCcmReportCountInfo.getCellID()
                            + ",existItem.getCh()=" + existItem.getCh()
                            + ",ch=" + ch);
*/
                }
//                System.out.println("================================");

                if (item != null) { // 统计过
                    item.setTmsiCount(item.getTmsiCount() + rptTmsiStatistics.getCount());
                } else { // 未统计过
                    item = new TmsiStatisticsModel();
                    // TAC TODO:无
                    item.setTac(0);
                    // CID TODO:无
                    item.setCid(0);
                    // PCI
                    item.setPci(rptCcmReportCountInfo.getCellID());
                    // bandId
                    item.setBandId(rptCcmReportCountInfo.getBandId());
                    // ch
                    item.setCh(ch);
                    // tmsi
                    item.setTmsi(rptTmsiStatistics.getM_tmsi());
                    // count累计
                    item.setTmsiCount(item.getTmsiCount() + rptTmsiStatistics.getCount());

                    // 存入缓存
                    bufTmsiStatisticsModelList.add(item);
                }
            }
        }
    }

    /**
     * Tmsi统计adapter
     */
    private void createTmsiStatisticsViewAdapter() {
        mTmsiStatisticsAdapter = new TmsiStatisticsAdapter();
        mTmsiStatisticsView.setAdapter(mTmsiStatisticsAdapter);
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
