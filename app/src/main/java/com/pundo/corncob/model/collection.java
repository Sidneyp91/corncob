package com.pundo.corncob.model;

public class collection {

    private String acres, harvest, crop, date, county, price;

    public collection() {
    }

    public collection(String acres, String harvest, String crop, String date, String county, String price) {
        this.acres = acres;
        this.harvest = harvest;
        this.crop = crop;
        this.date = date;
        this.county = county;
        this.price = price;
    }

    public String getAcres() {
        return acres;
    }

    public void setAcres(String acres) {
        this.acres = acres;
    }

    public String getHarvest() {
        return harvest;
    }

    public void setHarvest(String harvest) {
        this.harvest = harvest;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
