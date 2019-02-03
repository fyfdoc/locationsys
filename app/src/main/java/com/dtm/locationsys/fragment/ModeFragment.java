package com.dtm.locationsys.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.dtm.locationsys.R;
import com.dtm.locationsys.activity.MainActivity;
import com.dtm.locationsys.msgDef.SetFrameType;
import com.dtm.locationsys.msgDef.SetMasterParasReq;
import com.dtm.locationsys.msgDef.SetOperator;
import com.dtm.locationsys.socketService.UdpService;
import com.dtm.locationsys.utils.Constants;
import com.dtm.locationsys.utils.MsgCode;
import com.dtm.locationsys.utils.SysLog;
import com.dtm.locationsys.utils.WaitDialogUtil;

import java.net.DatagramSocket;

/**
 * 制式
 */
public class ModeFragment extends Fragment {
    private static final String MyTAG = "ModeFragment";
    // 参数设置类
    SetMasterParasReq setMasterParasReq;

    // 制式
    String strTddFdd;
    // 运营商
    String strOperator;

    // 制式
    Spinner tddFddSp;
    // 运营商
    Spinner operatorSp;

    Button btnConfirm;

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
                        info = "连接成功";
//                        MyToast(info);
//                        SysLog.i(MyTAG, info);
                        break;
                    case MsgCode.MSG_SEND_SUCCESS: // 消息发送成功
                        WaitDialogUtil.dismissWaitDialog();
                        info = "参数设置消息发送成功";
//                        MyToast(info);
                        setMsParasRps();
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

    // 给主站发送参数设置消息线程
    private Runnable SetMsParasThread = new Runnable() {
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
            boolean rs = UdpService.getSingleton().sendMessageBytes(
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
     * 发送参数设置消息
     */
    private void setMsParas() {
        try {
            // 启动参数设置线程
            Thread setMsParasThread = new Thread(SetMsParasThread);
            setMsParasThread.start();
        } catch (Exception e) {
            WaitDialogUtil.dismissWaitDialog();
            e.printStackTrace();
            SysLog.e(MyTAG, e.getMessage());
        }
    }

    /**
     * 设置参数响应处理
     */
    private void setMsParasRps() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mode, container, false);

        // 制式
        tddFddSp = view.findViewById(R.id.spinnerTddFdd);
        // 运营商
        operatorSp = view.findViewById(R.id.spinnerOperator);
        //确定按钮
        btnConfirm = view.findViewById(R.id.btn_confirm);

        // 确定按钮监听事件
        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // ==================Test=========
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                // ==================Test=========

/*
                // 制式
                strTddFdd = tddFddSp.getSelectedItem().toString();
                // 运营商
                strOperator = operatorSp.getSelectedItem().toString();

                // 设置制式
                long frame_type_flag = 0xabcdef12L;
                int frame_type = 1;
                if("TDD".equals(strTddFdd)){
                    frame_type = 0;
                }
                // 构造制式参数类
                SetFrameType setFrameType = new SetFrameType(frame_type_flag, frame_type);

                // 运营商
                long operator_flag = 0xabcdef13L;
                int operator_type = 0;
                if("中国移动".equals(strOperator)){
                    operator_type = 0;
                }else if("中国联通".equals(strOperator)){
                    operator_type = 1;
                } else if("中国电信".equals(strOperator)){
                    operator_type = 2;
                }
                // 构造运营商参数类
                SetOperator setOperator = new SetOperator(operator_flag, operator_type);

                // 构造设置参数类
                setMasterParasReq = new SetMasterParasReq(
                        null
                        , null
                        , null
                        , null
                        , null
                        , null
                        , null
                        , setFrameType
                        , setOperator
                        , null);

                // 遮罩
                WaitDialogUtil.showWaitDialog(getActivity(), Constants.WAITING_SAVE_INFO);

                // 设置参数下发
                setMsParas();

                */
            }




        });

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
