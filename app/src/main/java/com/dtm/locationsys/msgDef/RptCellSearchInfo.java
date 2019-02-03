package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ArfcnConvert;
import com.dtm.locationsys.utils.BitsHelper;
import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 * 守控小区消息结构体.
 */

public class RptCellSearchInfo extends MsgParseBase {

    public static final int BUF_SIZE = 4 + 4 + 4; // 字节数组大小

    public int valid_flag;        // :1; 指示小区是否有效，为0不需要显示本小区，为1需要显示下面信息
    public int cellIdentity;      // :28; //小区CID
    public int freq_band;         // :3; //频段（界面不显示）

    public int PCI;               // :9; //PCI
    public int rf_gain;           // :5; //增益（界面不显示）
    public int cell_power;        // :18; //功率（需要折算为dB，公式见下：）

    public int EARFCN;            // :16; //频点（需要转换为频点号，公式见下：）
    public int trackingAreaCode;  // :16; //小区TAC

    public boolean isLocked;      // 是否锁定
    /**
     * 构造方法，根据属性值构造类对象
     * 转换步骤：int->byte[]->bitStr->按位数截取bitStr->将各参数截取后的bitStr拼接起来
     * ->转换为int(每32位bitStr)->byte[]->将所有byte[]拼接为最终的byte[]
     * @param valid_flag
     * @param cellIdentity
     * @param freq_band
     * @param PCI
     * @param rf_gain
     * @param cell_power
     * @param EARFCN
     * @param trackingAreaCode
     */
    public RptCellSearchInfo(
            int valid_flag, int cellIdentity, int freq_band
            , int PCI, int rf_gain, int cell_power
            , int EARFCN, int trackingAreaCode) {

        this.valid_flag = valid_flag;
        this.cellIdentity = cellIdentity;
        this.freq_band = freq_band;
        this.PCI = PCI;
        this.rf_gain = rf_gain;
        this.cell_power = cell_power;
        this.EARFCN = EARFCN;
        this.trackingAreaCode = trackingAreaCode;

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

        // cellIdentity :28
        bytes = ByteConvert.int2Bytes(cellIdentity);
        s1 = ByteConvert.bytes2Bit(bytes);
        bitCout = s1.substring(4, 32);
        sb.append(bitCout);

        // freq_band :3
        bytes = ByteConvert.int2Bytes(freq_band);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(5, 8);
        sb.append(bitCout);

        // PCI :9
        bytes = ByteConvert.int2Bytes(PCI);
        s1 = ByteConvert.byte2Bit(bytes[2]);
        bitCout = s1.substring(7, 8);
        sb.append(bitCout);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // rf_gain :5
        bytes = ByteConvert.int2Bytes(rf_gain);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(3, 8);
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

        // EARFCN :16
        bytes = ByteConvert.int2Bytes(EARFCN);
        s1 = ByteConvert.byte2Bit(bytes[2]);
        sb.append(s1);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // trackingAreaCode :16
        bytes = ByteConvert.int2Bytes(trackingAreaCode);
        s1 = ByteConvert.byte2Bit(bytes[2]);
        sb.append(s1);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // 将拼接的BitStr转换为Uint(每32位bitStr)
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
    public RptCellSearchInfo(byte[] bytes){
        this.buf = bytes;

        // 将每4个字节数组转换为一个Unit
        long first_uint_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, INT_BYTE_SIZE));

        long second_unit_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, INT_BYTE_SIZE, INT_BYTE_SIZE * 2));

        long third_unit_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, INT_BYTE_SIZE * 2, INT_BYTE_SIZE * 3));

        // valid_flag :1
        this.valid_flag = BitsHelper.getIntFromUintByBits(first_uint_segment, 31, 31);
        // cellIdentity :28
        this.cellIdentity = BitsHelper.getIntFromUintByBits(first_uint_segment, 3, 30);
        // freq_band :3
        this.freq_band = BitsHelper.getIntFromUintByBits(first_uint_segment, 0, 2);

        // PCI :9
        this.PCI = BitsHelper.getIntFromUintByBits(second_unit_segment, 23, 31);
        // rf_gain :5
        this.rf_gain = BitsHelper.getIntFromUintByBits(second_unit_segment, 18, 22);
        // cell_power :18
        this.cell_power = BitsHelper.getIntFromUintByBits(second_unit_segment, 0, 17);

        // EARFCN :16
        this.EARFCN = BitsHelper.getIntFromUintByBits(third_unit_segment, 16, 31);
        // trackingAreaCode :16
        this.trackingAreaCode = BitsHelper.getIntFromUintByBits(third_unit_segment, 0, 15);

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    /**
     * 根据频点获取bandid
     * @return
     */
    public int getBandId() {
        // 用于转换ARFCN
        ArfcnConvert arfcnConvert = new ArfcnConvert();
        // 根据频点获取bandid
        int bandId = arfcnConvert.getDLBandIdByArfcn(this.EARFCN);
        return bandId;
    }

    /**
     * 频点转换为频点号
     * @return
     */
    public int getNrfcnN() {
        // 用于转换ARFCN
        ArfcnConvert arfcnConvert = new ArfcnConvert();
        // 频点转换为频点号
        int arfcnN = arfcnConvert.getDLArfcnNByArfcnF(this.getBandId(), Integer.valueOf(this.EARFCN));
        return arfcnN;
    }

    /**
     * 获取转换后的场强
     * @return
     */
    public String getRSRP() {
        return String.valueOf(ArfcnConvert.power2dB(this.cell_power));
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public int getValid_flag() {
        return valid_flag;
    }

    public void setValid_flag(int valid_flag) {
        this.valid_flag = valid_flag;
    }

    public int getCellIdentity() {
        return cellIdentity;
    }

    public void setCellIdentity(int cellIdentity) {
        this.cellIdentity = cellIdentity;
    }

    public int getFreq_band() {
        return freq_band;
    }

    public void setFreq_band(int freq_band) {
        this.freq_band = freq_band;
    }

    public int getPCI() {
        return PCI;
    }

    public void setPCI(int PCI) {
        this.PCI = PCI;
    }

    public int getRf_gain() {
        return rf_gain;
    }

    public void setRf_gain(int rf_gain) {
        this.rf_gain = rf_gain;
    }

    public int getCell_power() {
        return cell_power;
    }

    public void setCell_power(int cell_power) {
        this.cell_power = cell_power;
    }

    public int getEARFCN() {
        return EARFCN;
    }

    public void setEARFCN(int EARFCN) {
        this.EARFCN = EARFCN;
    }

    public int getTrackingAreaCode() {
        return trackingAreaCode;
    }

    public void setTrackingAreaCode(int trackingAreaCode) {
        this.trackingAreaCode = trackingAreaCode;
    }

    @Override
    public String toString() {
        return "RptCellSearchInfo{" +
                "valid_flag=" + valid_flag +
                ", cellIdentity=" + cellIdentity +
                ", freq_band=" + freq_band +
                ", PCI=" + PCI +
                ", rf_gain=" + rf_gain +
                ", cell_power=" + cell_power +
                ", EARFCN=" + EARFCN +
                ", trackingAreaCode=" + trackingAreaCode +
                '}';
    }
}
