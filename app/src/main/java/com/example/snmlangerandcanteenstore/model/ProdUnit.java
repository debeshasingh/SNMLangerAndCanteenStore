package com.example.snmlangerandcanteenstore.model;

public class ProdUnit {
    private String id;
    private String uName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    @Override
    public String toString() {
        return uName;
    }
}
