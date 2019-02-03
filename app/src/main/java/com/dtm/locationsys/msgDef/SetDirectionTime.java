package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 *  配置测向时长消息结构
 */

public class SetDirectionTime {
    public static final int BUF_SIZE = 4 + 4; // 字节数组大小

    public long direction_time_flag;  //配置测向时长
    public int direction_time;       //测向时长（单位ms）

    /**
     * 构造方法，根据属性值构造类对象
     * @param direction_time_flag
     * @param direction_time
     */
    public SetDirectionTime(long direction_time_flag, int direction_time) {
        this.direction_time_flag = direction_time_flag;
        this.direction_time = direction_time;

        // 组长byte数组
        byte[] bytes = ByteConvert.uint2Bytes(direction_time_flag);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.int2Bytes(direction_time);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

    }

    /**
     * 构造方法，根据byte[]构造类对象
     * @param bytes
     */
    public SetDirectionTime(byte[] bytes){
        this.direction_time_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, 4));

        this.direction_time = ByteConvert.bytes2Int(
                Arrays.copyOfRange(bytes, 4, 8));

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public String toString() {
        return "SetDirectionTime [direction_time_flag=" + direction_time_flag
                + ", direction_time=" + direction_time + "]";
    }

}
