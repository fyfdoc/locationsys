package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.cust.AntennaDirection;
import com.dtm.locationsys.utils.BitsHelper;
import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 中标及测向结果信息结构体
 * 上报各天线功率测量
 */

public class RptUlPowerMeasInfo extends MsgParseBase {
    public static final int CXX_MAX_UL_ANTENNA_NUM = 4;

    public static final int BUF_SIZE = 4 + 4 + CXX_MAX_UL_ANTENNA_NUM*4; // 字节数组大小

    public int EARFCN;        //:16; //中标用户所在小区中心频点（折算为载波号）
    public int cellID;        //:9; //中标用户所在小区PCI
    public int ul_rf_gain;    //:7; //上行增益信息（界面不显示）
    public long m_TMSI;       // Unit 中标用户TMSI
    // 中标用户各用户功率 Unit，需要转换为dB值，
    // 转换公式10*log10(ant_power) + 20*(3 - ul_rf_gain)，(纵坐标范围0~160)
    public long[] ant_power = new long[CXX_MAX_UL_ANTENNA_NUM];

    private double mPositionX;

    private double mPositionY;

    private Map<AntennaDirection, Integer> mAntennaPowers;

    /**
     * 构造方法，根据属性值构造类对象
     * 转换步骤：int->byte[]->bitStr->按位数截取bitStr->将各参数截取后的bitStr拼接起来
     * ->转换为int(每32位bitStr)->byte[]->将所有byte[]拼接为最终的byte[]
     * @param EARFCN
     * @param cellID
     * @param ul_rf_gain
     * @param m_TMSI
     * @param ant_power
     */
    public RptUlPowerMeasInfo(
            int EARFCN, int cellID, int ul_rf_gain
            , long m_TMSI, long[] ant_power) {

        this.EARFCN = EARFCN;
        this.cellID = cellID;
        this.ul_rf_gain = ul_rf_gain;
        this.m_TMSI = m_TMSI;
        this.ant_power = ant_power;
        mAntennaPowers = new HashMap<>();
        mAntennaPowers.put(AntennaDirection.FRONT_ANTENNA,10);
        mAntennaPowers.put(AntennaDirection.BACK_ANTENNA,20);
        mAntennaPowers.put(AntennaDirection.LEFT_ANTENNA,30);
        mAntennaPowers.put(AntennaDirection.RIGHT_ANTENNA,40);

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
        bitCout = s1.substring(7,8);
        sb.append(bitCout);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        sb.append(s1);

        // ul_rf_gain :7
        bytes = ByteConvert.int2Bytes(ul_rf_gain);
        s1 = ByteConvert.byte2Bit(bytes[3]);
        bitCout = s1.substring(1, 8);
        sb.append(bitCout);

        // m_TMSI :Uint
        bytes = ByteConvert.uint2Bytes(m_TMSI);
        s1 = ByteConvert.bytes2Bit(bytes);
        sb.append(s1);

        // ant_power
        for (int i = 0; i < CXX_MAX_UL_ANTENNA_NUM; i++){
            bytes = ByteConvert.uint2Bytes(ant_power[i]);
            s1 = ByteConvert.bytes2Bit(bytes);
            sb.append(s1);
        }
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
    public RptUlPowerMeasInfo(byte[] bytes){
        this.buf = bytes;

        // 将每4个字节数组转换为一个Unit
        long first_uint_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, 0, INT_BYTE_SIZE));

        long second_unit_segment = ByteConvert.bytes2Uint(
                Arrays.copyOfRange(bytes, INT_BYTE_SIZE, INT_BYTE_SIZE * 2));

        // EARFCN :16
        this.EARFCN = BitsHelper.getIntFromUintByBits(first_uint_segment, 16, 31);
        // cellID :9
        this.cellID = BitsHelper.getIntFromUintByBits(first_uint_segment, 7, 15);
        // ul_rf_gain :7
        this.ul_rf_gain = BitsHelper.getIntFromUintByBits(first_uint_segment, 0, 6);

        // m_TMSI :Uint
        this.m_TMSI = BitsHelper.getUintByBits(second_unit_segment, 0, 31);

        int index;
        for (int i = 0; i < CXX_MAX_UL_ANTENNA_NUM; i++){
            index = INT_BYTE_SIZE * 2 + (i * INT_BYTE_SIZE);
            this.ant_power[i] = ByteConvert.bytes2Uint(
                    Arrays.copyOfRange(bytes, index, index + INT_BYTE_SIZE));
        }

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public int getEARFCN() {
        return EARFCN;
    }

    public void setEARFCN(int EARFCN) {
        this.EARFCN = EARFCN;
    }

    public int getCellID() {
        return cellID;
    }

    public void setCellID(int cellID) {
        this.cellID = cellID;
    }

    public int getUl_rf_gain() {
        return ul_rf_gain;
    }

    public void setUl_rf_gain(int ul_rf_gain) {
        this.ul_rf_gain = ul_rf_gain;
    }

    public long getM_TMSI() {
        return m_TMSI;
    }

    public void setM_TMSI(long m_TMSI) {
        this.m_TMSI = m_TMSI;
    }

    public long[] getAnt_power() {
        return ant_power;
    }

    public void setAnt_power(long[] ant_power) {
        this.ant_power = ant_power;
    }

    public double getPositionX() {
        return mPositionX;
    }

    public double getPositionY() {
        return mPositionY;
    }

    public void setPosition(double posX, double posY) {
        mPositionX = posX;

        mPositionY = posY;
    }

    public void setAntennaPower(AntennaDirection antennaDirection, int power) {
        mAntennaPowers.put(antennaDirection, power);
    }

    public int getAntennaPower(AntennaDirection antennaDirection) {
        return mAntennaPowers.get(antennaDirection);
    }

    @Override
    public String toString() {
        return "RptUlPowerMeasInfo{" +
                "EARFCN=" + EARFCN +
                ", cellID=" + cellID +
                ", ul_rf_gain=" + ul_rf_gain +
                ", m_TMSI=" + m_TMSI +
                ", ant_power=" + Arrays.toString(ant_power) +
                '}';
    }
}
