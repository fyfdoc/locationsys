package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.BitsHelper;
import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 * 小区轮询结果.
 */

public class RptCellPollingInfo extends MsgParseBase {
    public static final int BUF_SIZE = 4 + 4; // 字节数组大小

    public int valid_flag;      //:1; //小区有效标识
    public int earfcn;          //:16; //小区频点（需要折算为频点号）
    public int rf_gain;         //:5; //下行增益（界面不显示）
    public int pci;             //:9; //小区PCI
    public int pad1;            //:1;

    public int cell_power;      //:18; //小区功率（折算为dB值）
    public int freq_band;       //:3; //频带
    public int earfcn_index;    //:6; //频点索引（界面不用）
    public int pad;             //:5;

    /**
     * 构造方法，根据属性值构造类对象
     * 转换步骤：int->byte[]->bitStr->按位数截取bitStr->将各参数截取后的bitStr拼接起来
     * ->转换为int(每32位bitStr)->byte[]->将所有byte[]拼接为最终的byte[]
     * @param valid_flag
     * @param earfcn
     * @param rf_gain
     * @param pci
     * @param pad1
     * @param cell_power
     * @param freq_band
     * @param earfcn_index
     * @param pad
     */
    public RptCellPollingInfo(int valid_flag, int earfcn
            , int rf_gain, int pci, int pad1, int cell_power, int freq_band
            , int earfcn_index, int pad) {

        this.valid_flag = valid_flag;
        this.earfcn = earfcn;
        this.rf_gain = rf_gain;
        this.pci = pci;
        this.pad1 = pad1;
        this.cell_power = cell_power;
        this.freq_band = freq_band;
        this.earfcn_index = earfcn_index;
        this.pad = pad;

        // 中间变量
        StringBuffer sb = new StringBuffer();
        String s1 = "";
        String bitCout = "";

        // 构造byte数组
        // valid_flag :1
        byte[] bytes = ByteConvert.int2Bytes(valid_flag);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(7, 8);
        sb.append(bitCout);

        // earfcn :16
        bytes = ByteConvert.int2Bytes(earfcn);
        s1 = ByteConvert.byte2Bit(bytes[2]);
        sb.append(s1);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // rf_gain :5
        bytes = ByteConvert.int2Bytes(rf_gain);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(3, 8);
        sb.append(bitCout);

        // pci :9
        bytes = ByteConvert.int2Bytes(pci);
        s1 = ByteConvert.byte2Bit(bytes[2]);
        bitCout = s1.substring(7, 8);
        sb.append(bitCout);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // pad1 :1
        bytes = ByteConvert.int2Bytes(pad1);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(7, 8);
        sb.append(bitCout);

        // cell_power :18
        bytes = ByteConvert.int2Bytes(cell_power);
        s1 = ByteConvert.byte2Bit(bytes[1]);
        bitCout = s1.substring(6, 8);
        sb.append(bitCout);
        s1 = ByteConvert.byte2Bit(bytes[2]);
        sb.append(s1);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // freq_band :3
        bytes = ByteConvert.int2Bytes(freq_band);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(5, 8);
        sb.append(bitCout);

        // earfcn_index :6
        bytes = ByteConvert.int2Bytes(earfcn_index);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(2, 8);
        sb.append(bitCout);

        // pad :5
        bytes = ByteConvert.int2Bytes(pad);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(3, 8);
        sb.append(bitCout);

//        System.out.println("sb=" + sb.toString());

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
    public RptCellPollingInfo(byte[] bytes){
        this.buf = bytes;

        // 将每4个字节数组转换为一个Unit
        long first_uint_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, INT_BYTE_SIZE));

        long second_unit_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, INT_BYTE_SIZE, INT_BYTE_SIZE * 2));


        // valid_flag :1
        this.valid_flag = BitsHelper.getIntFromUintByBits(first_uint_segment, 31, 31);
        // earfcn :16
        this.earfcn = BitsHelper.getIntFromUintByBits(first_uint_segment, 15, 30);
        // rf_gain :5
        this.rf_gain = BitsHelper.getIntFromUintByBits(first_uint_segment, 10, 14);
        // pci :9
        this.pci = BitsHelper.getIntFromUintByBits(first_uint_segment, 1, 9);
        // pad1 :1
        this.pad1 = BitsHelper.getIntFromUintByBits(first_uint_segment, 0, 0);

        // cell_power :18
        this.cell_power = BitsHelper.getIntFromUintByBits(second_unit_segment, 14, 31);
        // freq_band :3
        this.freq_band = BitsHelper.getIntFromUintByBits(second_unit_segment, 11, 13);
        // earfcn_index :6
        this.earfcn_index = BitsHelper.getIntFromUintByBits(second_unit_segment, 5, 10);
        // pad :5
        this.pad = BitsHelper.getIntFromUintByBits(second_unit_segment, 0, 4);
    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    @Override
    public String toString() {
        return "RptCellPollingInfo{" +
                "valid_flag=" + valid_flag +
                ", earfcn=" + earfcn +
                ", rf_gain=" + rf_gain +
                ", pci=" + pci +
                ", pad1=" + pad1 +
                ", cell_power=" + cell_power +
                ", freq_band=" + freq_band +
                ", earfcn_index=" + earfcn_index +
                ", pad=" + pad +
                '}';
    }
}
