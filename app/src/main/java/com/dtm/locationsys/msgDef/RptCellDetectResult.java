package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;
import com.dtm.locationsys.utils.SysLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 输出界面内容.
 *
 * DSP给界面上报检测结果，包含四类:
 * (1) 守控小区结果，标识约定为0xaaaaaaaa
 * (2) 设备底层统计信息，标识约定为0xbbbbbbbb
 * (3) 中标及测向结果，标识约定为0xcccccccc
 * (4) 小区轮询结果，标识约定为0xdddddddd、
 * (5) 下行捕码结果，flag约定为0xeeeeeeee
 */

public class RptCellDetectResult {
    // 消息头长度
    public static final int MSG_HEADER_LEN = 0;
    //守控小区结果个数
    public static final int CXX_MAX_CELL_NUM = 6;
    public static final int  CXX_MAX_CELL_EARFCN = 46;

    // 字节数组大小
    public static final int BUF_SIZE = MSG_HEADER_LEN
            + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
            + 4 + RptUlPowerMeasInfo.BUF_SIZE
            + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
            + 4 + 4 + (RptCellPollingInfo.BUF_SIZE * CXX_MAX_CELL_EARFCN * 2)
            + 4 + RptDlUserCatchInfo.BUF_SIZE;

    public long cell_search_flag; //守控小区结果
    // CXX_MAX_CELL_NUM
    public List<RptCellSearchInfo>  cell_search_infoList;

    public long ul_power_flag; //测向信息
    public RptUlPowerMeasInfo ul_power_meas_info;

    public long counter_report_flag; //设备底层统计消息
    // CXX_MAX_CELL_NUM
    public List<RptCcmReportCountInfo>  report_count_infoList;

    public long cell_polling_flag; //小区轮询结果
    public long valid_cell_num; //有效小区数
    //CXX_MAX_CELL_EARFCN * 2
    public List<RptCellPollingInfo>  ccm_send_cell_polling_infoList;

    public long user_catch_flag;//中标信息
    public RptDlUserCatchInfo dl_user_catch_info;

    /**
     * 构造方法，根据更新标识构造类对象，目前用于发送请求消息
     * @param msgType
     * @param msgLength
     * @param cell_search_flag
     * @param ul_power_flag
     * @param counter_report_flag
     * @param cell_polling_flag
     * @param user_catch_flag
     */
    public RptCellDetectResult(
            short msgType, short msgLength
            , long cell_search_flag, long ul_power_flag, long counter_report_flag
            , long cell_polling_flag, long user_catch_flag){

        int index;

        this.cell_search_flag = cell_search_flag;
        this.ul_power_flag = ul_power_flag;
        this.counter_report_flag = counter_report_flag;
        this.cell_polling_flag = cell_polling_flag;
        this.valid_cell_num = 0;
        this.user_catch_flag = user_catch_flag;


        // 构造byte数组
        byte[] bytes;
        // 守控小区结果
        this.cell_search_infoList = new ArrayList<>();
        for (int i = 0; i < CXX_MAX_CELL_NUM; i++){
            this.cell_search_infoList.add(i,
                    new RptCellSearchInfo(0, 0, 0, 0, 0, 0, 0, 0));
        }

        bytes = ByteConvert.uint2Bytes(this.cell_search_flag);
        System.arraycopy(bytes, 0, buf, 0 + MSG_HEADER_LEN, bytes.length);
        for (int i = 0; i < CXX_MAX_CELL_NUM; i++){
            bytes = this.cell_search_infoList.get(i).getBuf();
            index = RptCellSearchInfo.BUF_SIZE * i;
            System.arraycopy(bytes, 0, buf
                    , 0 + MSG_HEADER_LEN
                            + 4 + index
                    , bytes.length);
        }

        // 测向信息
        this.ul_power_meas_info = new RptUlPowerMeasInfo(0, 0, 0, 0
                , new long[RptUlPowerMeasInfo.CXX_MAX_UL_ANTENNA_NUM]);

        bytes = ByteConvert.uint2Bytes(this.ul_power_flag);
        System.arraycopy(bytes, 0, buf
                , 0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                , bytes.length);

        bytes = this.ul_power_meas_info.getBuf();
        System.arraycopy(bytes, 0, buf
                , 0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4
                , bytes.length);


        // 设备底层统计消息
        this.report_count_infoList = new ArrayList<>();
        for(int i = 0; i < CXX_MAX_CELL_NUM; i++){
            // 设备底层统计消息.
            RptReportCountStatisticsPara statistics_para_obj = new RptReportCountStatisticsPara(
                    0, 0, 0, 0, 0, 0, 0, 0);

            List<RptTmsiStatistics> tmsi_tableList = new ArrayList<>();
            for (int j = 0; j < RptCcmReportCountInfo.CXX_MAX_TMSI_NUM; j++){
                tmsi_tableList.add(j, new RptTmsiStatistics(0, 0, 0));
            }

            RptCcmReportCountInfo tmpObj = new RptCcmReportCountInfo(
                    (short)0, (short)0, statistics_para_obj, tmsi_tableList);
            this.report_count_infoList.add(i, tmpObj);

        }

        bytes = ByteConvert.uint2Bytes(this.cell_search_flag);
        System.arraycopy(bytes, 0, buf
                , 0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                , bytes.length);


        for(int i = 0; i < CXX_MAX_CELL_NUM; i++){
            index = 0 + MSG_HEADER_LEN
                    + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                    + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                    + 4 + (i * RptCcmReportCountInfo.BUF_SIZE);
            bytes = report_count_infoList.get(i).getBuf();
            System.arraycopy(bytes, 0, buf, index, bytes.length);
        }

        //小区轮询结果
        this.ccm_send_cell_polling_infoList = new ArrayList<>();
        for (int i = 0; i < CXX_MAX_CELL_EARFCN * 2; i++){
            this.ccm_send_cell_polling_infoList.add(i,
                    new RptCellPollingInfo(
                            0,0,0,0,0,0,0,0,0));
        }

        bytes = ByteConvert.uint2Bytes(this.cell_polling_flag);
        System.arraycopy(bytes, 0, buf
                , 0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                , bytes.length);
        bytes = ByteConvert.uint2Bytes(this.valid_cell_num);
        System.arraycopy(bytes, 0, buf
                , 0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4
                , bytes.length);
        for(int i = 0; i < CXX_MAX_CELL_EARFCN * 2; i++){
            index = i * RptCellPollingInfo.BUF_SIZE;

            bytes = this.ccm_send_cell_polling_infoList.get(i).getBuf();
            System.arraycopy(bytes, 0, buf
                    , 0 + MSG_HEADER_LEN
                            + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                            + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                            + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                            + 4 + 4 + index
                    , bytes.length);

        }

        // 下行捕码结果，flag约定为0xeeeeeeee
        this.dl_user_catch_info = new RptDlUserCatchInfo(
                0,0,0,0,0,0,0);
        bytes = ByteConvert.uint2Bytes(this.user_catch_flag);
        System.arraycopy(bytes, 0, buf
                ,  0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + 4 + (RptCellPollingInfo.BUF_SIZE * CXX_MAX_CELL_EARFCN * 2)
                , bytes.length);
        bytes = this.dl_user_catch_info.getBuf();
        System.arraycopy(bytes, 0, buf
                ,  0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + 4 + (RptCellPollingInfo.BUF_SIZE * CXX_MAX_CELL_EARFCN * 2)
                        + 4
                , bytes.length);

    }

    /**
     * 构造方法，根据属性值构造类对象
     * @param cell_search_infoList
     * @param ul_power_meas_info
     * @param report_count_infoList
     * @param valid_cell_num
     * @param ccm_send_cell_polling_infoList
     * @param dl_user_catch_info
     */
    public RptCellDetectResult(
            List<RptCellSearchInfo> cell_search_infoList
            , RptUlPowerMeasInfo ul_power_meas_info
            , List<RptCcmReportCountInfo> report_count_infoList
            , long valid_cell_num
            , List<RptCellPollingInfo> ccm_send_cell_polling_infoList
            , RptDlUserCatchInfo dl_user_catch_info) {

        int index;

        // 构造byte数组
        byte[] bytes;
/*
        bytes = ByteConvert.short2Bytes(msgType);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.short2Bytes(msgLength);
        System.arraycopy(bytes, 0, buf, 0 + 2, bytes.length);
*/
        // 守控小区结果
        if(cell_search_infoList != null){//参数不为空，使用参数初始化
            this.cell_search_flag = 0xaaaaaaaaL;
            this.cell_search_infoList = cell_search_infoList;
        }else {// 参数为空，使用默认值初始化
            this.cell_search_flag = 0x00000000L;
            this.cell_search_infoList = new ArrayList<>();
            for (int i = 0; i < CXX_MAX_CELL_NUM; i++){
                this.cell_search_infoList.add(i,
                        new RptCellSearchInfo(0, 0, 0, 0, 0, 0, 0, 0));
            }
        }
        bytes = ByteConvert.uint2Bytes(this.cell_search_flag);
        System.arraycopy(bytes, 0, buf, 0 + MSG_HEADER_LEN, bytes.length);
        for (int i = 0; i < CXX_MAX_CELL_NUM; i++){
            bytes = this.cell_search_infoList.get(i).getBuf();
            index = RptCellSearchInfo.BUF_SIZE * i;
            System.arraycopy(bytes, 0, buf
                    , 0 + MSG_HEADER_LEN
                        + 4 + index
                    , bytes.length);
        }

        // 测向信息
        if(ul_power_meas_info != null){
            this.ul_power_flag = 0xbbbbbbbbL;
            this.ul_power_meas_info = ul_power_meas_info;

        }else {
            this.ul_power_flag = 0x00000000L;
            this.ul_power_meas_info = new RptUlPowerMeasInfo(0, 0, 0, 0
                    , new long[RptUlPowerMeasInfo.CXX_MAX_UL_ANTENNA_NUM]);

        }
        bytes = ByteConvert.uint2Bytes(this.ul_power_flag);
        System.arraycopy(bytes, 0, buf
                , 0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                , bytes.length);

        bytes = this.ul_power_meas_info.getBuf();
        System.arraycopy(bytes, 0, buf
                , 0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4
                , bytes.length);


        // 设备底层统计消息
        if(report_count_infoList != null) {//参数不为空，使用参数初始化
            this.counter_report_flag = 0xccccccccL;
            this.report_count_infoList = report_count_infoList;
        }else{// 参数为空，使用默认值初始化
            this.counter_report_flag = 0x00000000L;
            this.report_count_infoList = new ArrayList<>();
            for(int i = 0; i < CXX_MAX_CELL_NUM; i++){
                // 设备底层统计消息.
                RptReportCountStatisticsPara statistics_para_obj = new RptReportCountStatisticsPara(
                        0, 0, 0, 0, 0, 0, 0, 0);

                List<RptTmsiStatistics> tmsi_tableList = new ArrayList<>();
                for (int j = 0; j < RptCcmReportCountInfo.CXX_MAX_TMSI_NUM; j++){
                    tmsi_tableList.add(j, new RptTmsiStatistics(0, 0, 0));
                }

                RptCcmReportCountInfo tmpObj = new RptCcmReportCountInfo(
                        (short)0, (short)0, statistics_para_obj, tmsi_tableList);
                this.report_count_infoList.add(i, tmpObj);

            }
        }
        bytes = ByteConvert.uint2Bytes(this.cell_search_flag);
        System.arraycopy(bytes, 0, buf
                , 0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                , bytes.length);


        for(int i = 0; i < CXX_MAX_CELL_NUM; i++){
            index = 0 + MSG_HEADER_LEN
                    + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                    + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                    + 4 + (i * RptCcmReportCountInfo.BUF_SIZE);
            bytes = report_count_infoList.get(i).getBuf();
            System.arraycopy(bytes, 0, buf, index, bytes.length);
        }

        //小区轮询结果
        if(ccm_send_cell_polling_infoList != null){//参数不为空，使用参数初始化
            this.cell_polling_flag = 0xddddddddL;
            this.valid_cell_num = valid_cell_num;
            this.ccm_send_cell_polling_infoList = ccm_send_cell_polling_infoList;
        }else{// 参数为空，使用默认值初始化
            this.cell_polling_flag = 0x00000000L;
            this.valid_cell_num = 0;
            this.ccm_send_cell_polling_infoList = new ArrayList<>();
            for (int i = 0; i < CXX_MAX_CELL_EARFCN * 2; i++){
                this.ccm_send_cell_polling_infoList.add(i,
                        new RptCellPollingInfo(
                                0,0,0,0,0,0,0,0,0));
            }

        }
        bytes = ByteConvert.uint2Bytes(this.cell_polling_flag);
        System.arraycopy(bytes, 0, buf
                , 0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                , bytes.length);
        bytes = ByteConvert.uint2Bytes(this.valid_cell_num);
        System.arraycopy(bytes, 0, buf
                , 0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4
                , bytes.length);
        for(int i = 0; i < CXX_MAX_CELL_EARFCN * 2; i++){
            index = i * RptCellPollingInfo.BUF_SIZE;

            bytes = this.ccm_send_cell_polling_infoList.get(i).getBuf();
            System.arraycopy(bytes, 0, buf
                    , 0 + MSG_HEADER_LEN
                            + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                            + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                            + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                            + 4 + 4 + index
                    , bytes.length);

        }

        // 下行捕码结果，flag约定为0xeeeeeeee
        if(dl_user_catch_info != null) {
            this.user_catch_flag = 0xeeeeeeeeL;
            this.dl_user_catch_info = dl_user_catch_info;
        }else{
            this.user_catch_flag = 0x00000000L;
            this.dl_user_catch_info = new RptDlUserCatchInfo(
                    0,0,0,0,0,0,0);
        }
        bytes = ByteConvert.uint2Bytes(this.user_catch_flag);
        System.arraycopy(bytes, 0, buf
                ,  0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + 4 + (RptCellPollingInfo.BUF_SIZE * CXX_MAX_CELL_EARFCN * 2)
                , bytes.length);
        bytes = this.dl_user_catch_info.getBuf();
        System.arraycopy(bytes, 0, buf
                ,  0 + MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + 4 + (RptCellPollingInfo.BUF_SIZE * CXX_MAX_CELL_EARFCN * 2)
                        + 4
                , bytes.length);

    }

    /**
     * 构造方法，将byte[]解析为类对象
     * @param bytes
     */
    public RptCellDetectResult(byte[] bytes) {
        int index;

        if(this.buf.length != bytes.length){// 参数数组长度错误，此处不退出，需要程序报错
            SysLog.e("RptCellDetectResult","The length of bytes error");
        }
        this.buf = bytes;
        // 守控小区结果
        this.cell_search_flag = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , MSG_HEADER_LEN
                , MSG_HEADER_LEN + 4));
        this.cell_search_infoList = new ArrayList<>();
        for(int i = 0; i < CXX_MAX_CELL_NUM; i++){
            index = (RptCellSearchInfo.BUF_SIZE * i);
            this.cell_search_infoList.add(i,
                    new RptCellSearchInfo(Arrays.copyOfRange(
                            bytes
                            , 0 + MSG_HEADER_LEN
                                    + 4 + index
                            , MSG_HEADER_LEN
                                    + 4 + (index + RptCellSearchInfo.BUF_SIZE))));
        }

        //测向信息
        this.ul_power_flag = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4));
        this.ul_power_meas_info = new RptUlPowerMeasInfo(Arrays.copyOfRange(
                bytes
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)));


        //设备底层统计消息
        this.counter_report_flag = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4));
        this.report_count_infoList = new ArrayList<>();
        for(int i = 0; i < CXX_MAX_CELL_NUM; i++){
            index = RptCcmReportCountInfo.BUF_SIZE * i;
            this.report_count_infoList.add(i, new RptCcmReportCountInfo(Arrays.copyOfRange(
                    bytes
                    , MSG_HEADER_LEN
                            + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                            + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                            + 4 + index
                    , MSG_HEADER_LEN
                            + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                            + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                            + 4 + (index + RptCcmReportCountInfo.BUF_SIZE) )));

        }


        // 小区轮询结果
        this.cell_polling_flag = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4));
        this.valid_cell_num = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + 4));
        this.ccm_send_cell_polling_infoList = new ArrayList<>();
        for(int i = 0; i < CXX_MAX_CELL_EARFCN * 2; i++){
            index = RptCellPollingInfo.BUF_SIZE * i;
            this.ccm_send_cell_polling_infoList.add(i,
                    new RptCellPollingInfo(Arrays.copyOfRange(
                            bytes
                            , MSG_HEADER_LEN
                                    + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                                    + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                                    + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                                    + 4 + 4 + index
                            , MSG_HEADER_LEN
                                    + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                                    + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                                    + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                                    + 4 + 4 + (index + RptCellPollingInfo.BUF_SIZE) )));
        }

        // 中标信息
        this.user_catch_flag = ByteConvert.bytes2Uint(Arrays.copyOfRange(
                bytes
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + 4 + (RptCellPollingInfo.BUF_SIZE * CXX_MAX_CELL_EARFCN * 2)
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + 4 + (RptCellPollingInfo.BUF_SIZE * CXX_MAX_CELL_EARFCN * 2)
                        + 4));
        this.dl_user_catch_info = new RptDlUserCatchInfo(Arrays.copyOfRange(
                bytes
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + 4 + (RptCellPollingInfo.BUF_SIZE * CXX_MAX_CELL_EARFCN * 2)
                        + 4
                , MSG_HEADER_LEN
                        + 4 + (RptCellSearchInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + (RptUlPowerMeasInfo.BUF_SIZE)
                        + 4 + (RptCcmReportCountInfo.BUF_SIZE * CXX_MAX_CELL_NUM)
                        + 4 + 4 + (RptCellPollingInfo.BUF_SIZE * CXX_MAX_CELL_EARFCN * 2)
                        + 4 + RptDlUserCatchInfo.BUF_SIZE));

    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public long getCell_search_flag() {
        return cell_search_flag;
    }

    public void setCell_search_flag(long cell_search_flag) {
        this.cell_search_flag = cell_search_flag;
    }

    public List<RptCellSearchInfo> getCell_search_infoList() {
        return cell_search_infoList;
    }

    public void setCell_search_infoList(List<RptCellSearchInfo> cell_search_infoList) {
        this.cell_search_infoList = cell_search_infoList;
    }

    public long getUl_power_flag() {
        return ul_power_flag;
    }

    public void setUl_power_flag(long ul_power_flag) {
        this.ul_power_flag = ul_power_flag;
    }

    public RptUlPowerMeasInfo getUl_power_meas_info() {
        return ul_power_meas_info;
    }

    public void setUl_power_meas_info(RptUlPowerMeasInfo ul_power_meas_info) {
        this.ul_power_meas_info = ul_power_meas_info;
    }

    public long getCounter_report_flag() {
        return counter_report_flag;
    }

    public void setCounter_report_flag(long counter_report_flag) {
        this.counter_report_flag = counter_report_flag;
    }

    public List<RptCcmReportCountInfo> getReport_count_infoList() {
        return report_count_infoList;
    }

    public void setReport_count_infoList(List<RptCcmReportCountInfo> report_count_infoList) {
        this.report_count_infoList = report_count_infoList;
    }

    public long getCell_polling_flag() {
        return cell_polling_flag;
    }

    public void setCell_polling_flag(long cell_polling_flag) {
        this.cell_polling_flag = cell_polling_flag;
    }

    public long getValid_cell_num() {
        return valid_cell_num;
    }

    public void setValid_cell_num(long valid_cell_num) {
        this.valid_cell_num = valid_cell_num;
    }

    public List<RptCellPollingInfo> getCcm_send_cell_polling_infoList() {
        return ccm_send_cell_polling_infoList;
    }

    public void setCcm_send_cell_polling_infoList(List<RptCellPollingInfo> ccm_send_cell_polling_infoList) {
        this.ccm_send_cell_polling_infoList = ccm_send_cell_polling_infoList;
    }

    public long getUser_catch_flag() {
        return user_catch_flag;
    }

    public void setUser_catch_flag(long user_catch_flag) {
        this.user_catch_flag = user_catch_flag;
    }

    public RptDlUserCatchInfo getDl_user_catch_info() {
        return dl_user_catch_info;
    }

    public void setDl_user_catch_info(RptDlUserCatchInfo dl_user_catch_info) {
        this.dl_user_catch_info = dl_user_catch_info;
    }

    @Override
    public String toString() {
        return "RptCellDetectResult{" +
                "cell_search_flag=" + cell_search_flag +
                ", cell_search_infoList=" + cell_search_infoList.toString() +
                ", ul_power_flag=" + ul_power_flag +
                ", ul_power_meas_info=" + ul_power_meas_info.toString() +
                ", counter_report_flag=" + counter_report_flag +
                ", report_count_infoList=" + report_count_infoList.toString() +
                ", cell_polling_flag=" + cell_polling_flag +
                ", valid_cell_num=" + valid_cell_num +
                ", ccm_send_cell_polling_infoList=" + ccm_send_cell_polling_infoList.toString() +
                ", user_catch_flag=" + user_catch_flag +
                ", dl_user_catch_info=" + dl_user_catch_info.toString() +
                '}';
    }
}
