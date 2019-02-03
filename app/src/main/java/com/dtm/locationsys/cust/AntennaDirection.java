package com.dtm.locationsys.cust;

/**
 * Created by pq on 10/5/17.
 */

public enum AntennaDirection {
    FRONT_ANTENNA(0), BACK_ANTENNA(1), LEFT_ANTENNA(2), RIGHT_ANTENNA(3);

    private int mCode;

    private AntennaDirection(int code) {
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }
}
