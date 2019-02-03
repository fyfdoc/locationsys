package com.dtm.locationsys.event;

/**
 * UI数据同步事件类.
 */

public class DataSyncEvent {
    private byte[] bytes;

    public DataSyncEvent(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
