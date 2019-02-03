package com.dtm.locationsys.model;

/**
 * TMSI次数统计类.
 */

public class TmsiStatisticsModel {

    public int tac;

    public int cid;

    public int pci;

    public int ch;

    public int bandId;

    public long tmsi;

    public int tmsiCount;



    public int getTac() {
        return tac;
    }

    public void setTac(int tac) {
        this.tac = tac;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getPci() {
        return pci;
    }

    public void setPci(int pci) {
        this.pci = pci;
    }

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    public int getBandId() {
        return bandId;
    }

    public void setBandId(int bandId) {
        this.bandId = bandId;
    }

    public long getTmsi() {
        return tmsi;
    }

    public void setTmsi(long tmsi) {
        this.tmsi = tmsi;
    }

    public int getTmsiCount() {
        return tmsiCount;
    }

    public void setTmsiCount(int tmsiCount) {
        this.tmsiCount = tmsiCount;
    }
}
