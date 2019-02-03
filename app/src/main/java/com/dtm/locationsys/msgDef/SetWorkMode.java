package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 *  配置系统工作模式消息结构
 */

public class SetWorkMode {
    public static final int BUF_SIZE = 4 + 4; // 字节数组大小

    public long work_mode_flag; //配置系统工作模式
    public int work_mode;      //0：车载模式；1：单兵模式

    /**
     * 构造方法，根据属性值构造类对象
     * @param work_mode_flag
     * @param work_mode
     */
    public SetWorkMode(long work_mode_flag, int work_mode) {
        this.work_mode_flag = work_mode_flag;
        this.work_mode = work_mode;

        // 组长byte数组
        byte[] bytes = ByteConvert.uint2Bytes(work_mode_flag);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.int2Bytes(work_mode);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

    }

    /**
     * 构造方法，根据byte[]构造类对象
     * @param bytes
     */
    public SetWorkMode(byte[] bytes){
        this.work_mode_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, 4));

        this.work_mode = ByteConvert.bytes2Int(
                Arrays.copyOfRange(bytes, 4, 8));

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public String toString() {
        return "SetWorkMode [work_mode_flag=" + work_mode_flag + ", work_mode="
                + work_mode + "]";
    }

}
