package com.example.snmlangerandcanteenstore.model;

public class InStock {
    private String mrnNo;
    private String venName;
    private Product prod;
    private String proUnit;//no of unit
    private String perUnit;//per of unit
    private String perUPrice;//per of unit price
    private String cDate;
    private String pInQty;//Quantity
    private String cBy;
    private String remark;
    private String price;

    public Product getProd() {
        return prod;
    }

    public void setProd(Product prod) {
        this.prod = prod;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMrnNo() {
        return mrnNo;
    }

    public void setMrnNo(String mrnNo) {
        this.mrnNo = mrnNo;
    }

    public String getProUnit() {
        return proUnit;
    }

    public void setProUnit(String proUnit) {
        this.proUnit = proUnit;
    }

    public String getVenName() {
        return venName;
    }

    public void setVenName(String venName) {
        this.venName = venName;
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
}
