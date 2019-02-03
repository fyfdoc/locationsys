package com.dtm.locationsys.socketService;


import android.os.Handler;
import android.os.Message;

import com.dtm.locationsys.utils.SysLog;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * UDP Socket基类
 */

public class UdpBaseManager {
    private static final String MYTAG = "UdpBaseManager";

    DatagramSocket s;

    protected Runnable receiveThread;

    List<Handler> handlers = new ArrayList<Handler>();

    private String destIpAddress;

    private int destPort;

    private int reconnectCount = 0;

    private int maxConnectCount = 4;

    protected Boolean running = false;

    public UdpBaseManager() {
    }

    /**
     * 注册Handler
     * @param h
     */
    public void registerHandler(Handler h) {
        if(handlers.contains(h)) {
            return;
        }
        handlers.add(h);
        System.out.println("=============handlers.size()=" + handlers.size());
    }

    /**
     * 删除Handler
     * @param h
     */
    public void deRegisterHandler(Handler h) {
        handlers.remove(h);
    }

    /**
     * 初始化方法
     * @param ipAddress
     * @param dPort
     * @param localPort
     */
    public void initial(String ipAddress, int dPort, int localPort) {
        unInitial();
        reconnectCount++;
        if (reconnectCount > maxConnectCount) {
            reconnectCount = 0;
            return;
        }
        destIpAddress = ipAddress;
        destPort = dPort;
        try {
            s = new DatagramSocket(localPort);
            s.connect(new InetSocketAddress(destIpAddress, destPort));
            reconnectCount = 0;
            receiveThreadRun();
            return;
        } catch (Exception e) {
            SysLog.e(MYTAG, e.getMessage());
            running = false;
            initial(destIpAddress, destPort, localPort);
            e.printStackTrace();
            s = null;
        }
    }

    /**
     * 接收消息线程
     */
    private void receiveThreadRun() {
        if (running) {
            return;
        }
        running = true;

        new Thread(receiveThread).start();
    }

    /**
     * 消息接收后分发给Handler
     * @param msgType
     * @param msg
     */
    void dealMsg(int msgType, byte[] msg) {
        for (Handler handle : handlers) {
            SysLog.i(MYTAG, handle.toString());

            Message dataMessage = new Message();
            dataMessage.what = msgType;
            dataMessage.obj = msg;
            handle.sendMessage(dataMessage);
        }
    }

    /**
     * 获取连接
     * @return
     */
    public DatagramSocket getSocketHandle() {
        return s;
    }

    /**
     * 发送消息
     * @param message
     * @return
     */
    public boolean sendMessageBytes(byte[] message) {
        try {
            DatagramPacket packet = new DatagramPacket(message, message.length);
            s.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
            SysLog.e(MYTAG, message.toString());
            SysLog.e(MYTAG, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     *关闭Socket
     */
    public void unInitial() {
        if (s != null) {
            try {
                s.close();
            }catch (Exception e) {
                e.printStackTrace();
                SysLog.e(MYTAG, e.getMessage());
            } finally {
                s = null;
            }
        }
        // 接收线程标志初始化为false
        running = false;
        // 清空Handler数组 TODO: 单实例时不应该清除，重建连接后之前Handler仍有效
        // handlers.clear();
    }
}
