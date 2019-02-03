package com.dtm.locationsys.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.dtm.locationsys.R;
import com.dtm.locationsys.msgDef.EquipmentVerifyReq;
import com.dtm.locationsys.msgDef.EquipmentVerifyRps;
import com.dtm.locationsys.socketService.CarTcpManager;
import com.dtm.locationsys.utils.ByteConvert;
import com.dtm.locationsys.utils.MsgCode;
import com.dtm.locationsys.utils.SysLog;
import com.dtm.locationsys.utils.XmlConfigParse;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {
    public final static String TAG = "LoginActivity";

    // 目的IP
    String destIpaddress = "";
    // 目的端口
    int destPort = 30000;
    // 用户名
    String username;
    // 密码
    String password;


    // UI控件
    // 用户名
    EditText userNameEt;
    // 密码
    EditText passwordEt;
    // 目标码
    EditText targetCodeEt;

    // 建立到主站的TCP连接线程
    private Runnable MSConnectThread = new Runnable() {
        @Override
        public void run() {
            try {
                SysLog.i(TAG, "======MSConnectThread Start========");

                destIpaddress = "192.168.42.10";
                destPort = 30000;
                // 和主站建立TCP连接
                CarTcpManager.getSingleton().initial(destIpaddress, destPort, 5000);
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



                SysLog.i(TAG, "======MSConnectThread End========");

            } catch (Exception e) {
                e.printStackTrace();
                SysLog.e(TAG, e.getMessage());
            }

        }
    };

    // 给主站发送验证消息线程
    private Runnable SendverifyMsgThread = new Runnable() {

        @Override
        public void run() {
            // 给主站发送登录消息

            short iValue1 = 123;
            byte[] bValue1 = ByteConvert.short2Bytes(iValue1);
//            int iTmp = ByteConvert.bytes2Int(bValue1);
//            System.out.println("iTmp=" + iTmp);



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
                        info = "TCP连接失败，请重启程序";
                        Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT).show();
                        break;
                    case MsgCode.CONN_CONNECT_SUCCESS: // TCP连接成功
                        info = "TCP连接成功";
                        Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT).show();
                        // TCP连接成功后，登陆处理
                        // 发送登录消息
                        sendEquipmentVerifyMsg();

                        break;
                    case MsgCode.MSG_SEND_SUCCESS: // 消息发送成功
                        info = "消息发送成功";
                        Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT).show();
                        break;
                    case MsgCode.MSG_SEND_FAIL: // 消息发送失败
                        info = "消息发送失败";
                        Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT).show();
                        break;
                    case MsgCode.CONN_CONNECT_EXIST: // TCP连接已建立，直接进行登录验证
                        // 发送登录验证消息
                        sendEquipmentVerifyMsg();
                        break;
                    case MsgCode.CONN_DISCONNECTED: // 与主站的TCP连接异常
                        // 接收主站消息时异常
                        info = "接收消息异常";
                        Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT).show();
                        break;
                    case  MsgCode.MSG_TYPE_TEST: // 接收消息测试
                        int intValue = ByteConvert.bytes2Int((byte[]) msg.obj);
                        Toast.makeText(LoginActivity.this, "接收消息=" + intValue,Toast.LENGTH_SHORT).show();
                        break;
                    case MsgCode.MSG_EQU_VERIFY_RPS: // 用户登录验证响应消息
                        equipmentVerifyRps((byte[]) msg.obj);
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                SysLog.e(TAG, e.getMessage());
            }
        }
    };


    /**
     * 建立TCP连接
     */
    private void connMasterStation() {
        SysLog.i(TAG, "get into login.");

        try {
            // 与主站建立TCP连接
            Thread connectThread = new Thread(MSConnectThread);
            connectThread.start();

        } catch (Exception e) {
            e.printStackTrace();
            SysLog.e("建立TCP连接失步", e.getMessage());
        }


    }

    /**
     * 发送登录消息
     */
    private void sendEquipmentVerifyMsg() {
        // 给主站发送登录消息
        Thread sendverifyMsgThread = new Thread(SendverifyMsgThread);
        sendverifyMsgThread.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // 用户名
        userNameEt = (EditText) findViewById(R.id.txt_user_name);
        // 密码
        passwordEt = (EditText) findViewById(R.id.txt_password);
        // 目标码
        targetCodeEt = (EditText) findViewById(R.id.txt_target_code);
        // 登录按钮
        Button btnLogin = (Button) findViewById(R.id.btn_login);


        // 登录按钮监听事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //xml test
                XmlConfigParse.getSingleton().init();
                String destIp = XmlConfigParse.getSingleton().getNodeValue("DestIp");
                SysLog.i(TAG, "DestIp=" + destIp);

                // 与主站建立TCP连接
                //connMasterStation();


/*
                // 登录成功后跳转到制式页面
                Intent intent = new Intent(LoginActivity.this, ModeActivity.class);
                startActivity(intent);
                finish();
     */
            }
        });
    }


    /**
     * 用户登录验证响应处理
     * @param bytesMsg
     */
    private void equipmentVerifyRps(byte[] bytesMsg) {
        // 将字节数组转换为对象
        EquipmentVerifyRps equipmentVerifyRps = new EquipmentVerifyRps(bytesMsg);

        // 日志信息
        SysLog.i(TAG, equipmentVerifyRps.toString());

        //返回验证结果: 0x00001111; 表示返回用户名密码验证通过；0x00001112表示返回验证码验证通过；
        // 0xffffffff表示验证失败
        if(equipmentVerifyRps.status == 0x00001111) { // 用户名密码验证通过
            Toast.makeText(LoginActivity.this, "用户名密码验证通过", Toast.LENGTH_SHORT).show();
        }else if(equipmentVerifyRps.status == 0x00001112) { //验证码验证通过
            Toast.makeText(LoginActivity.this, "验证码验证通过", Toast.LENGTH_SHORT).show();
        }else if (equipmentVerifyRps.status == 0xffffffff){
            Toast.makeText(LoginActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(LoginActivity.this, "未知错误，验证失败", Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * 测试
     */
    private void loginTest() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // 建立到远程服务器的socket
                    Socket socket = new Socket("192.168.42.236", 30000);
                    // 给服务器端发送消息
                    OutputStream os = socket.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    BufferedWriter bfw = new BufferedWriter(osw);
                    bfw.write("你好！我是Client.\r\n");
                    bfw.flush();

                    // 将Socket的输入流包装成BufferedReader
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    // 进行普通IO操作
                    String line = br.readLine();

                    br.close();
                    bfw.close();
                    socket.close();
                    // 传递数据给主流程
                    Message msg = new Message();
                    msg.obj = line;
                    msg.what = 0x0;
                    mHandler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                    SysLog.e(TAG, e.getMessage());

                    // 传递数据给主流程
                    Message msg = new Message();
                    msg.obj = e.getMessage();
                    msg.what = 0x0;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }
}
