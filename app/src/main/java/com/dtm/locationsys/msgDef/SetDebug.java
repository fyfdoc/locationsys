package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 *  debug配置消息结构
 */

public class SetDebug {
    public static final int DEBUG_MAX = 2;
    public static final int BUF_SIZE = 4 + 4 + DEBUG_MAX*4; // 字节数组大小

    public long debug_info_update_flag; //debug配置标识
    public long debug_info_flag;        //为1表示打开，为0表示关闭
    public int[] debug_info = new int[DEBUG_MAX];

    /**
     * 构造方法，根据属性值构造类对象
     * @param debug_info_update_flag
     * @param debug_info_flag
     * @param debug_info
     */
    public SetDebug(long debug_info_update_flag, long debug_info_flag
            , int[] debug_info) {

        this.debug_info_update_flag = debug_info_update_flag;
        this.debug_info_flag = debug_info_flag;
        this.debug_info = debug_info;

        // 组长byte数组
        byte[] bytes = ByteConvert.uint2Bytes(debug_info_update_flag);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.uint2Bytes(debug_info_flag);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

        int index;
        for(int i = 0; i < DEBUG_MAX; i++){
            index = 4 + (i*4);
            bytes = ByteConvert.int2Bytes(debug_info[i]);
            System.arraycopy(bytes, 0, buf, 0 + 4 + index, bytes.length);
        }

    }

    /**
     * 构造方法，根据byte[]构造类对象
     * @param bytes
     */
    public SetDebug(byte[] bytes){
        this.debug_info_update_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, 4));

        this.debug_info_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 4, 8));

        int index;
        for(int i = 0; i < DEBUG_MAX; i++){
            index = 8 + i*4;
            this.debug_info[i] = ByteConvert.bytes2Int(
                    Arrays.copyOfRange(bytes, index, index+4));
        }

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }


    public String toString() {
        return "SetDebug [debug_info_update_flag="
                + debug_info_update_flag
                + ", debug_info_flag="
                + debug_info_flag
                + ", debug_info="
                + (debug_info != null ? arrayToString(debug_info,
                debug_info.length) : null) + "]";
    }
    private String arrayToString(Object array, int len) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (int i = 0; i < len; i++) {
            if (i > 0)
                buffer.append(", ");
            if (array instanceof int[])
                buffer.append(((int[]) array)[i]);
        }
        buffer.append("]");
        return buffer.toString();
    }

}
