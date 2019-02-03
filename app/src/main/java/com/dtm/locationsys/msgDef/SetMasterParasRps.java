package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 * 基站参数设置响应消息
 */

public class SetMasterParasRps {
    public short msgType;

    public short msgLength;

    public int pad;

    // 0x00001111:表示参数设置成功; 0xffffffff:表示参数设置失败;
    public long status;

    /**
     * 构造方法，根据属性值构造类对象
     * @param byteMsg
     */
    public SetMasterParasRps(byte[] byteMsg) {
        // msgType
        msgType = ByteConvert.bytes2Short(Arrays.copyOfRange(byteMsg, 0, 2));
        // msgLength
        msgLength = ByteConvert.bytes2Short(Arrays.copyOfRange(byteMsg, 2, 4));
        // pad
        pad = ByteConvert.bytes2Int(Arrays.copyOfRange(byteMsg, 4, 8));
        // status
        status = ByteConvert.bytes2Uint(Arrays.copyOfRange(byteMsg, 8, 12));

    }


    @Override
    public String toString() {
        return "SetMasterParasRps{" +
                "msgType=" + msgType +
                ", msgLength=" + msgLength +
                ", pad=" + pad +
                ", status=" + status +
                '}';
    }
}
