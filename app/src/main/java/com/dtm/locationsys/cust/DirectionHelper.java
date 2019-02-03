package com.dtm.locationsys.cust;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by tomato on 2017/11/3.
 */

public class DirectionHelper {

    /**
     * 根据方向数组获取指针角度，方向与数组索引的关系为：右(0)、后(1)、左(2)、前(3)
     * @param powers
     * @return
     */
    public static float getPoniterAngle(long[] powers) {
        float angle;

        // 找到最大值及其索引
        long maxValue = powers[0];
        int maxIndex = 0;
        for (int i = 0; i < powers.length; i++ ) {
            if (powers[i] > maxValue) {
                maxIndex = i;
            }
        }
        if (0 == powers[maxIndex]) {
            angle = 0;
            return angle;
        }

        // 获取最大值相邻两个节点的索引
        int indexL = getLeftNeighbourIndex(maxIndex);
        int indexR = getRightNeighbourIndex(maxIndex);
        // 获取两个邻节点中值较大的节点
        int neightbourMaxIndex = indexL;
        if (powers[indexR] > powers[indexL]) {
            neightbourMaxIndex = indexR;
        }

        // 获取指针的角度
        angle = getAngle(powers, maxIndex, neightbourMaxIndex);

        System.out.println("maxIndex=" + maxIndex);
        System.out.println("indexL=" + indexL);
        System.out.println("indexR=" + indexR);
        System.out.println("neightbourMaxIndex=" + neightbourMaxIndex);
        System.out.println("angle=" + angle);

        return angle;
    }

    /**
     * 获取方向指针的角度
     * @param power
     * @param maxIndex
     * @param secondIndex
     * @return
     */
    public static float getAngle(long power[], int maxIndex, int secondIndex) {
        float rs = 0;

        // 两个节点值的比重
        float subWeight = ((float) (power[maxIndex] - power[secondIndex]))/power[maxIndex];
        subWeight = new BigDecimal(subWeight).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        float subAngle = 45 * (1 - subWeight);

        System.out.println("subWeight=" + subWeight);
        System.out.println("subAngle=" + subAngle);
        // 根据节点索引计算指针的度数(索引与方向关系：右(0)、后(1)、左(2)、前(3))
        switch (maxIndex) {
            case 0 : // 右
                if (1 == secondIndex) { // 后
                    rs = 0 + subAngle;
                } else if (3 == secondIndex) { // 前
                    rs = 0 - subAngle;
                }
                break;
            case 1 : // 后
                if (2 == secondIndex) { // 左
                    rs = 90 + subAngle;
                } else if (0 == secondIndex) {// 右
                    rs = 90 - subAngle;
                }
                break;
            case 2 : // 左
                if (3 == secondIndex) { // 前
                    rs = 180 + subAngle;
                } else if (1 == secondIndex) {// 后
                    rs = 180 - subAngle;
                }
                break;
            case 3 : // 前
                if (0 == secondIndex) { // 右
                    rs = 270 + subAngle;
                } else if (2 == secondIndex) {// 左
                    rs = 270 - subAngle;
                }
                break;
            default :
                rs = 0;
        }
        return rs;
    }

    public static int getLeftNeighbourIndex(int hostIndex) {
        int indexL;
        // 左边节点索引
        indexL = (hostIndex + 1);
        indexL = indexL > 3 ? (indexL - 4) : indexL;

        return indexL;
    }
    public static int getRightNeighbourIndex(int hostIndex) {
        int indexR;
        // 右边节点索引
        indexR = hostIndex - 1;
        indexR = indexR < 0 ? (indexR + 4) : indexR;

        return indexR;
    }
}
