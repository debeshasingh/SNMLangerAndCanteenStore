package com.example.snmlangerandcanteenstore.model;

public class Category {
    private String catId;
    private String catName;

    @Override
    public String toString() {
        return catName;
    }

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
}
