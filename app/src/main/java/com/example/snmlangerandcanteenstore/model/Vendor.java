package com.example.snmlangerandcanteenstore.model;

public class Vendor {
    private String vName;
    private String vMob;
    private String detail;
    private String vId;

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvMob() {
        return vMob;
    }

    public void setvMob(String vMob) {
        this.vMob = vMob;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    @Override
    public String toString() {
        return vName;
    }
}
