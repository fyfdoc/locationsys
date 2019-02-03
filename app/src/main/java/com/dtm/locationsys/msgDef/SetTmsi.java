package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 *  配置TMSI消息结构
 */

public class SetTmsi {
    public static final int BUF_SIZE = 4 + 4 + 4; // 字节数组大小

    public long tmsi_update_flag; //配置TMSI标识
    public long tmsi_flag;        //为1表示配置TMSI
    public long tmsi;             //TMSI值

    /**
     * 构造方法，根据属性值构造类对象
     * @param tmsi_update_flag
     * @param tmsi_flag
     * @param tmsi
     */
    public SetTmsi(long tmsi_update_flag, long tmsi_flag, long tmsi) {
        this.tmsi_update_flag = tmsi_update_flag;
        this.tmsi_flag = tmsi_flag;
        this.tmsi = tmsi;

        // 组长byte数组
        byte[] bytes = ByteConvert.uint2Bytes(tmsi_update_flag);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.uint2Bytes(tmsi_flag);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

        bytes = ByteConvert.uint2Bytes(tmsi);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 4, bytes.length);
    }

    /**
     * 构造方法，根据bytes构造类对象
     * @param bytes
     */
    public SetTmsi(byte[] bytes){
        this.tmsi_update_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, 4));

        this.tmsi_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 4, 8));

        this.tmsi = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 8, 12));
    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public String toString() {
        return "SetTmsi [tmsi_update_flag=" + tmsi_update_flag + ", tmsi_flag="
                + tmsi_flag + ", tmsi=" + tmsi + "]";
    }

}
