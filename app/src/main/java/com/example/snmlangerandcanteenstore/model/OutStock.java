package com.example.snmlangerandcanteenstore.model;

public class OutStock {
    private String indNo;
    private String cName;//Canteen name
    private Product prod;
    private String proUnit;//Number of unit
    private String perUnit;//Per of unit
    private String perUPrice;//Per of unit price
    private String cDate;
    private String pInQty;//Quantity
    private String cBy;
    private String remark;
    private String cNo;//Challan Number

    public Product getProd() {
        return prod;
    }

    public void setProd(Product prod) {
        this.prod = prod;
    }

    public String getcNo() {
        return cNo;
    }

    public void setcNo(String cNo) {
        this.cNo = cNo;
    }

    public String getIndNo() {
        return indNo;
    }

    public void setIndNo(String indNo) {
        this.indNo = indNo;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getProUnit() {
        return proUnit;
    }

    public void setProUnit(String proUnit) {
        this.proUnit = proUnit;
    }

    public String getPerUnit() {
        return perUnit;
    }

    public void setPerUnit(String perUnit) {
        this.perUnit = perUnit;
    }

    public String getPerUPrice() {
        return perUPrice;
    }

    public void setPerUPrice(String perUPrice) {
        this.perUPrice = perUPrice;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public String getpInQty() {
        return pInQty;
    }

    public void setpInQty(String pInQty) {
        this.pInQty = pInQty;
    }

    public String getcBy() {
        return cBy;
    }

    public void setcBy(String cBy) {
        this.cBy = cBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
