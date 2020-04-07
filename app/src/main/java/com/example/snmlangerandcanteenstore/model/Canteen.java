package com.example.snmlangerandcanteenstore.model;

public class Canteen {
    private String cName;
    private String zone;
    private String cMob;
    private String cId;

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getcMob() {
        return cMob;
    }

    public void setcMob(String cMob) {
        this.cMob = cMob;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    @Override
    public String toString() {
        return cName;
    }
}
