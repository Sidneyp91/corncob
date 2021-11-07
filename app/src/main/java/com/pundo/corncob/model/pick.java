package com.pundo.corncob.model;

public class pick {
    private String plots, acre, bag, counties, crops,status;

    public pick() {
    }

    public pick(String plots, String acre, String bag, String counties, String crops, String status) {
        this.plots = plots;
        this.acre = acre;
        this.bag = bag;
        this.counties = counties;
        this.crops = crops;
        this.status = status;
    }

    public String getPlots() {
        return plots;
    }

    public void setPlots(String plots) {
        this.plots = plots;
    }

    public String getAcre() {
        return acre;
    }

    public void setAcre(String acre) {
        this.acre = acre;
    }

    public String getBag() {
        return bag;
    }

    public void setBag(String bag) {
        this.bag = bag;
    }

    public String getCounties() {
        return counties;
    }

    public void setCounties(String counties) {
        this.counties = counties;
    }

    public String getCrops() {
        return crops;
    }

    public void setCrops(String crops) {
        this.crops = crops;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
