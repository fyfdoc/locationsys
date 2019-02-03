package com.dtm.locationsys.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dtm.locationsys.R;
import com.dtm.locationsys.activity.ModeActivity;
import com.dtm.locationsys.msgDef.EquipmentVerifyReq;
import com.dtm.locationsys.msgDef.EquipmentVerifyRps;
import com.dtm.locationsys.socketService.UdpService;
import com.dtm.locationsys.utils.Constants;
import com.dtm.locationsys.utils.MsgCode;
import com.dtm.locationsys.utils.SharedPrefHelper;
import com.dtm.locationsys.utils.SysLog;
import com.dtm.locationsys.utils.WaitDialogUtil;

import java.net.DatagramSocket;
import java.util.Locale;

/**
 * 登录Fragment
 */
public class LoginFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "SECTION_NUMBER";
    public final static String MyTAG = "LoginFragment";

    TextToSpeech tts;

    // 目的IP
    String destIpaddress = "";
    // 目的端口
    int destPort;
    // 本地端口
    int localPort;
    // 用户名
    String username;
    // 密码
    String password;
    // 目标码
    String targetCode;
    // 验证码
    String verifyCode;
    // 扰码
//    String scrambleCode;

    // UI控件
    // 用户名
    EditText userNameEt;
    // 密码
    EditText passwordEt;
    // 目标码
    EditText targetCodeEt;
    // 验证码
    EditText verifyCodeEt;

    // 建立到主站的TCP连接线程
    private Runnable MSConnectThread = new Runnable() {
        @Override
        public void run() {
            try {
                SysLog.i(MyTAG, "======MSConnectThread Start========");

                // 和主站建立连接
                DatagramSocket s;
//                s = UdpService.getSingleton().getSocketHandle();
//                if(s == null) {
                    UdpService.getSingleton().initial(destIpaddress, destPort, localPort);
                    s = UdpService.getSingleton().getSocketHandle();
//                }
                // 连接成功
                if (s != null && s.isConnected()) {
                    Message msg1 = mHandler.obtainMessage(MsgCode.CONN_CONNECT_SUCCESS, true);
                    mHandler.sendMessage(msg1);
                    UdpService.getSingleton().registerHandler(mHandler);
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

    // 给主站发送验证消息线程
    private Runnable SendverifyMsgThread = new Runnable() {

        @Override
        public void run() {
            // 给主站发送登录验证消息
            // 0xaaaaaaa1: 表示请求用户名密码验证;
            // 0xaaaaaaa2: 表示请求验证码验证;
            // 0xaaaaaaaa:表示用户名密码和验证码同时验证;
            EquipmentVerifyReq equipmentVerifyReq = new EquipmentVerifyReq(
                    0xaaaaaaaa
                    , username.toCharArray()
                    , password.toCharArray()
                    , targetCode.toCharArray()
                    , Integer.valueOf(verifyCode)
            );

            boolean rs = UdpService.getSingleton().sendMessageBytes(
                    equipmentVerifyReq.getBuf());
            if (rs) { // 消息发送成功
                Message msg1 = mHandler.obtainMessage(MsgCode.MSG_SEND_SUCCESS, true);
                mHandler.sendMessage(msg1);
            } else { // 消息发送失败
                Message msg1 = mHandler.obtainMessage(MsgCode.MSG_SEND_FAIL, true);
                mHandler.sendMessage(msg1);
            }
        }
    };

    // 回调消息处理Handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                String info;
                switch (msg.what) {
                    case MsgCode.CONN_CONNECT_FAIL: // Socket连接失败
                        WaitDialogUtil.dismissWaitDialog();
                        info = "与主站连接失败，请确认网络配置";
                        MyToast(info);
                        SysLog.e(MyTAG, info);
                        break;
                    case MsgCode.CONN_CONNECT_SUCCESS: // Socket连接成功
                        info = "与主站连接成功";
                        MyToast(info);
                        SysLog.i(MyTAG, info);
                        // TCP连接成功后，发送登录消息
                        sendEquipmentVerifyMsg();
                        break;
                    case MsgCode.MSG_SEND_SUCCESS: // 消息发送成功
//                        info = "消息发送成功";
//                        MyToast(info);
                        SysLog.i(MyTAG, "消息发送成功");
                        break;
                    case MsgCode.MSG_SEND_FAIL: // 消息发送失败
                        WaitDialogUtil.dismissWaitDialog();
                        info = "消息发送失败";
                        MyToast(info);
                        SysLog.e(MyTAG, info);
                        break;
                    case MsgCode.CONN_DISCONNECTED: // 与主站的连接异常，无法接收消息
                        WaitDialogUtil.dismissWaitDialog();
                        // 接收主站消息时异常
                        info = "接收消息异常，连接已断开";
                        MyToast(info);
                        SysLog.e(MyTAG, info);
                        break;
                    case MsgCode.MSG_EQU_VERIFY_RPS: // 用户登录验证响应消息
                        WaitDialogUtil.dismissWaitDialog();
                        equipmentVerifyRps((byte[]) msg.obj);
                        SysLog.i(MyTAG, "用户登录验证响应消息");
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

    /**
     * 与主站建立连接
     */
    private void connMasterStation() {
        try {
            // 与主站建立连接
            Thread connectThread = new Thread(MSConnectThread);
            connectThread.start();
        } catch (Exception e) {
            WaitDialogUtil.dismissWaitDialog();
            e.printStackTrace();
            SysLog.e("建立连接失败", e.getMessage());
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

    /**
     * 用户登录验证响应处理
     * @param bytesMsg
     */
    private void equipmentVerifyRps(byte[] bytesMsg) {
        // 将字节数组转换为对象
        EquipmentVerifyRps equipmentVerifyRps = new EquipmentVerifyRps(bytesMsg);
        // 日志信息
        SysLog.i(MyTAG, equipmentVerifyRps.toString());

        //0x00001111:验证通过  0xffffffff:表示验证失败
        if(equipmentVerifyRps.status == 0x00001111L) { // 用户名密码验证通过
//            MyToast("用户名密码验证通过");
            // 进入制式页面
            Intent intent = new Intent(getActivity(), ModeActivity.class);
            startActivity(intent);
        }else{
            MyToast("用户验证失败，请输入正确的登录信息");
            return;
        }
    }

    // 初始化TTS类
    class MyOnInitListener implements OnInitListener {
        @Override
        public void onInit(int status) {
            if(status == TextToSpeech.SUCCESS){
                // 设置地区（语言）
                int rs = tts.setLanguage(Locale.CHINA);
                if(rs != TextToSpeech.LANG_COUNTRY_AVAILABLE
                        && rs != TextToSpeech.LANG_AVAILABLE ) {
                    MyToast("没有安装中文TTS，或者TTS设置有误");
                }
                // 设置语速
                tts.setSpeechRate((float) 1.6);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化TTS
        if(tts == null){
            tts = new TextToSpeech(getActivity(), new MyOnInitListener());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        // 用户名
        userNameEt = view.findViewById(R.id.txt_user_name);
        // 密码
        passwordEt = view.findViewById(R.id.txt_password);
        // 目标码
        targetCodeEt = view.findViewById(R.id.txt_target_code);
        // 验证码
        verifyCodeEt = view.findViewById(R.id.txt_verification_code);
        // 登录按钮
        Button btnLogin =  view.findViewById(R.id.btn_login);
        // 语音测试按钮
        Button btnTtsTest = view.findViewById(R.id.btn_tts_test);

        // 获取上次保存的用户信息
        username = SharedPrefHelper.getLoginInfo(getActivity(), Constants.USERNAME);
        password = SharedPrefHelper.getLoginInfo(getActivity(), Constants.PASSWORD);
        targetCode = SharedPrefHelper.getLoginInfo(getActivity(), Constants.TARGET_CODE);
        verifyCode = SharedPrefHelper.getLoginInfo(getActivity(), Constants.VERIFY_CODE);
//        scrambleCode = SharedPrefHelper.getLoginInfo(getActivity(), Constants.SRCAMBLE_CODE);

        // TODO:测试数据====================start
        if ("".equals(username)){
            username = "1";
        }
        if ("".equals(password)){
            password = "2";
        }
        if ("".equals(targetCode)){
            targetCode = "3";
        }
        if ("".equals(verifyCode)){
            verifyCode = "4";
        }
//        if ("".equals(scrambleCode)){
//            scrambleCode = "123";
//        }
        // TODO:测试数据====================end

        // 设置系统保存的用户登录信息
        userNameEt.setText(username);
        passwordEt.setText(password);
        targetCodeEt.setText(targetCode);
        verifyCodeEt.setText(verifyCode);

        // 登录按钮监听事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取用户输入的登录信息
                username = userNameEt.getText().toString();
                password = passwordEt.getText().toString();
                targetCode = targetCodeEt.getText().toString();
                verifyCode = verifyCodeEt.getText().toString();
                // 扰码
//                scrambleCode = SharedPrefHelper.getLoginInfo(
//                        getActivity(), Constants.SRCAMBLE_CODE);;

                if(username == null || "".equals(username)){
                    MyToast("用户名为空，请输入用户名");
                    return;
                }
                if(password == null || "".equals(password)){
                    MyToast("密码为空，请输入密码");
                    return;
                }
                if(targetCode == null || "".equals(targetCode)){
                    MyToast("目标码为空，请输入目标码");
                    return;
                }
                if(verifyCode == null || "".equals(verifyCode)){
                    MyToast("校验码为空，请输入校验码");
                    return;
                }
//                if(scrambleCode == null || "".equals(scrambleCode)){
//                    MyToast("扰码为空，请在配置页面配置扰码后再登录");
//                    return;
//                }

                // 保存登录信息
                SharedPrefHelper.setLoginInfo(getActivity(), Constants.USERNAME, username);
                SharedPrefHelper.setLoginInfo(getActivity(), Constants.PASSWORD, password);
                SharedPrefHelper.setLoginInfo(getActivity(), Constants.TARGET_CODE, targetCode);
                SharedPrefHelper.setLoginInfo(getActivity(), Constants.VERIFY_CODE, verifyCode);

                // 获取网络配置参数
                destIpaddress = SharedPrefHelper.getNetworkInfo(
                        getActivity(), Constants.DEST_IP);

                String strTmp = SharedPrefHelper.getNetworkInfo(
                        getActivity(), Constants.DEST_PORT);
                if("".equals(strTmp)){
                    destPort = 0;
                }else {
                    destPort = Integer.valueOf(strTmp);
                }
                strTmp = SharedPrefHelper.getNetworkInfo(
                        getActivity(), Constants.LOCAL_PORT);
                if("".equals(strTmp)){
                    localPort = 0;
                } else {
                    localPort = Integer.valueOf(strTmp);
                }

                SysLog.i(MyTAG, "DestIp=" + destIpaddress + ",destPort=" + destPort
                        + ",localPort=" + localPort);

                if(destIpaddress == null || destIpaddress.equals("")){
                    MyToast("目标地址为空，请先配置网络参数登录");
                    return;
                }
                if(destPort == 0){
                    MyToast("目标端口为空，请先配置网络参数再登录");
                    return;
                }
                if(localPort == 0){
                    MyToast("本地端口为空，请先配置网络参数再登录");
                    return;
                }

                // 遮罩
                WaitDialogUtil.showWaitDialog(getActivity(), Constants.WAITING_LOGIN_INFO);

                // 与主站建立连接
                connMasterStation();
            }
        });

        // 语音测试按钮
        btnTtsTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String speakText = "你已进入小区1";
                // 判断是否支持某地区的语言
                int rlt = tts.isLanguageAvailable(Locale.CHINA);
                if(rlt == TextToSpeech.LANG_AVAILABLE
                        || rlt == TextToSpeech.LANG_COUNTRY_AVAILABLE){
                    tts.speak(speakText, TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    MyToast("无法播放语音");
                }
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
