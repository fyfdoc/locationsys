package com.dtm.locationsys.msgDef;

import com.dtm.locationsys.utils.ByteConvert;

import java.util.Arrays;

/**
 *  配置PCI/EARFCN消息结构
 */

public class SetCell {
    public static final int CXX_MAX_CELL_NUM = 6;
    public static final int BUF_SIZE = 4 + 2 + 2 + 2
            + 2*CXX_MAX_CELL_NUM + 2*CXX_MAX_CELL_NUM; // 字节数组大小

    public long  cell_update_flag;   //配置PCI/EARFCN标识
    public char cell_info_flag;      //为1表示打开锁定功能，为0标识关闭锁定功能
    public char num_of_cell;         //锁定小区个数
    public short pad;
    public short[] EARFCN_for_PCI = new short[CXX_MAX_CELL_NUM];  //EARFCN
    public short[] cell_id = new short[CXX_MAX_CELL_NUM];         //PCI

    /**
     * 构造方法，根据属性值构造类对象
     * @param cell_update_flag
     * @param cell_info_flag
     * @param num_of_cell
     * @param pad
     * @param EARFCN_for_PCI
     * @param cell_id
     */
    public SetCell(long cell_update_flag, char cell_info_flag, char num_of_cell
            , short pad, short[] EARFCN_for_PCI, short[] cell_id) {
        this.cell_update_flag = cell_update_flag;
        this.cell_info_flag = cell_info_flag;
        this.num_of_cell = num_of_cell;
        this.pad = pad;
        this.EARFCN_for_PCI = EARFCN_for_PCI;
        this.cell_id = cell_id;

        // 组长byte数组
        byte[] bytes = ByteConvert.uint2Bytes(cell_update_flag);
        System.arraycopy(bytes, 0, buf, 0, bytes.length);

        bytes = ByteConvert.char2Bytes(cell_info_flag);
        System.arraycopy(bytes, 0, buf, 0 + 4, bytes.length);

        bytes = ByteConvert.char2Bytes(num_of_cell);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 2, bytes.length);

        bytes = ByteConvert.short2Bytes(pad);
        System.arraycopy(bytes, 0, buf, 0 + 4 + 2 + 2, bytes.length);

        int index = 0;
        for(int i = 0; i < CXX_MAX_CELL_NUM; i++){
            index = 2 * (i+1);
            bytes = ByteConvert.short2Bytes(EARFCN_for_PCI[i]);
            System.arraycopy(bytes, 0, buf, 0 + 4 + 2 + 2 + index, bytes.length);
        }

        for(int i = 0; i < CXX_MAX_CELL_NUM; i++){
            index = 2 * (i);
            bytes = ByteConvert.short2Bytes(cell_id[i]);
            System.arraycopy(bytes, 0, buf
                    , 0 + 4 + 2 + 2 + 2 + 2*CXX_MAX_CELL_NUM + index, bytes.length);
        }


    }


    /**
     * 构造方法，根据byte[]构造类对象
     * @param bytes
     */
    public SetCell(byte[] bytes){
        this.cell_update_flag = ByteConvert.bytes2Uint(Arrays.copyOfRange(bytes, 0, 4));
        this.cell_info_flag = ByteConvert.bytes2Char(Arrays.copyOfRange(bytes, 4, 6));
        this.num_of_cell = ByteConvert.bytes2Char(Arrays.copyOfRange(bytes, 6, 8));
        this.pad = ByteConvert.bytes2Short(Arrays.copyOfRange(bytes, 8, 10));

        int index = 0;
        for(int i = 0; i < CXX_MAX_CELL_NUM; i++){
            index = 10 + i*2;
            this.EARFCN_for_PCI[i] = ByteConvert.bytes2Short(
                    Arrays.copyOfRange(bytes, index, index+2));
        }

        for(int i = 0; i < CXX_MAX_CELL_NUM; i++){
            index = 10 + CXX_MAX_CELL_NUM*2 + i*2;
            this.cell_id[i] = ByteConvert.bytes2Short(
                    Arrays.copyOfRange(bytes, index, index+2));
        }


    }

    // 属性的二进制结构
    private byte[] buf = new byte[BUF_SIZE];
    // 获取属性的二进制结构
    public byte[] getBuf (){
        return buf;
    }

    public String toString() {
        return "SetCell [cell_update_flag="
                + cell_update_flag
                + ", cell_info_flag="
                + cell_info_flag
                + ", num_of_cell="
                + num_of_cell
                + ", pad="
                + pad
                + ", EARFCN_for_PCI="
                + (EARFCN_for_PCI != null ? arrayToString(EARFCN_for_PCI,
                EARFCN_for_PCI.length) : null)
                + ", cell_id="
                + (cell_id != null ? arrayToString(cell_id, cell_id.length)
                : null) + "]";
    }
    private String arrayToString(Object array, int len) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (int i = 0; i < len; i++) {
            if (i > 0)
                buffer.append(", ");
            if (array instanceof short[])
                buffer.append(((short[]) array)[i]);
        }
        buffer.append("]");
        return buffer.toString();
    }
}
