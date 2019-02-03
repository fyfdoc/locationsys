package com.dtm.locationsys.utils;

/**
 * 消息代码
 */

public class MsgCode {
    public static final int CONN_DISCONNECTED = 1001;// 与主站的连接异常
    public static final int CONN_CONNECT_SUCCESS = 1002; // 连接成功
    public static final int CONN_CONNECT_FAIL = 1003; // 连接失败
    public static final int CONN_CONNECT_EXIST = 1004; // 连接已存在


    public static final int MSG_SEND_SUCCESS = 1101; // 消息发送成功
    public static final int MSG_SEND_FAIL = 1102; // 消息发送失败

    public static final int MSG_TYPE_TEST = 2999; // 测试用消息编码

    // 与基站消息交互的类型
    public static final short MSG_EQU_VERIFY_REQ = 3001; // 用户登录验证请求消息
    public static final short MSG_EQU_VERIFY_RPS = 3002; // 用户登录验证响应消息
    public static final short MSG_SET_PAR_REQ = 3003; // 配置参数请求消息
    public static final short MSG_SET_PAR_RPS = 3004; // 配置参数响应消息
    public static final short MSG_GET_PAR_REQ = 3005; // 获取参数请求消息
    public static final short MSG_GET_PAR_RPS = 3006; // 获取参数响应消息
    public static final short MSG_GET_TMSI_STA_REQ = 3007; // 获取TMSI统计请求消息
    public static final short MSG_GET_TMSI_STA_RPS = 3008; // 获取TMSI统计响应消息
    public static final short MSG_GET_UL_POWER_REQ = 3009; // 获取中标测向请求消息
    public static final short MSG_GET_UL_POWER_RPS = 3010; // 获取中标测向响应消息

    //
}
