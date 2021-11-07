package com.pundo.corncob.model;

public class fert {
    private String plotNo, acre, crops, amounts, types, regions, example, example1;

    public fert() {
    }

    public fert(String plotNo, String acre, String crops, String amounts, String types, String regions, String example, String example1) {
        this.plotNo = plotNo;
        this.acre = acre;
        this.crops = crops;
        this.amounts = amounts;
        this.types = types;
        this.regions = regions;
        this.example = example;
        this.example1 = example1;
    }

    public String getPlotNo() {
        return plotNo;
    }

    public void setPlotNo(String plotNo) {
        this.plotNo = plotNo;
    }

    public String getAcre() {
        return acre;
    }

    public void setAcre(String acre) {
        this.acre = acre;
    }

    public String getCrops() {
        return crops;
    }

    public void setCrops(String crops) {
        this.crops = crops;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getRegions() {
        return regions;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExample1() {
        return example1;
    }

    public void setExample1(String example1) {
        this.example1 = example1;
    }
}