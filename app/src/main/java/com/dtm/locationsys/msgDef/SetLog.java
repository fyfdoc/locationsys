package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 *  配置日志消息结构
 */

public class SetLog {
    public static final int BUF_SIZE = 4 + 4; // 字节数组大小

    public long log_open_update_flag; //配置日志标识
    public long log_open_flag;        //为1表示打开日志上传，为0表示关闭

    /**
     * 构造方法，根据属性值构造类对象
     * @param log_open_update_flag
     * @param log_open_flag
     */
    public SetLog(long log_open_update_flag, long log_open_flag) {
        this.log_open_update_flag = log_open_update_flag;
        this.log_open_flag = log_open_flag;

        // 组长byte数组
        byte[] bytes = ByteConvert.uint2Bytes(log_open_update_flag);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.uint2Bytes(log_open_flag);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

    }

    /**
     * 构造方法，根据byte[]构造类对象
     * @param bytes
     */
    public SetLog(byte[] bytes){
        this.log_open_update_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, 4));

        this.log_open_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 4, 8));
    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public String toString() {
        return "SetLog [log_open_update_flag=" + log_open_update_flag
                + ", log_open_flag=" + log_open_flag + "]";
    }

}
