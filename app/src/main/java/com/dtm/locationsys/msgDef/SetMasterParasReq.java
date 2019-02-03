package com.dtm.locationsys.msgDef;

import java.util.Arrays;

/**
 * 下发参数配置消息定义
 *
 * (1) 配置PCI/EARFCN, cell_update_flag约定为0xaaaaaaaa
 * (2) 配置TMSI, tmsi_update_flag约定为0xbbbbbbbb
 * (3) 配置辅助信息, assist_info_update_flag约定为0xcccccccc
 * (4) 配置频点, freqband_update_flag约定为0xdddddddd
 * (5) 配置日志记录开关, log_open_update_flag约定为0xeeeeeeee
 * (6) 配置DEBUG信息, debug_info_update_flag约定为0xffffffff
 * (7) 配置系统工作模式, work_mode_flag约定为0xabcdef11
 * (8) 配置系统工作制式, frame_type_flag约定为0xabcdef12
*/
public class SetMasterParasReq {
    // 字节数组大小
    public static final int BUF_SIZE = SetCell.BUF_SIZE + SetTmsi.BUF_SIZE
            + SetAssist.BUF_SIZE + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE
            + SetDebug.BUF_SIZE + SetWorkMode.BUF_SIZE + SetFrameType.BUF_SIZE
            + SetOperator.BUF_SIZE + SetDirectionTime.BUF_SIZE;

    // 配置PCI/EARFCN
    public SetCell mSetCell;

    // 配置TMSI
    public SetTmsi mSetTmsi;

    // 配置辅助信息
    public SetAssist mSetAssist;

    // 配置频点
    public SetFreqBand mSetFreqBand;

    // 配置日志记录开关
    public SetLog mSetLog;

    // 配置DEBUG信息
    public SetDebug mSetDebug;

    // 配置系统工作模式
    public SetWorkMode mSetWorkMode;

    // 配置系统工作制式
    public SetFrameType mSetFrameType;

    // 配置运营商
    public SetOperator mSetOperator;

    // 配置测向时长
    public SetDirectionTime mSetDirectionTime;

    /**
     * 构造方法，根据属性值构造类对象
     * @param setCell
     * @param setTmsi
     * @param setAssist
     * @param setFreqBand
     * @param setLog
     * @param setDebug
     * @param setWorkMode
     * @param setFrameType
     * @param setOperator
     * @param setDirectionTime
     */
    public SetMasterParasReq(SetCell setCell
            , SetTmsi setTmsi, SetAssist setAssist, SetFreqBand setFreqBand
            , SetLog setLog, SetDebug setDebug, SetWorkMode setWorkMode
            , SetFrameType setFrameType, SetOperator setOperator
            , SetDirectionTime setDirectionTime){

        byte[] bytes;

        // 配置PCI/EARFCN
        if(setCell != null){ //如果参数不为空，使用参数构建数据
            mSetCell = setCell;
            // 设置标识
            mSetCell.cell_update_flag = 0xaaaaaaaaL;
        }else{ // 如果参数为空，使用默认值构建参数
            mSetCell = new SetCell(0x00000000L,'0', '0',(short)0,
                    new short[SetCell.CXX_MAX_CELL_NUM], new short[SetCell.CXX_MAX_CELL_NUM]);
        }
        // 构造byte数组
        System.arraycopy(mSetCell.getBuf(), 0, buf, 0, SetCell.BUF_SIZE);


        // 配置TMSI
        if(setTmsi != null){ //如果参数不为空，使用参数构建数据
            mSetTmsi = setTmsi;
            // 设置标志
            mSetTmsi.tmsi_update_flag = 0xbbbbbbbbL;
        }else{ // 如果参数为空，使用默认值构建参数
            mSetTmsi = new SetTmsi(0x00000000L, 0, 0);
        }
        // 构造byte数组
        System.arraycopy(mSetTmsi.getBuf(), 0, buf
                , 0 + SetCell.BUF_SIZE, SetTmsi.BUF_SIZE);


        // 配置辅助信息
        if(setAssist != null){ //如果参数不为空，使用参数构建数据
            mSetAssist = setAssist;
            // 设置标志
            mSetAssist.assist_info_update_flag = 0xccccccccL;
        }else{ // 如果参数为空，使用默认值构建参数
            mSetAssist = new SetAssist(0x00000000L, 0, 0);
        }
        // 构造byte数组
        System.arraycopy(mSetAssist.getBuf(), 0, buf
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE, SetAssist.BUF_SIZE);


        // 配置频点
        if(setFreqBand != null){ //如果参数不为空，使用参数构建数据
            mSetFreqBand = setFreqBand;
            // 设置标志
            mSetFreqBand.freqband_update_flag = 0xddddddddL;
        }else{ // 如果参数为空，使用默认值构建参数
            mSetFreqBand = new SetFreqBand(0x00000000L, (short)0, (short)0);
        }
        // 构造byte数组
        System.arraycopy(mSetFreqBand.getBuf(), 0, buf, 0 + SetCell.BUF_SIZE
                + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE, SetFreqBand.BUF_SIZE);


        // 配置日志记录开关
        if(setLog != null){ //如果参数不为空，使用参数构建数据
            mSetLog = setLog;
            // 设置标志
            mSetLog.log_open_update_flag = 0xeeeeeeeeL;
        }else{ // 如果参数为空，使用默认值构建参数
            mSetLog = new SetLog(0x00000000L, 0);
        }
        // 构造byte数组
        System.arraycopy(mSetLog.getBuf(), 0, buf, 0 + SetCell.BUF_SIZE
                + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE + SetFreqBand.BUF_SIZE
                , SetLog.BUF_SIZE);

        // 配置DEBUG信息
        if(setDebug != null){ //如果参数不为空，使用参数构建数据
            mSetDebug = setDebug;
            // 设置标志
            mSetDebug.debug_info_update_flag = 0xffffffffL;
        }else{ // 如果参数为空，使用默认值构建参数
            mSetDebug = new SetDebug(0x00000000L, 0, new int[SetDebug.DEBUG_MAX]);
        }
        // 构造byte数组
        System.arraycopy(mSetDebug.getBuf(), 0, buf, 0 + SetCell.BUF_SIZE
                        + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE + SetFreqBand.BUF_SIZE
                + SetLog.BUF_SIZE, SetDebug.BUF_SIZE);


        // 配置系统工作模式
        if(setWorkMode != null){ //如果参数不为空，使用参数构建数据
            mSetWorkMode = setWorkMode;
            // 设置标志
            mSetWorkMode.work_mode_flag = 0xabcdef11L;
        }else{ // 如果参数为空，使用默认值构建参数
            mSetWorkMode = new SetWorkMode(0x00000000L, 0);
        }
        // 构造byte数组
        System.arraycopy(mSetWorkMode.getBuf(), 0, buf, 0 + SetCell.BUF_SIZE
                + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE + SetFreqBand.BUF_SIZE
                + SetLog.BUF_SIZE + SetDebug.BUF_SIZE, SetWorkMode.BUF_SIZE);


        // 配置系统工作制式
        if(setFrameType != null){ //如果参数不为空，使用参数构建数据
            mSetFrameType = setFrameType;
            // 设置标志
            mSetFrameType.frame_type_flag = 0xabcdef12L;
        }else{ // 如果参数为空，使用默认值构建参数
            mSetFrameType = new SetFrameType(0x00000000L, 0);
        }
        // 构造byte数组
        System.arraycopy(mSetFrameType.getBuf(), 0, buf, 0 + SetCell.BUF_SIZE
                + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE + SetFreqBand.BUF_SIZE
                + SetLog.BUF_SIZE + SetDebug.BUF_SIZE + SetWorkMode.BUF_SIZE,
                SetFrameType.BUF_SIZE);


        // 配置运营商
        if(setOperator != null){ //如果参数不为空，使用参数构建数据
            mSetOperator = setOperator;
            // 设置标志
            mSetOperator.operator_flag = 0xabcdef13L;
        }else{ // 如果参数为空，使用默认值构建参数
            mSetOperator = new SetOperator(0x00000000L, 0);
        }
        // 构造byte数组
        System.arraycopy(mSetOperator.getBuf(), 0, buf
                , 0 + SetCell.BUF_SIZE
                        + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE + SetFreqBand.BUF_SIZE
                        + SetLog.BUF_SIZE + SetDebug.BUF_SIZE + SetWorkMode.BUF_SIZE
                        + SetFrameType.BUF_SIZE,
                mSetOperator.BUF_SIZE);

        // 配置测向时长
        if(setDirectionTime != null){ //如果参数不为空，使用参数构建数据
            mSetDirectionTime = setDirectionTime;
            // 设置标志
            mSetDirectionTime.direction_time_flag = 0xabcdef14L;
        }else{ // 如果参数为空，使用默认值构建参数
            mSetDirectionTime = new SetDirectionTime(0x00000000L, 0);
        }
        // 构造byte数组
        System.arraycopy(mSetDirectionTime.getBuf(), 0, buf
                , 0 + SetCell.BUF_SIZE
                        + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE + SetFreqBand.BUF_SIZE
                        + SetLog.BUF_SIZE + SetDebug.BUF_SIZE + SetWorkMode.BUF_SIZE
                        + SetFrameType.BUF_SIZE + SetOperator.BUF_SIZE,
                mSetDirectionTime.BUF_SIZE);
    }

    /**
     * 构造方法，根据bytes构造类对象
     * @param bytes
     */
    public SetMasterParasReq(byte[] bytes){
        //配置PCI/EARFCN标识
        byte[] b1 = Arrays.copyOfRange(bytes
                , 0
                , 0 + SetCell.BUF_SIZE);
        mSetCell = new SetCell(b1);

        //配置TMSI标识
        b1 = Arrays.copyOfRange(bytes
                , 0 + SetCell.BUF_SIZE
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE);
        mSetTmsi = new SetTmsi(b1);

        //配置辅助信息标识
        b1 = Arrays.copyOfRange(bytes
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE);
        mSetAssist = new SetAssist(b1);

        //配置频率信息标识
        b1 = Arrays.copyOfRange(bytes
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE);
        mSetFreqBand = new SetFreqBand(b1);

        //配置日志标识
        b1 = Arrays.copyOfRange(bytes
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE);
        mSetLog = new SetLog(b1);

        //debug配置标识
        b1 = Arrays.copyOfRange(bytes
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE + SetDebug.BUF_SIZE);
        mSetDebug = new SetDebug(b1);

        //配置系统工作模式
        b1 = Arrays.copyOfRange(bytes
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE + SetDebug.BUF_SIZE
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE + SetDebug.BUF_SIZE
                        + SetWorkMode.BUF_SIZE);
        mSetWorkMode = new SetWorkMode(b1);

        //配置小区工作制式
        b1 = Arrays.copyOfRange(bytes
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE + SetDebug.BUF_SIZE
                        + SetWorkMode.BUF_SIZE
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE + SetDebug.BUF_SIZE
                        + SetWorkMode.BUF_SIZE + SetFrameType.BUF_SIZE);
        mSetFrameType = new SetFrameType(b1);

        //配置运营商
        b1 = Arrays.copyOfRange(bytes
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE + SetDebug.BUF_SIZE
                        + SetWorkMode.BUF_SIZE + SetFrameType.BUF_SIZE
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE + SetDebug.BUF_SIZE
                        + SetWorkMode.BUF_SIZE + SetFrameType.BUF_SIZE + SetOperator.BUF_SIZE);
        mSetOperator = new SetOperator(b1);

        //配置配置测向时长
        b1 = Arrays.copyOfRange(bytes
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE + SetDebug.BUF_SIZE
                        + SetWorkMode.BUF_SIZE + SetFrameType.BUF_SIZE + mSetOperator.BUF_SIZE
                , 0 + SetCell.BUF_SIZE + SetTmsi.BUF_SIZE + SetAssist.BUF_SIZE
                        + SetFreqBand.BUF_SIZE + SetLog.BUF_SIZE + SetDebug.BUF_SIZE
                        + SetWorkMode.BUF_SIZE + SetFrameType.BUF_SIZE + SetOperator.BUF_SIZE
                        + SetDirectionTime.BUF_SIZE);
        mSetDirectionTime = new SetDirectionTime(b1);
    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    @Override
    public String toString() {
        return "SetMasterParasReq{" +
                "mSetCell=" + mSetCell.toString() +
                ", mSetTmsi=" + mSetTmsi.toString() +
                ", mSetAssist=" + mSetAssist.toString() +
                ", mSetFreqBand=" + mSetFreqBand.toString() +
                ", mSetLog=" + mSetLog.toString() +
                ", mSetDebug=" + mSetDebug.toString() +
                ", mSetWorkMode=" + mSetWorkMode.toString() +
                ", mSetFrameType=" + mSetFrameType.toString() +
                ", mSetOperator=" + mSetOperator.toString() +
                ", mSetDirectionTime=" + mSetDirectionTime.toString() +
                '}';
    }
}
