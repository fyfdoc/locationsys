package com.dtm.locationsys.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.dtm.locationsys.cust.CustTableTextView;
import com.dtm.locationsys.R;
import com.dtm.locationsys.event.DataSyncEvent;
import com.dtm.locationsys.msgDef.RptCellSearchInfo;
import com.dtm.locationsys.msgDef.RptCellDetectResult;
import com.dtm.locationsys.msgDef.SetCell;
import com.dtm.locationsys.msgDef.SetMasterParasReq;
import com.dtm.locationsys.msgDef.SetMasterParasRps;
import com.dtm.locationsys.socketService.CarTcpManager;
import com.dtm.locationsys.socketService.UdpService;
import com.dtm.locationsys.utils.ArfcnConvert;
import com.dtm.locationsys.utils.Constants;
import com.dtm.locationsys.utils.MsgCode;
import com.dtm.locationsys.utils.SharedPrefHelper;
import com.dtm.locationsys.utils.SysLog;
import com.dtm.locationsys.utils.WaitDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MainPageFragment extends Fragment {
    private static final String MyTAG = "MainPageFragment";

    // 参数设置类型
    SetMasterParasReq setMasterParasReq;
    // 锁小区参数配置类
    SetCell setCell;

    // 参数获取实例
    RptCellDetectResult cellDetectReslut;
    // 守控小区数组，用于缓存获取到的守控小区
    List<RptCellSearchInfo> cellInfoList = new ArrayList<>();

    // 显示数据表的Layout
    private LinearLayout cellLayout;
    private RelativeLayout cellHeaderLayout;
    private RelativeLayout cellLineLayout;
    private String[] name={"发", "LOCK",  "TAC/CID/PCI"
            ,"CH/BAND","RSRP","效果","phase"};

    // 目标号码
    EditText targetNumEt;

    // 回调消息处理Handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                String info;
                switch (msg.what) {
                    case MsgCode.CONN_CONNECT_FAIL: // TCP连接失败
                        WaitDialogUtil.dismissWaitDialog();
                        info = "与主站连接失败，请重启程序";
                        MyToast(info);
                        SysLog.e(MyTAG, info);
                        break;
                    case MsgCode.CONN_CONNECT_SUCCESS: // TCP连接成功
                        info = "TCP连接成功";
//                        MyToast(info);
                        SysLog.i(MyTAG, info);
                        break;
                    case MsgCode.MSG_SEND_SUCCESS: // 消息发送成功
                        info = "参数查询消息发送成功";
//                        MyToast(info);
                        SysLog.i(MyTAG, info);
                        break;
                    case MsgCode.MSG_SEND_FAIL: // 消息发送失败
                        WaitDialogUtil.dismissWaitDialog();
                        info = "消息发送失败";
                        MyToast(info);
                        SysLog.e(MyTAG, info);
                        break;
                    case MsgCode.CONN_DISCONNECTED: // 与主站的连接异常
                        WaitDialogUtil.dismissWaitDialog();
                        // 接收主站消息时异常
                        info = "接收消息异常";
                        MyToast(info);
                        SysLog.e(MyTAG, info);
                        break;
                    case MsgCode.MSG_SET_PAR_RPS: // 参数设置响应消息
                        WaitDialogUtil.dismissWaitDialog();
//                        setMsParasRps((byte[]) msg.obj);
                        SysLog.i(MyTAG, "参数设置响应消息");
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                WaitDialogUtil.dismissWaitDialog();
                e.printStackTrace();
                SysLog.e(MyTAG, e.getMessage());
            }
        }
    };

    // 获取守控小区参数线程
    private Runnable GetCellSearchInfoThread = new Runnable() {
        @Override
        public void run() {
            // 获取连接
            DatagramSocket s = UdpService.getSingleton().getSocketHandle();
            // 连接成功
            if (s == null) {// 连接失败
                Message msg1 = mHandler.obtainMessage(MsgCode.CONN_CONNECT_FAIL, true);
                mHandler.sendMessage(msg1);
                return;
            }
            // 注册Handler
            UdpService.getSingleton().registerHandler(mHandler);

            // 给主站发送登录消息
            boolean rs = UdpService.getSingleton().sendMessageBytes(cellDetectReslut.getBuf());
            if (rs) { // 消息发送成功
                Message msg1 = mHandler.obtainMessage(MsgCode.MSG_SEND_SUCCESS, true);
                mHandler.sendMessage(msg1);
            } else { // 消息发送失败
                Message msg1 = mHandler.obtainMessage(MsgCode.MSG_SEND_FAIL, true);
                mHandler.sendMessage(msg1);
            }
        }
    };

    // 设置小区LOCK线程
    private Runnable SetCellLockThread = new Runnable() {
        @Override
        public void run() {
            // 获取连接
            DatagramSocket s = UdpService.getSingleton().getSocketHandle();
            // 连接成功
            if (s == null) {// 连接失败
                Message msg1 = mHandler.obtainMessage(MsgCode.CONN_CONNECT_FAIL, true);
                mHandler.sendMessage(msg1);
                return;
            }
            // 注册Handler
            UdpService.getSingleton().registerHandler(mHandler);

            // 给主站发送登录消息
            boolean rs = CarTcpManager.getSingleton().sendLocMessageByte(
                    setMasterParasReq.getBuf());
            if (rs) { // 消息发送成功
                Message msg1 = mHandler.obtainMessage(MsgCode.MSG_SEND_SUCCESS, true);
                mHandler.sendMessage(msg1);
            } else { // 消息发送失败
                Message msg1 = mHandler.obtainMessage(MsgCode.MSG_SEND_FAIL, true);
                mHandler.sendMessage(msg1);
            }
        }
    };

    /**
     * 发送获取参数消息
     */
    private void sendGetParasMsg() {
        try {
            // 启动获取参数线程
            Thread getParasThread = new Thread(GetCellSearchInfoThread);
            getParasThread.start();
        } catch (Exception e) {
            WaitDialogUtil.dismissWaitDialog();
            e.printStackTrace();
            SysLog.e(MyTAG, e.getMessage());
        }
    }

    /**
     * 设置小区LOCK
     */
    private void setCellLock() {
        try {
            // 启动获取参数线程
            Thread setCellLockThread = new Thread(SetCellLockThread);
            setCellLockThread.start();
        } catch (Exception e) {
            WaitDialogUtil.dismissWaitDialog();
            e.printStackTrace();
            SysLog.e(MyTAG, e.getMessage());
        }
    }

    /**
     * 设置参数响应处理
     * @param bytesMsg
     */
    private void setMsParasRps(byte[] bytesMsg) {
        // 将字节数组转换为对象
        SetMasterParasRps SetMasterParasRps = new SetMasterParasRps(bytesMsg);

        // 日志信息
        SysLog.i(MyTAG, SetMasterParasRps.toString());

        // 0x00001111:表示参数设置成功; 0xffffffff:表示参数设置失败;
        if(SetMasterParasRps.status == 0x00001111) { // 参数设置成功
            MyToast("参数设置成功");
        }else{
            MyToast("参数设置失败");
        }

    }

    /**
     * 解析参数上报响应消息
     * @param bytesMsg
     */
    private void getCellSearchInfoRps(byte[] bytesMsg){
        // 用获取到的参数构造参数结构
        RptCellDetectResult cellDetectRs =
                new RptCellDetectResult(bytesMsg);
        // 用于转换ARFCN
        ArfcnConvert arfcnConvert = new ArfcnConvert();

        int bandId;
        // 频点号
        int arfcnN;

        // 清空之前的数据
        cellLayout.removeAllViews();
        cellLayout.addView(cellHeaderLayout);
        //守控小区
        RptCellSearchInfo cellInfo;
        for (int i = 0; i < RptCellDetectResult.CXX_MAX_CELL_NUM; i++) {
            // 获取守控小区
            cellInfo = cellDetectRs.cell_search_infoList.get(i);
            if(cellInfo.valid_flag == 0){// 不需要显示的小区
                continue;
            }
            // 缓存守控小区
            cellInfoList.add(cellInfo);

            // 获取布局文件
            cellLineLayout = (RelativeLayout) LayoutInflater.from(
                    getActivity()).inflate(R.layout.table_body_cell, null);
            StringBuffer sb;
            // 发
//            Switch switchItem = cellLineLayout.findViewById(R.id.list_fa);

            // LOCK
            Switch switchLock = cellLineLayout.findViewById(R.id.list_lock);
            // 将本行的PCI设置为LOCK的ID
            switchLock.setId(cellInfo.PCI);

            // Lock按钮切换事件
            switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    // 构造配置PCI/EARFCN类
                    int cell_update_flag = 0xaaaaaaaa;
                    char cell_info_flag = '1';
                    char num_of_cell = '1';
                    short pad = 0;
                    short[] EARFCN_for_PCI = new short[SetCell.CXX_MAX_CELL_NUM];
                    short[] cell_id = new short[SetCell.CXX_MAX_CELL_NUM];

                    // 获取PCI
                    int PCI = compoundButton.getId();

                    // 根据PCI查找选中行的数据
                    for (int i = 0; i < SetCell.CXX_MAX_CELL_NUM; i++) {
                        if(PCI == cellInfoList.get(i).PCI){
                            // 目前只设置单行数据
                            EARFCN_for_PCI[0] = (short)cellInfoList.get(i).EARFCN;
                            cell_id[0] = (short)PCI;
                        };
                    }

                    // 锁
                    if(b){// 开
                        cell_info_flag = '1';
                    }else{// 关
                        cell_info_flag = '0';
                    }

                    // 构造小区参数设置类
                    setCell = new SetCell(cell_update_flag
                            , cell_info_flag
                            , num_of_cell
                            , pad
                            , EARFCN_for_PCI
                            , cell_id);

                    // 构造设置参数类
                    setMasterParasReq = new SetMasterParasReq(
                            setCell
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null);

                    // 遮罩
                    WaitDialogUtil.showWaitDialog(getActivity(), Constants.WAITING_SAVE_INFO);
                    // 设置参数下发
                    setCellLock();
                }
            });

            // TAC/CID/PCI
            CustTableTextView txt = cellLineLayout.findViewById(R.id.list_1_1);
            sb = new StringBuffer();
            sb.append(cellInfo.trackingAreaCode).append("/")
                    .append(cellInfo.cellIdentity).append("/").append(cellInfo.PCI);
            txt.setText(sb.toString());

            // CH/BAND
            txt =  cellLineLayout.findViewById(R.id.list_1_2);
            // 根据频点获取bandid
            bandId = arfcnConvert.getDLBandIdByArfcn(cellInfo.EARFCN);
            // 频点转换为频点号
            arfcnN = arfcnConvert.getDLArfcnNByArfcnF(bandId, Integer.valueOf(cellInfo.EARFCN));
            sb = new StringBuffer(String.valueOf(arfcnN)).append("/").append(bandId);
            txt.setText(sb.toString());

            // RSRP
            txt =  cellLineLayout.findViewById(R.id.list_1_3);
            sb = new StringBuffer(String.valueOf(ArfcnConvert.power2dB(cellInfo.cell_power)));
            txt.setText(sb.toString());

            // 效果
            txt =  cellLineLayout.findViewById(R.id.list_1_4);
            txt.setText("");
/*
            // phase
            txt =  cellLineLayout.findViewById(R.id.list_1_5);
            txt.setText("");
*/
            cellLayout.addView(cellLineLayout);

        }

    }





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        // 更新UI页面数据
        getCellSearchInfoRps(dataSyncEvent.getBytes());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        // 获取显示表格的Layout
        cellLayout = view.findViewById(R.id.cellTable);

        // 目标号码
        targetNumEt = view.findViewById(R.id.txt_target_num);
        // 设置目标码
        targetNumEt.setText(SharedPrefHelper.getLoginInfo(
                getActivity(), Constants.TARGET_CODE));

        // 初始化数据
        initData();

        return view;
    }

    //绑定数据
    private void initData() {
        //初始化标题,
        cellHeaderLayout = (RelativeLayout) LayoutInflater.from(
                getActivity()).inflate(R.layout.table_header_cell, null);

        CustTableTextView title;
        // 发
        title =  cellHeaderLayout.findViewById(R.id.list_fa);
        title.setText(name[0]);
        title.setTextColor(Color.BLUE);

        // LOCK
        title = cellHeaderLayout.findViewById(R.id.list_lock);
        title.setText(name[1]);
        title.setTextColor(Color.BLUE);

        // TAC/CID/PCI
        title =  cellHeaderLayout.findViewById(R.id.list_1_1);
        title.setText(name[2]);
        title.setTextColor(Color.BLUE);
        // CH/BAND
        title = cellHeaderLayout.findViewById(R.id.list_1_2);
        title.setText(name[3]);
        title.setTextColor(Color.BLUE);
        // RSRP
        title =  cellHeaderLayout.findViewById(R.id.list_1_3);
        title.setText(name[4]);
        title.setTextColor(Color.BLUE);
        // 效果
        title =  cellHeaderLayout.findViewById(R.id.list_1_4);
        title.setText(name[5]);
        title.setTextColor(Color.BLUE);

/*
        // phase
        title =  cellHeaderLayout.findViewById(R.id.list_1_5);
        title.setText(name[6]);
        title.setTextColor(Color.BLUE);
*/
        cellLayout.addView(cellHeaderLayout);
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
