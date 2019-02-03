package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ArfcnConvert;
import com.dtm.locationsys.utils.ByteConvert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 统计信息小区PCI.
 */

public class RptCcmReportCountInfo extends MsgParseBase {
    public static final int CXX_MAX_TMSI_NUM = 50;
    public static final int BUF_SIZE = 2 + 2
            + RptReportCountStatisticsPara.BUF_SIZE
            + (CXX_MAX_TMSI_NUM * RptTmsiStatistics.BUF_SIZE); // 字节数组大小

    public int cellID; // :Ushort 统计信息小区PCI
    public int EARFCN; // :Ushort 统计信息小区频点（折算为载波号）
    public RptReportCountStatisticsPara statistics_para;
    public List<RptTmsiStatistics> tmsi_tableList;

    /**
     * 构造方法，根据属性值构造类对象
     * @param cellID
     * @param EARFCN
     * @param statistics_para
     * @param tmsi_tableList
     */
    public RptCcmReportCountInfo(int cellID, int EARFCN
            , RptReportCountStatisticsPara statistics_para
            , List<RptTmsiStatistics> tmsi_tableList) {

        this.cellID = cellID;
        this.EARFCN = EARFCN;
        this.statistics_para = statistics_para;
        this.tmsi_tableList = tmsi_tableList;

        // 构造byte数组
        // cellID :Ushort
        byte[] bytes = ByteConvert.ushort2Bytes(cellID);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        // EARFCN :Ushort
        bytes = ByteConvert.ushort2Bytes(EARFCN);
        System.arraycopy(bytes, 0, buf, 0 + 2, bytes.length);

        bytes = statistics_para.getBuf();
        System.arraycopy(bytes, 0, buf, 0 + 2 + 2, bytes.length);

        int index;
        for(int i = 0; i < CXX_MAX_TMSI_NUM; i++){
            bytes = tmsi_tableList.get(i).getBuf();
            index = 0 + 2 + 2 + RptReportCountStatisticsPara.BUF_SIZE + (i * RptTmsiStatistics.BUF_SIZE);
            System.arraycopy(bytes, 0, buf, index, bytes.length);
        }

    }


    /**
     * 构造方法，将byte[]解析类对象
     * @param bytes
     */
    public RptCcmReportCountInfo(byte[] bytes){
        this.buf = bytes;

        this.cellID = ByteConvert.bytes2Ushort(Arrays.copyOfRange(
                bytes
                , 0
                , 2));
        this.EARFCN = ByteConvert.bytes2Ushort(Arrays.copyOfRange(
                bytes
                , 0 + 2
                , 0 + 2 + 2));

        this.statistics_para = new RptReportCountStatisticsPara(Arrays.copyOfRange(
                bytes
                , 0 + 2 + 2
                , 0 + 2 + 2 + RptReportCountStatisticsPara.BUF_SIZE));

        this.tmsi_tableList = new ArrayList<>();
        int index;
        for (int i = 0; i < CXX_MAX_TMSI_NUM; i++) {
            index = 0 + 2 + 2 + RptReportCountStatisticsPara.BUF_SIZE + (RptTmsiStatistics.BUF_SIZE * i);
            RptTmsiStatistics tmp = new RptTmsiStatistics(Arrays.copyOfRange(
                    bytes
                    , index
                    , index + RptTmsiStatistics.BUF_SIZE));
            this.tmsi_tableList.add(i, tmp);

        }


    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public int getCellID() {
        return cellID;
    }

    public void setCellID(int cellID) {
        this.cellID = cellID;
    }

    public int getEARFCN() {
        return EARFCN;
    }

    public void setEARFCN(int EARFCN) {
        this.EARFCN = EARFCN;
    }

    public RptReportCountStatisticsPara getStatistics_para() {
        return statistics_para;
    }

    public void setStatistics_para(RptReportCountStatisticsPara statistics_para) {
        this.statistics_para = statistics_para;
    }

    public List<RptTmsiStatistics> getTmsi_tableList() {
        return tmsi_tableList;
    }

    public void setTmsi_tableList(List<RptTmsiStatistics> tmsi_tableList) {
        this.tmsi_tableList = tmsi_tableList;
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

    @Override
    public String toString() {
        return "RptCcmReportCountInfo{" +
                "cellID=" + cellID +
                ", EARFCN=" + EARFCN +
                ", statistics_para=" + statistics_para.toString() +
                ", tmsi_tableList=" + tmsi_tableList.toString() +
                '}';
    }
}
