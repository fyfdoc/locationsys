package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 * 设备底层统计消息.
 */

public class RptReportCountStatisticsPara extends MsgParseBase {
    public static final int BUF_SIZE = 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4; // 字节数组大小

    public long sib1_num; //SIB1调度次数
    public long sib2_num; //SIB2调度次数
    public long sib1_detected_num; //SIB1检测次数
    public long sib2_detected_num; //SIB2检测次数
    public long msg1_detected_num; //MSG1检测次数
    public long msg2_detected_num; //MSG2检测次数
    public long msg3_detected_num; //MSG3检测次数
    public long msg4_detected_num; //MSG4检测次数

    /**
     * 构造方法，根据属性值构造类对象
     * @param sib1_num
     * @param sib2_num
     * @param sib1_detected_num
     * @param sib2_detected_num
     * @param msg1_detected_num
     * @param msg2_detected_num
     * @param msg3_detected_num
     * @param msg4_detected_num
     */
    public RptReportCountStatisticsPara(long sib1_num, long sib2_num
            , long sib1_detected_num, long sib2_detected_num
            , long msg1_detected_num, long msg2_detected_num
            , long msg3_detected_num, long msg4_detected_num) {

        this.sib1_num = sib1_num;
        this.sib2_num = sib2_num;
        this.sib1_detected_num = sib1_detected_num;
        this.sib2_detected_num = sib2_detected_num;
        this.msg1_detected_num = msg1_detected_num;
        this.msg2_detected_num = msg2_detected_num;
        this.msg3_detected_num = msg3_detected_num;
        this.msg4_detected_num = msg4_detected_num;



        // 构造byte数组
        byte[] bytes = ByteConvert.uint2Bytes(sib1_num);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.uint2Bytes(sib2_num);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

        bytes = ByteConvert.uint2Bytes(sib1_detected_num);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 4, bytes.length);

        bytes = ByteConvert.uint2Bytes(sib2_detected_num);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 4 + 4, bytes.length);

        bytes = ByteConvert.uint2Bytes(msg1_detected_num);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 4 + 4 + 4, bytes.length);

        bytes = ByteConvert.uint2Bytes(msg2_detected_num);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 4 + 4 + 4 + 4, bytes.length);

        bytes = ByteConvert.uint2Bytes(msg3_detected_num);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 4 + 4 + 4 + 4 + 4, bytes.length);

        bytes = ByteConvert.uint2Bytes(msg4_detected_num);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 4 + 4 + 4 + 4 + 4 + 4, bytes.length);


    }

    /**
     * 构造方法，将byte[]解析为类对象
     * @param bytes
     */
    public RptReportCountStatisticsPara(byte[] bytes){
        this.sib1_num = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , 0
                , 4));
        this.sib2_num = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , 0 + 4
                , 0 + 4 + 4));
        this.sib1_detected_num = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , 0 + 4 + 4
                , 0 + 4 + 4 + 4));
        this.sib2_detected_num = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , 0 + 4 + 4 + 4
                , 0 + 4 + 4 + 4 + 4));
        this.msg1_detected_num = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , 0 + 4 + 4 + 4 + 4
                , 0 + 4 + 4 + 4 + 4 + 4));
        this.msg2_detected_num = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , 0 + 4 + 4 + 4 + 4 + 4
                , 0 + 4 + 4 + 4 + 4 + 4 + 4));
        this.msg3_detected_num = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , 0 + 4 + 4 + 4 + 4 + 4 + 4
                , 0 + 4 + 4 + 4 + 4 + 4 + 4 + 4));
        this.msg4_detected_num = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , 0 + 4 + 4 + 4 + 4 + 4 + 4 + 4
                , 0 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4));
    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    };

    @Override
    public String toString() {
        return "RptReportCountStatisticsPara{" +
                "sib1_num=" + sib1_num +
                ", sib2_num=" + sib2_num +
                ", sib1_detected_num=" + sib1_detected_num +
                ", sib2_detected_num=" + sib2_detected_num +
                ", msg1_detected_num=" + msg1_detected_num +
                ", msg2_detected_num=" + msg2_detected_num +
                ", msg3_detected_num=" + msg3_detected_num +
                ", msg4_detected_num=" + msg4_detected_num +
                '}';
    }
}

