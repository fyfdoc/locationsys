package com.dtm.locationsys.event;

/**
 * UI数据同步事件类.
 */

public class DataSyncEventTest {

    private String msg;

    public DataSyncEventTest(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
