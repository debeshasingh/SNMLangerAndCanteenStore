package com.example.snmlangerandcanteenstore.model;

import java.util.List;

public class Mrn {
    private String mrnId;
    private String cDate;
    private String cBy;
    private List<InStock> inStocks;
    private String billNo;
    private String driver;
    private String vName;
    private String fQuantity;
    private String fAmount;
    private String driCon;

    public String getDriCon() {
        return driCon;
    }

    public void setDriCon(String driCon) {
        this.driCon = driCon;
    }

    public String getMrnId() {
        return mrnId;
    }

    public void setMrnId(String mrnId) {
        this.mrnId = mrnId;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public String getcBy() {
        return cBy;
    }

    public void setcBy(String cBy) {
        this.cBy = cBy;
    }

    public List<InStock> getInStocks() {
        return inStocks;
    }

    public void setInStocks(List<InStock> inStocks) {
        this.inStocks = inStocks;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getfQuantity() {
        return fQuantity;
    }

    public void setfQuantity(String fQuantity) {
        this.fQuantity = fQuantity;
    }

    public String getfAmount() {
        return fAmount;
    }

    public void setfAmount(String fAmount) {
        this.fAmount = fAmount;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }
}
