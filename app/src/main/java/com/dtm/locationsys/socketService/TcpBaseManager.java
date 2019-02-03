package com.dtm.locationsys.socketService;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dtm.locationsys.utils.SysLog;

/**
 * TCP Socket基类
 */

public class TcpBaseManager {

    Socket s;
    protected Runnable receiveThread;


    public TcpBaseManager() {
    }

    List<Handler> handlers = new ArrayList<Handler>();

    // static private TcpBaseManager instance;
    private String destIpAddress;

    private int destPort;

    private int reconnectCount = 0;

    private int maxConnectCount = 4;

    protected Boolean running = false;

    public void registerHandler(Handler h) {
        if(handlers.contains(h)) {
            return;
        }
        handlers.add(h);
        System.out.println("=============handlers.size()=" + handlers.size());
    }

    public void deRegisterHandler(Handler h) {
        handlers.remove(h);
    }

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
            s = new Socket();
            s.connect(new InetSocketAddress(destIpAddress, destPort), localPort);

            reconnectCount = 0;
            receiveThreadRun();
            return;
        }
        catch (Exception e) {
            SysLog.e("IO", e.getMessage());
            running = false;
            initial(destIpAddress, destPort, localPort);
            e.printStackTrace();
            s = null;
        }
    }

    private void receiveThreadRun() {
        if (running) {
            return;
        }
        running = true;

        new Thread(receiveThread).start();

    }

    void dealMsg(int msgType, byte[] msg) {
        for (Handler handle : handlers) {
            SysLog.i("==========", handle.toString());

            Message dataMessage = new Message();

            dataMessage.what = msgType;

            dataMessage.obj = msg;
            handle.sendMessage(dataMessage);
        }
    }

    public Socket getSocketHandle() {
        return s;
    }

    public boolean sendLocMessage(String message) {

        try {
            OutputStream ops = s.getOutputStream();

            OutputStreamWriter opsw = new OutputStreamWriter(ops);
            BufferedWriter bw = new BufferedWriter(opsw);

            bw.write(message);
            bw.flush();

        }
        catch (Exception e) {
            e.printStackTrace();
            SysLog.e("TCP Send Message", message);
            SysLog.e("TCP Send Message", e.getMessage());
            return false;
        }
        return true;

    }

    public boolean sendLocMessageByte(byte[] message) {

        try {
            s.getOutputStream().write(message);
            s.getOutputStream().flush();
//             s.getOutputStream().close();

        }
        catch (Exception e) {
            e.printStackTrace();
            SysLog.e("TCP Send Message", message.toString());
            SysLog.e("TCP Send Message", e.getMessage());
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

            }
            catch (IOException e) {
                e.printStackTrace();
                SysLog.e("TCP", e.getMessage());
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
