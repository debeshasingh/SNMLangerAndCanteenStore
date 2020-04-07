package com.example.snmlangerandcanteenstore.model;

import java.util.List;

public class Product {
    private String pId;
    private String pName;
    private String unit;
    private Category cat;

    public Category getCat() {
        return cat;
    }

    public void setCat(Category cat) {
        this.cat = cat;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        if(unit!=null){
            return pName + " (" + unit + ")";
        }else {
            return pName;
        }
    }
}
