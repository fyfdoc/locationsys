package com.dtm.locationsys;

import com.dtm.locationsys.cust.DirectionEnum;
import com.dtm.locationsys.utils.ByteConvert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JustTest {
    @Test
    public void test() throws Exception {
        int v = 0;

        char c = ByteConvert.int2char(v);

        System.out.println("c=" + c);
    }

    @Test
    public void sintest() {
        double a = 45;
        int radius = 210;
        double x2;
        double y2;
        double x1;
        double y1;

        y2 = 210 * Math.sin(a);
        x2 = 210 * Math.cos(a);

        x1 = 300 + x2;
        y1 = 300 - y2;

        System.out.println("sin(a)=" + Math.sin(a));
        System.out.println("cos(a)=" + Math.cos(a));
        System.out.println("y2=" + y2);
        System.out.println("x2=" + x2);
        System.out.println("x1=" + x1);
        System.out.println("y1=" + y1);


    }

    @Test
    public void CTest() {
        int CIRCLE_CENTER_X = 300;
        int CIRCLE_CENTER_Y = 300;
        int CIRCLE_R = 210;

        for (int i = 0; i < 360; i += 1) {
            int x = (int) (CIRCLE_CENTER_X - CIRCLE_R * Math.sin(Math.PI * (i - 90) / 180));
            int y = (int) (CIRCLE_CENTER_Y - CIRCLE_R - 10
                    + CIRCLE_R * Math.cos(Math.PI * (i - 90) / 180));
            System.out.println("x=" + x);
            System.out.println("y=" + y);
        }
    }

    @Test
    public void drawPointer() {
        int v = 90;
        // 圆心X坐标
        float CENTER_X = 300;
        // 圆心Y坐标
        float CENTER_Y = 300;
        // 园半径
        float RADIUS = 210;

        int x = (int) (CENTER_X - RADIUS * Math.sin(Math.PI * (v - 90) / 180));
        int y = (int) (CENTER_Y - RADIUS - 10
                + RADIUS * Math.cos(Math.PI * (v - 90) / 180));

        System.out.println("x=" + x);
        System.out.println("y=" + y);

    }

    @Test
    public void DirectionEnumTest() {
        System.out.println(DirectionEnum.FRONT_ANTENNA);
        System.out.println(DirectionEnum.FRONT_ANTENNA.getName());
        System.out.println(DirectionEnum.FRONT_ANTENNA.getIndex());
        System.out.println(DirectionEnum.getName(0));
    }

}

