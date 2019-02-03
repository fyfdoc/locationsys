package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 *  配置辅助信息消息结构
 */

public class SetAssist {
    public static final int BUF_SIZE = 4 + 4 + 4; // 字节数组大小

    public long assist_info_update_flag; //配置辅助信息标识
    public long assist_info_flag;        //为1表示打开辅助信息，为0表示关闭
    public long assist_info;             //辅助信息内容

    /**
     * 构造方法，根据属性值构造类对象
     * @param assist_info_update_flag
     * @param assist_info_flag
     * @param assist_info
     */
    public SetAssist(long assist_info_update_flag, long assist_info_flag, long assist_info) {
        this.assist_info_update_flag = assist_info_update_flag;
        this.assist_info_flag = assist_info_flag;
        this.assist_info = assist_info;

        // 构造byte数组
        byte[] bytes = ByteConvert.uint2Bytes(assist_info_update_flag);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.uint2Bytes(assist_info_flag);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

        bytes = ByteConvert.uint2Bytes(assist_info);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 4, bytes.length);
    }

    /**
     * 构造方法，根据byte[]构造类对象
     * @param bytes
     */
    public SetAssist(byte[] bytes){
        this.assist_info_update_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, 4));

        this.assist_info_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 4, 8));

        this.assist_info = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 8, 12));

    }


    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public String toString() {
        return "SetAssist [assist_info_update_flag=" + assist_info_update_flag
                + ", assist_info_flag=" + assist_info_flag + ", assist_info="
                + assist_info + "]";
    }

}
