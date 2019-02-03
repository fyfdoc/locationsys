package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 *  配置频率信息消息结构
 */

public class SetFreqBand {
    public static final int BUF_SIZE = 4 + 2 + 2; // 字节数组大小

    public long freqband_update_flag; //配置频率信息标识
    public short earfcn_info_flag;   //开关
    public short EARFCN;             //频点号


    /**
     * 构造方法，根据属性值构造类对象
     * @param freqband_update_flag
     * @param earfcn_info_flag
     * @param EARFCN
     */
    public SetFreqBand(long freqband_update_flag
            , short earfcn_info_flag, short EARFCN) {

        this.freqband_update_flag = freqband_update_flag;
        this.earfcn_info_flag = earfcn_info_flag;
        this.EARFCN = EARFCN;

        // 组长byte数组
        byte[] bytes = ByteConvert.uint2Bytes(freqband_update_flag);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.short2Bytes(earfcn_info_flag);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

        bytes = ByteConvert.short2Bytes(EARFCN);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 2, bytes.length);

    }

    /**
     * 构造方法，根据byte[]构造类对象
     * @param bytes
     */
    public SetFreqBand(byte[] bytes){
        this.freqband_update_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, 4));

        this.earfcn_info_flag = ByteConvert.bytes2Short(
                Arrays.copyOfRange(bytes, 4, 6));

        this.EARFCN = ByteConvert.bytes2Short(
                Arrays.copyOfRange(bytes, 6, 8));

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public String toString() {
        return "SetFreqBand [freqband_update_flag=" + freqband_update_flag
                + ", earfcn_info_flag=" + earfcn_info_flag + ", EARFCN="
                + EARFCN + "]";
    }

}
