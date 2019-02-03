package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 * 用户登录验证响应消息
 */

public class EquipmentVerifyRps {
    // 字节数组大小
    public static final int BUF_SIZE = 4 + 4;

    // Uint
    public long pad;

    // 返回验证结果, 0x00001111:表示用户名密码验证码都验证通过;0xffffffff表示验证失败
    // Uint
    public long status;


    /**
     * 构造方法，根据属性值构造类对象
     * @param varPad
     * @param varStatus
     */
    public EquipmentVerifyRps(long varPad, long varStatus){

        this.pad = varPad;
        this.status = varStatus;

        byte[] bytes;
        bytes = ByteConvert.uint2Bytes(varPad);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.uint2Bytes(varStatus);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

    }

    /**
     * 构造方法，根据byte[]构造类对象
     * @param byteMsg
     */
    public EquipmentVerifyRps(byte[] byteMsg) {
        // pad
        pad = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                byteMsg
                , 0
                , 4));
        // status
        status = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                byteMsg
                , 4
                , 8));

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    @Override
    public String toString() {
        return "EquipmentVerifyRps{" +
                ", pad=" + pad +
                ", status=" + status +
                '}';
    }
}
