package com.dtm.locationsys.utils;

/**
 * Created by pq on 9/29/17.
 */
public class BitsHelper {

    /**
     * convert bits to int value
     * @param originValue   the bits host int
     * @param bitsStartPos
     * @param bitsEndPos
     * @return
     */
    static public int getIntByBits(int originValue, int bitsStartPos, int bitsEndPos) {
        return (originValue >> bitsStartPos) & (createBitsMaskForInt(bitsEndPos - bitsStartPos + 1));
    }

    /**
     * covert bits to long value
     * @param originValue
     * @param bitsStartPos
     * @param bitsEndPos
     * @return
     */
    static public int getIntFromUintByBits(long originValue, int bitsStartPos, int bitsEndPos) {
        return (int)((originValue >> bitsStartPos) & (createBitsMaskForLong(bitsEndPos - bitsStartPos +1)));
    }


    /**
     * covert bits to long value
     * @param originValue
     * @param bitsStartPos
     * @param bitsEndPos
     * @return
     */
    static public long getUintByBits(long originValue, int bitsStartPos, int bitsEndPos) {
        return (originValue >> bitsStartPos) & (createBitsMaskForLong(bitsEndPos - bitsStartPos +1));
    }

    /**
     * create bits mask by length
     * @param bitsLength
     * @return the bits mask
     */
    static private int createBitsMaskForInt(int bitsLength) {
        int bitsMask = 0x00000001;

        while (bitsLength > 1 ) {
            bitsMask = (bitsMask << 1) | 0x00000001;

            bitsLength--;
        }

        return bitsMask;
    }

    /**
     * create bits mask by length for Long
     * @param bitsLength
     * @return the bits mask
     */
    static private long createBitsMaskForLong(int bitsLength) {
        long bitsMask = 0x0000000000000001;

        while (bitsLength > 1 ) {
            bitsMask = (bitsMask << 1) | 0x0000000000000001;

            bitsLength--;
        }

        return bitsMask;
    }
}
