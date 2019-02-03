package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 *  配置运营商消息结构
 */

public class SetOperator {
    public static final int BUF_SIZE = 4 + 4; // 字节数组大小

    public long operator_flag;  //配置运营商信息
    public int operator_type;  //0:中国移动；1：中国联通；2：中国电信

    /**
     * 构造方法，根据属性值构造类对象
     * @param operator_flag
     * @param operator_type
     */
    public SetOperator(long operator_flag, int operator_type) {
        this.operator_flag = operator_flag;
        this.operator_type = operator_type;

        // 组长byte数组
        byte[] bytes = ByteConvert.uint2Bytes(operator_flag);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.int2Bytes(operator_type);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

    }

    /**
     * 构造方法，根据byte[]构造类对象
     * @param bytes
     */
    public SetOperator(byte[] bytes){
        this.operator_flag = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, 4));

        this.operator_type = ByteConvert.bytes2Int(
                Arrays.copyOfRange(bytes, 4, 8));

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public String toString() {
        return "SetOperator [operator_flag=" + operator_flag + ", operator_type="
                + operator_type + "]";
    }

}
