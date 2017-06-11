package com.borovyksv.sql.vehicle;

import java.util.List;

public class Info {

    private String vendor;
    private List<String> model;
    private List<Integer> year;
    private String type;
    private List<String> options;

    public Info() {
    }

    public Info(String vendor, List<String> model, List<Integer> year, String type, List<String> options) {
        this.vendor = vendor;
        this.model = model;
        this.year = year;
        this.type = type;
        this.options = options;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public List<String> getModel() {
        return model;
    }

    public void setModel(List<String> model) {
        this.model = model;
    }

    public List<Integer> getYear() {
        return year;
    }

    public void setYear(List<Integer> year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Info{" +
                "vendor='" + vendor + '\'' +
                ", model=" + model +
                ", year=" + year +
                ", type='" + type + '\'' +
                ", options=" + options +
                '}';
    }
}
