package com.dtm.locationsys.utils;

import java.text.DecimalFormat;

/**
 * EARFCN转换工具类.
 */

public class ArfcnConvert {
    private static int ARFCN_COUNT = 45;

    private static  ARFCN_VAL_PAIR[]  arfcn_val_pairs;

    // ARFCN结构
    public class ARFCN_VAL_PAIR {
        // 下行
        public int N_DL_low;  // 下行频点号下限
        public int N_DL_high; // 下行频点号上限
        public int F_DL_low;  // 下行频点
        public int F_DL_high; // 最低下行频点

        // 上行
        public int N_UL_low;  // 上行频点号
        public int N_UL_high; // 最低上行频点号
        public int F_UL_low;  // 上行频点
        public int F_UL_high; // 最低上行频点
    }

    /**
     * 构造方法
     */
    public ArfcnConvert() {
        arfcn_val_pairs = new ARFCN_VAL_PAIR[ARFCN_COUNT];

        for(int i = 0; i < ARFCN_COUNT; i++){
            arfcn_val_pairs[i] = new ARFCN_VAL_PAIR();
        }
        // 初始化数据
        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        // 下行 bandid = 1
        arfcn_val_pairs[1].N_DL_low = 0;
        arfcn_val_pairs[1].N_DL_high = 599;
        arfcn_val_pairs[1].F_DL_low = 2110;
        arfcn_val_pairs[1].F_DL_high = 2170;
        // 上行 bandid = 1
        arfcn_val_pairs[1].N_UL_low = 18000;
        arfcn_val_pairs[1].N_UL_high = 18599;
        arfcn_val_pairs[1].F_UL_low = 1920;
        arfcn_val_pairs[1].F_UL_high = 1980;

        // 下行 bandid = 2
        arfcn_val_pairs[2].N_DL_low = 600;
        arfcn_val_pairs[2].N_DL_high = 1199;
        arfcn_val_pairs[2].F_DL_low = 1930;
        arfcn_val_pairs[2].F_DL_high = 1990;
        // 上行 bandid = 2
        arfcn_val_pairs[2].N_UL_low = 18600;
        arfcn_val_pairs[2].N_UL_high = 19199;
        arfcn_val_pairs[2].F_UL_low = 1850;
        arfcn_val_pairs[2].F_UL_high = 1910;

        // 下行 bandid = 3
        arfcn_val_pairs[3].N_DL_low = 1200;
        arfcn_val_pairs[3].N_DL_high = 1949;
        arfcn_val_pairs[3].F_DL_low = 1805;
        arfcn_val_pairs[3].F_DL_high = 1880;
        // 上行 bandid = 3
        arfcn_val_pairs[3].N_UL_low = 19200;
        arfcn_val_pairs[3].N_UL_high = 19949;
        arfcn_val_pairs[3].F_UL_low = 1710;
        arfcn_val_pairs[3].F_UL_high = 1785;

        // 下行 bandid = 4
        arfcn_val_pairs[4].N_DL_low = 1950;
        arfcn_val_pairs[4].N_DL_high = 2399;
        arfcn_val_pairs[4].F_DL_low = 19950;
        arfcn_val_pairs[4].F_DL_high = 20399;
        // 上行 bandid = 4
        arfcn_val_pairs[4].N_UL_low = 19200;
        arfcn_val_pairs[4].N_UL_high = 19949;
        arfcn_val_pairs[4].F_UL_low = 1710;
        arfcn_val_pairs[4].F_UL_high = 1755;

        // 下行 bandid = 5
        arfcn_val_pairs[5].N_DL_low = 2400;
        arfcn_val_pairs[5].N_DL_high = 2649;
        arfcn_val_pairs[5].F_DL_low = 869;
        arfcn_val_pairs[5].F_DL_high = 894;
        // 上行 bandid = 5
        arfcn_val_pairs[5].N_UL_low = 20400;
        arfcn_val_pairs[5].N_UL_high = 20649;
        arfcn_val_pairs[5].F_UL_low = 824;
        arfcn_val_pairs[5].F_UL_high = 849;

        // 下行 bandid = 6
        arfcn_val_pairs[6].N_DL_low = 2650;
        arfcn_val_pairs[6].N_DL_high = 2749;
        arfcn_val_pairs[6].F_DL_low = 875;
        arfcn_val_pairs[6].F_DL_high = 885;
        // 上行 bandid = 6
        arfcn_val_pairs[6].N_UL_low = 20650;
        arfcn_val_pairs[6].N_UL_high = 20749;
        arfcn_val_pairs[6].F_UL_low = 830;
        arfcn_val_pairs[6].F_UL_high = 840;

        // 下行 bandid = 7
        arfcn_val_pairs[7].N_DL_low = 2750;
        arfcn_val_pairs[7].N_DL_high = 3449;
        arfcn_val_pairs[7].F_DL_low = 2620;
        arfcn_val_pairs[7].F_DL_high = 2690;
        // 上行 bandid = 7
        arfcn_val_pairs[7].N_UL_low = 20750;
        arfcn_val_pairs[7].N_UL_high = 21449;
        arfcn_val_pairs[7].F_UL_low = 2500;
        arfcn_val_pairs[7].F_UL_high = 2570;

        // 下行 bandid = 8
        arfcn_val_pairs[8].N_DL_low = 3450;
        arfcn_val_pairs[8].N_DL_high = 3799;
        arfcn_val_pairs[8].F_DL_low = 925;
        arfcn_val_pairs[8].F_DL_high = 960;
        // 上行 bandid = 8
        arfcn_val_pairs[8].N_UL_low = 21450;
        arfcn_val_pairs[8].N_UL_high = 21799;
        arfcn_val_pairs[8].F_UL_low = 880;
        arfcn_val_pairs[8].F_UL_high = 915;

        // 下行 bandid = 9
        arfcn_val_pairs[9].N_DL_low = 3800;
        arfcn_val_pairs[9].N_DL_high = 4149;
  //      arfcn_val_pairs[9].F_DL_low = 1844.9;
    //    arfcn_val_pairs[9].F_DL_high = 1879.9;
        // 上行 bandid = 9
        arfcn_val_pairs[9].N_UL_low = 21800;
        arfcn_val_pairs[9].N_UL_high = 22149;
 //       arfcn_val_pairs[9].F_UL_low = 1749.9;
 //       arfcn_val_pairs[9].F_UL_high = 1784.9;

        // 下行 bandid = 10
        arfcn_val_pairs[10].N_DL_low = 4150;
        arfcn_val_pairs[10].N_DL_high = 4749;
        arfcn_val_pairs[10].F_DL_low = 2110;
        arfcn_val_pairs[10].F_DL_high = 2170;
        // 上行 bandid = 10
        arfcn_val_pairs[10].N_UL_low = 22150;
        arfcn_val_pairs[10].N_UL_high = 22749;
        arfcn_val_pairs[10].F_UL_low = 1710;
        arfcn_val_pairs[10].F_UL_high = 1770;


        // 下行 bandid = 11
        arfcn_val_pairs[11].N_DL_low = 4750;
        arfcn_val_pairs[11].N_DL_high = 4949;
 //       arfcn_val_pairs[11].F_DL_low = 1475.9;
//        arfcn_val_pairs[11].F_DL_high = 1495.9;
        // 上行 bandid = 11
        arfcn_val_pairs[11].N_UL_low = 22750;
        arfcn_val_pairs[11].N_UL_high = 22949;
 //       arfcn_val_pairs[11].F_UL_low = 1427.9;
 //       arfcn_val_pairs[11].F_UL_high = 1447.9;


        // 下行 bandid = 12
        arfcn_val_pairs[12].N_DL_low = 5010;
        arfcn_val_pairs[12].N_DL_high = 5179;
        arfcn_val_pairs[12].F_DL_low = 729;
        arfcn_val_pairs[12].F_DL_high = 746;
        // 上行 bandid = 12
        arfcn_val_pairs[12].N_UL_low = 23010;
        arfcn_val_pairs[12].N_UL_high = 23179;
        arfcn_val_pairs[12].F_UL_low = 699;
        arfcn_val_pairs[12].F_UL_high = 716;

        // 下行 bandid = 13
        arfcn_val_pairs[13].N_DL_low = 5180;
        arfcn_val_pairs[13].N_DL_high = 5279;
        arfcn_val_pairs[13].F_DL_low = 746;
        arfcn_val_pairs[13].F_DL_high = 756;
        // 上行 bandid = 13
        arfcn_val_pairs[13].N_UL_low = 23180;
        arfcn_val_pairs[13].N_UL_high = 23279;
        arfcn_val_pairs[13].F_UL_low = 777;
        arfcn_val_pairs[13].F_UL_high = 787;

        // 下行 bandid = 14
        arfcn_val_pairs[14].N_DL_low = 5280;
        arfcn_val_pairs[14].N_DL_high = 5379;
        arfcn_val_pairs[14].F_DL_low = 758;
        arfcn_val_pairs[14].F_DL_high = 768;
        // 上行 bandid = 14
        arfcn_val_pairs[14].N_UL_low = 23280;
        arfcn_val_pairs[14].N_UL_high = 23379;
        arfcn_val_pairs[14].F_UL_low = 788;
        arfcn_val_pairs[14].F_UL_high = 798;

        // 下行 bandid = 17
        arfcn_val_pairs[17].N_DL_low = 57309;
        arfcn_val_pairs[17].N_DL_high = 5849;
        arfcn_val_pairs[17].F_DL_low = 734;
        arfcn_val_pairs[17].F_DL_high = 746;
        // 上行 bandid = 17
        arfcn_val_pairs[17].N_UL_low = 23730;
        arfcn_val_pairs[17].N_UL_high = 23849;
        arfcn_val_pairs[17].F_UL_low = 704;
        arfcn_val_pairs[17].F_UL_high = 716;

        // 下行 bandid = 18
        arfcn_val_pairs[18].N_DL_low = 5850;
        arfcn_val_pairs[18].N_DL_high = 5999;
        arfcn_val_pairs[18].F_DL_low = 860;
        arfcn_val_pairs[18].F_DL_high = 875;
        // 上行 bandid = 18
        arfcn_val_pairs[18].N_UL_low = 23850;
        arfcn_val_pairs[18].N_UL_high = 23999;
        arfcn_val_pairs[18].F_UL_low = 815;
        arfcn_val_pairs[18].F_UL_high = 830;

        // 下行 bandid = 19
        arfcn_val_pairs[19].N_DL_low = 6000;
        arfcn_val_pairs[19].N_DL_high = 6149;
        arfcn_val_pairs[19].F_DL_low = 875;
        arfcn_val_pairs[19].F_DL_high = 890;
        // 上行 bandid = 19
        arfcn_val_pairs[19].N_UL_low = 24000;
        arfcn_val_pairs[19].N_UL_high = 24149;
        arfcn_val_pairs[19].F_UL_low = 830;
        arfcn_val_pairs[19].F_UL_high = 845;

        // 下行 bandid = 20
        arfcn_val_pairs[20].N_DL_low = 6150;
        arfcn_val_pairs[20].N_DL_high = 6449;
        arfcn_val_pairs[20].F_DL_low = 791;
        arfcn_val_pairs[20].F_DL_high = 821;
        // 上行 bandid = 20
        arfcn_val_pairs[20].N_UL_low = 24150;
        arfcn_val_pairs[20].N_UL_high = 24449;
        arfcn_val_pairs[20].F_UL_low = 832;
        arfcn_val_pairs[20].F_UL_high = 862;

        // 下行 bandid = 21
        arfcn_val_pairs[21].N_DL_low = 6450;
        arfcn_val_pairs[21].N_DL_high = 6599;
//        arfcn_val_pairs[21].F_DL_low = 1495.9;
//        arfcn_val_pairs[21].F_DL_high = 1510.9;
        // 上行 bandid = 21
        arfcn_val_pairs[21].N_UL_low = 24450;
        arfcn_val_pairs[21].N_UL_high = 24599;
//        arfcn_val_pairs[21].F_UL_low = 1447.9;
//        arfcn_val_pairs[21].F_UL_high = 1462.9;

        // 下行 bandid = 22
        arfcn_val_pairs[22].N_DL_low = 6600;
        arfcn_val_pairs[22].N_DL_high = 7399;
        arfcn_val_pairs[22].F_DL_low = 3510;
        arfcn_val_pairs[22].F_DL_high = 3590;
        // 上行 bandid = 22
        arfcn_val_pairs[22].N_UL_low = 24600;
        arfcn_val_pairs[22].N_UL_high = 25399;
        arfcn_val_pairs[22].F_UL_low = 3410;
        arfcn_val_pairs[22].F_UL_high = 3490;

        // 下行 bandid = 23
        arfcn_val_pairs[23].N_DL_low = 7500;
        arfcn_val_pairs[23].N_DL_high = 7699;
        arfcn_val_pairs[23].F_DL_low = 2180;
        arfcn_val_pairs[23].F_DL_high = 2200;
        // 上行 bandid = 23
        arfcn_val_pairs[23].N_UL_low = 25500;
        arfcn_val_pairs[23].N_UL_high = 25699;
        arfcn_val_pairs[23].F_UL_low = 2000;
        arfcn_val_pairs[23].F_UL_high = 2020;

        // 下行 bandid = 24
        arfcn_val_pairs[24].N_DL_low = 7700;
        arfcn_val_pairs[24].N_DL_high = 8039;
        arfcn_val_pairs[24].F_DL_low = 1525;
        arfcn_val_pairs[24].F_DL_high = 1559;
        // 上行 bandid = 24
        arfcn_val_pairs[24].N_UL_low = 25700;
        arfcn_val_pairs[24].N_UL_high = 26039;
//        arfcn_val_pairs[24].F_UL_low = 1626.5;
//        arfcn_val_pairs[24].F_UL_high = 1660.5;

        // 下行 bandid = 25
        arfcn_val_pairs[25].N_DL_low = 8040;
        arfcn_val_pairs[25].N_DL_high = 8689;
        arfcn_val_pairs[25].F_DL_low = 1930;
        arfcn_val_pairs[25].F_DL_high = 1995;
        // 上行 bandid =25
        arfcn_val_pairs[25].N_UL_low = 26040;
        arfcn_val_pairs[25].N_UL_high = 26689;
        arfcn_val_pairs[25].F_UL_low = 1850;
        arfcn_val_pairs[25].F_UL_high = 1915;

        // 下行 bandid = 26
        arfcn_val_pairs[26].N_DL_low = 8690;
        arfcn_val_pairs[26].N_DL_high = 9039;
        arfcn_val_pairs[26].F_DL_low = 859;
        arfcn_val_pairs[26].F_DL_high = 894;
        // 上行 bandid = 26
        arfcn_val_pairs[26].N_UL_low = 26690;
        arfcn_val_pairs[26].N_UL_high = 27039;
        arfcn_val_pairs[26].F_UL_low = 814;
        arfcn_val_pairs[26].F_UL_high = 849;

        // 下行 bandid = 27
        arfcn_val_pairs[27].N_DL_low = 9040;
        arfcn_val_pairs[27].N_DL_high = 9209;
        arfcn_val_pairs[27].F_DL_low = 852;
        arfcn_val_pairs[27].F_DL_high = 869;
        // 上行 bandid =27
        arfcn_val_pairs[27].N_UL_low = 27040;
        arfcn_val_pairs[27].N_UL_high = 27209;
        arfcn_val_pairs[27].F_UL_low = 807;
        arfcn_val_pairs[27].F_UL_high = 824;

        // 下行 bandid = 28
        arfcn_val_pairs[28].N_DL_low = 9210;
        arfcn_val_pairs[28].N_DL_high = 9659;
        arfcn_val_pairs[28].F_DL_low = 758;
        arfcn_val_pairs[28].F_DL_high = 803;
        // 上行 bandid = 28
        arfcn_val_pairs[28].N_UL_low = 27210;
        arfcn_val_pairs[28].N_UL_high = 27659;
        arfcn_val_pairs[28].F_UL_low = 703;
        arfcn_val_pairs[28].F_UL_high = 748;

        // 下行 bandid = 30
        arfcn_val_pairs[30].N_DL_low = 9770;
        arfcn_val_pairs[30].N_DL_high = 9869;
        arfcn_val_pairs[30].F_DL_low = 2350;
        arfcn_val_pairs[30].F_DL_high = 2360;
        // 上行 bandid =30
        arfcn_val_pairs[30].N_UL_low = 27660;
        arfcn_val_pairs[30].N_UL_high = 27759;
        arfcn_val_pairs[30].F_UL_low = 2305;
        arfcn_val_pairs[30].F_UL_high = 2315;

        // 下行 bandid = 31
        arfcn_val_pairs[31].N_DL_low = 9870;
        arfcn_val_pairs[31].N_DL_high = 9870;
//        arfcn_val_pairs[31].F_DL_low = 462.5;
//        arfcn_val_pairs[31].F_DL_high = 467.5;
        // 上行 bandid =31
        arfcn_val_pairs[31].N_UL_low = 27760;
        arfcn_val_pairs[31].N_UL_high = 27809;
//        arfcn_val_pairs[31].F_UL_low = 452.5;
//        arfcn_val_pairs[31].F_UL_high = 457.5;

        // 下行 bandid = 33
        arfcn_val_pairs[33].N_DL_low = 36000;
        arfcn_val_pairs[33].N_DL_high = 36199;
        arfcn_val_pairs[33].F_DL_low = 1900;
        arfcn_val_pairs[33].F_DL_high = 1920;
        // 上行 bandid =
        arfcn_val_pairs[33].N_UL_low = 36000;
        arfcn_val_pairs[33].N_UL_high = 36199;
        arfcn_val_pairs[33].F_UL_low = 1900;
        arfcn_val_pairs[33].F_UL_high = 1920;

        // 下行 bandid = 34
        arfcn_val_pairs[34].N_DL_low = 36200;
        arfcn_val_pairs[34].N_DL_high = 36349;
        arfcn_val_pairs[34].F_DL_low = 2010;
        arfcn_val_pairs[34].F_DL_high = 2025;
        // 上行 bandid =34
        arfcn_val_pairs[34].N_UL_low = 36200;
        arfcn_val_pairs[34].N_UL_high = 36349 ;
        arfcn_val_pairs[34].F_UL_low = 2010;
        arfcn_val_pairs[34].F_UL_high = 2025 ;

        // 下行 bandid = 35
        arfcn_val_pairs[35].N_DL_low = 36350;
        arfcn_val_pairs[35].N_DL_high = 36949;
        arfcn_val_pairs[35].F_DL_low = 1850;
        arfcn_val_pairs[35].F_DL_high = 1910;
        // 上行 bandid =35
        arfcn_val_pairs[35].N_UL_low = 36350;
        arfcn_val_pairs[35].N_UL_high = 36949;
        arfcn_val_pairs[35].F_UL_low = 1850;
        arfcn_val_pairs[35].F_UL_high = 1910;

        // 下行 bandid = 36
        arfcn_val_pairs[36].N_DL_low = 36950;
        arfcn_val_pairs[36].N_DL_high = 37549;
        arfcn_val_pairs[36].F_DL_low = 1930;
        arfcn_val_pairs[36].F_DL_high = 1990;
        // 上行 bandid =36
        arfcn_val_pairs[36].N_UL_low = 36950;
        arfcn_val_pairs[36].N_UL_high = 37549;
        arfcn_val_pairs[36].F_UL_low = 1930;
        arfcn_val_pairs[36].F_UL_high = 1990;

        // 下行 bandid = 37
        arfcn_val_pairs[37].N_DL_low = 37550;
        arfcn_val_pairs[37].N_DL_high = 37749;
        arfcn_val_pairs[37].F_DL_low = 1910;
        arfcn_val_pairs[37].F_DL_high = 1930;
        // 上行 bandid =37
        arfcn_val_pairs[37].N_UL_low = 37550;
        arfcn_val_pairs[37].N_UL_high = 37749;
        arfcn_val_pairs[37].F_UL_low = 1910;
        arfcn_val_pairs[37].F_UL_high = 1930;

        // 下行 bandid = 38
        arfcn_val_pairs[38].N_DL_low = 37750;
        arfcn_val_pairs[38].N_DL_high = 38249;
        arfcn_val_pairs[38].F_DL_low = 2570;
        arfcn_val_pairs[38].F_DL_high = 2620;
        // 上行 bandid =
        arfcn_val_pairs[38].N_UL_low = 37750 ;
        arfcn_val_pairs[38].N_UL_high = 38249;
        arfcn_val_pairs[38].F_UL_low = 2570;
        arfcn_val_pairs[38].F_UL_high = 2620;

        // 下行 bandid = 39
        arfcn_val_pairs[39].N_DL_low = 38250;
        arfcn_val_pairs[39].N_DL_high = 38649;
        arfcn_val_pairs[39].F_DL_low = 1880;
        arfcn_val_pairs[39].F_DL_high = 1920;
        // 上行 bandid =39
        arfcn_val_pairs[39].N_UL_low = 38250;
        arfcn_val_pairs[39].N_UL_high = 38649;
        arfcn_val_pairs[39].F_UL_low = 1880;
        arfcn_val_pairs[39].F_UL_high = 1920;

        // 下行 bandid = 40
        arfcn_val_pairs[40].N_DL_low = 38650;
        arfcn_val_pairs[40].N_DL_high = 39649;
        arfcn_val_pairs[40].F_DL_low = 2300;
        arfcn_val_pairs[40].F_DL_high = 2400;
        // 上行 bandid =
        arfcn_val_pairs[40].N_UL_low = 38650;
        arfcn_val_pairs[40].N_UL_high = 39649;
        arfcn_val_pairs[40].F_UL_low = 2300;
        arfcn_val_pairs[40].F_UL_high = 2400;

        // 下行 bandid = 41
        arfcn_val_pairs[41].N_DL_low = 39650;
        arfcn_val_pairs[41].N_DL_high = 41589;
        arfcn_val_pairs[41].F_DL_low = 2496;
        arfcn_val_pairs[41].F_DL_high = 2690;
        // 上行 bandid =
        arfcn_val_pairs[41].N_UL_low = 39650;
        arfcn_val_pairs[41].N_UL_high = 41589;
        arfcn_val_pairs[41].F_UL_low = 2496;
        arfcn_val_pairs[41].F_UL_high = 2690;

        // 下行 bandid = 42
        arfcn_val_pairs[42].N_DL_low = 41590;
        arfcn_val_pairs[42].N_DL_high = 43589;
        arfcn_val_pairs[42].F_DL_low = 3400;
        arfcn_val_pairs[42].F_DL_high = 3600;
        // 上行 bandid = 42
        arfcn_val_pairs[42].N_UL_low = 41590;
        arfcn_val_pairs[42].N_UL_high = 43589;
        arfcn_val_pairs[42].F_UL_low = 3400;
        arfcn_val_pairs[42].F_UL_high = 3600;

        // 下行 bandid = 43
        arfcn_val_pairs[43].N_DL_low = 43590;
        arfcn_val_pairs[43].N_DL_high = 45589;
        arfcn_val_pairs[43].F_DL_low = 3600;
        arfcn_val_pairs[43].F_DL_high = 3800;
        // 上行 bandid =
        arfcn_val_pairs[43].N_UL_low = 43590;
        arfcn_val_pairs[43].N_UL_high = 45589;
        arfcn_val_pairs[43].F_UL_low = 3600;
        arfcn_val_pairs[43].F_UL_high = 3800;

        // 下行 bandid = 44
        arfcn_val_pairs[44].N_DL_low = 45590;
        arfcn_val_pairs[44].N_DL_high = 46589;
        arfcn_val_pairs[44].F_DL_low = 703;
        arfcn_val_pairs[44].F_DL_high = 803;
        // 上行 bandid = 44
        arfcn_val_pairs[44].N_UL_low = 45590;
        arfcn_val_pairs[44].N_UL_high = 46589;
        arfcn_val_pairs[44].F_UL_low = 703;
        arfcn_val_pairs[44].F_UL_high = 803;

    }

    /**
     * 根据频点获取Bandid,（下行）
     * @param arfcn 频点
     * @return
     */
    public int getDLBandIdByArfcn(int arfcn) {
        int bandId = 0;

        for(int i = 1; i < ARFCN_COUNT; i++ ){
            // TODO: 是根据N判断还是F判断？
            // 无效值
            if(arfcn_val_pairs[i].N_DL_high <= 0){
                continue;
            }

            if(arfcn <= arfcn_val_pairs[i].N_DL_high
                    && arfcn >= arfcn_val_pairs[i].N_DL_low){
                bandId = i;
                break;
            }
        }

        return bandId;
    }

    /**
     * 根据频点获取Bandid,（上行）
     * @param arfcn 频点
     * @return
     */
    public int getULBandIdByArfcn(int arfcn) {
        int bandId = 0;
        for(int i = 1; i < ARFCN_COUNT; i++ ){
            // TODO: 是根据N判断还是F判断？
            // 无效值
            if(arfcn_val_pairs[i].N_UL_high <= 0){
                continue;
            }

            if(arfcn <= arfcn_val_pairs[i].N_UL_high
                    &&  arfcn >= arfcn_val_pairs[i].N_UL_low){
                bandId = i;
                break;
            }
        }

        return bandId;
    }


    /**
     * 将频点转换为频点号(下行)
     * @param arfcnF
     * @return
     */
    public int getDLArfcnNByArfcnF(int bandId, int arfcnF){
        int N = 0;

        // 公式：Ndl = (Fdl - Fdl_low)*10 + Noffs_dl
        N = (arfcnF - arfcn_val_pairs[bandId].F_DL_low) * 10
                + arfcn_val_pairs[bandId].N_DL_low;

        return N;
    }

    /**
     * 将频点转换为频点号(上行)
     * @param arfcnF
     * @return
     */
    public int getULArfcnNByArfcnF(int bandId, int arfcnF){
        int N = 0;

        // 公式：Nul = (Ful - Ful_low)*10 + Noffs_ul
        N = (arfcnF - arfcn_val_pairs[bandId].F_UL_low) * 10
                + arfcn_val_pairs[bandId].N_UL_low;

        return N;
    }


    /**
     * 将功率转换为dB
     * @param power
     * @return
     */
    public static double power2dB(double power) {
        double newPower;
        DecimalFormat df = new DecimalFormat("######0.00");

        newPower = 10 * Math.log10(power) - 140;
        newPower = Double.valueOf(df.format(newPower));

        return newPower;
    }

}
