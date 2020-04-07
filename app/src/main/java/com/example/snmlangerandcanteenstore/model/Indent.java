package com.example.snmlangerandcanteenstore.model;

import java.util.List;

public class Indent {
    private String indId;
    private String cDate;
    private String cBy;
    private List<OutStock> oStocks;
    private String cNo;//Challan number
    private String cName;//Canteen Name
    private String fQuantity;

    public String getIndId() {
        return indId;
    }

    public void setIndId(String indId) {
        this.indId = indId;
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

    public List<OutStock> getoStocks() {
        return oStocks;
    }

    public void setoStocks(List<OutStock> oStocks) {
        this.oStocks = oStocks;
    }

    public String getcNo() {
        return cNo;
    }

    public void setcNo(String cNo) {
        this.cNo = cNo;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getfQuantity() {
        return fQuantity;
    }

    public void setfQuantity(String fQuantity) {
        this.fQuantity = fQuantity;
    }
}
