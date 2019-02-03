package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 *  配置小区工作制式消息结构
 */

public class SetFrameType {
    public static final int BUF_SIZE = 4 + 4; // 字节数组大小

    public long frame_type_flag; //配置小区工作制式
    public int frame_type;      //0：TDD；1：FDD

    /**
     * 构造方法，根据属性值构造类对象
     * @param frame_type_flag
     * @param frame_type
     */
    public SetFrameType(long frame_type_flag, int frame_type) {
        this.frame_type_flag = frame_type_flag;
        this.frame_type = frame_type;

        // 组长byte数组
        byte[] bytes = ByteConvert.uint2Bytes(frame_type_flag);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.int2Bytes(frame_type);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

    }

    /**
     * 构造方法，根据byte[]构造类对象
     * @param bytes
     */
    public SetFrameType(byte[] bytes){
        this.frame_type_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, 4));

        this.frame_type = ByteConvert.bytes2Int(
                Arrays.copyOfRange(bytes, 4, 8));

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public String toString() {
        return "SetFrameType [frame_type_flag=" + frame_type_flag
                + ", frame_type=" + frame_type + "]";
    }

}
