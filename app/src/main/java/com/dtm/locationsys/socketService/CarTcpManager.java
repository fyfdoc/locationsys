package com.dtm.locationsys.socketService;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import android.os.Handler;

import com.dtm.locationsys.event.DataSyncEvent;
import com.dtm.locationsys.utils.ByteConvert;
import com.dtm.locationsys.utils.MsgCode;
import com.dtm.locationsys.utils.SysLog;

import org.greenrobot.eventbus.EventBus;

/**
 * 与基站交互消息类
 */
public class CarTcpManager extends TcpBaseManager {
    private static final String MYTAG = "CarTcpManager";

    private static CarTcpManager instance;

    /**
     * 获取单实例
     */
    public static CarTcpManager getSingleton() {
        if (instance == null) {
            instance = new CarTcpManager();
        }
        return instance;
    }

    /**
     * 接收基站响应消息线程
     */
    private CarTcpManager() {

        receiveThread = new Runnable() {
            public void run() {
                // 接收的总消息大小
                int totalCount = 0;
                // 存储接收到的消息
                byte[] totalRepsMsg = new byte[1024 * 5];
                try {
                    // 输入流
                    InputStream is = s.getInputStream();

                    while(true) {

                        // 接收消息数组
                        byte[] repsMsg = new byte[1024 * 5];
                        int msgCount = is.read(repsMsg);
                        System.out.println("msgCount=" + msgCount);


                        // 输出日志
                        SysLog.i("TCP", "msgCount=" + msgCount);

                        int msgType;
/*
                        // 消息类型
                        int msgType = ByteConvert.bytes2Short(
                                Arrays.copyOfRange(repsMsg, 0, 2));
                        // 消息长度
                        int msgLength = ByteConvert.bytes2Short(
                                Arrays.copyOfRange(repsMsg, 2, 4));

                        // 输出日志
                        SysLog.i("TCP", "Receive Master Msg: msgType=" + msgType
                                + ", msgLength=" + msgLength);
*/
                        long first4 = ByteConvert.bytes2Uint(
                                Arrays.copyOfRange(repsMsg, 0, 4));
                        long second4 = ByteConvert.bytes2Uint(
                                Arrays.copyOfRange(repsMsg, 4, 8));
                        System.out.println("first4=" + first4 + ",second4=" + second4);

                        // 登录验证消息
                        if (first4 == 0 && (second4 == 0x00001111L || second4 == 0xffffffffL)) {
                            // 登录响应消息
                            msgType = MsgCode.MSG_EQU_VERIFY_RPS;
                            // 消息处理
                            dealMsg(msgType, repsMsg);
                        } else { // 参数上报
                            if (totalCount <= 3484) { // 接收消息未完毕
                                System.arraycopy(repsMsg, 0, totalRepsMsg, totalCount, msgCount);
                            }

                            // 当前接收到的消息数
                            totalCount += msgCount;
                            System.out.println("totalCount=" + totalCount);

                            if (totalCount >= 3484) { // 接收消息完毕
                                EventBus.getDefault().post(new DataSyncEvent(totalRepsMsg));
                                // 清空
                                totalRepsMsg = new byte[1024 * 5];
                                totalCount = 0;
                            }


                        }

                        // 消息处理
//                        dealMsg(msgType, repsMsg);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    SysLog.e("TCP", "接收消息异常");
                    SysLog.e("TCP", e.getMessage());
                    CarTcpManager.getSingleton().unInitial();
                    running = false;
                    for (Handler handle : handlers) {
                        handle.sendEmptyMessage(MsgCode.CONN_DISCONNECTED);
                    }
                }

            }
        };

    }
}
