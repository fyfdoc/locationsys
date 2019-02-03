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
import android.widget.EditText;
import android.widget.Toast;

import com.dtm.locationsys.R;
import com.dtm.locationsys.activity.MainActivity;
import com.dtm.locationsys.msgDef.SetDirectionTime;
import com.dtm.locationsys.msgDef.SetMasterParasReq;
import com.dtm.locationsys.msgDef.SetMasterParasRps;
import com.dtm.locationsys.socketService.CarTcpManager;
import com.dtm.locationsys.utils.Constants;
import com.dtm.locationsys.utils.MsgCode;
import com.dtm.locationsys.utils.SharedPrefHelper;
import com.dtm.locationsys.utils.SysLog;
import com.dtm.locationsys.utils.WaitDialogUtil;

import java.net.Socket;


/**
 * 配置Fragment
 */
public class ConfigFragment extends Fragment {
    private static final String MyTAG = "ConfigFragment";
    // 参数设置类
    SetMasterParasReq setMasterParasReq;

    // 目的IP
    String destIpaddress = "";
    // 目的端口
    int destPort = 30000;
    // 本地端口
    int localPort = 5000;


    // 测向时长
    String strDirectionTime;

    // 测向时长
    EditText directionTimeEt;



    // 建立到主站的TCP连接线程
    private Runnable MSConnectThread = new Runnable() {
        @Override
        public void run() {
            try {
                SysLog.i(MyTAG, "======MSConnectThread Start========");

                // 和主站建立TCP连接
                CarTcpManager.getSingleton().initial(destIpaddress, destPort, localPort);
                Socket s = CarTcpManager.getSingleton().getSocketHandle();
                // 连接成功
                if (s != null && s.isConnected()) {
                    Message msg1 = mHandler.obtainMessage(MsgCode.CONN_CONNECT_SUCCESS, true);
                    mHandler.sendMessage(msg1);
                    CarTcpManager.getSingleton().registerHandler(mHandler);

                } else {// 连接失败
                    Message msg1 = mHandler.obtainMessage(MsgCode.CONN_CONNECT_FAIL, true);
                    mHandler.sendMessage(msg1);

                }

                SysLog.i(MyTAG, "======MSConnectThread End========");

            } catch (Exception e) {
                WaitDialogUtil.dismissWaitDialog();
                e.printStackTrace();
                SysLog.e(MyTAG, e.getMessage());
            }

        }
    };

    // 回调消息处理Handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                String info = "";
                switch (msg.what) {
                    case MsgCode.CONN_CONNECT_FAIL: // TCP连接失败
                        WaitDialogUtil.dismissWaitDialog();
                        info = "TCP连接失败，请重启程序";
                        MyToast(info);
                        SysLog.e(MyTAG, info);
                        break;
                    case MsgCode.CONN_CONNECT_SUCCESS: // TCP连接成功
                        info = "TCP连接成功";
//                        MyToast(info);
                        SysLog.i(MyTAG, info);
                        // TCP连接成功后，下发参数配置
                        setMsParas();

                        break;
                    case MsgCode.MSG_SEND_SUCCESS: // 消息发送成功
                        info = "参数设置消息发送成功";
//                        MyToast(info);
                        SysLog.i(MyTAG, info);
                        break;
                    case MsgCode.MSG_SEND_FAIL: // 消息发送失败
                        WaitDialogUtil.dismissWaitDialog();
                        info = "消息发送失败";
                        MyToast(info);
                        SysLog.e(MyTAG, info);
                        break;
                    case MsgCode.CONN_CONNECT_EXIST: // TCP连接已建立，直接进行登录验证
                        info = "TCP连接已存在";
                        MyToast(info);
                        break;
                    case MsgCode.CONN_DISCONNECTED: // 与主站的TCP连接异常
                        WaitDialogUtil.dismissWaitDialog();
                        // 接收主站消息时异常
                        info = "接收消息异常";
                        MyToast(info);
                        SysLog.e(MyTAG, info);
                        break;
                    case MsgCode.MSG_SET_PAR_RPS: // 参数设置响应消息
                        WaitDialogUtil.dismissWaitDialog();
                        setMsParasRps((byte[]) msg.obj);
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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        // 测向时长
        directionTimeEt = view.findViewById(R.id.txt_direction_time);
        // 测向时长按钮
        Button btnDirTime = view.findViewById(R.id.btn_direction_time);


        // 测向时长保存按钮
        btnDirTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 获取网络配置参数
                destIpaddress = SharedPrefHelper.getNetworkInfo(
                        getActivity(), Constants.DEST_IP);
                destPort = Integer.valueOf(SharedPrefHelper.getNetworkInfo(
                        getActivity(), Constants.DEST_PORT));
                localPort = Integer.valueOf(SharedPrefHelper.getNetworkInfo(
                        getActivity(), Constants.LOCAL_PORT));

                // 测向时长
                strDirectionTime = directionTimeEt.getText().toString();
                // 校验
                if(strDirectionTime == null || "".equals(strDirectionTime)){
                    MyToast("测向时长不能为空，请输入正确的值");
                    return;
                }
                // 构造测向时长类
                SetDirectionTime setDirectionTime = new SetDirectionTime(
                        0xabcdef14, Integer.valueOf(strDirectionTime));

                // 构造设置参数类
                short msgLength = (short) SetMasterParasReq.BUF_SIZE;
                setMasterParasReq = new SetMasterParasReq(
                        null
                        , null
                        , null
                        , null
                        , null
                        , null
                        , null
                        , null
                        , null
                        , setDirectionTime);

                // 遮罩
                WaitDialogUtil.showWaitDialog(getActivity(), Constants.WAITING_SAVE_INFO);

                // 设置参数下发
                connMasterStation();

            }
        });

        return view;
    }


    /**
     * 建立TCP连接
     */
    private void connMasterStation() {
        try {
            // 与主站建立TCP连接
            Thread connectThread = new Thread(MSConnectThread);
            connectThread.start();

        } catch (Exception e) {
            WaitDialogUtil.dismissWaitDialog();
            e.printStackTrace();
            SysLog.e(MyTAG, e.getMessage());
        }
    }

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
     * 用户登录验证响应处理
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
     * 自定义Toast
     * @param info
     */
    private void MyToast(String info){
        Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();

    }

}
