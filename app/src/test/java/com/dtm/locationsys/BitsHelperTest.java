package com.dtm.locationsys;

import com.dtm.locationsys.utils.BitsHelper;
import com.dtm.locationsys.utils.ByteConvert;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by tomato on 2017/10/23.
 */

public class BitsHelperTest {



    @Test
    public void bitsHelperTest() {
        byte[] bytes;
/*
        // int
        int v1 = 12;
        bytes = ByteConvert.int2Bytes(v1);

        String s1 = ByteConvert.bytes2Bit(bytes);

        System.out.println("bytes=" + Arrays.toString(bytes));
        System.out.println("s1=" + s1);

        int v2 = BitsHelper.getIntBits(v1, 3, 4);
        bytes = ByteConvert.int2Bytes(v2);
        s1 = ByteConvert.bytes2Bit(bytes);

        System.out.println("s1=" + s1);
        System.out.println("v2=" + v2);

*/
        // long
        long l1 = 2684354562L;
        bytes = ByteConvert.long2Bytes(l1);

        String s2 = ByteConvert.bytes2Bit(bytes);

        System.out.println("s2=" + s2);

        long lt1 = BitsHelper.getUintByBits(l1, 29, 31);

        s2 = ByteConvert.bytes2Bit(ByteConvert.long2Bytes(lt1));

        System.out.println("s2=" + s2);
        System.out.println("lt1=" + lt1);






    }


}
