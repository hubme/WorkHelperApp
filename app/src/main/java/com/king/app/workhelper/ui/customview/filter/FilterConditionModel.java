package com.king.app.workhelper.ui.customview.filter;

public class FilterConditionModel {
    private String brand;
    private String model;

    public FilterConditionModel() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isReset() {
        return brand == null && model == null;
    }
}
