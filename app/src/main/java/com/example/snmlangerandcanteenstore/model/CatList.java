package com.example.snmlangerandcanteenstore.model;

import java.util.List;

public class CatList {
    private String catId;
    private String catName;
    private List<InStock> inStocks;
    private List<OutStock> outStocks;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public List<InStock> getInStocks() {
        return inStocks;
    }

    public void setInStocks(List<InStock> inStocks) {
        this.inStocks = inStocks;
    }

    public List<OutStock> getOutStocks() {
        return outStocks;
    }

    public void setOutStocks(List<OutStock> outStocks) {
        this.outStocks = outStocks;
    }
}
