package com.dtm.locationsys.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dtm.locationsys.R;
import com.dtm.locationsys.cust.CellSearchAdapter;
import com.dtm.locationsys.cust.FragmentItf;
import com.dtm.locationsys.event.DataSyncEvent;
import com.dtm.locationsys.msgDef.RptCellSearchInfo;
import com.dtm.locationsys.msgDef.RptCellDetectResult;
import com.dtm.locationsys.msgDef.SetCell;
import com.dtm.locationsys.msgDef.SetMasterParasReq;
import com.dtm.locationsys.socketService.UdpService;
import com.dtm.locationsys.utils.ByteConvert;
import com.dtm.locationsys.utils.Constants;
import com.dtm.locationsys.utils.MsgCode;
import com.dtm.locationsys.utils.SysLog;
import com.dtm.locationsys.utils.WaitDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.DatagramSocket;

/**
 * 守控小区.
 */
public class CellSearchFragment extends Fragment implements FragmentItf {
    private static final String MyTAG = "CellSearchFragment";
    // 参数设置类型
    SetMasterParasReq setMasterParasReq;
    // 锁小区参数配置类
    SetCell setCell;

    // 守控小区
    private RecyclerView mRecyclerView;
    // 守控小区适配器
    private CellSearchAdapter mCellSearchAdapter;

    // 回调消息处理Handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                String info;
                switch (msg.what) {
                    case MsgCode.CONN_CONNECT_FAIL: // 连接失败
                        WaitDialogUtil.dismissWaitDialog();
                        info = "与主站连接失败，请重启程序";
                        MyToast(info);
                        SysLog.e(MyTAG, info);
                        break;
                    case MsgCode.CONN_CONNECT_SUCCESS: // 连接成功
                        info = "与主站连接成功";
//                        MyToast(info);
                        SysLog.i(MyTAG, info);
                        break;
                    case MsgCode.MSG_SEND_SUCCESS: // 消息发送成功
                        WaitDialogUtil.dismissWaitDialog();
                        info = "参数消息发送成功";
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
            boolean rs = UdpService.getSingleton().sendMessageBytes(setMasterParasReq.getBuf());
            if (rs) { // 消息发送成功
                Message msg1 = mHandler.obtainMessage(MsgCode.MSG_SEND_SUCCESS, true);
                mHandler.sendMessage(msg1);
            } else { // 消息发送失败
                Message msg1 = mHandler.obtainMessage(MsgCode.MSG_SEND_FAIL, true);
                mHandler.sendMessage(msg1);
            }
        }
    };

    public CellSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cell_search, container, false);

        mRecyclerView = view.findViewById(R.id.search_cells_recycler_view);
        // 设置adapter
        if (null == mCellSearchAdapter) {
            createRecyclerViewAdapter();
        }
        // 设置表格分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()
                , DividerItemDecoration.VERTICAL));
        // 设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Inflate the layout for this fragment
        return view;
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
        updateCellSearchGrid(dataSyncEvent.getBytes());
    }

    /**
     * 锁频开关回调方法
     */
    @Override
    public void callbakHandler() {
        // TODO:目前只下发锁频数据？

        // 构造配置PCI/EARFCN类
        int cell_update_flag = 0xaaaaaaaa;
        char cell_info_flag = '1'; // 为1表示打开锁定功能，为0标识关闭锁定功能
        char num_of_cell = ByteConvert.int2char(mCellSearchAdapter.mLockedCellSearchList.size());
        short pad = 0;
        short[] EARFCN_for_PCI = new short[SetCell.CXX_MAX_CELL_NUM];
        short[] cell_id = new short[SetCell.CXX_MAX_CELL_NUM];

        int index = 0;
        for (RptCellSearchInfo item : mCellSearchAdapter.mLockedCellSearchList) {
            if (index >= SetCell.CXX_MAX_CELL_NUM) {
                break;
            }
            EARFCN_for_PCI[index] = (short) item.getEARFCN();
            cell_id[index] = (short) item.getPCI();
            index++;
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
     * 更新数据
     * @param bytes
     */
    private void updateCellSearchGrid(byte[] bytes) {
        // 用获取到的参数构造参数结构
        RptCellDetectResult cellDetectResult =
                new RptCellDetectResult(bytes);
        // 守控小区列表
        mCellSearchAdapter.update(cellDetectResult.cell_search_infoList);
    }

    /**
     * 初始化adapter
     */
    private void createRecyclerViewAdapter() {
        mCellSearchAdapter = new CellSearchAdapter(this);
        mRecyclerView.setAdapter(mCellSearchAdapter);
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
