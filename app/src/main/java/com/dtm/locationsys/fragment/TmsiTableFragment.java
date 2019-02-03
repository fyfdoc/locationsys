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
import android.widget.Toast;

import com.dtm.locationsys.cust.CustTableTextView;
import com.dtm.locationsys.R;
import com.dtm.locationsys.event.DataSyncEvent;
import com.dtm.locationsys.model.TmsiStatisticsModel;
import com.dtm.locationsys.msgDef.RptCcmReportCountInfo;
import com.dtm.locationsys.msgDef.RptCellDetectResult;
import com.dtm.locationsys.msgDef.RptTmsiStatistics;
import com.dtm.locationsys.utils.ArfcnConvert;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * TMSI数据表
 */
public class TmsiTableFragment extends Fragment {
    private static final String MyTAG = "TmsiTableFragment";

    // TMSI次数统计缓存,用于页面显示
    List<TmsiStatisticsModel> tmsiStatisticsModelsList = new ArrayList<>();

    // 参数获取实例
    RptCellDetectResult cellDetectReslut;

    // 显示数据表的Layout
    private LinearLayout cellLayout;
    private RelativeLayout cellHeaderLayout;
    private RelativeLayout cellLineLayout;
    private LinearLayout cellBodyLayout;
    private String[] name={"TAC/CID/PCI", "CH/BAND", "TMSI", "次数"};

    /**
     * 解析参数上报消息
     * @param bytesMsg
     */
    private void getParasInfoRps(byte[] bytesMsg){
        // 用获取到的参数构造参数结构
        RptCellDetectResult cellDetectRs =
                new RptCellDetectResult(bytesMsg);
        // 用于转换ARFCN
        ArfcnConvert arfcnConvert = new ArfcnConvert();

        int bandId;
        int ch;
        // TMSI次数统计
        RptCcmReportCountInfo reportCount;
        RptTmsiStatistics tmsiInfo;
        for (int i = 0; i < RptCellDetectResult.CXX_MAX_CELL_NUM; i++) {
            // 设备底层统计消息
            reportCount = cellDetectRs.report_count_infoList.get(i);
            // 循环TMSI
            for (int j = 0; j < RptCcmReportCountInfo.CXX_MAX_TMSI_NUM; j++) {
                // 上报的Tmsi信息
                tmsiInfo = reportCount.tmsi_tableList.get(j);
                // 有效才统计
                if (tmsiInfo.valid != 1) {
                    continue;
                }
                // 获取频带和频点号
                bandId = arfcnConvert.getDLBandIdByArfcn(reportCount.EARFCN);
                ch = arfcnConvert.getDLArfcnNByArfcnF(bandId,reportCount.EARFCN);

                TmsiStatisticsModel item = null;
                // 检查此TMSI是否上报过
                for (int x = 0; x < tmsiStatisticsModelsList.size(); x++) {
                    TmsiStatisticsModel existItem = tmsiStatisticsModelsList.get(x);
                    // PCI、CH、TMSI是key
                    if((existItem.getTmsi() != 0)
                            && existItem.getTmsi() == tmsiInfo.m_tmsi
                            && existItem.getPci() == reportCount.cellID
                            && existItem.getCh() == ch){ // 统计过
                        item = existItem;
                        break;
                    }
                    System.out.println("existItem.getTmsi()=" + existItem.getTmsi()
                            + ",tmsiInfo.m_tmsi=" + tmsiInfo.m_tmsi
                            + ",existItem.getPci()=" + existItem.getPci()
                            + ",reportCount.cellID=" + reportCount.cellID
                            + ",existItem.getCh()=" + existItem.getCh()
                            + ",ch=" + ch);
                }
                System.out.println("================================");

                if (item != null) { // 统计过
                    item.setTmsiCount(item.getTmsiCount() + tmsiInfo.count);
                } else { // 未统计过
                    item = new TmsiStatisticsModel();
                    // TAC TODO:无
                    item.setTac(0);
                    // CID TODO:无
                    item.setCid(0);
                    // PCI
                    item.setPci(reportCount.cellID);
                    // bandId
                    item.setBandId(bandId);
                    // ch
                    item.setCh(ch);
                    // tmsi
                    item.setTmsi(tmsiInfo.m_tmsi);
                    // count累计
                    item.setTmsiCount(item.getTmsiCount() + tmsiInfo.count);

                    // 存入缓存
                    tmsiStatisticsModelsList.add(item);
                }
            }
        }

        // 清空页面数据
        cellBodyLayout.removeAllViews();


        // 页面显示TMSI统计信息
        for (int i = 0; i < tmsiStatisticsModelsList.size(); i++) {
            cellLineLayout = (RelativeLayout) LayoutInflater.from(
                    getActivity()).inflate(R.layout.table_header_tmsi, null);

            TmsiStatisticsModel item = tmsiStatisticsModelsList.get(i);

            // TAC/CID/PCI
            CustTableTextView txt = cellLineLayout.findViewById(R.id.list_1_1);
            StringBuffer sb = new StringBuffer();
            sb.append(item.getTac()).append("/").append(item.getCid())
                    .append("/").append(item.getPci());
            txt.setText(sb.toString());

            // CH/BAND
            txt =  cellLineLayout.findViewById(R.id.list_1_2);
            sb = new StringBuffer();
            sb.append(item.getCh()).append("/").append(item.getBandId());
            txt.setText(sb.toString());

            // TMSI
            txt =  cellLineLayout.findViewById(R.id.list_1_3);
            sb = new StringBuffer();
            sb.append(item.getTmsi());
            txt.setText(sb);

            // 次数
            txt =  cellLineLayout.findViewById(R.id.list_1_4);
            sb = new StringBuffer();
            sb.append(item.getTmsiCount());
            txt.setText(sb);

            cellBodyLayout.addView(cellLineLayout);
        }
    }

    //绑定数据
    private void initData() {
        //初始化标题,
        cellHeaderLayout = (RelativeLayout) LayoutInflater.from(
                getActivity()).inflate(R.layout.table_header_tmsi, null);

        // TAC/CID/PCI
        CustTableTextView title =  cellHeaderLayout.findViewById(R.id.list_1_1);
        title.setText(name[0]);
        title.setTextColor(Color.BLUE);
        // CH/BAND
        title = cellHeaderLayout.findViewById(R.id.list_1_2);
        title.setText(name[1]);
        title.setTextColor(Color.BLUE);
        // TMSI
        title =  cellHeaderLayout.findViewById(R.id.list_1_3);
        title.setText(name[2]);
        title.setTextColor(Color.BLUE);
        // 次数
        title =  cellHeaderLayout.findViewById(R.id.list_1_4);
        title.setText(name[3]);
        title.setTextColor(Color.BLUE);

        // 表格头
        cellLayout.addView(cellHeaderLayout);
        // 表格体
        cellLayout.removeView(cellBodyLayout);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        View view = inflater.inflate(R.layout.fragment_tmsi_table, container, false);
        // 获取显示表格的Layout
        cellLayout = view.findViewById(R.id.cellTable);

        // 初始化数据
        initData();

        return view;
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
