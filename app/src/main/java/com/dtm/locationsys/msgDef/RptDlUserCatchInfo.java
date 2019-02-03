package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.BitsHelper;
import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 * 中标用户信息.
 */

public class RptDlUserCatchInfo extends MsgParseBase {
    public static final int BUF_SIZE = 4 + 4 + 4; // 字节数组大小

    public int EARFCN;        //:16; //中标用户频点（折算为载波号）
    public int cellID;        //:9; //中标用户所在PCI
    public int pad;           //:7;

    public int crnti;         //:16; //中标用户CRNTI
    public int ta;            //:11; //中标用户下行TA
    public int pad1;          //:5;

    public long m_TMSI;       //:Uint 中标用户TMSI


    /**
     * 构造方法，根据属性值构造类对象
     * @param EARFCN
     * @param cellID
     * @param pad
     * @param crnti
     * @param ta
     * @param pad1
     * @param m_TMSI
     */
    public RptDlUserCatchInfo(int EARFCN, int cellID, int pad
            , int crnti, int ta, int pad1, long m_TMSI) {

        this.EARFCN = EARFCN;
        this.cellID = cellID;
        this.pad = pad;
        this.crnti = crnti;
        this.ta = ta;
        this.pad1 = pad1;
        this.m_TMSI = m_TMSI;

        // 中间变量
        StringBuffer sb = new StringBuffer();
        String s1 = "";
        String bitCout = "";

        // 构造byte数组
        // EARFCN :16
        byte[] bytes = ByteConvert.int2Bytes(EARFCN);
        s1 = ByteConvert.byte2Bit(bytes[2]);
        sb.append(s1);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // cellID :9
        bytes = ByteConvert.int2Bytes(cellID);
        s1 = ByteConvert.byte2Bit(bytes[2]);
        bitCout = s1.substring(7, 8);
        sb.append(bitCout);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // pad :7
        bytes = ByteConvert.int2Bytes(pad);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(1, 8);
        sb.append(bitCout);

        // crnti :16
        bytes = ByteConvert.int2Bytes(crnti);
        s1 = ByteConvert.byte2Bit(bytes[2]);
        sb.append(s1);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // ta :11
        bytes = ByteConvert.int2Bytes(ta);
        s1 = ByteConvert.byte2Bit(bytes[2]);
        bitCout = s1.substring(5, 8);
        sb.append(bitCout);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // pad1 :5
        bytes = ByteConvert.int2Bytes(pad1);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(3, 8);
        sb.append(bitCout);

        // m_TMSI :Uint
        bytes = ByteConvert.uint2Bytes(m_TMSI);
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
    public RptDlUserCatchInfo(byte[] bytes){
        this.buf = bytes;

        // 将每4个字节数组转换为一个Unit
        long first_uint_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, INT_BYTE_SIZE));

        long second_unit_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, INT_BYTE_SIZE, INT_BYTE_SIZE * 2));

        long third_unit_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, INT_BYTE_SIZE * 2, INT_BYTE_SIZE * 3));


        // EARFCN :16
        this.EARFCN = BitsHelper.getIntFromUintByBits(first_uint_segment, 16, 31);
        // cellID :9
        this.cellID = BitsHelper.getIntFromUintByBits(first_uint_segment, 7, 15);
        // pad :7
        this.pad = BitsHelper.getIntFromUintByBits(first_uint_segment, 0, 6);

        // crnti :16
        this.crnti = BitsHelper.getIntFromUintByBits(second_unit_segment, 16, 31);
        // ta :11
        this.ta = BitsHelper.getIntFromUintByBits(second_unit_segment, 5, 15);
        // pad1 :5
        this.pad1 = BitsHelper.getIntFromUintByBits(second_unit_segment, 0, 4);

        // m_TMSI :Uint
        this.m_TMSI = BitsHelper.getUintByBits(third_unit_segment, 0, 31);

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    @Override
    public String toString() {
        return "RptDlUserCatchInfo{" +
                "EARFCN=" + EARFCN +
                ", cellID=" + cellID +
                ", pad=" + pad +
                ", crnti=" + crnti +
                ", ta=" + ta +
                ", pad1=" + pad1 +
                ", m_TMSI=" + m_TMSI +
                '}';
    }
}
