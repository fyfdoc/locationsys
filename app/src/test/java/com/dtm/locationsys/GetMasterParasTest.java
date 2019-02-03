package com.dtm.locationsys;

import com.dtm.locationsys.msgDef.RptCcmReportCountInfo;
import com.dtm.locationsys.msgDef.RptCellPollingInfo;
import com.dtm.locationsys.msgDef.RptCellSearchInfo;
import com.dtm.locationsys.msgDef.RptDlUserCatchInfo;
import com.dtm.locationsys.msgDef.EquipmentVerifyReq;
import com.dtm.locationsys.msgDef.EquipmentVerifyRps;
import com.dtm.locationsys.msgDef.RptCellDetectResult;
import com.dtm.locationsys.msgDef.RptReportCountStatisticsPara;
import com.dtm.locationsys.msgDef.RptTmsiStatistics;
import com.dtm.locationsys.msgDef.RptUlPowerMeasInfo;
import com.dtm.locationsys.utils.ByteConvert;
import com.dtm.locationsys.utils.MsgCode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomato on 2017/9/16.
 */

public class GetMasterParasTest {

    @Test
    public void getMasterParasByIntTest(){
        // 构造获取守控小区参数结构
        RptCellDetectResult cellDetectReslut = new RptCellDetectResult(
                MsgCode.MSG_GET_PAR_REQ
                , (short) RptCellDetectResult.BUF_SIZE
                , 0xaaaaaaaa
                , 0x00000000
                , 0x00000000
                , 0x00000000
                , 0x00000000);

        System.out.println(cellDetectReslut.toString());

    }

    @Test
    public void getMasterParasTest(){

        //守控小区结果
        List<RptCellSearchInfo> cell_search_infoList = new ArrayList<>();
        for(int i = 0; i < RptCellDetectResult.CXX_MAX_CELL_NUM; i++){
            cell_search_infoList.add(i, new RptCellSearchInfo(
                    1,2,3,4,5,6,7,8
            ));
        }

        // 测向信息
        long[] i1 = {1,2,3,4};
        RptUlPowerMeasInfo ul_power_meas_info =
                new RptUlPowerMeasInfo(2,3,4,5,i1);


        //设备底层统计消息
        List<RptCcmReportCountInfo> report_count_infoList = new ArrayList<>();
        for (int i = 0; i < RptCellDetectResult.CXX_MAX_CELL_NUM; i++){
            RptReportCountStatisticsPara statistics_para = new RptReportCountStatisticsPara(
                    1,2,3,4,5,6,7,8
            );
            List<RptTmsiStatistics> tmsi_tableList = new ArrayList<>();
            for (int j = 0; j < RptCcmReportCountInfo.CXX_MAX_TMSI_NUM; j++){
                tmsi_tableList.add(j, new RptTmsiStatistics(
                        1,2,3
                ));
            }

            report_count_infoList.add(i, new RptCcmReportCountInfo(
                    5, 1, statistics_para, tmsi_tableList
            ));
        }

        // 小区轮询结果
        int valid_cell_num = 6;
        List<RptCellPollingInfo>  ccm_send_cell_polling_infoList = new ArrayList<>();
        for (int i = 0; i < RptCellDetectResult.CXX_MAX_CELL_EARFCN * 2; i++){
            ccm_send_cell_polling_infoList.add(i, new RptCellPollingInfo(
                    1,2,3,4,1,10,1,12,13
            ));
        }


        //中标信息
        RptDlUserCatchInfo dl_user_catch_info = new RptDlUserCatchInfo(
                2,3,4,5,6,7,8
        );


        RptCellDetectResult r1 = new RptCellDetectResult(
                cell_search_infoList
                , ul_power_meas_info
                , report_count_infoList
                , valid_cell_num
                , ccm_send_cell_polling_infoList
                , dl_user_catch_info
        );

        byte[] bytes = r1.getBuf();


        RptCellDetectResult r2 = new RptCellDetectResult(bytes);

        System.out.println(r1.toString());
        System.out.println(r2.toString());

        System.out.print("RptCellDetectResult.BUF_SIZE=" + RptCellDetectResult.BUF_SIZE);

    }


    @Test
    public void GetMsg_ccm_om_ul_power_meas_info_test() {
        int EARFCN = 1;        //:16; //中标用户所在小区中心频点（折算为载波号）
        int cellID = 2;        //:9; //中标用户所在小区PCI
        int ul_rf_gain = 3;    //:7; //上行增益信息（界面不显示）
        long m_TMSI = 4;        //中标用户TMSI
        // 中标用户各用户功率，需要转换为dB值，
        // 转换公式10*log10(ant_power) + 20*(3 - ul_rf_gain)，(纵坐标范围0~160)
        long[] ant_power = {1L,2L,3L,4L};

        RptUlPowerMeasInfo rptUlPowerMeasInfo =
                new RptUlPowerMeasInfo(
                        EARFCN
                        , cellID
                        , ul_rf_gain
                        , m_TMSI
                        , ant_power);

        System.out.println(rptUlPowerMeasInfo.toString());
        System.out.println("BUF_SIZE=" + rptUlPowerMeasInfo.BUF_SIZE);

        RptUlPowerMeasInfo rptUlPowerMeasInfo2 =
                new RptUlPowerMeasInfo(
                        rptUlPowerMeasInfo.getBuf());

        System.out.println(rptUlPowerMeasInfo2.toString());

    }

    @Test
    public void GetYxx_ccm_send_om_cell_info_test() {
        int valid_flag = 1;        // :1; 指示小区是否有效，为0不需要显示本小区，为1需要显示下面信息
        int cellIdentity = 2;      // :28; //小区CID
        int freq_band = 3;         // :3; //频段（界面不显示）

        int PCI = 4;               // :9; //PCI
        int rf_gain = 5;           // :5; //增益（界面不显示）
        int cell_power = 6;        // :18; //功率（需要折算为dB，公式见下：）

        int EARFCN = 7;            // :16; //频点（需要转换为频点号，公式见下：）
        int trackingAreaCode = 8;  // :16; //小区TAC

        RptCellSearchInfo rptCellSearchInfo =
                new RptCellSearchInfo(
                        valid_flag
                        , cellIdentity
                        , freq_band
                        , PCI
                        , rf_gain
                        , cell_power
                        , EARFCN
                        , trackingAreaCode);

        System.out.println(rptCellSearchInfo.toString());

        // 解析byte[]为类对象
        RptCellSearchInfo rptCellSearchInfo2 =
                new RptCellSearchInfo(rptCellSearchInfo.getBuf());

        System.out.println(rptCellSearchInfo2.toString());

    }

    @Test
    public void GetYxx_dl_user_catch_info_test() {

        int EARFCN = 1;        //:16; //中标用户频点（折算为载波号）
        int cellID = 2;        //:9; //中标用户所在PCI
        int pad = 3;           //:7;

        int crnti = 4;         //:16; //中标用户CRNTI
        int ta = 5;            //:11; //中标用户下行TA
        int pad1 = 6;          //:5;

        long m_TMSI = 7;       //:Uint 中标用户TMSI

        RptDlUserCatchInfo rptDlUserCatchInfo =
                new RptDlUserCatchInfo(
                        EARFCN
                        , cellID
                        , pad
                        , crnti
                        , ta
                        , pad1
                        , m_TMSI
                        );

        System.out.println(rptDlUserCatchInfo.toString());

        RptDlUserCatchInfo rptDlUserCatchInfo2 =
                new RptDlUserCatchInfo(rptDlUserCatchInfo.getBuf());

        System.out.println(rptDlUserCatchInfo2.toString());

    }

    @Test
    public void GetYxx_statistics_para_test(){
        long sib1_num = 1; //SIB1调度次数
        long sib2_num = 2; //SIB2调度次数
        long sib1_detected_num = 3; //SIB1检测次数
        long sib2_detected_num = 4; //SIB2检测次数
        long msg1_detected_num = 5; //MSG1检测次数
        long msg2_detected_num = 6; //MSG2检测次数
        long msg3_detected_num = 7; //MSG3检测次数
        long msg4_detected_num = 8; //MSG4检测次数

        RptReportCountStatisticsPara rptReportCountStatisticsPara =
                new RptReportCountStatisticsPara(
                        sib1_num
                        , sib2_num
                        , sib1_detected_num
                        , sib2_detected_num
                        , msg1_detected_num
                        , msg2_detected_num
                        , msg3_detected_num
                        , msg4_detected_num
                );

        System.out.println(rptReportCountStatisticsPara.toString());

        RptReportCountStatisticsPara rptReportCountStatisticsPara2 =
                new RptReportCountStatisticsPara(rptReportCountStatisticsPara.getBuf() );

        System.out.println(rptReportCountStatisticsPara2.toString());


    }

    @Test
    public void GetYxx_tmsi_statistics_test() {

        int valid = 1;  //:1; //1为有效，有效才需要统计该TMSI
        int count = 2;  //:31; //需要累计设备上电后收到该TMSI的次数
        long m_tmsi = 3; //TMSI :Uint

        RptTmsiStatistics rptTmsiStatistics =
                new RptTmsiStatistics(
                        valid
                        , count
                        , m_tmsi
                );

        System.out.println(rptTmsiStatistics.toString());

        RptTmsiStatistics rptTmsiStatistics2 =
                new RptTmsiStatistics(rptTmsiStatistics.getBuf());

        System.out.println(rptTmsiStatistics2.toString());

    }

    @Test
    public void GetMsg_om_ccm_report_count_test() {

        int cellID = 1; // :Ushort 统计信息小区PCI
        int EARFCN = 2; // :Ushort 统计信息小区频点（折算为载波号）


        long sib1_num = 1; //SIB1调度次数
        long sib2_num = 2; //SIB2调度次数
        long sib1_detected_num = 3; //SIB1检测次数
        long sib2_detected_num = 4; //SIB2检测次数
        long msg1_detected_num = 5; //MSG1检测次数
        long msg2_detected_num = 6; //MSG2检测次数
        long msg3_detected_num = 7; //MSG3检测次数
        long msg4_detected_num = 8; //MSG4检测次数

        RptReportCountStatisticsPara statistics_para =
                new RptReportCountStatisticsPara(
                        sib1_num
                        , sib2_num
                        , sib1_detected_num
                        , sib2_detected_num
                        , msg1_detected_num
                        , msg2_detected_num
                        , msg3_detected_num
                        , msg4_detected_num
                );

        List<RptTmsiStatistics> tmsi_tableList = new ArrayList<>();
        for (int i = 0; i < RptCcmReportCountInfo.CXX_MAX_TMSI_NUM; i++) {
            int valid = 1;  //:1; //1为有效，有效才需要统计该TMSI
            int count = 2;  //:31; //需要累计设备上电后收到该TMSI的次数
            long m_tmsi = 3; //TMSI :Uint

            RptTmsiStatistics rptTmsiStatistics =
                    new RptTmsiStatistics(
                            valid
                            , count
                            , m_tmsi
                    );
            tmsi_tableList.add(i, rptTmsiStatistics);
        }


        RptCcmReportCountInfo rptCcmReportCountInfo =
                new RptCcmReportCountInfo(
                        cellID
                        , EARFCN
                        , statistics_para
                        , tmsi_tableList
                );

        System.out.println(rptCcmReportCountInfo.toString());

        RptCcmReportCountInfo rptCcmReportCountInfo2 =
                new RptCcmReportCountInfo(rptCcmReportCountInfo.getBuf());

        System.out.println(rptCcmReportCountInfo2.toString());
    }

    @Test
    public void GetYxx_ccm_send_om_cell_polling_info_test() {
        int valid_flag = 1;      //:1; //小区有效标识
        int earfcn = 2;          //:16; //小区频点（需要折算为频点号）
        int rf_gain = 3;         //:5; //下行增益（界面不显示）
        int pci = 4;             //:9; //小区PCI
        int pad1 = 1;            //:1;

        int cell_power = 6;      //:18; //小区功率（折算为dB值）
        int freq_band = 7;       //:3; //频带
        int earfcn_index = 8;    //:6; //频点索引（界面不用）
        int pad = 9;             //:5;


        RptCellPollingInfo rptCellPollingInfo =
                new RptCellPollingInfo(
                        valid_flag
                        , earfcn
                        , rf_gain
                        , pci
                        , pad1
                        , cell_power
                        , freq_band
                        , earfcn_index
                        , pad
                );

        System.out.println(rptCellPollingInfo.toString());

        RptCellPollingInfo rptCellPollingInfo2 =
                new RptCellPollingInfo(
                        rptCellPollingInfo.getBuf());

        System.out.println(rptCellPollingInfo2.toString());

    }

    @Test
    public void EquipmentVerifyReq_test() {
        short msgType = 1; // 消息类型

        short msgLength = 2; // 消息长度

        // 0xaaaaaaa1: 表示请求用户名密码验证;
        // 0xaaaaaaa2: 表示请求验证码验证;
        // 0xaaaaaaaa: 表示用户名密码和验证码同时验证;
        long msgFlag = 0xaaaaaaa1L;

        char[] username = new char[32]; //用户名
        username[0] = 'f';
        username[1] = 'y';
        username[2] = 'f';

        char[] password = new char[32]; //密码
        password[0] = '1';
        password[1] = '2';
        password[2] = '3';
        password[3] = '4';

        char[] phoneNum = new char[16]; //待测手机号
        phoneNum[0] = '1';
        phoneNum[1] = '3';
        phoneNum[2] = '4';
        phoneNum[3] = '6';
        phoneNum[4] = '6';
        phoneNum[5] = '7';
        phoneNum[6] = '7';
        phoneNum[7] = '6';
        phoneNum[8] = '6';
        phoneNum[9] = '3';
        phoneNum[10] = '9';

//    public long scrambleCode; //扰码

        long verifyCode = 99L; //验证码

        EquipmentVerifyReq equipmentVerifyReq = new EquipmentVerifyReq(
                msgFlag
                , username
                , password
                , phoneNum
                , verifyCode
        );

        System.out.println(equipmentVerifyReq.toString());

        EquipmentVerifyReq equipmentVerifyReq2 = new EquipmentVerifyReq(
                equipmentVerifyReq.getBuf());

        System.out.println(equipmentVerifyReq2.toString());


    }

    @Test
    public void EquipmentVerifyRps_test() {

        short msgType = 123;

        short msgLength = 1;

        // Uint
        long pad = 0L;

        // 返回验证结果, 0x00001111:表示用户名密码验证码都验证通过;0xffffffff表示验证失败
        // Uint
        long status = 0xffffffffL;

        EquipmentVerifyRps equipmentVerifyRps =
                new EquipmentVerifyRps(
                        pad
                        , status
                );

        System.out.println(equipmentVerifyRps);

        EquipmentVerifyRps equipmentVerifyRps2 =
                new EquipmentVerifyRps(equipmentVerifyRps.getBuf());

        System.out.println(equipmentVerifyRps2);


    }

    @Test
    public void numTest() {
        long l1 = -12345L;

        byte[] bytes = ByteConvert.long2Bytes(l1);

        long l2 = ByteConvert.bytes2Long(bytes);

        System.out.println("bytes=" + ByteConvert.bytes2Bit(bytes));
        System.out.println("l2=" + l2);
    }
}
