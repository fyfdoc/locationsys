package com.dtm.locationsys;

import com.dtm.locationsys.utils.ArfcnConvert;
import com.dtm.locationsys.utils.ByteConvert;

import org.junit.Test;

import java.util.Arrays;

/**
 * .
 */

public class ArfcnCovertTest {
    @Test
    public void getDLBandIdByArfcnTest(){
        //
        ArfcnConvert arfcnConvert = new ArfcnConvert();

        // 2180
        int arfcnF = 2184;
        int bandId = arfcnConvert.getDLBandIdByArfcn(arfcnF);

        System.out.println("arfcnF=" + arfcnF + "bandId=" + bandId);

    }

    @Test
    public void getULBandIdByArfcnTest(){
        //
        ArfcnConvert arfcnConvert = new ArfcnConvert();

        // 1900 MHz	–	1920 MHz
        int arfcnF = 1900;
        int bandId = arfcnConvert.getULBandIdByArfcn(arfcnF);

        System.out.println("arfcnF=" + arfcnF + "bandId=" + bandId);

    }

    @Test
    public void BinaryTest(){
        int valid_flag = 0;      // :1; //小区有效标识
        int earfcn = 12;        // :16; //小区频点（需要折算为频点号）
        int rf_gain = 15;         // :5; //下行增益（界面不显示）
        int pci = 67;            // :9; //小区PCI
        int pad1 = 0;            // :1;


        // 构造后的字节数组
        byte[] bytes = new byte[4];

        String s1 = "";
        String s2 = "";
        String s3 = "";
        String s4 = "";
        String bitCut = "";

        // bit字符串拼接结果
        StringBuffer rs = new StringBuffer();

        // valid_flag
        System.out.println("===============valid_flag start===================");
        byte[] par_B = ByteConvert.int2Bytes(valid_flag);
        s1 = ByteConvert.byte2Bit(par_B[3]);
        bitCut = s1.substring(7,8);
        rs.append(bitCut);
//        rs.append("-");

        System.out.println("s1=" + s1);
        System.out.println("s2=" + s2);
        System.out.println("s3=" + s3);
        System.out.println("s4=" + s4);
        System.out.println("bitCut=" + bitCut);
        System.out.println("rs=" + rs.toString());

        // earfcn
        System.out.println("===============earfcn start===================");
        par_B = ByteConvert.int2Bytes(earfcn);
        s1 = ByteConvert.byte2Bit(par_B[2]);
        s2 = ByteConvert.byte2Bit(par_B[3]);
        rs.append(s1);
        rs.append(s2);
//        rs.append("-");

        System.out.println("s1=" + s1);
        System.out.println("s2=" + s2);
        System.out.println("s3=" + s3);
        System.out.println("s4=" + s4);
        System.out.println("bitCut=" + bitCut);
        System.out.println("rs=" + rs.toString());

        // rf_gain
        System.out.println("===============rf_gain start===================");
        par_B = ByteConvert.int2Bytes(rf_gain);
        s1 = ByteConvert.byte2Bit(par_B[3]);
        bitCut = s1.substring(3,8);
        rs.append(bitCut);
//        rs.append("-");

        System.out.println("s1=" + s1);
        System.out.println("s2=" + s2);
        System.out.println("s3=" + s3);
        System.out.println("s4=" + s4);
        System.out.println("bitCut=" + bitCut);
        System.out.println("rs=" + rs.toString());

        // pci
        System.out.println("===============pci start===================");
        par_B = ByteConvert.int2Bytes(pci);
        s1 = ByteConvert.byte2Bit(par_B[2]);
        s2 = ByteConvert.byte2Bit(par_B[3]);
        bitCut = s1.substring(7,8);
        rs.append(bitCut);
        rs.append(s2);

//        rs.append("-");

        System.out.println("s1=" + s1);
        System.out.println("s2=" + s2);
        System.out.println("s3=" + s3);
        System.out.println("s4=" + s4);
        System.out.println("bitCut=" + bitCut);
        System.out.println("rs=" + rs.toString());

        // pad1
        System.out.println("===============pad1 start===================");
        par_B = ByteConvert.int2Bytes(pad1);
        s1 = ByteConvert.byte2Bit(par_B[3]);
        bitCut = s1.substring(7,8);
        rs.append(bitCut);
//        rs.append("-");

        System.out.println("s1=" + s1);
        System.out.println("s2=" + s2);
        System.out.println("s3=" + s3);
        System.out.println("s4=" + s4);
        System.out.println("bitCut=" + bitCut);
        System.out.println("rs=" + rs.toString());

        byte[] parBytes = new byte[4];

        // 将bit字符串转换为byte数组
        for (int i = 0; i < rs.length()/32; i++) {
            int index = i * 32;
            String str4 = rs.substring(0,32);
            int int1 = Integer.parseInt(str4, 2);
            byte[] bs1 = ByteConvert.int2Bytes(int1);
            System.arraycopy(bs1, 0, parBytes, index, bs1.length);

        }

        System.out.println("parBytes=" + Arrays.toString(parBytes));
        System.out.println("parBytes=" + ByteConvert.bytes2Bit(parBytes));



        // ============================解析================
        String strParBits = ByteConvert.bytes2Bit(parBytes);

        int i1 = Integer.parseInt(strParBits.substring(0,1),2);
//        int i2 = Integer.parseInt(strParBits.substring(9,17) + strParBits.substring(1,9),2);
        int i2 = Integer.parseInt(strParBits.substring(1,17),2);
        int i3 = Integer.parseInt(strParBits.substring(17,22),2);
//        int i4 = Integer.parseInt(strParBits.substring(30,31) + strParBits.substring(22,30),2);
        int i4 = Integer.parseInt(strParBits.substring(22,31),2);
        int i5 = Integer.parseInt(strParBits.substring(31,32),2);

        System.out.println("i1=" + i1);
        System.out.println("i2=" + i2);
        System.out.println("i3=" + i3);
        System.out.println("i4=" + i4);
        System.out.println("i5=" + i5);


//        System.out.println("int=" + Integer.parseInt("001111111111111111111111111111111", 2));











    }


}
