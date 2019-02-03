package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.BitsHelper;
import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;


/**
 * 设备底层统计信息结构体.
 */

public class RptTmsiStatistics extends MsgParseBase {
    public static final int BUF_SIZE = 4 + 4; // 字节数组大小

    public int valid;  //:1; //1为有效，有效才需要统计该TMSI
    public int count;  //:31; //需要累计设备上电后收到该TMSI的次数
    public long m_tmsi; //TMSI :Uint

    /**
     * 构造方法，根据属性值构造类对象
     * @param valid
     * @param count
     * @param m_tmsi
     */
    public RptTmsiStatistics(int valid, int count, long m_tmsi) {
        this.valid = valid;
        this.count = count;
        this.m_tmsi = m_tmsi;

        // 中间变量
        StringBuffer sb = new StringBuffer();
        String s1 = "";
        String bitCout = "";

        // 构造byte数组
        // valid :1
        byte[] bytes = ByteConvert.int2Bytes(valid);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(7, 8);
        sb.append(bitCout);

        // count :31
        bytes = ByteConvert.int2Bytes(count);
        s1 = ByteConvert.byte2Bit(bytes[0]);
        bitCout = s1.substring(1, 8);
        sb.append(bitCout);
        s1 = ByteConvert.byte2Bit(bytes[1]);
        sb.append(s1);
        s1 = ByteConvert.byte2Bit(bytes[2]);
        sb.append(s1);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // m_tmsi :Uint
        bytes = ByteConvert.uint2Bytes(m_tmsi);
        s1 = ByteConvert.bytes2Bit(bytes);
        sb.append(s1);

        // 组装buf，将拼接的BitStr转换为int(每32位bitStr)
        for (int i = 0; i < sb.length()/32; i++) {
            int index = i * 32;
            // 32位转换为long防止溢出
            long l1 = Long.parseLong(sb.substring(index, index + 32), 2);
            bytes = ByteConvert.uint2Bytes(l1);
            System.arraycopy(bytes, 0, buf, i * 4, bytes.length);
        }

    }

    /**
     * 构造方法，将byte[]解析为类对象
     * @param bytes
     */
    public RptTmsiStatistics(byte[] bytes){
        this.buf = bytes;

        // 将每4个字节数组转换为一个Unit
        long first_uint_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, INT_BYTE_SIZE));

        long second_unit_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, INT_BYTE_SIZE, INT_BYTE_SIZE * 2));

        // valid :1
        this.valid = BitsHelper.getIntFromUintByBits(first_uint_segment, 31, 31);

        // count :31
        this.count = BitsHelper.getIntFromUintByBits(first_uint_segment, 0, 30);

        // m_tmsi :Uint
        this.m_tmsi = BitsHelper.getUintByBits(second_unit_segment, 0, 31);

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }


    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getM_tmsi() {
        return m_tmsi;
    }

    public void setM_tmsi(long m_tmsi) {
        this.m_tmsi = m_tmsi;
    }

    @Override
    public String toString() {
        return "RptTmsiStatistics{" +
                "valid=" + valid +
                ", count=" + count +
                ", m_tmsi=" + m_tmsi +
                '}';
    }
}
