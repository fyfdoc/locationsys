package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 * 登录请求消息定义
 */

public class EquipmentVerifyReq {
    // 字节数组大小
    public static final int BUF_SIZE = 4 + 2*32 + 2*32 + 2*16 + 4;

    // 0xaaaaaaa1: 表示请求用户名密码验证;
    // 0xaaaaaaa2: 表示请求验证码验证;
    // 0xaaaaaaaa: 表示用户名密码和验证码同时验证;
    // Uint
    public long msgFlag;
    // char[32]
    public char[] username; //用户名
    // char[32]
    public char[] password; //密码
    // char[16]
    public char[] phoneNum; //待测手机号

//    public long scrambleCode; //扰码
    // Uint
    public long verifyCode; //验证码

    /**
     * 构造方法,根据属性值构造类对象
     * @param msgFlag
     * @param username
     * @param password
     * @param phoneNum
     * @param verifyCode
     */
     public EquipmentVerifyReq(long msgFlag, char[] username
            , char[] password, char[] phoneNum, long verifyCode) {

        this.msgFlag = msgFlag;
        this.username = username;
        this.password = password;
        this.phoneNum = phoneNum;
//        this.scrambleCode = scrambleCode;
        this.verifyCode = verifyCode;

        byte[] bytes;
        bytes = ByteConvert.uint2Bytes(msgFlag);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.chars2Bytes(username);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

        bytes = ByteConvert.chars2Bytes(password);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 2*32, bytes.length);

        bytes = ByteConvert.chars2Bytes(phoneNum);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 2*32 + 2*32, bytes.length);

//        bytes = ByteConvert.int2Bytes(scrambleCode);
//        System.arraycopy(bytes, 0, buf, 0 + 2 + 2 + 4 + 2*32 + 2*32 + 2*16, bytes.length);

        bytes = ByteConvert.uint2Bytes(verifyCode);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 2*32 + 2*32 + 2*16, bytes.length);

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    /**
     * 构造方法，根据byte[]构造类对象
     * @param byteMsg
     */
    public EquipmentVerifyReq(byte[] byteMsg) {
        msgFlag = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                byteMsg
                , 0
                , 4));

        username = ByteConvert.bytes2Chars(Arrays.copyOfRange(
                byteMsg
                , 4
                , 2*32));

        password = ByteConvert.bytes2Chars(Arrays.copyOfRange(
                byteMsg
                , 4 + 2*32
                , 4 + 2*32 + 2*32));

        phoneNum = ByteConvert.bytes2Chars(Arrays.copyOfRange(
                byteMsg
                , 4 + 2*32 + 2*32
                , 4 + 2*32 + 2*32 + 2*16));
//        scrambleCode = ByteConvert.bytes2Int(Arrays.copyOfRange(byteMsg, 8 + 2*32 + 2*32 + 2*16, 8 + 2*32 + 2*32 + 2*16 + 4));
        verifyCode = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                byteMsg
                , 4 + 2*32 + 2*32 + 2*16
                , 4 + 2*32 + 2*32 + 2*16 + 4));


    }

    public String toString() {
        return "EquipmentVerifyReq [msgFlag=" + msgFlag + ", username=" + String.valueOf(username)
                + ", password=" + String.valueOf(password) + ", phoneNum=" + String.valueOf(phoneNum)
                + ", verifyCode="
                + verifyCode + "]";
    }

}
