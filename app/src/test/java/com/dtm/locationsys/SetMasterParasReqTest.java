package com.dtm.locationsys;

import com.dtm.locationsys.msgDef.SetAssist;
import com.dtm.locationsys.msgDef.SetCell;
import com.dtm.locationsys.msgDef.SetDebug;
import com.dtm.locationsys.msgDef.SetDirectionTime;
import com.dtm.locationsys.msgDef.SetFrameType;
import com.dtm.locationsys.msgDef.SetFreqBand;
import com.dtm.locationsys.msgDef.SetLog;
import com.dtm.locationsys.msgDef.SetMasterParasReq;
import com.dtm.locationsys.msgDef.SetOperator;
import com.dtm.locationsys.msgDef.SetTmsi;
import com.dtm.locationsys.msgDef.SetWorkMode;

import org.junit.Test;

/**
 * Created by tomato on 2017/9/16.
 */

public class SetMasterParasReqTest {

    @Test
    public void setMasterParasTest(){


        //配置PCI/EARFCN标识
        long cell_update_flag = 0xaaaaaaaaL;
        char cell_info_flag = '1';
        char num_of_cell = '6';
        short pad = 0;
        short[] EARFCN_for_PCI = {11,22,33,44,55,66};
        short[] cell_id = {1,2,3,4,5,6};
        SetCell setCell = new SetCell(cell_update_flag, cell_info_flag, num_of_cell, pad, EARFCN_for_PCI , cell_id);
        System.out.println(setCell.toString());

        long tmsi_update_flag = 0xbbbbbbbbL;
        int tmsi_flag = 1;
        int tmsi = 21;
        //配置TMSI标识
        SetTmsi setTmsi = new SetTmsi(tmsi_update_flag, tmsi_flag, tmsi);
        System.out.println(setTmsi.toString());

        long assist_info_update_flag = 0xccccccccL;
        int assist_info_flag = 1;
        int assist_info = 31;
        //配置辅助信息标识
        SetAssist setAssist = new SetAssist(assist_info_update_flag, assist_info_flag, assist_info);
        System.out.println(setAssist.toString());

        long freqband_update_flag = 0xddddddddL;
        short earfcn_info_flag = 1;
        short EARFCN = 235;
        //配置频率信息标识
        SetFreqBand setFreqBand = new SetFreqBand(freqband_update_flag, earfcn_info_flag, EARFCN);
        System.out.println(setFreqBand.toString());

        long log_open_update_flag = 0xeeeeeeeeL;
        int log_open_flag = 1;
        //配置日志标识
        SetLog setLog = new SetLog(log_open_update_flag, log_open_flag);
        System.out.println(setLog.toString());

        long debug_info_update_flag = 0xffffffffL;
        int debug_info_flag = 1;
        int[] debug_info = {1,2};
        //debug配置标识
        SetDebug setDebug = new SetDebug(debug_info_update_flag, debug_info_flag, debug_info);
        System.out.println(setDebug.toString());

        long work_mode_flag = 0xabcdef11L;
        int work_mode = 1;
        //配置系统工作模式
        SetWorkMode setWorkMode = new SetWorkMode(work_mode_flag, work_mode);
        System.out.println(setWorkMode.toString());

        long frame_type_flag = 0xabcdef12L;
        int frame_type = 1;
        //配置小区工作制式
        SetFrameType setFrameType = new SetFrameType(frame_type_flag, frame_type);
        System.out.println(setFrameType.toString());

        //配置小区工作制式
        long operator_flag = 0xabcdef13L;
        int operator_type = 12;
        SetOperator setOperator = new SetOperator(operator_flag, operator_type);
        System.out.println(setOperator.toString());

        //配置小区工作制式
        long direction_time_flag = 0xabcdef14L;
        int direction_time = 13;
        SetDirectionTime setDirectionTime = new SetDirectionTime(direction_time_flag
                , direction_time);
        System.out.println(setDirectionTime.toString());


        // 基站参数设置
        short msgType = 1001;
        short msgLength = 1024;
        SetMasterParasReq setMasterParasReq = new SetMasterParasReq(setCell,
                setTmsi, setAssist, setFreqBand, setLog, setDebug, setWorkMode, setFrameType
                , setOperator, setDirectionTime);

//        SetMasterParasReq setMasterParasReq = new SetMasterParasReq(msgType, msgLength, null,
//                null, null, null, null, null, null, null, null , null);

        byte[] par1 = setMasterParasReq.getBuf();

        SetMasterParasReq m2 = new SetMasterParasReq(par1);

        System.out.println(m2.toString());

        System.out.println("setMasterParasReq.BUF_SIZE=" + setMasterParasReq.BUF_SIZE);


    }
}
