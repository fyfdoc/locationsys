package com.dtm.locationsys.model;

import java.io.Serializable;

/**
 * Config.xml文件对象
 */

public class ConfigXml implements Serializable {
    private String destIp;
    private int destPort;
    private String localPort;

    public ConfigXml(String destIp, int destPort, String localPort) {
        super();
        this.destIp = destIp;
        this.destPort = destPort;
        this.localPort = localPort;
    }

    public ConfigXml(){
        super();
    }

    public String getDestIp() {
        return destIp;
    }

    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    public int getDestPort() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }

    public String getLocalPort() {
        return localPort;
    }

    public void setLocalPort(String localPort) {
        this.localPort = localPort;
    }
}
