package com.dtm.locationsys.socketService;

import android.os.Handler;

import com.dtm.locationsys.event.DataSyncEvent;
import com.dtm.locationsys.msgDef.EquipmentVerifyRps;
import com.dtm.locationsys.msgDef.RptCellDetectResult;
import com.dtm.locationsys.utils.MsgCode;
import com.dtm.locationsys.utils.SysLog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Arrays;

/**
 * 与基站交互消息类
 */
public class UdpService extends UdpBaseManager {
    private static final String MYTAG = "UdpService";

    private static UdpService instance;

    /**
     * 获取单实例
     */
    public static UdpService getSingleton() {
        if (instance == null) {
            instance = new UdpService();
        }
        return instance;
    }

    /**
     * 接收基站消息线程
     */
    private UdpService() {
        receiveThread = new Runnable() {
            public void run() {
                // 接收消息大小
                int receiveDataSize;
                // 接收到的消息类型
                int msgType;
                // 接收消息数据报
                byte[] receiveData = new byte[1024*4];
                DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);

                try{
                    while (true) {
                        // 接收消息
                        s.receive(packet);
                        receiveDataSize = packet.getLength();
                        SysLog.i(MYTAG, "receiveDataSize=" + receiveDataSize);

                        // 判断消息类型
                        if (receiveDataSize == EquipmentVerifyRps.BUF_SIZE) { // 登录验证消息
                            msgType = MsgCode.MSG_EQU_VERIFY_RPS;
                            // 消息处理
                            dealMsg(msgType, Arrays.copyOfRange(
                                    receiveData, 0, EquipmentVerifyRps.BUF_SIZE));
                        } else if (receiveDataSize == RptCellDetectResult.BUF_SIZE) { // 参数上报
                            EventBus.getDefault().post(new DataSyncEvent(Arrays.copyOfRange(
                                    receiveData, 0, RptCellDetectResult.BUF_SIZE)));

                        } else { // 无效消息
                            SysLog.w(MYTAG, "UDP received invalid messages.");
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    SysLog.e(MYTAG, e.getMessage());
                    UdpService.getSingleton().unInitial();
                    running = false;
                    for (Handler handle : handlers) {
                        handle.sendEmptyMessage(MsgCode.CONN_DISCONNECTED);
                    }
                }
            }
        };

    }
}
